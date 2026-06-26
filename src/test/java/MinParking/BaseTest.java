package MinParking;

import org.example.Slot;
import org.example.Vehicle;
import org.example.min.ParkingLotMin;
import org.example.min.SlotMin;
import org.example.min.VehicleMin;
import org.example.min.VehicleSize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BaseTest {
    @Test
    public void createParkingLot() {
        int slotCount = 10;
        ParkingLotMin parkingLot = new ParkingLotMin(slotCount);

        List<Slot> slots = parkingLot.getSlots().stream().toList();
        Assertions.assertEquals(slotCount, slots.size());

        Vehicle lV = new VehicleMin(123, VehicleSize.LARGE);
        Vehicle sV = new VehicleMin(555, VehicleSize.SMALL);
        Vehicle oV = new VehicleMin(999, VehicleSize.OVERSIZE);

        //large
        for (int i = 0; i < slotCount; i++) {
            Slot slot = slots.get(i);
            Assertions.assertTrue(parkingLot.getSlots().contains(slot));
            Assertions.assertEquals(slot.slotID, i);
            Assertions.assertTrue(slot.isEmpty());
            Assertions.assertEquals(slot.size.getSize(), VehicleSize.LARGE.getSize());

            Assertions.assertTrue(parkingLot.slotAvailable(slot, lV));
            Assertions.assertTrue(parkingLot.slotAvailable(slot, sV));
            Assertions.assertFalse(parkingLot.slotAvailable(slot, oV));

            Assertions.assertFalse(parkingLot.enterSlot(slot,oV));
            Assertions.assertFalse(parkingLot.exitSlot(slot,oV));

            Assertions.assertTrue(parkingLot.enterSlot(slot,lV));
            Assertions.assertFalse(parkingLot.enterSlot(slot,new VehicleMin(555, VehicleSize.SMALL)));
            Assertions.assertFalse(parkingLot.slotAvailable(slot, lV));
            Assertions.assertTrue(parkingLot.exitSlot(slot,lV));
        }

        //small
        for (int i = 0; i < slotCount; i++) {
            Slot slot = slots.get(i);
            Assertions.assertTrue(parkingLot.getSlots().contains(slot));
            Assertions.assertEquals(slot.slotID, i);
            Assertions.assertTrue(slot.isEmpty());
            Assertions.assertNotNull(parkingLot.setSlotSize(slot,VehicleSize.SMALL));
            Assertions.assertEquals(slot.size.getSize(), VehicleSize.SMALL.getSize());

            Assertions.assertTrue(parkingLot.slotAvailable(slot, sV));
            Assertions.assertFalse(parkingLot.slotAvailable(slot, lV));
            Assertions.assertFalse(parkingLot.slotAvailable(slot, oV));

            Assertions.assertFalse(parkingLot.enterSlot(slot,oV));
            Assertions.assertFalse(parkingLot.exitSlot(slot,oV));

            Assertions.assertTrue(parkingLot.enterSlot(slot,sV));
            Assertions.assertFalse(parkingLot.enterSlot(slot,new VehicleMin(555, VehicleSize.SMALL)));
            Assertions.assertFalse(parkingLot.slotAvailable(slot, sV));
            Assertions.assertTrue(parkingLot.exitSlot(slot,sV));
        }

    }

    @Test
    public void noDuplicateVehicles() {
        int slotCount = 10;
        ParkingLotMin parkingLot = new ParkingLotMin(slotCount);

        List<Slot> slots = parkingLot.getSlots().stream().toList();
        Assertions.assertEquals(slotCount, slots.size());

        Vehicle lV = new VehicleMin(123, VehicleSize.LARGE);
        Vehicle sV = new VehicleMin(555, VehicleSize.SMALL);
        Vehicle oV = new VehicleMin(999, VehicleSize.OVERSIZE);

        Assertions.assertTrue(parkingLot.enterSlot(slots.get(0), lV));
        Assertions.assertFalse(parkingLot.enterSlot(slots.get(1), lV));

        Assertions.assertEquals(parkingLot.checkSlot(slots.get(0)),lV);
    }

    @Test
    public void getVehicles() {
        int slotCount = 200;
        ParkingLotMin parkingLot = new ParkingLotMin(slotCount);

        List<Slot> slots = parkingLot.getSlots().stream().toList();
        for (Slot slot : slots) {
            parkingLot.setSlotSize(slot,VehicleSize.OVERSIZE);
        }
        ArrayList<Vehicle> vehicles = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            VehicleSize size;
            if (i % 2 == 0) {
                size = VehicleSize.SMALL;
            } else if (i % 3 == 1) {
                size = VehicleSize.LARGE;
            } else {
                size = VehicleSize.OVERSIZE;
            }
            vehicles.add(new VehicleMin(i, size));
        }

        Set<Vehicle> entered  = new HashSet<>();
        for (int i = 0; i < vehicles.size(); i++) {
            for (Slot slot : slots) {
                if (parkingLot.enterSlot(slot, vehicles.get(i))) {
                    entered.add(vehicles.get(i));
                    Assertions.assertEquals(parkingLot.getAllVehicles().size(), entered.size());
                }
            }

        }




    }

    @Test
    public void slots() {
        ArrayList<Slot> slots = new ArrayList<>();
        int slotCount = 10;
        for (int i = 0; i < slotCount; i++) {
            slots.add(new SlotMin(i, VehicleSize.SMALL));
        }

        Vehicle lV = new VehicleMin(123, VehicleSize.LARGE);
        Vehicle sV = new VehicleMin(555, VehicleSize.SMALL);
        Vehicle oV = new VehicleMin(999, VehicleSize.OVERSIZE);


        // small
        for (int i = 0; i < slotCount; i++) {
            Slot slot = slots.get(i);
            Assertions.assertEquals(slot.slotID, i);
            Assertions.assertTrue(slot.isEmpty());
            Assertions.assertEquals(slot.size.getSize(), VehicleSize.SMALL.getSize());

            // is available
            Assertions.assertTrue(slot.isAvailable(new VehicleMin(1, VehicleSize.SMALL)));
            Assertions.assertFalse(slot.isAvailable(new VehicleMin(2, VehicleSize.LARGE)));
            Assertions.assertFalse(slot.isAvailable(new VehicleMin(3, VehicleSize.OVERSIZE)));


            Assertions.assertFalse(slot.enterSlot(oV));
            Assertions.assertTrue(slot.isEmpty());

            Assertions.assertFalse(slot.enterSlot(lV));
            Assertions.assertTrue(slot.isEmpty());

            Assertions.assertTrue(slot.enterSlot(sV));
            Assertions.assertFalse(slot.isEmpty());

            Assertions.assertFalse(slot.enterSlot(new VehicleMin(4, VehicleSize.SMALL)));
            Assertions.assertFalse(slot.leaveSlot(new VehicleMin(4, VehicleSize.SMALL)));

            Assertions.assertTrue(slot.leaveSlot(sV));
            Assertions.assertTrue(slot.isAvailable(sV));
        }

        // large
        for (int i = 0; i < slotCount; i++) {
            Slot slot = slots.get(i);
            Assertions.assertEquals(slot.slotID, i);
            Assertions.assertTrue(slot.isEmpty());
            Assertions.assertEquals(slot.size.getSize(), VehicleSize.LARGE.getSize());

            // is available
            Assertions.assertTrue(slot.isAvailable(new VehicleMin(1, VehicleSize.SMALL)));
            Assertions.assertTrue(slot.isAvailable(new VehicleMin(2, VehicleSize.LARGE)));
            Assertions.assertFalse(slot.isAvailable(new VehicleMin(3, VehicleSize.OVERSIZE)));


            Assertions.assertFalse(slot.enterSlot(oV));
            Assertions.assertTrue(slot.isEmpty());

            Assertions.assertTrue(slot.enterSlot(lV));
            Assertions.assertFalse(slot.isEmpty());

            Assertions.assertFalse(slot.enterSlot(sV));
            Assertions.assertTrue(slot.isEmpty());

            Assertions.assertFalse(slot.enterSlot(new VehicleMin(4, VehicleSize.SMALL)));
            Assertions.assertFalse(slot.leaveSlot(new VehicleMin(4, VehicleSize.SMALL)));

            Assertions.assertTrue(slot.leaveSlot(sV));
            Assertions.assertTrue(slot.isAvailable(sV));
        }

    }

}
