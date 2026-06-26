package org.example;

import org.example.min.VehicleSize;

public abstract class Vehicle{
    public int driverID;
    // License plate
    public VehicleSize size;

    public Vehicle(int driverID, VehicleSize size){
        this.driverID = driverID;
        this.size = size;
    }


}
