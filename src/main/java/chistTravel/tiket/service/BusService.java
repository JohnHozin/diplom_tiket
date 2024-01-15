package chistTravel.tiket.service;

import chistTravel.tiket.db.entity.Bus;
import chistTravel.tiket.db.repository.BusRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusService {

    @Autowired
    private BusRepo repository;

    public List<Bus> listAllBus(){
        return (List<Bus>) repository.findAll();
    }

    public Bus saveBus(Bus bus){
        return repository.save(bus);
    }
}
