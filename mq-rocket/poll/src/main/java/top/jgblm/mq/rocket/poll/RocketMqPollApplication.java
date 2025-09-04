package top.jgblm.mq.rocket.poll;

import jakarta.annotation.Resource;
import org.apache.rocketmq.common.message.MessageConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import top.jgblm.mq.rocket.common.SimpleMsg;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class RocketMqPollApplication {

    private static final Logger log = LoggerFactory.getLogger(RocketMqPollApplication.class);

    @Resource
    private StreamBridge streamBridge;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RocketMqPollApplication.class, args);
        PollableMessageSource destIn = context.getBean(PollableMessageSource.class);
        new Thread(() -> {
            while (true) {
                try {
                    destIn.poll((m) -> {
                        SimpleMsg newPayload = (SimpleMsg) m.getPayload();
                        log.info(newPayload.getMsg());
                    }, new ParameterizedTypeReference<SimpleMsg>() {
                    });
                    Thread.sleep(1000);

                } catch (Exception e) {
                    // handle failure
                }
            }
        }).start();
    }

    @Bean
    public ApplicationRunner producer() {
        return args -> {
            for (int i = 0; i < 15; i++) {
                String key = "KEY" + i;
                Map<String, Object> headers = new HashMap<>();
                headers.put(MessageConst.PROPERTY_KEYS, key);
                Message<SimpleMsg> msg = new GenericMessage<>(
                    new SimpleMsg("Hello RocketMQ " + i), headers);
                streamBridge.send("producer-out-0", msg);
            }
        };
    }


}
