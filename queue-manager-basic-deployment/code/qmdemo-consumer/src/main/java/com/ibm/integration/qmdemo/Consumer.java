package com.ibm.integration.qmdemo;

import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Session;
import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

public class Consumer {

        public static void main(String[] args) {

                try {

                        MQConnectionFactory cf = new MQConnectionFactory();

                        cf.setHostName("qmdemo-ibm-mq");
                        cf.setPort(1414);
                        cf.setQueueManager("QMDEMO");
                        cf.setChannel("DEV.APP.SVRCONN.0TLS");
                        cf.setTransportType(WMQConstants.WMQ_CM_CLIENT);
                        cf.setAppName("MY-CONSUMER");
                        cf.setClientReconnectOptions(WMQConstants.WMQ_CLIENT_RECONNECT);

                        Connection con = null;

                        System.out.println(cf.toString());

                        System.out.println("Starting Consumer - creating connection...");
                        con = cf.createConnection("app","newpassword");
                        System.out.println("Creating session...");
                        Session session = con.createSession(false,Session.AUTO_ACKNOWLEDGE);
                        Destination getFrom = session.createQueue("DEV.QUEUE.1");
                        MessageConsumer consumer = session.createConsumer(getFrom);
                        
                        int messageCount=1;
                        con.start();
                        
                        while (true) {

        					Message message = consumer.receive();
        					System.out.println("Received message, count: " + messageCount);
        					messageCount++;
                                                Thread.sleep(500);
        				}
                        

                } catch (Exception e) {

                        e.printStackTrace();
                        for (Throwable cause = e.getCause(); cause != null; cause = cause.getCause()) {
                           System.err.println("Caused by:");
                           cause.printStackTrace();
                        }

                }
}
}
