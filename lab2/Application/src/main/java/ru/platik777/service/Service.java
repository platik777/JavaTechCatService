package ru.platik777.service;

import ru.platik777.dao.CatDAO;
import ru.platik777.dao.OwnerDAO;
import ru.platik777.entity.Cat;
import ru.platik777.entity.Owner;

import java.util.List;

public class Service {
    private final CatDAO catDAO;
    private final OwnerDAO ownerDAO;

    public Service(CatDAO catDAO, OwnerDAO ownerDAO) {
        this.catDAO = catDAO;
        this.ownerDAO = ownerDAO;
    }

    public List<Cat> getAllCats() {
        return catDAO.getAllCats();
    }

    public List<Owner> getAllOwners() {
        return ownerDAO.getAllOwners();
    }

    public Cat findCatById(int id) {
        return catDAO.findById(id);
    }

    public Owner findOwnerById(int id) {
        return ownerDAO.findById(id);
    }

    public void save(Cat cat) {
        catDAO.save(cat);
    }

    public void update(Cat cat) {
        catDAO.update(cat);
    }

    public void delete(Cat cat) {
        catDAO.delete(cat);
    }

    public void save(Owner owner) {
        ownerDAO.save(owner);
    }

    public void update(Owner owner) {
        ownerDAO.update(owner);
    }

    public void delete(Owner owner) {
        ownerDAO.delete(owner);
    }

    public void makeFriendsById(int cat1Id, int cat2Id) {
        catDAO.makeFriendsById(cat1Id, cat2Id);
    }
}
