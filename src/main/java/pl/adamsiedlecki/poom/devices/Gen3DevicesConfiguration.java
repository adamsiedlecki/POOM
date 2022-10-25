package pl.adamsiedlecki.poom.devices;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import pl.adamsiedlecki.devices.gen3.client.api.Gen3DevicesApi;
import pl.adamsiedlecki.devices.gen3.client.invoker.ApiClient;
import pl.adamsiedlecki.poom.configuration.ConfigProperties;

import java.time.Duration;
import java.util.stream.Collectors;

@Configuration
public class Gen3DevicesConfiguration {

    private final ConfigProperties otmConfigProperties;
    private final RestTemplate restTemplate;

    public Gen3DevicesConfiguration(ConfigProperties otmConfigProperties, MappingJackson2HttpMessageConverter customMappingJackson2HttpMessageConverter) {
        this.otmConfigProperties = otmConfigProperties;
        this.restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(2000))
                .setReadTimeout(Duration.ofMillis(3000))
                .build();
        var messageConvertersExceptDefaultJackson = restTemplate.getMessageConverters()
                .stream()
                .filter(c-> !(c instanceof MappingJackson2HttpMessageConverter))
                .collect(Collectors.toList());
        messageConvertersExceptDefaultJackson.add(customMappingJackson2HttpMessageConverter);
        restTemplate.setMessageConverters(messageConvertersExceptDefaultJackson);
    }

    @Bean
    @Primary
    public Gen3DevicesApi gen3DevicesApi() {
        return new Gen3DevicesApi(getApiClient());
    }

    private ApiClient getApiClient() {
        return new ApiClient(restTemplate).setBasePath(otmConfigProperties.getGen3ApiAddress());
    }
}
