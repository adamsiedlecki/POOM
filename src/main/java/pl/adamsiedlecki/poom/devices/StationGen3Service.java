package pl.adamsiedlecki.poom.devices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import pl.adamsiedlecki.devices.gen3.client.api.Gen3DevicesApi;
import pl.adamsiedlecki.devices.gen3.model.GenericMessageInput;
import pl.adamsiedlecki.devices.gen3.model.GenericMessageOutput;
import pl.adamsiedlecki.poom.devices.exceptions.Gen3DevicesApiException;
import pl.adamsiedlecki.poom.devices.exceptions.InvalidResponseFromStationException;
import pl.adamsiedlecki.poom.devices.exceptions.ResponseFromWrongStationException;
import pl.adamsiedlecki.poom.devices.exceptions.StationProbablyInDangerException;

@Service
@RequiredArgsConstructor
@Slf4j
@Configuration
public class StationGen3Service {

    private final Gen3DevicesApi gen3DevicesApi;

    public boolean sendDeviceOnRequest(long targetDevice) {
        GenericMessageInput input = new GenericMessageInput();
        input.setTid(targetDevice);
        input.setCmm(GenericMessageInput.CmmEnum.DEVON);
        return "devOnOk".equals(genericSend(input).getRes());
    }

    public boolean sendDeviceOffRequest(long targetDevice) {
        GenericMessageInput input = new GenericMessageInput();
        input.setTid(targetDevice);
        input.setCmm(GenericMessageInput.CmmEnum.DEVOFF);
        return "devOffOk".equals(genericSend(input).getRes());
    }

    public boolean sendDeviceStatusRequest(long targetDevice) {
        GenericMessageInput input = new GenericMessageInput();
        input.setTid(targetDevice);
        input.setCmm(GenericMessageInput.CmmEnum.DEVOFF);
        var res = genericSend(input).getRes();
        if(res == null) {
            throw new InvalidResponseFromStationException("Res is null");
        }
        return switch(res) {
            case "OFF" -> false;
            case "ON" -> true;
            default -> throw new InvalidResponseFromStationException("Res code is unknown: " + res);
        };
    }

    private GenericMessageOutput genericSend(GenericMessageInput input) {
        if (input.getTid() == null) {
            log.error("GEN3 target station is not specified!");
        }
        GenericMessageOutput output;
        log.info("Sending GEN3 request: \n" + input);
        try {
            output = gen3DevicesApi.sendGenericRequest(input);
            log.info("Received GEN3 response: \n" + output);
            if (output == null) {
                throw new StationProbablyInDangerException();
            }
        } catch (RestClientException e) {
            log.error("Exception was thrown during generic GEN3 request.");
            throw new Gen3DevicesApiException(e.getMessage());
        }
        if (!output.getA().equals(input.getTid())) {
            log.error("The gen3 response is not from the right station!");
            throw new ResponseFromWrongStationException(String.format("Request was sent to %d, but response came from %d",input.getTid(), output.getA()));
        }
        return output;
    }

}
