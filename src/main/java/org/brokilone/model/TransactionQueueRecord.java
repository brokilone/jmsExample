package org.brokilone.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Kseniia Ushakova
 */
@Data
@AllArgsConstructor
public class TransactionQueueRecord {
  private String topic;
  private BrokerMessage brokerMessage;
}
