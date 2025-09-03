package top.jgblm.mq.rocket.producer;

import jakarta.annotation.Resource;
import org.apache.rocketmq.common.message.MessageConst;
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

@SpringBootApplication
public class RocketMqApplication {
    @Resource
    private StreamBridge streamBridge;

    public static void main(String[] args) {
        SpringApplication.run(RocketMqApplication.class, args);
    }

    @Bean
    public ApplicationRunner producer() {
        return args -> {
            for (int i = 0; i < 10; i++) {
                String key = "KEY" + i;
                Map<String, Object> headers = new HashMap<>();
                headers.put(MessageConst.PROPERTY_KEYS, key);
                headers.put(MessageConst.PROPERTY_ORIGIN_MESSAGE_ID, i);

                Message<SimpleMsg> msg = new GenericMessage<SimpleMsg>(new SimpleMsg("Hello RocketMQ " + i), headers);
                streamBridge.send("producer-out-0", msg);
            }
        };
    }


}
