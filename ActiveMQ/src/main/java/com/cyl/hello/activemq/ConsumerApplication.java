package com.cyl.hello.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ConsumerApplication {

    public static void main(String[] args) throws Exception {
        // 创建一个ConnectionFactory对象连接MQ服务器
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        // 创建一个连接对象
        Connection connection = connectionFactory.createConnection();
        // 开启连接
        connection.start();
        // 使用Connection对象创建一个Session对象
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 创建一个Destination对象。queue对象
        Queue queue = session.createQueue("test-queue");
        // 使用Session对象创建一个消费者对象。
        MessageConsumer consumer = session.createConsumer(queue);
        // 接收消息
        consumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
                // 打印结果
                TextMessage textMessage = (TextMessage) message;
                String text;
                try {
                    text = textMessage.getText();
                    System.out.println("这是接收到的消息：" + text);
                } catch (JMSException e) {
                    e.printStackTrace();
                }

            }
        });
        // 等待接收消息
        System.in.read();
        // 关闭资源
        consumer.close();
        session.close();
        connection.close();
    }
}
