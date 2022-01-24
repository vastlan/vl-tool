package com.vast.vl_tool.threadpool;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author qmhc
 * @description
 * @created 2021-06-23 09:39
 */

@SpringBootConfiguration
public class ThreadPoolConfigurer {
  public static final int CORE_POOL_SIZE = 20; // 核心线程池大小

  public static final int MAX_POOL_SIZE = Integer.MAX_VALUE; // 最大线程数

  public static final int KEEP_ALIVE_TIME = 15; // 线程空闲后的存活秒数

  public static final int QUEUE_CAPACITY = 200; // 任务队列容量

  public static final String NAME_PREFIX = "async-task-"; // 线程名称前缀

  public static void sleep(long milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      System.out.println("ThreadPoolConfig 休眠异常");
      e.printStackTrace();
    }
  }

  @Bean
  public ThreadPoolTaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    executor.setCorePoolSize(CORE_POOL_SIZE);
    executor.setMaxPoolSize(MAX_POOL_SIZE);
    executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
    executor.setQueueCapacity(QUEUE_CAPACITY);
    executor.setThreadNamePrefix(NAME_PREFIX);

    // 拒绝策略
    // CallerRunsPolicy 策略为队列满时，由调用线程自行处理该任务
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

    executor.initialize();

    return executor;
  }

}
