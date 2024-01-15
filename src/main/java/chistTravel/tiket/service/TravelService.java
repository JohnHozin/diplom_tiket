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
//        String [] dateSplit = date.split(".");
//        System.out.println(dateSplit[0]);
//        System.out.println(dateSplit[1]);
//        LocalDate dateX = LocalDate.of(Integer.parseInt(dateSplit[2]),Integer.parseInt(dateSplit[1]),Integer.parseInt(dateSplit[0]));
        System.out.println("date = " + dateParsed);
        System.out.println("dateNow = " + dateNow);
        System.out.println("timeNow = " + timeNow);
//        System.out.println("dateX = " + dateX);
        LocalTime timePlusHour = timeNow.plusHours(1);
        System.out.println(timePlusHour);
//        DateFormatter formatter = DateFormatter.parse();
//        boolean after =

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
//        if (date.equals(formatter.format(dateNow).split(" ")[0])) {
//            for (Travels travel: travels) {
//                if (travel.getDateParsed().equals(date)) {
//                    if ()
//                    travelsForDateDisabled.add(travel);
//                }
//            }
//        }
//
//        time = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
//        user.setRegistered(formatter.format(time));



//        Collections.sort(travelsForDate, (a, b) -> {
//            return a.getTimeParsed() > b.getTimeParsed();
//        });
//        travelsForDateDisabled.sort(Comparator.comparing(Travels::getTimeParsed));

        return travelsForDateDisabled;
    }
}
