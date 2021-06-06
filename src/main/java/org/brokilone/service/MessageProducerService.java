package org.brokilone.service;

import org.brokilone.model.BrokerMessage;

/**
 * @author Kseniia Ushakova
 */
public interface MessageProducerService {
  void launchProcessing();

  void addMessage(String topic, BrokerMessage brokerMessage);
}
