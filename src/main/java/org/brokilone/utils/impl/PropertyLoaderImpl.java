package org.brokilone.utils.impl;

import lombok.extern.slf4j.Slf4j;
import org.brokilone.exception.PropertyReadingException;
import org.brokilone.utils.PropertyLoader;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Kseniia Ushakova
 */
@Slf4j
public class PropertyLoaderImpl implements PropertyLoader {

  @Override
  public Properties loadProps(String fileName) {
    Properties props = new Properties();
    var stream = Thread.currentThread().getContextClassLoader()
        .getResourceAsStream(fileName);
    try {
      props.load(stream);
    } catch (IOException e) {
      throw new PropertyReadingException("Unable to load properties from " + fileName, e);
    }
    return props;
  }
}
