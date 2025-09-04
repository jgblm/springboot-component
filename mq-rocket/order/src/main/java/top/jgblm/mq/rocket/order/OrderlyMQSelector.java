package top.jgblm.mq.rocket.order;

import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderlyMQSelector implements MessageQueueSelector {
    /**
     * 顺序消息队列选择器
     * 同一个tag的消息会发送到同一个队列中，这样就能保证顺序消息
     */
    @Override
    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
        Integer id = (Integer) ((MessageHeaders) arg).get(MessageConst.PROPERTY_ORIGIN_MESSAGE_ID);
        int index = id % RocketMqOrderApplication.tags.length % mqs.size();
        return mqs.get(index);
    }
}
