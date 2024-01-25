package chistTravel.tiket.db.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "travels_t")
public class Travels {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @OneToMany(mappedBy = "travel")
    private List<Route> routes;

    @Column()
    private String timeToDrive;

    @Column()
    private String timeParsed;

    @Column()
    private String dateParsed;

    @Column()
    private String dateFull;

    @Column()
    private boolean forward;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public List<Route> getTravels() {
        return routes;
    }

    public void setTravels(List<Route> routes) {
        this.routes = routes;
    }

    public String getTimeToDrive() {
        return timeToDrive;
    }

    public void setTimeToDrive(String timeToDrive) {
        this.timeToDrive = timeToDrive;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public String getTimeParsed() {
        return timeParsed;
    }

    public void setTimeParsed(String timeParsed) {
        this.timeParsed = timeParsed;
    }

    public String getDateParsed() {
        return dateParsed;
    }

    public void setDateParsed(String dateParsed) {
        this.dateParsed = dateParsed;
    }

    public boolean isForward() {
        return forward;
    }

    public void setForward(boolean forward) {
        this.forward = forward;
    }

    public String getDateFull() {
        return dateFull;
    }

    public void setDateFull(String dateFull) {
        this.dateFull = dateFull;
    }

    @Override
    public String toString() {
        return "Travels{" +
                "id=" + id +
                ", bus=" + bus +
                ", driver=" + driver +
                ", timeToDrive='" + timeToDrive + '\'' +
                ", timeParsed='" + timeParsed + '\'' +
                ", dateParsed='" + dateParsed + '\'' +
                ", forward=" + forward +
                '}';
    }


}





