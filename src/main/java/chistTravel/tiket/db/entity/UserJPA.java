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

    @Column(unique = true, length = 100)
    private String username;

    @Column(length = 100)
    private String password;

    @Column(length = 50)
    private String registered;

    @ManyToMany
    @JoinTable(name = "users_roles_jpa",
            joinColumns = @JoinColumn(name = "user_id_jpa"),
            inverseJoinColumns = @JoinColumn(name = "role_id_jpa"))
    private Collection<RoleJPA> rolesJPA;

    @Column(length = 100)
    private String firstName; // имя

//    @Column(nullable = false, length = 100)
    @Column(length = 100)
    private String lastName; // фамилия

    @Column(length = 100)
    private String surName;

//    @Column(nullable = false, length = 50)
    @Column(unique = true, length = 50)
    private String phone;

    @Column(length = 200)
    private String comment;

    @OneToMany(mappedBy = "user")
    private List<Route> travels;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    @Override
    public String toString() {
        return "UserJPA{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", registered='" + registered + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", surName='" + surName + '\'' +
                ", phone='" + phone + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
