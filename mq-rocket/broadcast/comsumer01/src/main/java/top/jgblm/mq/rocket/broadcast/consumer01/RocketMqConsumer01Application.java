package top.jgblm.mq.rocket.broadcast.consumer01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import top.jgblm.mq.rocket.common.SimpleMsg;

import java.util.function.Consumer;

@SpringBootApplication
public class RocketMqConsumer01Application {
    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(RocketMqConsumer01Application.class, args);
    }

    @Bean
    public Consumer<Message<SimpleMsg>> consumer() {
        return message -> {
            SimpleMsg simpleMsg = message.getPayload();
            System.out.println("消费者01接收到消息：" + simpleMsg.getMsg());
        };
    }
}
