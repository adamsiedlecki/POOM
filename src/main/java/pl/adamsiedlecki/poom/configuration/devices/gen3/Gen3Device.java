package pl.adamsiedlecki.poom.configuration.devices.gen3;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Gen3Device {
    private long id;
    private String name;
    private String longitude;
    private String latitude;
    private long locationPlaceId;
    private boolean hasBattery;
    private boolean canBeInDanger;
    private boolean external;
    private boolean isPrivate;
    private List<String> tags;
}
