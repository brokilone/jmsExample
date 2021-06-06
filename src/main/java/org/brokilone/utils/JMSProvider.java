package org.brokilone.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.brokilone.destination.DestinationCreator;

import java.util.List;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Session;

/**
 * @author Kseniia Ushakova
 */
@Slf4j
public class JMSProvider {
  private static String URL = "vm://localhost";
  
  public static ConnectionFactory getConnectionFactory() {
    log.info("Creating connectionFactory");
    var factory = new ActiveMQConnectionFactory();
    factory.setBrokerURL(PropertyHandler.getPropertyByKey(Constants.JMS_ACTIVEMQ_URL));
    factory.setUserName(PropertyHandler.getPropertyByKey(Constants.JMS_ACTIVEMQ_USERNAME));
    factory.setPassword(PropertyHandler.getPropertyByKey(Constants.JMS_ACTIVEMQ_PASSWORD));
    factory.setTrustedPackages(List.of(Constants.TRUSTED_PACKAGE));
    return factory;
  }

  public static Destination getDestination(Session session, DestinationCreator creator) {
    return creator.getDestination(session);
  }

}
