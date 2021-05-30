package org.brokilone.destination;

import javax.jms.Destination;
import javax.jms.Session;

/**
 * @author Kseniia Ushakova
 */

@FunctionalInterface
public interface DestinationCreator {
  String SUBJECT = "Jms_Example";

  Destination getDestination(Session session);
}
