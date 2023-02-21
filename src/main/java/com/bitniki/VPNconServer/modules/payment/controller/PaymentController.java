package com.bitniki.VPNconServer.modules.payment.controller;

import com.bitniki.VPNconServer.exception.EntityNotFoundException;
import com.bitniki.VPNconServer.modules.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/bill/{billId}")
    public RedirectView checkBill(@PathVariable String billId) {
        //Get status page name
        String statusPage = paymentService.checkBillAndAddSubToUser(billId);
        //Redirect to status page
        return new RedirectView("/payments/status/" + statusPage);
    }

    @GetMapping("/bill")
    public ResponseEntity<String> createBill(@RequestParam Long subs_id, @RequestParam Long user_id)
            throws EntityNotFoundException {
        return ResponseEntity.ok(paymentService.createBill(subs_id, user_id));
    }
}
