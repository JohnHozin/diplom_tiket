package chistTravel.tiket.db.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "landingSite_t")
public class LandingSite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String placeName;

    @Column(nullable = false, length = 10)
    private String time;

    @OneToMany(mappedBy = "landingSite")
    private List<Route> routes;

    @Column(length = 8)
    private boolean direction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isDirection() {
        return direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "LandingSite{" +
                "id=" + id +
                ", placeName='" + placeName + '\'' +
                ", time='" + time + '\'' +
                ", direction='" + direction + '\'' +
                '}';
    }
}
