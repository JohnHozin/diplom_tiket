package chistTravel.tiket.service;


import chistTravel.tiket.db.entity.LandingSite;
import chistTravel.tiket.db.entity.UserJPA;
import chistTravel.tiket.db.repository.LandingSiteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class LandingSiteService {
    @Autowired
    private LandingSiteRepo repository;

    public List<LandingSite> listAllLandingSites(){
        return (List<LandingSite>) repository.findAll();
    }

    public LandingSite saveLandingSite(LandingSite landingSite){
        return repository.save(landingSite);
    }

    public LandingSite findLandingSiteById(Long id){
        Optional<LandingSite> landingSite = repository.findById(id);
        return landingSite.orElse(null);
    }
}
