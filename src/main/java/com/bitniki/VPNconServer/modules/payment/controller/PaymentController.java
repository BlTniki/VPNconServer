package com.bitniki.VPNconServer.modules.payment.controller;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;
import com.bitniki.VPNconServer.modules.payment.model.PaymentToCreate;
import com.bitniki.VPNconServer.modules.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

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
            throws EntityNotFoundException {
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
