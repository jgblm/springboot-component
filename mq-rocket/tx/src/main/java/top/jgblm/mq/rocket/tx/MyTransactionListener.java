package top.jgblm.mq.rocket.tx;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

/**
 * 自定义事务监听器
 * 本地事务状态
 *  UNKNOW 对于状态不确定的消息，会通过回查机制最终确定消息状态
 *  ROLLBACK_MESSAGE 回滚事务
 *  COMMIT_MESSAGE 提交事务
 */
@Component
public class MyTransactionListener implements TransactionListener {
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        Object num = msg.getProperty("test");

        if ("1".equals(num)) {
            System.out.println("executer: " + new String(msg.getBody()) + " unknown");
            return LocalTransactionState.UNKNOW;
        }
        else if ("2".equals(num)) {
            System.out.println("executer: " + new String(msg.getBody()) + " rollback");
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        System.out.println("executer: " + new String(msg.getBody()) + " commit");
        return LocalTransactionState.COMMIT_MESSAGE;
    }

    /**
     * 回查事务
     * 当事务消息的本地事务状态返回 UNKNOWN 时，RocketMQ Broker 会定期回调事务监听器的 checkLocalTransaction 方法
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        System.out.println("check: " + new String(msg.getBody()));
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
