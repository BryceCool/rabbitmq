package com.rabbitmq.producer.routingkey;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: Administrator
 * @Date: 2021/8/15 10:33
 */
public class Routing_Key_Producer {

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
         * 5. 创建Exchange
         * exchangeDeclare(String exchange,BuiltinExchangeType type,boolean durable,boolean autoDelete,boolean internal,
         *         Map<String, Object> arguments)
         * 参数：
         * 1. exchange   : 交换机名称
         * 2. type       : 交换机类型。
         *    DIRECT("direct"): 定向发送
         *    FANOUT("fanout"): 扇形(广播)，发送消息到每一个queue
         *    TOPIC("topic"):   通配符的形式
         *    HEADERS("headers"): 参数匹配
         * 3. durable    : 是否可持久化
         * 4. autoDelete : 是否自动删除
         * 5. internal   : 是否内部使用,一般为false
         * 6. argument   : 参数
         */
        String exchangeName = "test_direct";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true, false, false, null);

        /*
           6. 创建queue
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
        String queue1Name = "test_direct_queue1";
        String queue2Name = "test_direct_queue2";
        channel.queueDeclare(queue1Name, true, false, false, null);
        channel.queueDeclare(queue2Name, true, false, false, null);

        /*
         * 7. 绑定队列 和 交换机
         * queueBind(String queue, String exchange, String routingKey)
         * 参数：
         * 1. queue : 队列名称
         * 2. exchange : 交换机名称
         * 3. routingKey : 队列和交换机绑定规则
         *    如果交换机的类型为fanout，routingKey 设置为""
         */
        channel.queueBind(queue1Name, exchangeName, "error");
        channel.queueBind(queue2Name, exchangeName, "info");
        channel.queueBind(queue2Name, exchangeName, "warning");
        channel.queueBind(queue2Name, exchangeName, "error");

        /*
            8. 发送消息
            basicPublish(String exchange, String routingKey, boolean mandatory, BasicProperties props, byte[] body)
            1. exchange :   交换机名称。简单模式下交换机会使用默认的""
            2. routingKey : 路由名称
            3. mandatory :
            4. props :      配置信息
            5. body :       消息数据
         */
        //String msg = "日志：调用finalAll()方法，日志级别为info";
        //channel.basicPublish(exchangeName, "info", null, msg.getBytes());

        String errorMsg = "日志：调用delete()方法出错，日志级别为error";
        channel.basicPublish(exchangeName, "error", null, errorMsg.getBytes());
        // 9. 释放资源
        channel.close();
        connection.close();
    }
}
