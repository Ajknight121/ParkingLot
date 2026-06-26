package org.example;

import org.example.min.VehicleSize;

public abstract class Slot {
    public int slotID; // Number for random gen
    public Vehicle occupiedBy;
    public VehicleSize size;


    public Slot(int slotID) {
        this.slotID = slotID;
        this.occupiedBy = null;
        this.size = VehicleSize.LARGE;
    }

    public Slot(int slotID, VehicleSize size) {
        this.slotID = slotID;
        this.occupiedBy = null;
        this.size = size;
    }

    public boolean setSize(VehicleSize size) {
        this.size = size;
        return true;
    }

    public boolean isEmpty() {
        return this.occupiedBy == null;
    }
    public abstract boolean isAvailable(Vehicle vehicle);

    public abstract boolean enterSlot(Vehicle vehicle);

    public abstract boolean leaveSlot(Vehicle vehicle);
}
