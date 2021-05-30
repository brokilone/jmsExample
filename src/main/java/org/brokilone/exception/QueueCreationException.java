package org.brokilone.exception;

/**
 * @author Kseniia Ushakova
 */
public class QueueCreationException extends RuntimeException {
  public QueueCreationException(String message, Throwable cause) {
    super(message, cause);
  }
}
