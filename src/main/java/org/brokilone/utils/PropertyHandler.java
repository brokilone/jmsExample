package org.brokilone.utils;

import lombok.experimental.UtilityClass;
import org.brokilone.utils.impl.PropertyLoaderImpl;

import java.util.Properties;

/**
 * @author Kseniia Ushakova
 */
@UtilityClass
public class PropertyHandler {
  private PropertyLoader propertyLoader;
  static {
    propertyLoader = new PropertyLoaderImpl();
  }

  public String getPropertyByKey(String messageBrokerSleepTime) {
    var properties = propertyLoader.loadProps(Constants.PROPERTY_PATH);
    return properties.getProperty(messageBrokerSleepTime);
  }
}
