package com.bitniki.VPNconServer.modules.payment.service.impl;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;
import com.bitniki.VPNconServer.modules.payment.entity.PaymentEntity;
import com.bitniki.VPNconServer.modules.payment.exception.PaymentNotFoundException;
import com.bitniki.VPNconServer.modules.payment.exception.PaymentValidationFailedException;
import com.bitniki.VPNconServer.modules.payment.model.PaymentToCreate;
import com.bitniki.VPNconServer.modules.payment.provider.Provider;
import com.bitniki.VPNconServer.modules.payment.provider.model.Notification;
import com.bitniki.VPNconServer.modules.payment.repository.PaymentRepo;
import com.bitniki.VPNconServer.modules.payment.service.PaymentService;
import com.bitniki.VPNconServer.modules.payment.status.PaymentStatus;
import com.bitniki.VPNconServer.modules.payment.validator.PaymentValidator;
import com.bitniki.VPNconServer.modules.subscription.entity.SubscriptionEntity;
import com.bitniki.VPNconServer.modules.subscription.model.UserSubscriptionFromRequest;
import com.bitniki.VPNconServer.modules.subscription.service.SubscriptionService;
import com.bitniki.VPNconServer.modules.subscription.service.UserSubscriptionService;
import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import com.bitniki.VPNconServer.modules.user.service.UserService;
import com.bitniki.VPNconServer.modules.user.util.EncryptionUtil.EncryptionUtil;
import com.bitniki.VPNconServer.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepo paymentRepo;
    @Autowired
    private Provider provider;
    @Autowired
    private EncryptionUtil encryptor;

    @Autowired
    private UserService userService;
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private UserSubscriptionService userSubscriptionService;

    @SuppressWarnings("FieldCanBeLocal")
    private final String PAYMENT_MSG =
"""
Подписка:<br/>
Цена в рублях: %s<br/>
Кол-во дней: %s<br/>
Кол-во конфигов: %s<br/>
""";

    /**
     * Генерирует uuid на основе аргументов метода.
     * @param userId Id юзера.
     * @param subscriptionId Id юзера.
     * @param toPay сумма к оплате.
     * @param timeStamp временая метка.
     * @return uuid.
     */
    private String generateUuid(Long userId, Long subscriptionId, BigDecimal toPay, LocalDateTime timeStamp) {
        String toEncode = "%d&%d&%s&%s".formatted(userId, subscriptionId, toPay.toString(), timeStamp.toString());

        return encryptor.encode(toEncode);
    }

    private void processPayment(Notification notification) {
        try {
            // load payment entity
            PaymentEntity payment = paymentRepo.findByUuid(notification.getUuid())
                    .orElseThrow(
                            () -> new PaymentNotFoundException("Can't find payment for this pretty valid notification:\n" + notification)
                    );

            // stop work if payment not IN_PROCESS
            if (!payment.getStatus().equals(PaymentStatus.IN_PROCESS)) {
                return;
            }

            //valid payment amount
            BigDecimal actualPaymentAmountToRequired = notification.getAmount()
                    .divide(payment.getToPay(), 2, RoundingMode.HALF_DOWN);
            // check that actual amount is not less than 90% of required.
            if (actualPaymentAmountToRequired.compareTo(BigDecimal.valueOf(0.9)) < 0) {
                payment.setStatus(PaymentStatus.FAILURE);
                paymentRepo.save(payment);
                throw new PaymentValidationFailedException(
                        "Payment amount is less than required for this payment:\n" + notification
                );
            }

            // set subscription for user
            UserSubscriptionFromRequest request = UserSubscriptionFromRequest.builder()
                    .userId(payment.getUserId())
                    .subscriptionId(payment.getSubscriptionId())
                    .build();
            userSubscriptionService.addSubscriptionToUser(request);

            // update payment status and save
            payment.setStatus(PaymentStatus.COMPLETED);
            paymentRepo.save(payment);

        } catch (Exception e) {
            log.error("Payment processing fails with this message:\n" + e.getMessage());
        } finally {
            log.info("process this Payment notification:\n" + notification);
        }
    }

    @Override
    public Spliterator<PaymentEntity> getAll() {
        return paymentRepo.findAll().spliterator();
    }

    @Override
    public PaymentEntity getOneByUuid(@NotNull String uuid) throws PaymentNotFoundException {
        return paymentRepo.findByUuid(uuid)
                .orElseThrow(
                        () -> new PaymentNotFoundException("Payment with uuid %s not found".formatted(uuid))
                );
    }

    @Override
    public String createPaymentAndRenderHTML(@NotNull PaymentToCreate model) throws EntityNotFoundException, PaymentValidationFailedException {
        // valid model
        Validator validator = PaymentValidator.validateAllFields(model);
        if(validator.hasFails()) {
            throw new PaymentValidationFailedException(validator.toString());
        }

        // load user
        UserEntity user = userService.getOneById(model.getUserId());
        // load subscription
        SubscriptionEntity subscription = subscriptionService.getOneById(model.getSubscriptionId());

        // get fields for Payment
        LocalDateTime now = LocalDateTime.now();
        BigDecimal toPay = BigDecimal.valueOf(subscription.getPriceInRub());
        String uuid = generateUuid(
                user.getId(),
                subscription.getId(),
                toPay,
                now
        );

        // create and save entity
        PaymentEntity entity = PaymentEntity.builder()
                .uuid(uuid)
                .userId(user.getId())
                .subscriptionId(subscription.getId())
                .toPay(toPay)
                .timeStamp(now)
                .status(PaymentStatus.IN_PROCESS)
                .build();
        paymentRepo.save(entity);

        // render HTML
        String msg = PAYMENT_MSG.formatted(
                subscription.getPriceInRub().toString(),
                subscription.getDurationDays().toString(),
                subscription.getDurationDays().toString()
        );
        return provider.renderHtml(msg, uuid);
    }

    @Override
    public void processNotification(@NotNull Map<String, String> map) {
        // do with notification
        provider.makeNotification(map).ifPresent(notification -> {

        });
    }

    @Override
    @Scheduled(cron = "0 0/5 * ? * * *")
    public void checkForMissedPayments() {
        // set day before that we don't watch payments
        final LocalDateTime FROM_DAY = LocalDateTime.now().minusDays(5);

        // get IN_PROCESS payments
        Set<String> inProgressUuid = paymentRepo.findUuidsByStatusAndMinTimestamp(
                PaymentStatus.IN_PROCESS,
                FROM_DAY
        );
        // stop work if empty
        if (inProgressUuid.isEmpty()) {
            return;
        }

        // load payments from Provider history
        Set<Notification> historyPayments = provider.getLastNotificationsFromHistory(FROM_DAY);

        // process payments
        for(Notification notification: historyPayments) {
            if (inProgressUuid.contains(notification.getUuid())) {
                processPayment(notification);
            }
        }
    }
}
