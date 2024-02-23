package com.vast.vl_tool.test;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * @author vastlan
 * @description
 * @created 2023/11/8 11:06
 */

@SpringBootConfiguration
public class RestTemplateConfig {

  @Bean
  public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
    RestTemplate restTemplate = new RestTemplate(factory);
    restTemplate.setErrorHandler(new ResponseErrorHandler() {
      @Override
      public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return false;
      }

      @Override
      public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {

      }
    });
    return restTemplate;
  }

  @Bean(name = "httpsFactory")
  public HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory()
  {
    try {
      CloseableHttpClient httpClient = HttpClientUtils.acceptsUntrustedCertsHttpClient();
      HttpComponentsClientHttpRequestFactory httpsFactory =
        new HttpComponentsClientHttpRequestFactory(httpClient);
      httpsFactory.setReadTimeout(40000);
      httpsFactory.setConnectTimeout(40000);
      return httpsFactory;
    }
    catch (Exception e ){
      return  null;
    }
  }

}
