package org.brokilone.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.brokilone.model.BrokerMessage;
import org.brokilone.model.TransactionQueueRecord;
import org.brokilone.service.MessageProducerService;
import org.brokilone.utils.Constants;
import org.brokilone.utils.PropertyHandler;
import org.brokilone.utils.TransactionGson;

import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author Kseniia Ushakova
 */
@Slf4j
public class KafkaMessageProducerServiceImpl implements MessageProducerService {
  private final Producer producer;
  private final Long sleepTime;

  private ConcurrentLinkedDeque<TransactionQueueRecord> transactionsDeque;

  public KafkaMessageProducerServiceImpl() {
    log.info("KafkaMessageProducerService init ...");
    this.producer = buildKafkaProducer();
    this.transactionsDeque = new ConcurrentLinkedDeque<>();
    sleepTime = Long.parseLong(PropertyHandler.getPropertyByKey(Constants.MESSAGE_BROKER_SLEEP_TIME));
  }

  private Producer buildKafkaProducer() {
    log.info("Build producer..");
    var configProps = new Properties();

    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, PropertyHandler.getPropertyByKey(Constants.BOOTSTRAP));
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

    return new KafkaProducer(configProps);
  }

  @Override
  public void launchProcessing() {
    log.info("launchProcessing()..");

    Runnable processing = () -> {
      log.info("New thread of processing is launched");
      while (true) {
        while (!transactionsDeque.isEmpty()) {
          log.info("send transaction");
          sendTransaction(transactionsDeque.poll());
        }
        try {
          Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
          log.error(e.getMessage(), e);
        }
      }
    };
    new Thread(processing).start();
  }

  @Override
  public void addMessage(String topic, BrokerMessage brokerMessage) {
    log.info("addMessage() - start");
    var transactionQueueRecord = new TransactionQueueRecord(topic, brokerMessage);
    transactionsDeque.add(transactionQueueRecord);
    log.info("addMessage() - end");
  }

  private void sendTransaction(TransactionQueueRecord record) {
    log.info("Sending transaction : {} to topic - {}", record.getBrokerMessage(), record.getTopic());

    var message = TransactionGson.getInstance().toJson(record.getBrokerMessage());
    log.info("send message " + message);

    var producerRecord = new ProducerRecord<String, String>(record.getTopic(), UUID.randomUUID().toString(), message);

    producer.send(producerRecord, ((recordMetadata, e) -> {
      Optional.ofNullable(e).ifPresentOrElse(exception -> {
            log.error("Failed to send to topic {}", recordMetadata.topic(), e);
            transactionsDeque.addFirst(record);
          },
          () -> log.info("Susccessfully sent to topic {}", recordMetadata.topic()));
    }));
  }
}

