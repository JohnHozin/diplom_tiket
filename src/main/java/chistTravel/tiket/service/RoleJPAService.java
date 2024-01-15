package chistTravel.tiket.service;

import chistTravel.tiket.db.entity.RoleJPA;
import chistTravel.tiket.db.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoleJPAService {
    @Autowired
    private RoleRepository repository;

    public List<RoleJPA> listAllRoles(){
        return repository.findAll();
    }
}

