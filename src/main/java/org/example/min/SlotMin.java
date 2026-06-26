package org.example.min;

import org.example.Slot;
import org.example.Vehicle;

public class SlotMin extends Slot {
    public SlotMin(int slotID, VehicleSize size) {
        super(slotID,size);
    }

    @Override
    public boolean isAvailable(Vehicle vehicle) {
        return this.occupiedBy == null && vehicle.size.getSize() <= this.size.getSize();
    }

    @Override
    public boolean enterSlot(Vehicle vehicle) {
        if (this.occupiedBy == null && vehicle.size.getSize() <= this.size.getSize()) {
            this.occupiedBy = vehicle;
            return true;
        }
        return false;
    }

    @Override
    public boolean leaveSlot(Vehicle vehicle) {
        if (this.occupiedBy == null) {
            return false;
        }
        if (this.occupiedBy == vehicle) {
            this.occupiedBy = null;
            return true;
        }
        return false;
    }
}
