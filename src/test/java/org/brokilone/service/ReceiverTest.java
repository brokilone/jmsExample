package org.brokilone.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.brokilone.destination.DestinationCreator;
import org.brokilone.destination.impl.QueueCreator;
import org.brokilone.destination.impl.TopicCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/**
 * @author Kseniia Ushakova
 */
class ReceiverTest {
  SyncReceiver syncReceiver;
  AsyncReceiver listener;
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
      Thread.sleep(1000);
    }
    syncReceiver.join();
    sender.destroy();

    assertEquals(5, syncReceiver.messageStore.size());

  }

  @Test
  @DisplayName("Should successfully receive messages synchronously from queue")
  void shouldReceiveMessagesSynchronouslyFromQueue() throws Exception {
    creator = new QueueCreator();
    sender = new Sender(creator);

    syncReceiver = new SyncReceiver(creator);
    syncReceiver.start();

    for (int i = 0; i < 5; i++) {
      sender.sendMessage(UUID.randomUUID().toString());
      Thread.sleep(1000);
    }
    syncReceiver.join();
    sender.destroy();

    assertEquals(5, syncReceiver.messageStore.size());

  }

  @Test
  @DisplayName("Should successfully receive messages asynchronously from topic")
  void shouldReceiveMessagesAsynchronouslyFromTopic() throws Exception {
    creator = new TopicCreator();
    sender = new Sender(creator);

    listener = new AsyncReceiver(creator);

    for (int i = 0; i < 5; i++) {
      sender.sendMessage(UUID.randomUUID().toString());
      Thread.sleep(1000);
    }

    sender.destroy();
    listener.destroy();
    assertEquals(5, listener.messageStore.size());

  }
  @Test
  @DisplayName("Should successfully receive messages asynchronously from queue")
  void shouldReceiveMessagesAsynchronouslyFromQueue() throws Exception {
    creator = new QueueCreator();
    sender = new Sender(creator);

    listener = new AsyncReceiver(creator);

    for (int i = 0; i < 5; i++) {
      sender.sendMessage(UUID.randomUUID().toString());
      Thread.sleep(1000);
    }

    sender.destroy();
    listener.destroy();
    assertEquals(5, listener.messageStore.size());

  }

  @Test
  @DisplayName("Should successfully receive messages from common queue")
  void shouldReceiveMessagesFromCommonQueue() throws Exception {
    creator = new QueueCreator();
    sender = new Sender(creator);

    listener = new AsyncReceiver(creator);
    syncReceiver = new SyncReceiver(creator);
    syncReceiver.start();

    for (int i = 0; i < 5; i++) {
      sender.sendMessage(UUID.randomUUID().toString());
      Thread.sleep(1000);
    }

    sender.destroy();
    listener.destroy();
    assertEquals(5, listener.messageStore.size() + syncReceiver.messageStore.size());

  }

  @Test
  @DisplayName("Should successfully receive messages from common topic")
  void shouldReceiveMessagesFromCommonTopic() throws Exception {
    creator = new TopicCreator();
    sender = new Sender(creator);

    listener = new AsyncReceiver(creator);
    syncReceiver = new SyncReceiver(creator);
    syncReceiver.start();

    for (int i = 0; i < 5; i++) {
      sender.sendMessage(UUID.randomUUID().toString());
      Thread.sleep(1000);
    }
    syncReceiver.join();

    sender.destroy();
    listener.destroy();
    assertEquals(5, listener.messageStore.size());
    assertEquals(5, syncReceiver.messageStore.size());

  }
}