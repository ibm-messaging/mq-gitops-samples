package com.ibm.integration.qmdemo;

import javax.jms.Message;
import javax.jms.MessageConsumer;

import java.net.URL;
import java.net.MalformedURLException;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Session;
import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

public class Consumer {

        public static void main(String[] args) {
                // uncomment this line to make a connection via TLS from outside of an OpenShift cluster.
                // System.setProperty("com.ibm.mq.cfg.SSL.outboundSNI","HOSTNAME");
                // uncomment this line to turn off certificate validation - use for test purposes only.
                //System.setProperty("com.ibm.mq.cfg.SSL.certificateValPolicy","NONE");
                try {
                        String mqAppPassword = System.getenv("MQ_APP_PASSWORD");

                        if (mqAppPassword == null)
                        {
                                throw new Exception("No environment variable supplied for password, supply env var MQ_APP_PASSWORD with a password.");
                        }

                        URL chanTab = null;

			try {
				chanTab = new URL("http://ccdt-service.default.svc.cluster.local:8080/notls/ccdt.json");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

                        MQConnectionFactory cf = new MQConnectionFactory();

                        cf.setCCDTURL(chanTab);
                        cf.setQueueManager("*ANY_QM");

                        // uncomment this line to switch to a TLS enabled MQ channel.
			//cf.setSSLCipherSuite("*ANY_TLS12_OR_HIGHER");

                        cf.setTransportType(WMQConstants.WMQ_CM_CLIENT);
                        cf.setAppName("MY-CONSUMER");
                        cf.setClientReconnectOptions(WMQConstants.WMQ_CLIENT_RECONNECT);
                        cf.setClientReconnectTimeout(30);
                        cf.setBalancingOptions(WMQConstants.WMQ_BALANCING_OPTIONS_IGNORE_TRANSACTIONS);
		        cf.setBalancingTimeout(WMQConstants.WMQ_BALANCING_TIMEOUT_IMMEDIATE);

                        Connection con = null;

                        System.out.println(cf.toString());

                        System.out.println("Starting Consumer - creating connection...");
                        con = cf.createConnection("app",mqAppPassword);
                        System.out.println("Creating session...");
                        Session session = con.createSession(false,Session.DUPS_OK_ACKNOWLEDGE);
                        Destination getFrom = session.createQueue("APP.DEMO.1");
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
