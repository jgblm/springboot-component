package top.jgblm.mq.rocket.subscription.sql;

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
import java.util.function.Consumer;

@SpringBootApplication
public class RocketMqSubscriptionSqlApplication {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RocketMqSubscriptionSqlApplication.class);

    @Resource
    private StreamBridge streamBridge;

    public static void main(String[] args) {
        SpringApplication.run(RocketMqSubscriptionSqlApplication.class, args);
    }

    /**
     * color array.
     */
    public static final String[] color = new String[]{"red1", "red2", "red3", "red4", "red5"};

    /**
     * price array.
     */
    public static final Integer[] price = new Integer[]{1, 2, 3, 4, 5};

    @Bean
    public ApplicationRunner producer() {
        return args -> {
            for (int i = 0; i < 100; i++) {
                String key = "KEY" + i;
                Map<String, Object> headers = new HashMap<>();
                headers.put(MessageConst.PROPERTY_KEYS, key);
                headers.put("color", color[i % color.length]);
                headers.put("price", price[i % price.length]);
                headers.put(MessageConst.PROPERTY_ORIGIN_MESSAGE_ID, i);
                Message<SimpleMsg> msg = new GenericMessage<>(new SimpleMsg("Hello RocketMQ " + i), headers);
                streamBridge.send("producer-out-0", msg);
            }
        };
    }

    @Bean
    public Consumer<Message<SimpleMsg>> consumer() {
        return msg -> {
            String colorHeaderKey = "color";
            String priceHeaderKey = "price";
            log.info(Thread.currentThread().getName() + " Receive New Messages: " + msg.getPayload().getMsg() + " COLOR:" +
                msg.getHeaders().get(colorHeaderKey).toString() + " " +
                "PRICE: " + msg.getHeaders().get(priceHeaderKey).toString());
        };
    }
}
