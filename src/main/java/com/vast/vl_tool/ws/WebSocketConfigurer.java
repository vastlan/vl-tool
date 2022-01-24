package com.vast.vl_tool.ws;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @author vastlan
 * @description
 *  若需要自定义实现，请自行实现 WebSocketMessageBrokerConfigurer 接口
 * @created 2022/1/21 16:29
 */

@SpringBootConfiguration
@EnableWebSocketMessageBroker
public class WebSocketConfigurer implements WebSocketMessageBrokerConfigurer {

  public final static long HEART_BEAT_IN_COMING = 10000L;

  public final static long HEART_BEAT_OUT_GOING = 10000L;

  public final static String WS_URL_PREFIX = "/websocket";

  public final static String ORIGIN_PATTERNS = "*";

  public final static String RECEIVE_URL = "/message";

  public final static String SEND_URL = "/topic";

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint(WS_URL_PREFIX).setAllowedOriginPatterns(ORIGIN_PATTERNS);
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

    scheduler.setPoolSize(1);
    scheduler.initialize();

    // 接收前端发送
    registry.setApplicationDestinationPrefixes(RECEIVE_URL);

    // 往前端发送
    registry
      .enableSimpleBroker(SEND_URL)
      .setHeartbeatValue(new long[]{HEART_BEAT_IN_COMING, HEART_BEAT_OUT_GOING})
      .setTaskScheduler(scheduler);
  }
}
