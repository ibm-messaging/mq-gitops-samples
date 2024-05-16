package com.ibm.integration.qmdemo;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Session;
import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

public class Producer {

	public static void main(String[] args) {

		try {	

			MQConnectionFactory cf = new MQConnectionFactory();

			cf.setHostName("qmdemo-ibm-mq");
			cf.setPort(1414);
			cf.setQueueManager("QMDEMO");
			cf.setChannel("DEV.APP.SVRCONN");
			cf.setTransportType(WMQConstants.WMQ_CM_CLIENT);
//			cf.setBooleanProperty(JmsConstants.USER_AUTHENTICATION_MQCSP, true);		
			cf.setAppName("MY-PRODUCER");
			cf.setClientReconnectOptions(WMQConstants.WMQ_CLIENT_RECONNECT);
//			cf.setClientReconnectTimeout(320);

			Connection con = null;
			
			System.out.println(cf.toString());

			System.out.println("Starting Producer - creating connection...");
			con = cf.createConnection("app","newpassword");
			System.out.println("Creating session...");
			Session session = con.createSession(false,Session.AUTO_ACKNOWLEDGE);
			Destination sendTo = session.createQueue("DEV.QUEUE.1");
			MessageProducer producer = session.createProducer(sendTo);

			Message msg = session.createTextMessage("Test some data here"); 

			System.out.println("Sending...");

				for (int i = 0; i < 1000; i++) {
					producer.send(msg);
					Thread.sleep(3000);
				}

			con.close();
			System.out.println("Finished...");

		} catch (JMSException e) {

			System.out.println(e + "\n" + e.getLinkedException() + e.getMessage());

		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
	}
}
