package org.example.min;

public enum VehicleSize {
    SMALL(20),
    LARGE(50),
    OVERSIZE(100);

    private final int size;

    VehicleSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
