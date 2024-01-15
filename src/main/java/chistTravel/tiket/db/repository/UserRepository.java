package chistTravel.tiket.db.repository;

import chistTravel.tiket.db.entity.UserJPA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserJPA, Long> {

    UserJPA findByUsername(String username);
}
