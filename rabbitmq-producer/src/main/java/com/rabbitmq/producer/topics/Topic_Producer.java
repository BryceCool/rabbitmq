package com.rabbitmq.producer.topics;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: Administrator
 * @Date: 2021/8/28 13:54
 */
public class Topic_Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.152.128");
        factory.setPort(5672);
        factory.setVirtualHost("/garven");
        factory.setUsername("garven");
        factory.setPassword("garven");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "test_topics";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC, true, false, false, null);

        String queue1Name = "test_topic_queue1";
        String queue2Name = "test_topic_queue2";
        channel.queueDeclare(queue1Name, true, false, false, null);
        channel.queueDeclare(queue2Name, true, false, false, null);

        channel.queueBind(queue1Name, exchangeName, "#.error");
        channel.queueBind(queue1Name, exchangeName, "order.*");
        channel.queueBind(queue2Name, exchangeName, "*.*");

        String dbMsg = "存入数据库级别日志，日志级别为：error 或 订单信息";
        channel.basicPublish(exchangeName, "order.create", null, dbMsg.getBytes());

        String consoleMsg = "打印到控制台日志，日志级别为：info 或 不是订单信息和error日志";
        channel.basicPublish(exchangeName, "goods.number", null, consoleMsg.getBytes());

        channel.close();
        connection.close();
    }
}
