package top.jgblm.mq.rocket.tx;

import com.alibaba.cloud.stream.binder.rocketmq.constant.RocketMQConst;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;
import top.jgblm.mq.rocket.common.SimpleMsg;

import java.util.function.Consumer;

@SpringBootApplication
public class RocketMqTxApplication {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(RocketMqTxApplication.class);

    @Resource
    private StreamBridge streamBridge;
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(RocketMqTxApplication.class, args);
    }

    @Bean
    public ApplicationRunner producer() {
        return args -> {
            for (int i = 1; i <= 4; i++) {
                MessageBuilder<SimpleMsg> builder = MessageBuilder.withPayload(new SimpleMsg("Hello Tx msg " + i));
                builder.setHeader("test", String.valueOf(i))
                    .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
                builder.setHeader(RocketMQConst.USER_TRANSACTIONAL_ARGS, "binder");
                Message<SimpleMsg> msg = builder.build();
                streamBridge.send("producer-out-0", msg);
                System.out.println("send Msg:" + msg);
            }
        };
    }

    @Bean
    public Consumer<Message<SimpleMsg>> consumer() {
        return msg -> {
            Object arg = msg.getHeaders();
            log.info(Thread.currentThread().getName() + " Receive New Messages: " + msg.getPayload().getMsg() + " ARG:"
                + arg.toString());
        };
    }
}
