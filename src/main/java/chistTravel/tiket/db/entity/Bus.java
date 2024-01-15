package chistTravel.tiket.db.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "bus_t")
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String busName;

    @Column(nullable = false, length = 20)
    private String number;

    @Column(nullable = false, length = 20)
    private int capacity;

    @OneToMany(mappedBy = "bus")
    private List<Travels> travels;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<Travels> getTravels() {
        return travels;
    }

    public void setTravels(List<Travels> travels) {
        this.travels = travels;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "id=" + id +
                ", busName='" + busName + '\'' +
                ", number='" + number + '\'' +
                ", capacity='" + capacity + '\'' +
//                ", travels=" + travels +
                '}';
    }
}
