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
                // uncomment this line to make a connection via TLS from outside of an OpenShift cluster
                // System.setProperty("com.ibm.mq.cfg.SSL.outboundSNI","HOSTNAME");
                // uncomment this line to turn off certificate validation - use for test purposes only
                // System.setProperty("com.ibm.mq.cfg.SSL.certificateValPolicy","NONE");
                try {
                        String mqAppPassword = System.getenv("MQ_APP_PASSWORD");

                        if (mqAppPassword == null)
                        {
                                throw new Exception("No environment variable supplied for password, supply env var MQ_APP_PASSWORD with a password.");
                        }

                        MQConnectionFactory cf = new MQConnectionFactory();

                        cf.setHostName("qmdemo-ibm-mq");
                        cf.setPort(1414);
                        // uncomment this line to switch to TLS
			// cf.setSSLCipherSuite("*ANY");
                        cf.setQueueManager("QMDEMO");
                        cf.setChannel("DEV.APP.SVRCONN.0TLS");
                        cf.setTransportType(WMQConstants.WMQ_CM_CLIENT);
                        cf.setAppName("MY-CONSUMER");
                        cf.setClientReconnectOptions(WMQConstants.WMQ_CLIENT_RECONNECT);

                        Connection con = null;

                        System.out.println(cf.toString());

                        System.out.println("Starting Consumer - creating connection...");
                        con = cf.createConnection("app",mqAppPassword);
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
