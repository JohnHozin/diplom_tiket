package chistTravel.tiket.service;

import chistTravel.tiket.db.entity.Bus;
import chistTravel.tiket.db.entity.Driver;
import chistTravel.tiket.db.entity.UserJPA;
import chistTravel.tiket.db.repository.BusRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Bus findBusById(Long id){
        Optional<Bus> bus = repository.findById(id);
        return bus.orElse(null);
    }

    public void deleteBusById(Long id){
        Optional<Bus> deleted = repository.findById(id);
        deleted.ifPresent(bus -> repository.delete(bus));
    }

    public Bus updateBus(Long id, Bus bus){
        bus.setId(id);
        return saveBus(bus);
    }
}
