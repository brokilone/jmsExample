package org.brokilone.destination.impl;

import org.brokilone.model.Type;
import org.brokilone.destination.DestinationCreator;
import org.brokilone.exception.TopicCreationException;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

/**
 * @author Kseniia Ushakova
 */
public class TopicCreator implements DestinationCreator {
  @Override
  public Destination getDestination(Session session) {
    try {
      return session.createTopic(SUBJECT + "_" + Type.TOPIC.name());
    } catch (JMSException e) {
      throw new TopicCreationException("Failed to create topic!", e);
    }
  }
}
