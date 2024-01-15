package chistTravel.tiket.db.repository;

import chistTravel.tiket.db.entity.RoleJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleJPA, Long> {
}
