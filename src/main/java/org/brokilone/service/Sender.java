package org.brokilone.service;

import org.brokilone.destination.DestinationCreator;
import org.brokilone.utils.JMSProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

/**
 * @author Kseniia Ushakova
 */
public class Sender {
  private Logger logger = LoggerFactory.getLogger(Sender.class);
  private final MessageProducer producer;
  private final Connection connection;
  private final Session session;

  public Sender(DestinationCreator creator) throws JMSException {
    var connectionFactory = JMSProvider.getConnectionFactory();
    connection = connectionFactory.createConnection();
    connection.start();

    session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);// non-transacted
    var destination = JMSProvider.getDestination(session, creator);
    producer = session.createProducer(destination);
  }

  public void sendMessage(String message) throws JMSException {
    logger.info("Sending message {} : start", message);
    var textMessage = session.createTextMessage(message);
    producer.send(textMessage);
  }

  public void destroy() throws JMSException {
    connection.close();
  }
}
