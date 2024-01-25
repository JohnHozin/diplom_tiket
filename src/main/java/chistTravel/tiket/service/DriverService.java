package chistTravel.tiket.service;

import chistTravel.tiket.db.entity.Driver;
import chistTravel.tiket.db.entity.UserJPA;
import chistTravel.tiket.db.repository.DriverRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class DriverService {

    @Autowired
    private DriverRepo repository;

    public List<Driver> listAllDrivers(){
        return (List<Driver>) repository.findAll();
    }

    public void deleteDriverById(Long id){
        Optional<Driver> deleted = repository.findById(id);
        deleted.ifPresent(driver -> repository.delete(driver));
    }

    public Driver saveDriver(Driver driver){
        return repository.save(driver);
    }
}
