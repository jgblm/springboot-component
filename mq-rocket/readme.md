## 本地使用 docker compose 启动 rocketmq 注意事项
1. rocketmq本身有两个进程：broker和nameserver。启动broker时需要通过配置文件指定使用本机的局域网ip来注册到nameserver，否则外部应用无法连接broker来发送或接收消息。
2. 为了方便监控rocketmq，docker-compose.yml中再添加一个dashboard。
3. 也可以通过proxy也访问rocketmq，如此不需要对外暴露broker和nameserver和端口。

## 使用rocketmq注意事项
1. 使用rocketmq时，需要手动创建topic
2. application.yml中绑定名称的限制来自Spring Cloud Stream的函数式编程模型
  - 输入绑定：{functionName}-in-{index}
  - 输出绑定：{functionName}-out-{index}
  - index为输入或输出参数的位置，默认为0
3. producer的两种编程模型
  - Spring Cloud Stream的函数式编程模型(需要function.definition) Spring Cloud Stream自动管理消息发送，基于函数返回值
  - StreamBridge Producer：通过编程方式手动控制消息发送时机和内容，只需要配置对应的绑定即可