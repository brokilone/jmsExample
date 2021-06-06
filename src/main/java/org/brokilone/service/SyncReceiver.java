package org.brokilone.service;

import lombok.extern.slf4j.Slf4j;
import org.brokilone.destination.DestinationCreator;
import org.brokilone.utils.JMSProvider;

import java.util.ArrayList;
import java.util.List;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * @author Kseniia Ushakova
 */
@Slf4j
public class SyncReceiver extends Thread{

  private final MessageConsumer consumer;
  private final Connection connection;
  private final Session session;

  public List<String> messageStore = new ArrayList<>();

  public SyncReceiver(DestinationCreator creator) throws JMSException {
    super("ReceiverExample");
    setDaemon(true);

    var connectionFactory = JMSProvider.getConnectionFactory();
    connection = connectionFactory.createConnection();
    connection.start();

    session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);// non-transacted
    var destination = JMSProvider.getDestination(session, creator);
    consumer = session.createConsumer(destination);
  }

  @Override
  public void run() {
    try {
      int i = 0;
      while (i < 5) {
        var message = consumer.receive();
        if (message instanceof TextMessage) {
          var text = ((TextMessage) message).getText();
          log.info("Received message {}", text);
          messageStore.add(text);
        }
        Thread.sleep(1000);
        i++;
      }
    } catch (JMSException | InterruptedException e) {
      log.error(e.getLocalizedMessage());
    } finally {
      try {
        connection.close();
      } catch (JMSException e) {
        log.error(e.getLocalizedMessage());
      }
    }

  }
}
