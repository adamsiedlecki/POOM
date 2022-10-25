package pl.adamsiedlecki.poom.scheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.poom.configuration.devices.gen3.Gen3Device;
import pl.adamsiedlecki.poom.configuration.devices.gen3.Gen3DevicesInfo;
import pl.adamsiedlecki.poom.devices.StationGen3Service;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PredatoryBirdAudioSchedule {
    private static final String BUSINESS_SERVICE_NAME = "Predatory bird station schedule";

    private final StationGen3Service stationGen3Service;
    private final Gen3DevicesInfo gen3DevicesInfo;

    @Scheduled(cron = "${predatory.bird.audio.on.cron}")
    public void devicesOn() {
        log.info("{} - ON running", BUSINESS_SERVICE_NAME);
        var devices = getPredatoryBirdAudioStations();

        for(Gen3Device device: devices) {
            try {
                var isSuccessful = stationGen3Service.sendDeviceOnRequest(device.getId());
                if(isSuccessful) {
                    log.info("Turned station: {} ON ({})", device.getId(), device.getName());
                } else {
                    log.error("Could not turn on station: {} ({})", device.getId(), device.getName());
                }
            } catch (RuntimeException ex) {
                log.error("Error while turning on station: {} ({}) : {}", device.getId(), device.getName(), ex.getMessage());
            }
        }
    }

    @Scheduled(cron = "${predatory.bird.audio.off.cron}")
    public void devicesOff() {
        log.info("{} - OFF running", BUSINESS_SERVICE_NAME);
        var devices = getPredatoryBirdAudioStations();

        for(Gen3Device device: devices) {
            try {
                var isSuccessful = stationGen3Service.sendDeviceOffRequest(device.getId());
                if(isSuccessful) {
                    log.info("Turned station: {} OFF ({})", device.getId(), device.getName());
                } else {
                    log.error("Could not turn off station: {} ({})", device.getId(), device.getName());
                }
            } catch (RuntimeException ex) {
                log.error("Error while turning off station: {} ({}) : {}", device.getId(), device.getName(), ex.getMessage());
            }
        }
    }

    private List<Gen3Device> getPredatoryBirdAudioStations() {
        return gen3DevicesInfo.getDevices()
                .stream()
                .filter(device -> device.getTags().stream().anyMatch(tag -> tag.equals("predatoryBirdAudioStation")))
                .toList();
    }
}
