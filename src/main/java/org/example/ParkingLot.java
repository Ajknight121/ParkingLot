package org.example;

import org.example.min.ParkingLotMin;
import org.example.min.VehicleSize;

import java.util.Collection;

public abstract class ParkingLot {

    public static ParkingLot createParkingLot(int slots) {
        return new ParkingLotMin(slots);
    }

    public abstract boolean hasVehicle(Vehicle vehicle);
    public abstract Slot setSlotSize(Slot slot, VehicleSize size);
    public abstract boolean enterSlot(Slot slot, Vehicle vehicle);
    public abstract boolean exitSlot(Slot slot, Vehicle vehicle);
    public abstract boolean slotAvailable(Slot slot, Vehicle vehicle);
    public abstract Vehicle checkSlot(Slot slot);
    public abstract Collection<Vehicle> getAllVehicles();
    public abstract Collection<Slot> getSlots();


}
