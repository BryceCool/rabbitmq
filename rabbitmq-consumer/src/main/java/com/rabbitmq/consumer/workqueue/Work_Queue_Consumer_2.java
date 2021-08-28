package com.rabbitmq.consumer.workqueue;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: Administrator
 * @Date: 2021/8/15 10:52
 */
public class Work_Queue_Consumer_2 {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 1. 创建ConnectionFactory
        ConnectionFactory factory = new ConnectionFactory();

        // 2. 设置连接参数
        factory.setHost("192.168.152.128");
        factory.setPort(5672);
        factory.setVirtualHost("/garven");
        factory.setUsername("garven");
        factory.setPassword("garven");

        // 3. 创建Connection
        Connection connection = factory.newConnection();

        // 4. 创建Channel
        Channel channel = connection.createChannel();

        /*
           5. 创建queue
           queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete,Map<String, Object> arguments)
           参数：
           1. queue :       队列名称
           2. durable :     是否持久化。当mq重启之后，queue还在
           3. exclusive :
                            * 是否独占。只能有一个消费者监听这个队列
                            * 当Connection 关闭时，是否删除队列
           4. autoDelete :  是否自动删除。当没有Consumer时，自动删除。
           5. arguments :   额外参数
         */
        channel.queueDeclare("work_queue", true, false, false, null);

        // 6. 消费消息
        Consumer consumer = new DefaultConsumer(channel) {

            /**
             * 回调方法，当收到消息时，会自动执行以下方法
             * @param consumerTag 标识
             * @param envelope 获取配置信息，交换机、路由器
             * @param properties 配置信息
             * @param body 消息数据
             * @throws IOException 业务异常
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(consumerTag);
                System.out.println(envelope.getExchange());
                System.out.println(envelope.getRoutingKey());
                System.out.println(new String(body));
                System.out.println(properties);
            }
        };

        channel.basicConsume("hello_world", true, consumer);
    }
}
