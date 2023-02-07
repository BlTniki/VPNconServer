package com.bitniki.VPNconServer.service;

import com.bitniki.VPNconServer.entity.MailEntity;
import com.bitniki.VPNconServer.entity.PeerEntity;
import com.bitniki.VPNconServer.entity.SubscriptionEntity;
import com.bitniki.VPNconServer.entity.UserEntity;
import com.bitniki.VPNconServer.exception.notFoundException.EntityNotFoundException;
import com.bitniki.VPNconServer.exception.notFoundException.SubscriptionNotFoundException;
import com.bitniki.VPNconServer.exception.notFoundException.UserNotFoundException;
import com.bitniki.VPNconServer.exception.validationFailedException.MailValidationFailedException;
import com.bitniki.VPNconServer.exception.validationFailedException.SubscriptionValidationFailedException;
import com.bitniki.VPNconServer.role.Role;
import com.bitniki.VPNconServer.model.Subscription;
import com.bitniki.VPNconServer.model.UserWithRelations;
import com.bitniki.VPNconServer.repository.SubscriptionRepo;
import com.bitniki.VPNconServer.repository.UserRepo;
import com.bitniki.VPNconServer.validator.SubscriptionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Service
@EnableScheduling
public class SubscriptionService {
    @Autowired
    private SubscriptionRepo subscriptionRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    PeerService peerService;
    @Autowired
    MailService mailService;

    private final String notifyToPay = "Хей! Завтра сгорит твоя подписка! Думаю что стоит продлить";
    private final String notifyAboutExpire = "Твоя подписка сгорела из-за неуплаты(";

    public List<Subscription> getAll() {
        List<SubscriptionEntity> subscriptions = new ArrayList<>();
        subscriptionRepo.findAll().forEach(subscriptions::add);
        return subscriptions.stream().map(Subscription::toModel).toList();
    }

    public Subscription getById(Long id) throws SubscriptionNotFoundException {
        SubscriptionEntity entity = subscriptionRepo.findById(id)
                .orElseThrow(
                        () -> new SubscriptionNotFoundException("Subscription not found!")
                );
        return Subscription.toModel(entity);
    }

    /*
    Get subs by role
     */
    public List<Subscription> getByRole(Role role) {
        List<SubscriptionEntity> subscriptions = subscriptionRepo.findByRole(role);
        return subscriptions.stream().map(Subscription::toModel).toList();
    }

    public Subscription createSubscription(SubscriptionEntity entity) throws SubscriptionValidationFailedException {
        //valid entity
        SubscriptionValidator validator = SubscriptionValidator.validateAllFields(entity);
        if(validator.hasFails()) {
            throw new SubscriptionValidationFailedException(validator.toString());
        }
        //save and return
        return Subscription.toModel(subscriptionRepo.save(entity));
    }

    public Subscription deleteSubscription(Long id) throws SubscriptionNotFoundException {
        SubscriptionEntity entity = subscriptionRepo.findById(id)
                .orElseThrow(
                        () -> new SubscriptionNotFoundException("Subscription not found!")
                );
        subscriptionRepo.delete(entity);
        return Subscription.toModel(entity);
    }

    /**
     * Add subscription to user
     *     add subscription and set expiration day.
     *     If the user's expiration date is zero or before than today or subscription changes, set a new date.
     *     Else, add to exist user's expiration date
     * @param subsId subscription entity id
     * @param userId user entity id
     * @return UserWithRelations
     * @throws EntityNotFoundException if user or subscription not found by id
     */
    public UserWithRelations addSubscriptionToUser(Long subsId, Long userId)
            throws EntityNotFoundException {
        //Find user
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(
                        () -> new UserNotFoundException("User not found!")
                );
        //Find subscription
        SubscriptionEntity subscription = subscriptionRepo.findById(subsId)
                .orElseThrow(
                        () -> new SubscriptionNotFoundException("Subscription not found!")
                );

        //Set expiration day
        LocalDate dateToday = LocalDate.now();
        LocalDate newExpirationDay;
        //if the user's expiration date is zero or before than today or subscription changes, set a new date.
        //else, add to exist user's expiration date
        if(user.getSubscriptionExpirationDay() == null
                || user.getSubscriptionExpirationDay().isBefore(dateToday)
                || !user.getSubscription().equals(subscription)) {
            newExpirationDay = dateToday.plusDays(subscription.getDays());
        } else {
            newExpirationDay = user.getSubscriptionExpirationDay().plusDays(subscription.getDays());
        }
        user.setSubscriptionExpirationDay(newExpirationDay);
        //Add subs to user
        user.setSubscription(subscription);

        //Activate peers.
        // Activate only so many peers that does not exceed the number of available
        List<PeerEntity> userPeers = user.getPeers();
        //Iterate until i less than peers available and less that userPeers size
        for (int i = 0; i<subscription.getPeersAvailable() && i<userPeers.size(); i++) {
            //activate each peer by id
            peerService.activatePeerOnHost(userPeers.get(i).getId());
        }

        //save user and return
        return UserWithRelations.toModel(userRepo.save(user));
    }

    public UserWithRelations deleteSubscriptionFromUser(Long userId) throws EntityNotFoundException {
        //Find user
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(
                        () -> new UserNotFoundException("User not found!")
                );
        //clean subscription and expiration day
        user.setSubscription(null);
        user.setSubscriptionExpirationDay(null);
        //deactivate all peers
        if(user.getPeers() != null) {
            for (PeerEntity peer : user.getPeers()) {
                peerService.deactivatePeerOnHost(peer.getId());
            }
        }
        //Save and return
        return UserWithRelations.toModel(userRepo.save(user));
    }

    /*
    Scheduled task for every day at 12:00 PM (noon)
    It find users by subscriptionExpirationDay
    If ExpirationDay tomorrow — send notification
    If ExpirationDay today — use deleteSubscriptionFromUser
     */
    @Scheduled(cron = "0 0 12 * * ?")
    public void checkSubscriptionExpiration() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        List<UserEntity> users = userRepo.findBySubscriptionExpirationDayIsNotNull();
        for (UserEntity user: users) {
            LocalDate expireDate = user.getSubscriptionExpirationDay();
            if(expireDate.equals(tomorrow)) {
                //Notice User
                MailEntity mail = new MailEntity();
                mail.setForTelegram(true);
                mail.setPayload(notifyToPay);
                try {
                    mailService.createByUserId(user.getId(), mail);
                } catch (UserNotFoundException | MailValidationFailedException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
            if(expireDate.isBefore(tomorrow)) {
                try {
                    deleteSubscriptionFromUser(user.getId());
                } catch (EntityNotFoundException e) {
                    throw new RuntimeException(e);
                }
                //Notice User
                MailEntity mail = new MailEntity();
                mail.setForTelegram(true);
                mail.setPayload(notifyAboutExpire);
                try {
                    mailService.createByUserId(user.getId(), mail);
                } catch (UserNotFoundException | MailValidationFailedException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
        }
    }
}
