package com.bitniki.VPNconServer.modules.payment.service;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;
import com.bitniki.VPNconServer.modules.payment.entity.PaymentEntity;
import com.bitniki.VPNconServer.modules.payment.exception.PaymentNotFoundException;
import com.bitniki.VPNconServer.modules.payment.model.PaymentToCreate;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Spliterator;

public interface PaymentService {

    /**
     * Возвращает список всех существующих платежей.
     * @return {@link Spliterator} с платежами.
     */
    Spliterator<PaymentEntity> getAll();

    /**
     * Возвращает сущность платежа по-данному uuid.
     * @param uuid Идентификатор платежа.
     * @return Сущность платежа.
     * @throws PaymentNotFoundException Если сущность не найдена по-данному uuid.
     */
    PaymentEntity getOneByUuid(@NotNull Long uuid) throws PaymentNotFoundException;

    /**
     * Создаёт новую сущность платежа и возвращает HTML страницу в виде строки для оплаты подписки.
     * @param model Модель {@link PaymentToCreate} с указанными юзером и подпиской.
     * @return HTML страницу в виде строки.
     * @throws EntityNotFoundException Если юзер или подписка не найдена.
     */
    String createPaymentAndRenderHTML(@NotNull PaymentToCreate model) throws EntityNotFoundException;

    /**
     * Метод для обработки уведомлений об оплате от платёжного провайдера и завершении платежа.
     * Получает уведомление в виде Map который метод преобразует в сущность уведомления
     * при помощи класса провайдера.
     * @param map Map с данными уведомления.
     */
    void processNotification(@NotNull Map<String, String> map);

    /**
     * Метод для проверки наличия пропущенных платежей в истории платёжного провайдера.
     * При нахождении оных, метод завершит платёж.
     * Предполагается автоматический запуск раз в n минут при помощи планировщика.
     */
    void checkForMissedPayments();
}
