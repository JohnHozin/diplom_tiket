package chistTravel.tiket.db.entity;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user_jpa")
public class UserJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // номер телефона

    @Column()
    private String password;

    @Column()
    private String registered;

    @ManyToMany
    @JoinTable(name = "users_roles_jpa",
            joinColumns = @JoinColumn(name = "user_id_jpa"),
            inverseJoinColumns = @JoinColumn(name = "role_id_jpa"))
    private Collection<RoleJPA> rolesJPA;

    @Column()
    private String firstName; // имя

    @Column(nullable = false)
    private String lastName; // фамилия

    @Column()
    private String surName;

    @Column(length = 200)
    private String comment;

    @OneToMany(mappedBy = "user")
    private List<Route> travels;

    @Column(nullable = false)
    private boolean writtenByDispatcher;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<RoleJPA> getRolesJPA() {
        return rolesJPA;
    }

    public void setRolesJPA(Collection<RoleJPA> rolesJPA) {
        this.rolesJPA = rolesJPA;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Route> getTravels() {
        return travels;
    }

    public void setTravels(List<Route> travels) {
        this.travels = travels;
    }

    public boolean isWrittenByDispatcher() {
        return writtenByDispatcher;
    }

    public void setWrittenByDispatcher(boolean writtenByDispatcher) {
        this.writtenByDispatcher = writtenByDispatcher;
    }

    @Override
    public String toString() {
        return "UserJPA{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
