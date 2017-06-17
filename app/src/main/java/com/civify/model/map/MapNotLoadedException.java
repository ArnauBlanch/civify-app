package com.civify.model.map;

public class MapNotLoadedException extends RuntimeException {

    public MapNotLoadedException() {
        super("Map is not loaded yet!");
    }

    public MapNotLoadedException(String message) {
        super(message);
    }
}
