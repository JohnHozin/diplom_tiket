package chistTravel.tiket.service;

import chistTravel.tiket.db.entity.Driver;
import chistTravel.tiket.db.repository.DriverRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DriverService {

    @Autowired
    private DriverRepo repository;

    public List<Driver> listAllDrivers(){
        return (List<Driver>) repository.findAll();
    }

    public Driver saveDriver(Driver driver){
        return repository.save(driver);
    }
}
