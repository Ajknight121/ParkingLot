package org.example;

import org.example.min.VehicleMin;
import org.example.min.VehicleSize;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ParkingLot parkingLot = ParkingLot.createParkingLot(10);

        System.out.println("Parking Lot Management");

        while (true) {
            printMenu();

            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    printAllSlots(parkingLot);
                    break;
                case "2":
                    printAllVehicles(parkingLot);
                    break;
                case "3":
                    resizeSlot(parkingLot, scanner);
                    break;
                case "4":
                    parkVehicle(parkingLot, scanner);
                    break;
                case "5":
                    removeVehicle(parkingLot, scanner);
                    break;
                case "6":
                    System.out.println("Exiting the application. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void printMenu() {
        System.out.println("\nMENU");
        System.out.println("1. View All Slots");
        System.out.println("2. View All Parked Vehicles");
        System.out.println("3. Set Slot Size");
        System.out.println("4. Park a Vehicle");
        System.out.println("5. Remove a Vehicle");
        System.out.println("6. Exit");
        System.out.print("Select an option: ");
    }

    public static void printAllSlots(ParkingLot parkingLot) {
        System.out.println("\nParking Slots");
        parkingLot.getSlots().forEach((slot) -> {
            String occupiedBy = slot.occupiedBy != null
                    ? "Driver ID: " + slot.occupiedBy.driverID
                    : "None";
            System.out.printf("ID: %s | Size: %s | OccupiedBy: %s%n", slot.slotID, slot.size, occupiedBy);
        });
    }

    public static void printAllVehicles(ParkingLot parkingLot) {
        System.out.println("\nParked Vehicles");
        parkingLot.getSlots().forEach((slot) -> {
            if (slot.occupiedBy != null) {
                System.out.printf("DriverID: %s | Size: %s | Slot: %s%n", slot.occupiedBy.driverID, slot.occupiedBy.size, slot.slotID);
            }
        });
    }

    public static void resizeSlot(ParkingLot parkingLot, Scanner scanner) {
        System.out.print("Enter Slot ID to resize: ");
        int slotId;
        try {
            slotId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid input format. Slot ID must be an integer.");
            return;
        }

        Slot targetSlot = null;
        for (Slot slot : parkingLot.getSlots()) {
            if (slot.slotID == slotId) {
                targetSlot = slot;
                break;
            }
        }

        if (targetSlot == null) {
            System.out.println("Error: Slot not found.");
            return;
        }

        VehicleSize newSize = getVehicleSize(parkingLot,scanner);
        if (newSize == null) {
            return;
        }

        if (targetSlot.occupiedBy != null && targetSlot.occupiedBy.size.getSize() > newSize.getSize()) {
            System.out.println("Error: Cannot resize to " + newSize + ". Slot contains a parked vehicle of size " + targetSlot.occupiedBy.size + ".");
            return;
        }

        if (parkingLot.setSlotSize(targetSlot, newSize) != null) {
            System.out.println("Success: Slot " + slotId + " resized to " + newSize + ".");

        } else {
            System.out.println("Error: Failed to set slot size.");
        }

    }

    public static VehicleSize getVehicleSize(ParkingLot parkingLot, Scanner scanner) {
        VehicleSize newSize = null;
        while (newSize == null) {
            System.out.print("Enter size (Small, Large, Oversize): ");
            String sizeInput = scanner.nextLine().trim().toUpperCase();
            if (sizeInput.equals("S")) {
                newSize = VehicleSize.SMALL;
            } else if (sizeInput.equals("L")) {
                newSize = VehicleSize.LARGE;
            } else if (sizeInput.equals("O")) {
                newSize = VehicleSize.OVERSIZE;
            } else {
                try {
                    newSize = VehicleSize.valueOf(sizeInput);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: Invalid size");
                    return null;
                }
            }

        }
        return newSize;
    }

    public static void parkVehicle(ParkingLot parkingLot, Scanner scanner) {
        System.out.print("Enter Driver ID: ");
        int driverId;
        try {
            driverId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Error: Driver ID must be an integer.");
            return;
        }

        // Check if the driver is already parked
        for (Vehicle v : parkingLot.getAllVehicles()) {
            if (v.driverID == driverId) {
                System.out.println("Error: Vehicle with Driver ID " + driverId + " is already parked.");
                return;
            }
        }

        VehicleSize size = getVehicleSize(parkingLot, scanner);

        Vehicle vehicle = new VehicleMin(driverId, size);

        // Find available slots
        List<Slot> availableSlots = new ArrayList<>();
        for (Slot slot : parkingLot.getSlots()) {
            if (parkingLot.slotAvailable(slot, vehicle)) {
                availableSlots.add(slot);
            }
        }

        if (availableSlots.isEmpty()) {
            System.out.println("No available slots found for a vehicle of size " + size + ".");
            return;
        }

        System.out.println("Available slots for this vehicle:");
        for (Slot slot : availableSlots) {
            System.out.printf("  Slot ID: %s (Size: %s)%n", slot.slotID, slot.size);
        }

        System.out.print("Enter the Slot ID to park in: ");
        int targetSlotId;
        try {
            targetSlotId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Error: Slot ID must be an integer.");
            return;
        }

        Slot targetSlot = null;
        for (Slot slot : availableSlots) {
            if (slot.slotID == targetSlotId) {
                targetSlot = slot;
                break;
            }
        }

        if (targetSlot == null) {
            System.out.println("Error: The slot selected is either not available or does not exist.");
            return;
        }

        boolean success = parkingLot.enterSlot(targetSlot, vehicle);
        if (success) {
            System.out.println("Driver ID:" + driverId + " parked in Slot:" + targetSlotId);
        } else {
            System.out.println("Error: Failed to park vehicle.");
        }
    }

    public static void removeVehicle(ParkingLot parkingLot, Scanner scanner) {
        System.out.print("Enter Driver ID to remove: ");
        int driverId;
        try {
            driverId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Error: Driver ID must be an integer.");
            return;
        }


        Vehicle foundVehicle = null;
        for (Vehicle v : parkingLot.getAllVehicles()) {
            if (v.driverID == driverId) {
                foundVehicle = v;
                break;
            }
        }

        if (foundVehicle == null) {
            System.out.println("Error: Vehicle not in the parking lot.");
            return;
        }

        Slot targetSlot = null;
        for (Slot slot : parkingLot.getSlots()) {
            if (slot.occupiedBy == foundVehicle) {
                targetSlot = slot;
                break;
            }
        }

        if (targetSlot == null) {
            System.out.println("Error: Vehicle no longer in the parking lot.");
            return;
        }

        boolean success = parkingLot.exitSlot(targetSlot, foundVehicle);
        if (success) {
            System.out.println("Driver ID:" + driverId + "removed from slot:" + targetSlot.slotID);
        } else {
            System.out.println("Error: Failed to remove vehicle.");
        }
    }

}

