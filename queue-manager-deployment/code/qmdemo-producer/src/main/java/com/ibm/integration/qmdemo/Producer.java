package com.ibm.integration.qmdemo;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Session;
import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import java.net.URL;
import java.net.MalformedURLException;


public class Producer {

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
						
						URL chanTab1 = null;

						try {
							chanTab1 = new URL("http://ccdt-service.default.svc.cluster.local:8080/notls/ccdt.json");
						} catch (MalformedURLException e) {
							e.printStackTrace();
						}

			MQConnectionFactory cf = new MQConnectionFactory();

			cf.setCCDTURL(chanTab1);
			cf.setQueueManager("*ANY_QM");

			// uncomment this line to switch to TLS
			// cf.setSSLCipherSuite("*ANY");

			cf.setTransportType(WMQConstants.WMQ_CM_CLIENT);
			cf.setAppName("MY-PRODUCER");
			cf.setClientReconnectOptions(WMQConstants.WMQ_CLIENT_RECONNECT);
			cf.setClientReconnectTimeout(120);
            cf.setBalancingOptions(WMQConstants.WMQ_BALANCING_OPTIONS_IGNORE_TRANSACTIONS);
		    cf.setBalancingTimeout(WMQConstants.WMQ_BALANCING_TIMEOUT_IMMEDIATE);


			Connection con = null;
			
			System.out.println(cf.toString());

			System.out.println("Starting Producer - creating connection...");
			con = cf.createConnection("app",mqAppPassword);
			System.out.println("Creating session...");
			Session session = con.createSession(false,Session.AUTO_ACKNOWLEDGE);
			Destination sendTo = session.createQueue("DEV.QUEUE.1");
			MessageProducer producer = session.createProducer(sendTo);

			Message msg = session.createTextMessage("Test some data here"); 

			System.out.println("Sending...");

				for (int i = 0; i < 4000; i++) {
					producer.send(msg);
					Thread.sleep(3000);
				}

			con.close();
			System.out.println("Finished...");

		} catch (Exception e) {

                        e.printStackTrace();
                        for (Throwable cause = e.getCause(); cause != null; cause = cause.getCause()) {
                           System.err.println("Caused by:");
                           cause.printStackTrace();
                        }

		}
}
}
