package com.bridgelabz.parkinglot.service;
import com.bridgelabz.parkinglot.enums.DriverType;
import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.exception.ParkingLotException.ExceptionType;
import com.bridgelabz.parkinglot.model.VehicleDetails;
import com.bridgelabz.parkinglot.observer.AirportSecurityImpl;
import com.bridgelabz.parkinglot.observer.ParkingLotObserver;
import com.bridgelabz.parkinglot.observer.ParkingOwnerImpl;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
public class ParkingLot {
    Map<Integer, VehicleDetails> parkingLotData = new HashMap<>();
    public final int TOTAL_PARKING_LOT_CAPACITY;
    private final int TOTAL_LOTS;
    private final int SINGLE_LOT_CAPACITY;
    private final int singleLotCapacity;
    public static boolean status;
    private static Integer lotNo = 1;
    ParkingLotObserver parkingLotObserver = new ParkingLotObserver();

    public ParkingLot(int parkingLotCapacity, int noOfLots) {
        this.TOTAL_LOTS = noOfLots;
        this.TOTAL_PARKING_LOT_CAPACITY = parkingLotCapacity * noOfLots;
        this.SINGLE_LOT_CAPACITY = parkingLotCapacity;
        this.singleLotCapacity = parkingLotCapacity;
        ParkingOwnerImpl parkingOwner = new ParkingOwnerImpl();
        parkingLotObserver.addIntoViewerList(parkingOwner);
        AirportSecurityImpl airportSecurity = new AirportSecurityImpl();
        parkingLotObserver.addIntoViewerList(airportSecurity);
    }

    public void park(VehicleDetails vehicle) throws ParkingLotException {
        if (this.checkPresent(vehicle)) {
            throw new ParkingLotException(ExceptionType.VEHICLE_ALREADY_PARKED, "This vehicle already parked");
        }
        if (parkingLotData.size() == TOTAL_PARKING_LOT_CAPACITY) {
            parkingLotObserver.notificationUpdate(true);
            throw new ParkingLotException(ExceptionType.PARKING_LOT_IS_FULL, "Parking lot is full");
        }
        this.allocateLotNo(vehicle);
        vehicleStatus(true);
    }

    public void unPark(String vehicle) throws ParkingLotException {
        isMyVehiclePresent(vehicle);
        int counter = 0;
        for (VehicleDetails t : parkingLotData.values()) {
            counter++;
            if (t.vehicleNumber.equals(vehicle)) {
                parkingLotData.remove(counter);
                parkingLotObserver.notificationUpdate(false);
                vehicleStatus(false);
            }
            break;
        }
    }

    private void vehicleStatus(boolean status) {
        ParkingLot.status = status;
    }

    public void allocateLotNo(VehicleDetails vehicle) throws ParkingLotException {
        if (this.checkPresent(vehicle)) {
            throw new ParkingLotException(ExceptionType.VEHICLE_ALREADY_PARKED, "This vehicle already parked");
        }
        int lotStarted;
        int lotEnded;
        switch (vehicle.driverType) {
            case HANDICAP_DRIVER:
                lotStarted = 1;
                lotEnded = TOTAL_PARKING_LOT_CAPACITY;
                break;
            default:
                lotStarted = (SINGLE_LOT_CAPACITY * lotNo) - (singleLotCapacity - 1);
                lotEnded = SINGLE_LOT_CAPACITY * lotNo;
                break;
        }
        for (int key = lotStarted; key < lotEnded; key++) {
            if (!parkingLotData.containsKey(key)) {
                this.parkThroughWarden(key, vehicle);
                break;
            }
        }
    }

    private void parkThroughWarden(int key, VehicleDetails vehicle) {
        parkingLotData.putIfAbsent(key, vehicle);
        if (vehicle.driverType == DriverType.NON_HANDICAP_DRIVER) {
            lotNo++;
        }
        if (lotNo == TOTAL_LOTS + 1) {
            lotNo = 1;
        }
    }

    public boolean isMyVehiclePresent(String vehicleNumber) throws ParkingLotException {
        for (VehicleDetails t : parkingLotData.values()) {
            if (t.vehicleNumber.equals(vehicleNumber)) {
                return true;
            }
        }
        throw new ParkingLotException(ExceptionType.VEHICLE_NOT_PARKED, "Vehicle Not present");
    }

    private boolean checkPresent(VehicleDetails vehicle) {
        for (VehicleDetails t : parkingLotData.values()) {
            if (t.vehicleNumber.equals(vehicle.vehicleNumber)) {
                return true;
            }
        }
        return false;
    }

    public LocalTime vehicleArrivedTime(String givenCarName) throws ParkingLotException {
        for (VehicleDetails t : parkingLotData.values()) {
            if (t.vehicleNumber.equals(givenCarName)) {
                return t.vehicleParkingTime;
            }
        }
        throw new ParkingLotException(ParkingLotException.ExceptionType.VEHICLE_NOT_PARKED, "Vehicle Not present");
    }
}


