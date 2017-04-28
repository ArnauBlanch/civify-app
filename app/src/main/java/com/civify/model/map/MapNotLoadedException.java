package com.civify.model.map;

public class MapNotLoadedException extends Exception {

    public MapNotLoadedException() {
        super("Map is not loaded yet!");
    }
}
