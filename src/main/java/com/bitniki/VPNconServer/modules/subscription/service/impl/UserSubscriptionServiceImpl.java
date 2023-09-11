package com.bitniki.VPNconServer.modules.subscription.service.impl;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;
import com.bitniki.VPNconServer.modules.mail.exception.ReminderValidationFailedException;
import com.bitniki.VPNconServer.modules.mail.model.ReminderToCreate;
import com.bitniki.VPNconServer.modules.mail.service.ReminderService;
import com.bitniki.VPNconServer.modules.mail.type.ReminderType;
import com.bitniki.VPNconServer.modules.peer.connectHandler.exception.PeerConnectHandlerException;
import com.bitniki.VPNconServer.modules.peer.entity.PeerEntity;
import com.bitniki.VPNconServer.modules.peer.service.PeerService;
import com.bitniki.VPNconServer.modules.subscription.entity.SubscriptionEntity;
import com.bitniki.VPNconServer.modules.subscription.entity.UserSubscriptionEntity;
import com.bitniki.VPNconServer.modules.subscription.exception.SubscriptionNotFoundException;
import com.bitniki.VPNconServer.modules.subscription.exception.UserSubscriptionNotFoundException;
import com.bitniki.VPNconServer.modules.subscription.exception.UserSubscriptionValidationFailedException;
import com.bitniki.VPNconServer.modules.subscription.model.UserSubscriptionFromRequest;
import com.bitniki.VPNconServer.modules.subscription.repository.UserSubscriptionRepo;
import com.bitniki.VPNconServer.modules.subscription.service.SubscriptionService;
import com.bitniki.VPNconServer.modules.subscription.service.UserSubscriptionService;
import com.bitniki.VPNconServer.modules.subscription.validator.UserSubscriptionValidator;
import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.modules.user.service.UserService;
import com.bitniki.VPNconServer.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@EnableScheduling
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

    @Autowired
    private UserSubscriptionRepo userSubscriptionRepo;

    @Autowired
    private UserService userService;
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private ReminderService reminderService;

    private PeerService peerService;

    @Override
    public void setPeerService(PeerService peerService) {
        this.peerService = peerService;
    }

    @Override
    public Spliterator<UserSubscriptionEntity> getAll() {
        return userSubscriptionRepo.findAll().spliterator();
    }

    @Override
    public UserSubscriptionEntity getOneByUserId(@NotNull Long userId) throws UserSubscriptionNotFoundException {
        return userSubscriptionRepo.findByUserId(userId)
                .orElseThrow(
                        () -> new UserSubscriptionNotFoundException(
                                "Cant find associated subscription for user with id %d".formatted(userId)
                        )
                );
    }

    @Override
    public List<UserSubscriptionEntity> getAllByExpirationDay(@NotNull LocalDate day) {
        return userSubscriptionRepo.findByExpirationDay(day);
    }

    /**
     * Создаёт связь между юзером и подпиской.
     * @param model указания юзера и подписки.
     * @return Сущность связи юзера и подписки.
     * @throws EntityNotFoundException Если юзер или подписка не найдена.
     */
    private UserSubscriptionEntity create(@NotNull UserSubscriptionFromRequest model)
            throws EntityNotFoundException {

        // load user
        UserEntity user = userService.getOneById(model.getUserId());

        // load subscription
        SubscriptionEntity subscription = subscriptionService.getOneById(model.getSubscriptionId());

        // Create entity
        return UserSubscriptionEntity.builder()
                .user(user)
                .subscription(subscription)
                .expirationDay(LocalDate.now().plusDays(subscription.getDurationDays()))
                .build();
    }

    // dima the best kotEk

    /**
     * Если у юзера есть актуальная подписка и добавляется другая подписка, то день сгорания вычисляется так:
     * 1. Вычисляется количество оставшихся дней до истечения текущей подписки.
     * 2. Это количество дней преобразуется в эквивалентную денежную стоимость с учетом стоимости текущей подписки.
     * 3. Вычисляется количество дней для новой подписки, что имеют такую же эквивалентную стоимость.
     * 4. К этому числу добавляется количество дней действия новой подписки.
     * 5. Полученное итоговое количество дней добавляется к текущей дате, и это будет днем истечения срока новой подписки.
     * @param oldSub Сущность подписки, которая была до обновления.
     * @param newSub Сущность подписки, которая будет после подписки.
     * @param oldExpirationDay День сгорания старой подписки.
     * @return Дату, когда должна сгореть новая подписка.
     */
    private LocalDate calculateNewExpiration(SubscriptionEntity oldSub, SubscriptionEntity newSub, LocalDate oldExpirationDay) {
        LocalDate today = LocalDate.now();

        int dayRemain = (int) ChronoUnit.DAYS.between(today, oldExpirationDay);
        // check for non-positive val
        if (dayRemain <= 0) {
            return today.plusDays(newSub.getDurationDays());
        }

        int oldSubPrice = oldSub.getPriceInRub(), newSubPrice = newSub.getPriceInRub();
        int oldSubDur = oldSub.getDurationDays();

        // calculate new subscription duration
        int newSubDur = (int) ( dayRemain * ( (double) oldSubPrice / oldSubDur ) + newSubPrice ) / newSubPrice;

        return LocalDate.now().plusDays(newSubDur);
    }

    /**
     * Обновляет день сгорания подписки для юзера.
     * Если подписка изменилась, то новый день сгорания вычисляется с помощью calculateNewExpiration().
     * @param entity Существующая связь юзера и подписки.
     * @param model Указания для какого юзера и подписки следует произвести обновление.
     * @return Сущность связи юзера и подписки.
     * @throws SubscriptionNotFoundException Если указанная подписка не найдена.
     */
    private UserSubscriptionEntity update(@NotNull UserSubscriptionEntity entity, @NotNull UserSubscriptionFromRequest model)
            throws SubscriptionNotFoundException {
        // check if subscription is changing.
        if ( entity.getSubscription().getId().equals(model.getSubscriptionId()) ) {
            // if not changed, just add subscription duration days
            SubscriptionEntity subscription = entity.getSubscription();
            entity.setExpirationDay(
                    entity.getExpirationDay().plusDays(subscription.getDurationDays())
            );
        } else {
            // else calculate new expiration day
            // load new subscription
            SubscriptionEntity newSubscription = subscriptionService.getOneById(model.getSubscriptionId());

            // calc new expiration day
            LocalDate newExpirationDay = calculateNewExpiration(
                    entity.getSubscription(),
                    newSubscription,
                    entity.getExpirationDay()
            );

            entity.setSubscription(newSubscription);
            entity.setExpirationDay(newExpirationDay);
        }

        return entity;
    }

    @Override
    public UserSubscriptionEntity addSubscriptionToUser(@NotNull UserSubscriptionFromRequest model)
            throws UserSubscriptionValidationFailedException, EntityNotFoundException {
        // do validation
        Validator validator = UserSubscriptionValidator.validateAllFields(model);
        if (validator.hasFails()) {
            throw new UserSubscriptionValidationFailedException(validator.toString());
        }

        Optional<UserSubscriptionEntity> entityOptional = userSubscriptionRepo.findByUserId(model.getUserId());
        UserSubscriptionEntity entity;
        // check if there already exist entity with this user and add sub
        if (entityOptional.isPresent()) {
            entity = update(entityOptional.get(), model);
        } else {
            entity = create(model);
        }

        return userSubscriptionRepo.save(entity);
    }

    @Override
    public UserSubscriptionEntity deleteByUserId(@NotNull Long userId) throws UserSubscriptionNotFoundException {
        // load entity
        UserSubscriptionEntity entity = userSubscriptionRepo.findByUserId(userId)
                .orElseThrow(
                        () -> new UserSubscriptionNotFoundException(
                                "Can't find subscription for user with id %d".formatted(userId)
                        )
                );

        userSubscriptionRepo.delete(entity);

        return entity;
    }

    @Override
    public Boolean isUserCanCreatePeer(@NotNull Long userId, int userExistingPeersNumber) {
        // try to load entity, if failed then return false
        UserSubscriptionEntity entity;
        try {
            entity = getOneByUserId(userId);
        } catch (UserSubscriptionNotFoundException e) {
            return Boolean.FALSE;
        }

        // get maximum available peer number
        int maxPeerNumber = entity.getSubscription().getPeersAvailable();

        return userExistingPeersNumber < maxPeerNumber;
    }

    @Override
    @Scheduled(cron = "0 10 13 * * ?")
    public void checkExpirationDay() throws EntityNotFoundException, ReminderValidationFailedException, PeerConnectHandlerException {
        final LocalDate TODAY = LocalDate.now();
        final String TODAY_REMINDER_TEXT = "Твоя подписка сгорела :(\n\nЕсли желаешь продолжить пользоваться сервисом, то стоит оплатить подписку";
        final String TOMORROW_REMINDER_TEXT = "Хей! Завтра сгорает твоя подписка!\n\nЕсли желаешь продолжить пользоваться сервисом, то стоит оплатить подписку";

        log.info("Checking for expirations");

        // load entities that expire tomorrow
        List<UserSubscriptionEntity> expireTomorrow = userSubscriptionRepo.findByExpirationDay(
                TODAY.plusDays(1)
        );

        // notify users in list
        for(UserSubscriptionEntity entity: expireTomorrow) {
            reminderService.create(
                    ReminderToCreate.builder()
                            .reminderType(ReminderType.EXPIRE_TOMORROW)
                            .userId(entity.getUser().getId())
                            .payload(TOMORROW_REMINDER_TEXT)
                            .build()
            );
        }

        // load entities that expire today
        List<UserSubscriptionEntity> expireToday = userSubscriptionRepo.findAllByFromExpirationDay(
                TODAY
        );

        // notify users in list, delete entities, and deactivate peers for this users
        for(UserSubscriptionEntity user: expireToday) {
            reminderService.create(
                    ReminderToCreate.builder()
                            .reminderType(ReminderType.EXPIRE_TOMORROW)
                            .userId(user.getUser().getId())
                            .payload(TODAY_REMINDER_TEXT)
                            .build()
            );
            userSubscriptionRepo.delete(user);
            for(PeerEntity peer: peerService.getAllByUserId(user.getId())) {
                peerService.deactivatePeerOnHostById(peer.getId());
            }
        }
    }
}
