package chistTravel.tiket.db.repository;

import chistTravel.tiket.db.entity.Driver;
import chistTravel.tiket.db.entity.UserJPA;
import org.springframework.data.repository.CrudRepository;

public interface DriverRepo extends CrudRepository<Driver, Long> {
    Driver findByUsername(String username);
}
