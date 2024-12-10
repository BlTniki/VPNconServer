package ru.bitniki.sms;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.bitniki.sms.configuration.AppConfiguration;

@ActiveProfiles("test")
@SpringBootTest(classes = AppConfiguration.class)
public class SmsApplicationTests {
    @Test
    void contextLoads() {
    }
}

