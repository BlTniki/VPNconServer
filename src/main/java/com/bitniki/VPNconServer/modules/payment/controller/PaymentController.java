package com.bitniki.VPNconServer.modules.payment.controller;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;
import com.bitniki.VPNconServer.modules.payment.entity.PaymentEntity;
import com.bitniki.VPNconServer.modules.payment.exception.PaymentNotFoundException;
import com.bitniki.VPNconServer.modules.payment.exception.PaymentValidationFailedException;
import com.bitniki.VPNconServer.modules.payment.model.PaymentToCreate;
import com.bitniki.VPNconServer.modules.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    /**
     * Выгрузка всех платежей из БД.
     * @return Список всех платежей.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('any') & hasAuthority('payment:read')")
    public ResponseEntity<List<PaymentEntity>> getAll() {
        return ResponseEntity.ok(
                StreamSupport.stream(paymentService.getAll(), false)
                        .toList()
        );
    }

    /**
     * Получение платежа по его uuid.
     * @param uuid Идентификатор платежа.
     * @return Платёж по-данному uuid.
     * @throws PaymentNotFoundException Если платёж с данным uuid не найден.
     */
    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('any') & hasAuthority('payment:read')")
    public ResponseEntity<PaymentEntity> getOneByUuid(@PathVariable String uuid)
            throws PaymentNotFoundException {
        return ResponseEntity.ok(paymentService.getOneByUuid(uuid));
    }

    /**
     * Ендпоинт для создания платежа и получения формы оплаты.
     * Получает параметры userId и subscriptionId и возвращает HTML страницу с формой оплаты.
     * @param userId Id юзера, для которого следует добавить подписку после оплаты.
     * @param subscriptionId Id подписки, которую следует добавить юзеру после оплаты.
     * @return HTML страница с формой оплаты.
     * @throws EntityNotFoundException если юзер или подписка не найдена.
     */
    @GetMapping("/create")
    public ResponseEntity<String> createPaymentAndGetPaymentForm(@RequestParam Long userId, @RequestParam Long subscriptionId)
            throws EntityNotFoundException, PaymentValidationFailedException {
        PaymentToCreate model = new PaymentToCreate(userId, subscriptionId);
        return ResponseEntity.ok(
                paymentService.createPaymentAndRenderHTML(model)
        );
    }

    /**
     * Метод для получения и дальнейшей обработки уведомления об оплате от платёжного провайдера.
     * @param map Тело уведомления.
     * @return "Success" и код 200 вне зависимости от обработки уведомления.
     */
    @PostMapping("/notification")
    public ResponseEntity<String> processNotification(@RequestBody Map<String, String> map) {
        paymentService.processNotification(map);
        return ResponseEntity.ok("Success");
    }

}
