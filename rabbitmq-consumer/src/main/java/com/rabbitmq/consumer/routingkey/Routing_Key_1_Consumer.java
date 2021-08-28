package com.rabbitmq.consumer.routingkey;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: Administrator
 * @Date: 2021/8/28 13:03
 */
public class Routing_Key_1_Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.152.128");
        factory.setPort(5672);
        factory.setVirtualHost("/garven");
        factory.setUsername("garven");
        factory.setPassword("garven");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // queue name
        String queue1Name = "test_direct_queue1";

        // 消费第一个队列: 打印日志到控制台
        Consumer consumer1 = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(new String(body));
                System.out.println("将日志输存储到数据库...");
            }
        };
        channel.basicConsume(queue1Name, true, consumer1);
    }
}
