package com.bitniki.VPNconServer.modules.payment.provider;

import com.bitniki.VPNconServer.modules.payment.provider.model.Notification;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public abstract class Provider {
    /**
     * Генерирует HTML страницу с формой оплаты.
     * @param body Текст, который важно сообщить покупателю.
     * @param uuid идентификатор платежа, по которому устанавливается факт платежа.
     * @return HTML страницу в виде строки.
     */
    public abstract String renderHtml(String body, String uuid);

    /**
     * Создаёт {@link Notification} на основе уведомления от платёжного провайдера.
     * @param map Тело уведомления.
     * @return {@link Optional}, который может быть пустым если уведомление не прошло валидацию.
     */
    public abstract Optional<Notification> makeNotification(Map<String, String> map);

    /**
     * Возвращает множество уведомлений оплаты из истории платёжного провайдера,
     * которые были созданы позже указанного дня.
     * @param dayFrom День, раньше которого не интересуют уведомления.
     * @return Множество уведомлений.
     */
    public abstract Set<Notification> getLastNotificationsFromHistory(LocalDateTime dayFrom);
}
