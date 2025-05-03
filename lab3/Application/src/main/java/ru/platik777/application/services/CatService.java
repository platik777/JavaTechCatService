package ru.platik777.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.platik777.application.models.Cat;
import ru.platik777.infrastructure.entities.CatEntity;
import ru.platik777.infrastructure.entities.OwnerEntity;
import ru.platik777.infrastructure.repositories.CatRepository;
import ru.platik777.infrastructure.repositories.OwnerRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CatService {
    private final CatRepository catRepository;
    private final OwnerRepository ownerRepository;

    @Autowired
    public CatService(CatRepository catRepository, OwnerRepository ownerRepository) {
        this.catRepository = catRepository;
        this.ownerRepository = ownerRepository;
    }

    public Cat getCatById(Integer id) throws ChangeSetPersister.NotFoundException {
        CatEntity catEntity = catRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
        return new Cat(catEntity);
    }

    public List<Cat> getAllCats() {
        List<CatEntity> catEntities = catRepository.findAll();

        List<Cat> cats = new ArrayList<>();
        for (CatEntity catEntity : catEntities) {
            cats.add(new Cat(catEntity));
        }
        return cats;
    }

    public List<Cat> getCatFriends(Integer id) throws ChangeSetPersister.NotFoundException {
        CatEntity catEntity = catRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
        List<Cat> friends = new ArrayList<>();
        for (CatEntity catEntityFriend : catEntity.getFriends()) {
            friends.add(new Cat(catEntityFriend));
        }
        return friends;
    }

    public List<Cat> getCatsByBreed(String breed) {
        List<CatEntity> catEntities = catRepository.findCatEntitiesByBreed(breed);
        List<Cat> cats = new ArrayList<>();
        for (CatEntity catEntity : catEntities) {
            cats.add(new Cat(catEntity));
        }
        return cats;
    }

    public List<Cat> getCatsByName(String name) {
        List<CatEntity> catEntities = catRepository.findCatEntitiesByName(name);
        List<Cat> cats = new ArrayList<>();
        for (CatEntity catEntity : catEntities) {
            cats.add(new Cat(catEntity));
        }
        return cats;
    }

    public List<Cat> getCatsByNameAndBreed(String name, String breed) {
        List<CatEntity> catEntities = catRepository.findCatEntitiesByNameAndBreed(name, breed);
        List<Cat> cats = new ArrayList<>();
        for (CatEntity catEntity : catEntities) {
            cats.add(new Cat(catEntity));
        }
        return cats;
    }

    @Transactional
    public void makeFriend(Integer id1, Integer id2) throws ChangeSetPersister.NotFoundException {
        CatEntity cat1 = catRepository.findById(id1).orElseThrow(ChangeSetPersister.NotFoundException::new);
        CatEntity cat2 = catRepository.findById(id2).orElseThrow(ChangeSetPersister.NotFoundException::new);
        cat1.addFriend(cat2);
    }

    @Transactional
    public void removeFriend(Integer id1, Integer id2) throws ChangeSetPersister.NotFoundException {
        CatEntity cat1 = catRepository.findById(id1).orElseThrow(ChangeSetPersister.NotFoundException::new);
        CatEntity cat2 = catRepository.findById(id2).orElseThrow(ChangeSetPersister.NotFoundException::new);
        cat1.removeFriend(cat2);
    }

    @Transactional
    public Cat createCat(Cat cat) {
        CatEntity catEntity = new CatEntity(
                cat.getName(), cat.getBirthday(), cat.getBreed(), cat.getAge(),cat.getColor());
        OwnerEntity ownerEntity = ownerRepository.findById(cat.getOwner_id()).orElse(null);
        catEntity.setOwnerEntity(ownerEntity);
        catRepository.save(catEntity);

        return new Cat(catEntity);
    }

    @Transactional
    public Cat updateCat(Integer id, Cat cat) throws ChangeSetPersister.NotFoundException {
        CatEntity catEntity = catRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);

        catEntity.setName(cat.getName());
        catEntity.setBirthday(cat.getBirthday());
        catEntity.setBreed(cat.getBreed());
        catEntity.setAge(cat.getAge());
        catEntity.setColor(cat.getColor());

        return new Cat(catEntity);
    }

    @Transactional
    public void deleteCat(Integer id) {
        catRepository.deleteById(id);
    }
}
