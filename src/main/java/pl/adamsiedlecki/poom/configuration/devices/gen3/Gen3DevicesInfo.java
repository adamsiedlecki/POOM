package pl.adamsiedlecki.poom.configuration.devices.gen3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "gen3-devices")
public class Gen3DevicesInfo {

    private List<Gen3Device> devices = new ArrayList<>();

    public List<Gen3Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Gen3Device> devices) {
        this.devices = devices;
    }

}
