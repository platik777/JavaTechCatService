package ru.platik777.dao;

import ru.platik777.entity.Owner;

import java.util.List;

public interface OwnerDAO {
    public List<Owner> getAllOwners();

    public void save(Owner owner);

    public Owner findById(int id);

    public void update(Owner owner);

    public void delete(Owner owner);
}
