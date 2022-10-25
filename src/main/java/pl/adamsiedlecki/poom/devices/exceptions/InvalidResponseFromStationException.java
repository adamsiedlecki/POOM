package pl.adamsiedlecki.poom.devices.exceptions;

public class InvalidResponseFromStationException extends RuntimeException {

    public InvalidResponseFromStationException() {
    }

    public InvalidResponseFromStationException(String message) {
        super(message);
    }
}
