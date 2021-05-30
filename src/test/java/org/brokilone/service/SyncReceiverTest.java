package org.brokilone.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.brokilone.destination.DestinationCreator;
import org.brokilone.destination.impl.TopicCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/**
 * @author Kseniia Ushakova
 */
class SyncReceiverTest {
  SyncReceiver syncReceiver;
  DestinationCreator creator;
  Sender sender;

  @Test
  @DisplayName("Should successfully receive messages synchronously from topic")
  void shouldReceiveMessagesSynchronouslyFromTopic() throws Exception {
    creator = new TopicCreator();
    sender = new Sender(creator);

    syncReceiver = new SyncReceiver(creator);
    syncReceiver.start();

    for (int i = 0; i < 5; i++) {
      sender.sendMessage(UUID.randomUUID().toString());
    }

    syncReceiver.join();
    sender.destroy();

    assertEquals(5, syncReceiver.messageStore.size());

  }


}