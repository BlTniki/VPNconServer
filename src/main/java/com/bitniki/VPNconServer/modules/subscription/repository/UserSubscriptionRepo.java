package com.bitniki.VPNconServer.modules.subscription.repository;

import com.bitniki.VPNconServer.modules.subscription.entity.UserSubscriptionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserSubscriptionRepo extends CrudRepository<UserSubscriptionEntity, Long> {
    Optional<UserSubscriptionEntity> findByUserId(Long userId);

    /**
     * Возвращает записи, у которых день сгорания равен данному.
     * @param expirationDay Искомый день сгорания.
     * @return Записи {@link UserSubscriptionEntity}.
     */
    List<UserSubscriptionEntity> findByExpirationDay(LocalDate expirationDay);

    /**
     * Возвращает записи, у которых день сгорания раньше или равен данному.
     * @param expirationDay День, до которого должен быть день сгорания. Включительно.
     * @return Записи {@link UserSubscriptionEntity}.
     */
    @Query("SELECT us FROM UserSubscriptionEntity us WHERE us.expirationDay <= :expirationDay")
    List<UserSubscriptionEntity> findAllByFromExpirationDay(LocalDate expirationDay);
}
