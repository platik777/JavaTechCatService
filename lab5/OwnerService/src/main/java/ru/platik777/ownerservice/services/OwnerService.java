package ru.platik777.ownerservice.services;

import models.Cat;
import models.OwnerDetails;
import models.OwnerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.platik777.entities.CatEntity;
import ru.platik777.entities.OwnerEntity;
import ru.platik777.ownerservice.repositories.OwnerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OwnerService implements UserDetailsService {
    private final OwnerRepository ownerRepository;

    @Autowired
    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public OwnerDetails getOwnerById(Integer id) throws ChangeSetPersister.NotFoundException {
        OwnerEntity ownerEntity = ownerRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);

        return new OwnerDetails(ownerEntity);
    }

    public OwnerDetails getOwnerByEmail(String email) {
        Optional<OwnerEntity> ownerEntity = ownerRepository.findByEmail(email);
        return ownerEntity.map(OwnerDetails::new).orElse(null);
    }

    public List<Cat> getOwnerCatsById(Integer id) throws ChangeSetPersister.NotFoundException {
        OwnerEntity ownerEntity = ownerRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
        List<Cat> cats = new ArrayList<>();
        List<CatEntity> catEntities = ownerEntity.getCatEntities();
        for (CatEntity catEntity : catEntities) {
            cats.add(new Cat(catEntity));
        }
        return cats;
    }

    public List<OwnerDetails> getAllOwners() {
        List<OwnerEntity> ownerEntities = ownerRepository.findAll();
        List<OwnerDetails> ownerDetails = new ArrayList<>();
        for (OwnerEntity ownerEntity : ownerEntities) {
            ownerDetails.add(new OwnerDetails(ownerEntity));
        }
        return ownerDetails;
    }

    @Transactional
    public OwnerDetails saveOwner(OwnerDetails ownerDetails) {
        OwnerEntity ownerEntity = ownerRepository.save(
                new OwnerEntity(ownerDetails.getName(), ownerDetails.getBirthday(),
                        ownerDetails.getEmail(), ownerDetails.getPassword(), ownerDetails.getRole()));
        return new OwnerDetails(ownerEntity);
    }

    @Transactional
    public OwnerDetails updateOwner(OwnerDto ownerDetails, Integer id) throws ChangeSetPersister.NotFoundException {
        OwnerEntity ownerEntity = ownerRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);

        ownerEntity.setId(id);
        ownerEntity.setName(ownerDetails.getName());
        ownerEntity.setBirthday(ownerDetails.getBirthday());

        ownerRepository.save(ownerEntity);
        return new OwnerDetails(ownerEntity);
    }

    @Transactional
    public void deleteOwner(Integer id) {
        ownerRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        OwnerEntity ownerEntity = ownerRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User " + email + " not found"));
        return new OwnerDetails(ownerEntity);
    }
}
