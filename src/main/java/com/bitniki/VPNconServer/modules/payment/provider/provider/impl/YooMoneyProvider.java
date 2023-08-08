package com.bitniki.VPNconServer.modules.payment.provider.provider.impl;

import com.bitniki.VPNconServer.modules.payment.provider.exception.NotificationValidationFailedException;
import com.bitniki.VPNconServer.modules.payment.provider.model.Notification;
import com.bitniki.VPNconServer.modules.payment.provider.model.impl.YooMoneyNotification;
import com.bitniki.VPNconServer.modules.payment.provider.provider.Provider;
import kotlin.NotImplementedError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class YooMoneyProvider extends Provider {
    private final String YOOMONEY_SECRET_KEY;
    private final String YOOMONEY_ACCOUNT;
    private final String SUCCESS_URL;

//    private final String HASH_PATTERN = "%s&%s&%s&%s&%s&%s&%s&%s&%s";
    @SuppressWarnings("FieldCanBeLocal")
    private final String RAW_PAGE = """
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
    <p>%s</p>
    <form method="POST" action="https://yoomoney.ru/quickpay/confirm">
        <input type="hidden" name="receiver" value="%s"/>
        <input type="hidden" name="label" value="%s"/>
        <input type="hidden" name="quickpay-form" value="button"/>
        <input type="hidden" name="sum" value="%s" data-type="number"/>
        <input type="hidden" name="successURL" value="%s"/>
        <input type="hidden" name="paymentType" value="AC"/>
        <input type="submit" value="Оплатить"/>
    </form>
</div>
</body>
</html>
""";
    @Override
    public String renderHtml(String body, String uuid, String amount) {
        return RAW_PAGE.formatted(
                body,
                YOOMONEY_ACCOUNT,
                uuid,
                amount,
                SUCCESS_URL
        );
    }

    /**
     * Проверяет, совпадает ли хэш уведомления с присланным хэшем.
     * @param toCheck Необходимые для проверки поля уведомления в виде "a&b&c&d&..."
     * @param hash Хэш, с которым следует сравнить
     * @return Истину, если хэши не совпадают.
     */
    private boolean isHashInvalid(String toCheck, String hash) {
        String toCheckHashed = DigestUtils.sha1Hex(toCheck);
        return !hash.equals(toCheckHashed);
    }

    @Override
    public Optional<Notification> makeNotification(Map<String, String> map) {
        // try to make notification
        try {
            // extruding needed fields
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            YooMoneyNotification notification = YooMoneyNotification.builder()
                    .notification_type(map.get("notification_type"))
                    .operation_id(map.get("operation_id"))
                    .amount(new BigDecimal(map.get("amount")))
                    .withdraw_amount(new BigDecimal(map.getOrDefault("withdraw_amount", "0")))
                    .currency(map.get("currency"))
                    .datetime(
                            LocalDateTime.parse(
                                    URLDecoder.decode(map.get("datetime"), StandardCharsets.UTF_8),
                                    formatter
                            )
                    )
                    .sender(map.getOrDefault("sender", ""))
                    .codepro(Boolean.valueOf(map.get("codepro")))
                    .sha1_hash(map.get("sha1_hash"))
                    .label(map.getOrDefault("label", ""))
                    .build();

            // check hash
            if (isHashInvalid(notification.toStringForHash(YOOMONEY_SECRET_KEY), notification.getSha1_hash())) {
                throw new NotificationValidationFailedException("Invalid hash");
            }

            // finally
            return Optional.of(notification);
        } catch (Exception e) {
            log.error("Failed to make Notification from this map:\n" + map + "\n with this error:\n" + e + "\n" + Arrays.toString(e.getStackTrace()));
            return Optional.empty();
        }
    }

    @Override
    public Set<Notification> getLastNotificationsFromHistory(LocalDateTime dayFrom) {
        // Эта фича не может быть реализованна
        // TODO: реализуй через почту.
        throw new NotImplementedError();
    }
}
