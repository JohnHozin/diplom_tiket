package chistTravel.tiket.db.entity;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "rolesJPA")
public class RoleJPA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "rolesJPA")
    private Collection<UserJPA> usersJPA;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RoleJPA{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
