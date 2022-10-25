package pl.adamsiedlecki.poom.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class ConfigProperties {

    @Value("${gen3.api.address}")
    private String gen3ApiAddress;

    @Value("${predatory.bird.audio.on.cron}")
    private String predatoryBirdAudioOnCron;

    @Value("${predatory.bird.audio.off.cron}")
    private String predatoryBirdAudioOffCron;
}
