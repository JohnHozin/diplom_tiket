package chistTravel.tiket.db.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "driver_t")
public class Driver extends UserJPA{

    @OneToMany(mappedBy = "driver")
    private List<Travels> driverTravels;

    public List<Travels> getDriverTravels() {
        return driverTravels;
    }

    public void setDriverTravels(List<Travels> driverTravels) {
        this.driverTravels = driverTravels;
    }

    @Override
    public String toString() {
        return "Driver{" + super.toString()  +
                '}';
    }
}
