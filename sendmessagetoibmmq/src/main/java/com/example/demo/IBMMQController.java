package com.example.demo;


import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

@SpringBootApplication
@RestController
public class IBMMQController {

	public static void main(String[] args) {
		SpringApplication.run(IBMMQController.class, args);
	}

	@PostMapping("/sendmessagetoibmmq")
	public void sendMessage(@RequestBody Message ibmmqmessage)
	{
		Connection connection = null;
		 Session session = null;
		 Destination destination = null;
		 MessageProducer producer = null;
		 try {

			 JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
			 JmsConnectionFactory cf = ff.createConnectionFactory();

			  cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, "ngridqm-9f57.qm.us-south.mq.appdomain.cloud"); 
			  cf.setIntProperty(WMQConstants.WMQ_PORT, 32545);
			  cf.setStringProperty(WMQConstants.WMQ_CHANNEL, "INDYCHANNEL");
			  cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
			  cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, "ngridqm");
			  cf.setStringProperty(WMQConstants.USERID, "katta");
			  cf.setStringProperty(WMQConstants.PASSWORD, "CbU1SZna0_HjAWxj6CV18QQmIXDpXm1mkd-_3UweWvzr");
  			  cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
  			  
			  connection = cf.createConnection();
			  session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			  destination = session.createQueue("queue:///servicein");

			  producer = session.createProducer(destination);
			  TextMessage message = session.createTextMessage(ibmmqmessage.getData());
			  connection.start();
			  producer.send(message);

			  }catch (JMSException jmsex) {
				  jmsex.printStackTrace();
			  }
	
		}
}

 