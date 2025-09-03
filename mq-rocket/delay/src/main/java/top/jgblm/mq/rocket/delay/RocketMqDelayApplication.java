package top.jgblm.mq.rocket.delay;

import jakarta.annotation.Resource;
import org.apache.rocketmq.common.message.MessageConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import top.jgblm.mq.rocket.common.SimpleMsg;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@SpringBootApplication
public class RocketMqDelayApplication {
    private static final Logger log = LoggerFactory
        .getLogger(RocketMqDelayApplication.class);

    @Resource
    private StreamBridge streamBridge;

    public static void main(String[] args) {
        SpringApplication.run(RocketMqDelayApplication.class, args);
    }

    @Bean
    public ApplicationRunner producerDelay() {
        return args -> {
            for (int i = 0; i < 10; i++) {
                String key = "KEY" + i;
                Map<String, Object> headers = new HashMap<>();
                headers.put(MessageConst.PROPERTY_KEYS, key);
                headers.put(MessageConst.PROPERTY_ORIGIN_MESSAGE_ID, i);
                headers.put(MessageConst.PROPERTY_DELAY_TIME_LEVEL, 2);
                Message<SimpleMsg> msg = new GenericMessage<>(new SimpleMsg("Delay RocketMQ " + i), headers);
                streamBridge.send("producer-out-0", msg);
            }
            log.info("Delay RocketMQ Send Success");
        };
    }

    @Bean
    public Consumer<Message<SimpleMsg>> consumer() {
        return msg -> {
            log.info(Thread.currentThread().getName() + " Consumer Receive New Messages: " + msg.getPayload().getMsg());
        };
    }
}
