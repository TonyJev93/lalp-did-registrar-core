package net.lotte.lalpid.did.registrar.infrastructure.http;

import java.nio.charset.Charset;
import java.util.Collections;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	// Connection을 지속적으로 유지해야 하는 경우 해당 RestTemplate 사용
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {

		RestTemplate restTemplate = restTemplateBuilder.build();

		HttpComponentsClientHttpRequestFactory httpComponentsFactory = new HttpComponentsClientHttpRequestFactory();
		ClientHttpRequestFactory bufferingFactory = new BufferingClientHttpRequestFactory(httpComponentsFactory);

		restTemplate.setRequestFactory(bufferingFactory);
		restTemplate.setInterceptors(Collections.singletonList(new LoggingRequestInterceptor()));

		return restTemplate;
	}

    // Connection이 항상 새로 생겨야 하는 경우 해당 RestTemplate 사용 ( ex. 외부 서버와의 Session 통신 )
    @Bean(name="restTemplateForSocket")
    public RestTemplate restTemplateForSocket(RestTemplateBuilder restTemplateBuilder) {

		RestTemplate restTemplate = restTemplateBuilder.build();

		SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
		ClientHttpRequestFactory bufferingFactory = new BufferingClientHttpRequestFactory(httpRequestFactory);

		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
		restTemplate.setRequestFactory(bufferingFactory);
		restTemplate.setInterceptors(Collections.singletonList(new LoggingRequestInterceptor()));

		return restTemplate;
	}
}
