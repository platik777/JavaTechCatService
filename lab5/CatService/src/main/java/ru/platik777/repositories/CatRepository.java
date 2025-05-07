package ru.platik777.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.platik777.entities.CatEntity;

import java.util.List;

@Repository
public interface CatRepository extends JpaRepository<CatEntity, Integer> {
    List<CatEntity> findCatEntitiesByBreed(String breed);

    List<CatEntity> findCatEntitiesByNameAndBreed(String name, String breed);

    List<CatEntity> findCatEntitiesByName(String catName);
}
