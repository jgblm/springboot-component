package top.jgblm.mq.rocket.order;

import com.alibaba.cloud.stream.binder.rocketmq.support.RocketMQMessageConverterSupport;
import jakarta.annotation.Resource;
import org.apache.rocketmq.common.message.MessageConst;
import org.slf4j.Logger;
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
public class RocketMqOrderApplication {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(RocketMqOrderApplication.class);

    @Resource
    private StreamBridge streamBridge;

    public static final String[] tags = new String[]{"TagA", "TagB", "TagC", "TagD", "TagE"};

    public static void main(String[] args) {
        SpringApplication.run(RocketMqOrderApplication.class, args);
    }

    @Bean
    public ApplicationRunner producer() {
        return args -> {
            for (int i = 0; i < 100; i++) {
                String key = "KEY" + i;
                Map<String, Object> headers = new HashMap<>();
                headers.put(MessageConst.PROPERTY_KEYS, key);
                headers.put(MessageConst.PROPERTY_TAGS, tags[i % tags.length]);
                headers.put(MessageConst.PROPERTY_ORIGIN_MESSAGE_ID, i);
                Message<SimpleMsg> msg = new GenericMessage<>(new SimpleMsg("Hello RocketMQ " + i), headers);
                streamBridge.send("producer-out-0", msg);
            }
        };
    }

    @Bean
    public Consumer<Message<SimpleMsg>> consumer() {
        return msg -> {
            String tagHeaderKey = RocketMQMessageConverterSupport.toRocketHeaderKey(
                MessageConst.PROPERTY_TAGS);
            log.info(Thread.currentThread().getName() + " Receive New Messages: " + msg.getPayload().getMsg() + " TAG:" +
                msg.getHeaders().get(tagHeaderKey).toString());
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        };

    }
}
