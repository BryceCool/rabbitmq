package com.rabbitmq.consumer.topics;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: Administrator
 * @Date: 2021/8/28 14:04
 */
public class Topic_2_Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.152.128");
        factory.setPort(5672);
        factory.setVirtualHost("/garven");
        factory.setUsername("garven");
        factory.setPassword("garven");
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        String queue2Name = "test_topic_queue2";

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(new String(body));
                System.out.println("消费消息打印到控制台");
            }
        };

        channel.basicConsume(queue2Name, true, consumer);

    }
}
