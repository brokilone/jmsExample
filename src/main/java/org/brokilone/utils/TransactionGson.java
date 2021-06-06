package org.brokilone.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Kseniia Ushakova
 */
@Slf4j
public final class TransactionGson {
  private static final Gson GSON = new GsonBuilder()
      .setLongSerializationPolicy(LongSerializationPolicy.STRING)
      .create();

  public static Gson getInstance(){
    return GSON;
  }
}
