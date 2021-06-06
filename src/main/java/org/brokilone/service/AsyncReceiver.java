package org.brokilone.service;

import lombok.extern.slf4j.Slf4j;
import org.brokilone.destination.DestinationCreator;
import org.brokilone.utils.JMSProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * @author Kseniia Ushakova
 */
@Slf4j
public class AsyncReceiver implements MessageListener {

  private final Connection connection;
  private final Session session;

  public List<String> messageStore = new ArrayList<>();

  public AsyncReceiver(DestinationCreator creator) throws JMSException {

    var connectionFactory = JMSProvider.getConnectionFactory();
    connection = connectionFactory.createConnection();
    connection.start();

    session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);// non-transacted
    var destination = JMSProvider.getDestination(session, creator);
    session.createConsumer(destination).setMessageListener(this);
  }


  @Override
  public void onMessage(Message message) {
    if (message instanceof TextMessage) {
      String text;
      try {
        text = ((TextMessage) message).getText();
        log.info("Received message {}", text);
        messageStore.add(text);
      } catch (JMSException e) {
        e.printStackTrace();
      }
    }
  }

  void destroy() throws JMSException {
    connection.close();
  }
}
