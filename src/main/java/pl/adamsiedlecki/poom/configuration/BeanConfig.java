package pl.adamsiedlecki.poom.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

    @Bean
    @Primary
    public RestTemplate restTemplate(MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(2000))
                .setReadTimeout(Duration.ofMillis(30_000))
                .build();

        var messageConvertersExceptDefaultJackson = restTemplate.getMessageConverters()
                .stream()
                .filter(c-> !(c instanceof MappingJackson2HttpMessageConverter))
                .collect(Collectors.toList());
        messageConvertersExceptDefaultJackson.add(mappingJackson2HttpMessageConverter);
        restTemplate.setMessageConverters(messageConvertersExceptDefaultJackson);
        return restTemplate;
    }

    @Bean
    @Primary
    public MappingJackson2HttpMessageConverter jacksonMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    @Bean
    @Primary
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
                .registerModule(new JavaTimeModule())
                .findAndRegisterModules();
    }
}
