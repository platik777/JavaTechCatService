package ru.platik777.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.platik777.infrastructure.entities.OwnerEntity;

@Repository
public interface OwnerRepository extends JpaRepository<OwnerEntity, Integer> {
}
