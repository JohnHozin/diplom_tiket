package chistTravel.tiket.service;

import chistTravel.tiket.db.entity.Travels;
import chistTravel.tiket.db.repository.TravelsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class TravelService {
    @Autowired
    private TravelsRepo repository;

    public List<Travels> listAllTravels(){
        return (List<Travels>) repository.findAll();
    }

    public Travels saveTravel(Travels travels){
        return repository.save(travels);
    }

    public Travels findTravelsById(Long id){
        Optional<Travels> travels = repository.findById(id);
        return travels.orElse(null);
    }


    public List<Travels> findTravelsByDateForward(String date){
        List<Travels> travels = (List<Travels>) repository.findAll();
        List<Travels> travelsForDate = new ArrayList<>();
        for (Travels travel: travels) {
            if (travel.getDateParsed().equals(date) && travel.isForward()) {
                travelsForDate.add(travel);
            }
        }
        travelsForDate.sort(Comparator.comparing(Travels::getTimeParsed));
        return travelsForDate;
    }

    public List<Travels> findTravelsByDateBack(String date){
        List<Travels> travels = (List<Travels>) repository.findAll();
        List<Travels> travelsForDate = new ArrayList<>();
        for (Travels travel: travels) {
            if (travel.getDateParsed().equals(date) && !travel.isForward()) {
                travelsForDate.add(travel);
            }
        }
        travelsForDate.sort(Comparator.comparing(Travels::getTimeParsed));
        return travelsForDate;
    }

    public List<Travels> findTravelsByDateDisabled(String dateParsed){
        List<Travels> travels = (List<Travels>) repository.findAll();
        List<Travels> travelsForDateDisabled = new ArrayList<>();
        LocalTime timeNow = LocalTime.now();
        LocalDate dateNow = LocalDate.now();
        System.out.println("date = " + dateParsed);
        System.out.println("dateNow = " + dateNow);
        System.out.println("timeNow = " + timeNow);
        LocalTime timePlusHour = timeNow.plusHours(1);
        System.out.println(timePlusHour);

        return travelsForDateDisabled;
    }
}
