package com.civify.model.map;

public class MapNotReadyException extends Exception {

    public MapNotReadyException() {
        super("Map is not ready yet!");
    }
}
