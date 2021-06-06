package org.brokilone.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.brokilone.model.TransactionQueueRecord;
import org.brokilone.model.BrokerMessage;
import org.brokilone.service.MessageProducerService;
import org.brokilone.utils.Constants;
import org.brokilone.utils.JMSProvider;
import org.brokilone.utils.PropertyHandler;
import org.brokilone.utils.TransactionGson;

import java.util.concurrent.ConcurrentLinkedDeque;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Session;

/**
 * @author Kseniia Ushakova
 */
@Slf4j
public class ActiveMQJmsSenderServiceImpl implements MessageProducerService {
  private final ConnectionFactory connectionFactory;
  private final Long sleepTime;

  private ConcurrentLinkedDeque<TransactionQueueRecord> transactionQueue;

  public ActiveMQJmsSenderServiceImpl() {
    log.info("Init MessageProducerService");
    this.connectionFactory = connectionFactory();
    this.transactionQueue = new ConcurrentLinkedDeque<>();
    sleepTime = Long.parseLong(PropertyHandler.getPropertyByKey(Constants.MESSAGE_BROKER_SLEEP_TIME));
  }

  private ConnectionFactory connectionFactory() {
    return JMSProvider.getConnectionFactory();
  }

  @Override
  public void launchProcessing() {
    log.info("launchProcessing()..");

    Runnable processing = () -> {
      log.info("New thread of processing is launched");
      while (true) {
        while (!transactionQueue.isEmpty()) {
          log.info("send transaction");
          sendTransaction(transactionQueue.poll());
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
    log.info("add message: {} to topic {}", brokerMessage, topic);
    transactionQueue.add(new TransactionQueueRecord(topic, brokerMessage));
  }

  private void sendTransaction(TransactionQueueRecord record) {
    log.info("Sending transaction : {} to topic - {}", record.getBrokerMessage(), record.getTopic());
    try {
      var connection = connectionFactory.createConnection();
      connection.start();
      var session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      var destination = session.createQueue(record.getTopic());
      var producer = session.createProducer(destination);
      producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

      var message = session.createTextMessage(TransactionGson.getInstance().toJson(record.getBrokerMessage()));
      log.info("set property " + getMessageObjectClass(record));
      message.setStringProperty("_type", getMessageObjectClass(record));

      producer.send(message);
      session.close();
      connection.close();
    } catch (JMSException e) {
      log.error(e.getMessage(), e);
    }
  }

  private String getMessageObjectClass(TransactionQueueRecord record) {
    return record.getBrokerMessage().getClass().toString().split(StringUtils.SPACE)[1];
  }
}
