package org.brokilone;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.brokilone.model.MessageSample;
import org.brokilone.service.impl.KafkaMessageProducerServiceImpl;
import org.brokilone.utils.Constants;
import org.brokilone.utils.PropertyHandler;

/**
 * @author Kseniia Ushakova
 */
@Slf4j
public class Main {

  @SneakyThrows
  public static void main(String[] args) {
    var messageProducerService = new KafkaMessageProducerServiceImpl();
    messageProducerService.launchProcessing();

    for (int i = 0; i < 5; i++) {
      var brokerMessage = new MessageSample();
      log.info("Send message {}", brokerMessage);
      messageProducerService.addMessage(PropertyHandler.getPropertyByKey(Constants.MESSAGE_BROKER_TJM), brokerMessage);
      Thread.sleep(3000);
    }
  }
}
