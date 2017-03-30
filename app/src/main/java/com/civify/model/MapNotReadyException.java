package com.civify.model;

public class MapNotReadyException extends Exception {

    public MapNotReadyException() {
        super("Map is not ready yet!");
    }
}
