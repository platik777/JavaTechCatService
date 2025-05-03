package ru.platik777.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.platik777.application.models.Cat;
import ru.platik777.application.models.Owner;
import ru.platik777.infrastructure.entities.CatEntity;
import ru.platik777.infrastructure.entities.OwnerEntity;
import ru.platik777.infrastructure.repositories.OwnerRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OwnerService {
    private final OwnerRepository ownerRepository;

    @Autowired
    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Owner getOwnerById(Integer id) throws ChangeSetPersister.NotFoundException {
        OwnerEntity ownerEntity = ownerRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);

        return new Owner(ownerEntity);
    }

    public List<Cat> getCatsByOwnerId(Integer id) throws ChangeSetPersister.NotFoundException {
        OwnerEntity ownerEntity = ownerRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
        List<Cat> cats = new ArrayList<>();
        List<CatEntity> catEntities = ownerEntity.getCatEntities();
        for (CatEntity catEntity : catEntities) {
            cats.add(new Cat(catEntity));
        }
        return cats;
    }

    public List<Owner> getAllOwners() {
        List<OwnerEntity> ownerEntities = ownerRepository.findAll();
        List<Owner> owners = new ArrayList<>();
        for (OwnerEntity ownerEntity : ownerEntities) {
            owners.add(new Owner(ownerEntity));
        }
        return owners;
    }

    @Transactional
    public Owner createOwner(Owner owner) {
        OwnerEntity ownerEntity = ownerRepository.save(
                new OwnerEntity(owner.getName(), owner.getBirthday()));
        return new Owner(ownerEntity);
    }

    @Transactional
    public Owner updateOwner(Owner owner, Integer id) throws ChangeSetPersister.NotFoundException {
        OwnerEntity ownerEntity = ownerRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);

        ownerEntity.setId(id);
        ownerEntity.setName(owner.getName());
        ownerEntity.setBirthday(owner.getBirthday());

        ownerRepository.save(ownerEntity);
        return new Owner(ownerEntity);
    }

    @Transactional
    public void deleteOwner(Integer id) {
        ownerRepository.deleteById(id);
    }
}
