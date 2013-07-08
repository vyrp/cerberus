package br.itabits.cerberus.database;

public class DeviceNotSetException extends RuntimeException {
    private static final long serialVersionUID = 8640181822506421967L;

    public DeviceNotSetException(String message) {
        super(message);
    }
}
