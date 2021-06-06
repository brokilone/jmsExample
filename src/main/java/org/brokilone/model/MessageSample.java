package org.brokilone.model;

import lombok.ToString;

import java.util.UUID;

/**
 * @author Kseniia Ushakova
 */
@ToString
public class MessageSample implements BrokerMessage {
  private UUID id = UUID.randomUUID();

}
