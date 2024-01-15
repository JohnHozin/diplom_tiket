package chistTravel.tiket.service;

import chistTravel.tiket.db.entity.Route;
import chistTravel.tiket.db.repository.RouteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RouteService {
    @Autowired
    private RouteRepo repository;

    public List<Route> listAllRoute(){
        return (List<Route>) repository.findAll();
    }

    public Route saveRoute(Route route){
        return repository.save(route);
    }

    public void deleteRouteById(Long id){
        Optional<Route> deleted = repository.findById(id);
        deleted.ifPresent(route -> repository.delete(route));
    }

    public void statusToFalseRouteById(Long id){
        Optional<Route> deleted = repository.findById(id);
        deleted.ifPresent(route -> route.setStatus(false));
        deleted.ifPresent(route -> saveRoute(deleted.get()));
    }

    public void statusToTrueRouteById(Long id){
        Optional<Route> deleted = repository.findById(id);
        deleted.ifPresent(route -> route.setStatus(true));
        deleted.ifPresent(route -> saveRoute(deleted.get()));
    }
}
