package org.brokilone.utils;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.brokilone.destination.DestinationCreator;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Session;

/**
 * @author Kseniia Ushakova
 */
public class JMSProvider {
  private static String URL = "vm://localhost";
  
  public static ConnectionFactory getConnectionFactory() {
    return new ActiveMQConnectionFactory(URL);
  }

  public static Destination getDestination(Session session, DestinationCreator creator) {
    return creator.getDestination(session);
  }

}
