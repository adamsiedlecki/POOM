package pl.adamsiedlecki.poom.devices.exceptions;

public class ResponseFromWrongStationException extends RuntimeException{

    public ResponseFromWrongStationException(String message) {
        super(message);
    }
}
