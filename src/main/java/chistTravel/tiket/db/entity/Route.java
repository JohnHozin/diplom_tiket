package chistTravel.tiket.db.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "route_t")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserJPA user;

    @ManyToOne
    @JoinColumn(name = "travel_id")
    private Travels travel;

    @ManyToOne
    @JoinColumn(name = "landingSite_id")
    private LandingSite landingSite;

    @Column()
    private String landingTime;

    @Column()
    private String registered;

    @Column()
    private Boolean status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserJPA getUser() {
        return user;
    }

    public void setUser(UserJPA user) {
        this.user = user;
    }


    public Travels getTravel() {
        return travel;
    }

    public void setTravel(Travels travel) {
        this.travel = travel;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public LandingSite getLandingSite() {
        return landingSite;
    }

    public void setLandingSite(LandingSite landingSite) {
        this.landingSite = landingSite;
    }

    public String getLandingTime() {
        return landingTime;
    }

    public void setLandingTime(String landingTime) {
        this.landingTime = landingTime;
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", user=" + user +
                ", registered=" + registered +
                '}';
    }
}
