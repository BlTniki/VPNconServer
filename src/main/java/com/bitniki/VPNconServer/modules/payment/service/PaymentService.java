package com.bitniki.VPNconServer.modules.payment.service;

import com.bitniki.VPNconServer.modules.mail.service.MailService;
import com.bitniki.VPNconServer.modules.subscription.entity.SubscriptionEntity;
import com.bitniki.VPNconServer.modules.subscription.service.SubscriptionService;
import com.bitniki.VPNconServer.modules.payment.entity.PaymentEntity;
import com.bitniki.VPNconServer.modules.payment.repository.PaymentRepo;
import com.bitniki.VPNconServer.exception.EntityNotFoundException;
import com.bitniki.VPNconServer.modules.subscription.exception.SubscriptionNotFoundException;
import com.bitniki.VPNconServer.modules.user.exception.UserNotFoundException;
import com.bitniki.VPNconServer.modules.subscription.repository.SubscriptionRepo;
import com.bitniki.VPNconServer.modules.user.repository.UserRepo;
import com.qiwi.billpayments.sdk.client.BillPaymentClient;
import com.qiwi.billpayments.sdk.model.BillStatus;
import com.qiwi.billpayments.sdk.model.MoneyAmount;
import com.qiwi.billpayments.sdk.model.in.PaymentInfo;
import com.qiwi.billpayments.sdk.model.out.BillResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
@Service
public class PaymentService {
    @Value("${server.url}")
    private String serverUrl;
    @Value("${qiwi.publicKey}")
    private String publicKey;
    @Autowired
    private PaymentRepo paymentRepo;
    @Autowired
    private SubscriptionRepo subscriptionRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private MailService mailService;
    @Autowired
    private BillPaymentClient paymentClient;

    private final String rawPayUrl = "https://oplata.qiwi.com/create";
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm");
    @SuppressWarnings("FieldCanBeLocal")
    private final String rawPage = """
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="referrer" content="no-referrer-when-downgrade">
    <title>Pay</title>
    <link href="https://fonts.googleapis.com/css?family=Nunito+Sans:400,400i,700,900&display=swap" rel="stylesheet">
</head>
<style>
      body {
        text-align: center;
        padding: 40px 0;
        background: #EBF0F5;
      }
        h1 {
          color: #88B04B;
          font-family: "Nunito Sans", "Helvetica Neue", sans-serif;
          font-weight: 900;
          font-size: 40px;
          margin-bottom: 10px;
        }
        p {
          color: #404F5E;
          font-family: "Nunito Sans", "Helvetica Neue", sans-serif;
          font-size:20px;
          margin: 0;
        }
      p2 {
        color: #9ABC66;
        font-size: 100px;
        line-height: 200px;
      }
      .card {
        background: white;
        padding: 60px;
        width: 300px;
        border-radius: 4px;
        box-shadow: 0 2px 3px #C8D0D8;
        display: inline-block;
        margin: 0 auto;
      }
    </style>
<body>
<div class="card">
    <div style="border-radius:200px; height:200px; width:200px; background: #F8FAF5; margin:0 auto;">
        <p2 class="checkmark">🏪</p2>
    </div>
    <h1>Покупка</h1>
    <p>Подписка:<br/>
        Цена в рублях: %s<br/>
        Кол-во дней: %s<br/>
        Кол-во конфигов: %s<br/>
        <a href="%s">Оплатить</a>
    </p>
</div>
</body>
</html>
""";

    /**
     * Check bill by given billId.
     * On success payment add subscription to user
     * Send user html with payment status
     * @param billId bill id from path var
     * @return html status page name
     */
    public String checkBillAndAddSubToUser(String billId) {
        //Check bill
        BillResponse response;
        response = paymentClient.getBillInfo(billId);

        //if status == PAID
        if(response.getStatus().getValue().equals(BillStatus.PAID)) {
            //Load payment from repo
            Optional<PaymentEntity> entity = paymentRepo.findByBillId(billId);
            if(entity.isEmpty()) {
                return "error.html";
            }
            PaymentEntity payment = entity.get();
            Long subsId = payment.getSubsId();
            Long userId = payment.getUserId();
            //add subscription to user
            try {
                subscriptionService.addSubscriptionToUser(subsId, userId);
            } catch (EntityNotFoundException e) {
                //return error.html
                return "error.html";
            }
            //Save cheque
            mailService.saveCheque(
                    "User "
                    + userId
                    + "Bayed sub with id "
                    + subsId + "\n at"
                    + LocalDateTime.now()
                    + "\n with billId "
                    + billId
            );
            //delete bill from repo
            paymentRepo.delete(payment);
            // return success.html
            return "success.html";
        }
        //if status == WAITING
        if(response.getStatus().getValue().equals(BillStatus.WAITING)) {
            // return waiting.html
            return "waiting.html";
        }
        //if status != (PAID or WAITING)
        //return error.html
        return "error";
    }

    /**
     * Render html page with payment link and write new record to payment table
     * @param subsId subscription id
     * @param userId user id
     * @return payment url
     * @throws EntityNotFoundException if subscription or user not found
     */
    public String createBill(Long subsId, Long userId) throws EntityNotFoundException {
        //Check entities existing
        userRepo.findById(userId)
                .orElseThrow(
                        () -> new UserNotFoundException("User not found!")
                );
        SubscriptionEntity subscription = subscriptionRepo.findById(subsId)
                .orElseThrow(
                        () -> new SubscriptionNotFoundException("Subscription not found!")
                );
        //Create billId
        String billId = UUID.randomUUID().toString();

        //Create new record
        PaymentEntity payment = new PaymentEntity();
        payment.setBillId(billId);
        payment.setSubsId(subsId);
        payment.setUserId(userId);
        paymentRepo.save(payment);


        //make payUrl
        MoneyAmount amount = new MoneyAmount(
                BigDecimal.valueOf(subscription.getPriceInRub()),
                Currency.getInstance("RUB")
        );
        String successUrl = serverUrl + "/payments/bill/" + billId;
        PaymentInfo paymentInfo = new PaymentInfo(publicKey, amount, billId, successUrl);
        String payUrl = paymentClient.createPaymentForm(paymentInfo);

        //return rendered page
        return String.format(
                rawPage,
                subscription.getPriceInRub(),
                subscription.getDays(),
                subscription.getPeersAvailable(),
                payUrl
        );
    }
}
