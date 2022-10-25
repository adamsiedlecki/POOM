package pl.adamsiedlecki.poom.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    private final MappingJackson2HttpMessageConverter customMappingJackson2HttpMessageConverter;

    @Bean
    @Primary
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(2000))
                .setReadTimeout(Duration.ofMillis(30_000))
                .build();

        var messageConvertersExceptDefaultJackson = restTemplate.getMessageConverters()
                .stream()
                .filter(c-> !(c instanceof MappingJackson2HttpMessageConverter))
                .collect(Collectors.toList());
        messageConvertersExceptDefaultJackson.add(customMappingJackson2HttpMessageConverter);
        restTemplate.setMessageConverters(messageConvertersExceptDefaultJackson);
        return restTemplate;
    }
}
