package ru.platik777.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.platik777.entities.OwnerEntity;

import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<OwnerEntity, Integer> {
    Optional<OwnerEntity> findByEmail(String email);
}
