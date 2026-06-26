package org.example.min;

import org.example.ParkingLot;
import org.example.Slot;
import org.example.Vehicle;

import java.util.Collection;
import java.util.HashMap;

public class ParkingLotMin extends ParkingLot {
    public HashMap<Integer, Slot> slots = new HashMap<>();
    public HashMap<Vehicle, Slot> vehicles = new HashMap<>();

    public ParkingLotMin(int slots) {
        for (int i = 0; i < slots; i++) {
            this.slots.put(i, new SlotMin(i, VehicleSize.LARGE));
        }
    }

    @Override
    public boolean hasVehicle(Vehicle vehicle) {
        return vehicles.get(vehicle)  != null;
    }

    public Slot setSlotSize(Slot slot, VehicleSize size) {
        if (slot.setSize(size)) {
            return slot;
        }
        return null;
    }

    @Override
    public boolean enterSlot(Slot slot, Vehicle vehicle) {
        if (!slots.containsKey(slot.slotID)) {
            return false;
        }
        if (vehicles.get(vehicle) != null) {
            return false;
        }
        if (slot.enterSlot(vehicle)) {
            vehicles.put(vehicle, slot);
            return true;
        }
        return false;
    }

    @Override
    public boolean exitSlot(Slot slot, Vehicle vehicle) {
        if (!slots.containsKey(slot.slotID)) {
            return false;
        }
        if (slot.leaveSlot(vehicle)) {
            vehicles.remove(vehicle);
            return true;
        }
        return false;
    }

    public boolean slotAvailable(Slot slot, Vehicle vehicle) {
        if (!slots.containsKey(slot.slotID)) {return false;}
        return slot.isAvailable(vehicle);
    }

    @Override
    public Vehicle checkSlot(Slot slot) {
        return slot.occupiedBy;
    }

    @Override
    public Collection<Vehicle> getAllVehicles() {
        return vehicles.keySet();
    }

    @Override
    public Collection<Slot> getSlots() {
        return slots.values();
    }
}
