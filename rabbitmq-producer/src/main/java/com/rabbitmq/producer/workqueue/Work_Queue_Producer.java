package com.rabbitmq.producer.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: Administrator
 * @Date: 2021/8/15 10:33
 */
public class Work_Queue_Producer {

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

        /*
            6. 发送消息
            basicPublish(String exchange, String routingKey, boolean mandatory, BasicProperties props, byte[] body)
            1. exchange :   交换机名称。简单模式下交换机会使用默认的""
            2. routingKey : 路由名称
            3. mandatory :
            4. props :      配置信息
            5. body :       消息数据
         */
        for (int i = 0; i < 10; i++) {
            String msg = i + ",hello rabbitmq ~~";
            channel.basicPublish("", "work_queue", null, msg.getBytes());
        }


        // 7. 释放资源
        channel.close();
        connection.close();
    }
}
