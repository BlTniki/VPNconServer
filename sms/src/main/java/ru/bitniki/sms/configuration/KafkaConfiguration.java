package ru.bitniki.sms.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.validation.annotation.Validated;
import ru.bitniki.sms.domain.subscriptions.dto.UserSubscriptionEvent;

@Validated
@ConditionalOnProperty(prefix = "kafka", name = "enable", havingValue = "true")
@EnableKafka
@ConfigurationProperties(prefix = "kafka")
public record KafkaConfiguration(@NotNull Producer producer) {

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, producer.bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic subscriptionUpdatesTopic() {
        return TopicBuilder
                .name(producer.topic.name)
                .partitions(producer.topic.partitions)
                .replicas(producer.topic.replicas)
                .compact()
                .build();
    }

    @Bean
    public ProducerFactory<String, UserSubscriptionEvent> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producer.bootstrapServers);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, producer.clientId);
        props.put(ProducerConfig.ACKS_CONFIG, producer.acksMode);
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, (int) producer.deliveryTimeout.toMillis());
        props.put(ProducerConfig.LINGER_MS_CONFIG, producer.lingerMs);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, producer.batchSize);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, producer.maxInFlightPerConnection);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, producer.enableIdempotence);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, UserSubscriptionEvent> userSubscriptionProducer(
            ProducerFactory<String, UserSubscriptionEvent> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }


    record Topic(
            String name,
            Integer partitions,
            Integer replicas
    ) {}

    record Producer(
            String bootstrapServers,
            String clientId,
            String acksMode,
            Duration deliveryTimeout,
            Integer lingerMs,
            Integer batchSize,
            Integer maxInFlightPerConnection,
            Boolean enableIdempotence,
            Topic topic
    ) {}
}
