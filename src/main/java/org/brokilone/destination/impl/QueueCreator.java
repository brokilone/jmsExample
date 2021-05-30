package org.brokilone.destination.impl;

import org.brokilone.model.Type;
import org.brokilone.destination.DestinationCreator;
import org.brokilone.exception.QueueCreationException;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

/**
 * @author Kseniia Ushakova
 */
public class QueueCreator implements DestinationCreator {

  @Override
  public Destination getDestination(Session session) {
    try {
      return session.createQueue(SUBJECT + "_" + Type.QUEUE.name());
    } catch (JMSException e) {
      throw new QueueCreationException("Failed to create queue!", e);
    }
  }
}
