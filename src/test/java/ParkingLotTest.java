import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.observer.AirportSecurityImpl;
import com.bridgelabz.parkinglot.observer.ParkingOwnerImpl;
import com.bridgelabz.parkinglot.service.ParkingLot;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
public class ParkingLotTest {
    Object firstVehicle;
    Object secondVehicle;
    ParkingLot parkingLot;

    @Before
    public void setUp() {
        new Object();
        parkingLot = new ParkingLot(1);
        secondVehicle = new Object();
    }

    @Test
    public void givenAVehicle_IfParked_ShouldReturnTrue() throws ParkingLotException {
        parkingLot.park(firstVehicle);
        boolean parkingStatus = ParkingLot.status;
        Assert.assertTrue(parkingStatus);
    }

    @Test
    public void givenAVehicle_IfUnParked_ShouldReturnTrue() throws ParkingLotException {
        parkingLot.park(firstVehicle);
        parkingLot.UnPark(firstVehicle);
        boolean unParkingStatus = ParkingLot.status;
        Assert.assertFalse(unParkingStatus);
    }

    @Test
    public void givenAVehicle_IfNotPresentAndWantToUnParke_ShouldHandleException() {
        try {
            parkingLot.park(firstVehicle);
            Object unknownVehicle = new Object();
            parkingLot.UnPark(unknownVehicle);
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.ExceptionType.NO_VEHICLE_PRESENT, e.type);
        }
    }

    @Test
    public void givenVehicle_IfParkingLotIsFull_ShouldHandleException() {
        try {
            parkingLot.park(firstVehicle);
            Object anotherVehicle = new Object();
            parkingLot.park(anotherVehicle);
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.ExceptionType.PARKING_LOT_IS_FULL, e.type);
        }
    }

    @Test
    public void givenVehicle_IfParkingLotIsFullInformToOwner_ShouldHandleException() {
        try {
            parkingLot.park(firstVehicle);
            Object anotherVehicle = new Object();
            parkingLot.park(anotherVehicle);
        } catch (ParkingLotException e) {
            Assert.assertTrue(ParkingOwnerImpl.status);
            Assert.assertEquals(ParkingLotException.ExceptionType.PARKING_LOT_IS_FULL, e.type);
        }
    }

    @Test
    public void givenVehicle_IfAlreadyPresent_ShouldHandleException() throws ParkingLotException {
        try {
            parkingLot.park(firstVehicle);
            parkingLot.park(firstVehicle);
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.ExceptionType.VEHICLE_ALREADY_PARKED, e.type);
        }
    }

    @Test
    public void givenVehicle_IfParkingLotIsFullInformToAirportSecurity_ShouldHandleException() {
        try {
            parkingLot.park(firstVehicle);
            Object anotherVehicle = new Object();
            parkingLot.park(anotherVehicle);
        } catch (ParkingLotException e) {
            Assert.assertTrue(AirportSecurityImpl.status);
            Assert.assertEquals(ParkingLotException.ExceptionType.PARKING_LOT_IS_FULL, e.type);
        }
    }

    @Test
    public void givenVehicle_IfParkingLotNowAvailable_ShouldReturnFalse() throws ParkingLotException {
        try {
            parkingLot.park(firstVehicle);
            parkingLot.park(secondVehicle);
        } catch (ParkingLotException e) {
            Assert.assertEquals(ParkingLotException.ExceptionType.PARKING_LOT_IS_FULL, e.type);
            parkingLot.UnPark(firstVehicle);
            Assert.assertFalse(ParkingOwnerImpl.status);
        }
    }

    @Test
    public void givenVehicle_OnBasisOfVehicle_ShouldReturnSlotNo() throws ParkingLotException {
        parkingLot.park(firstVehicle);
        int lotNum = parkingLot.allocateLotNo(secondVehicle);
        Assert.assertEquals(1, lotNum);
    }
}


