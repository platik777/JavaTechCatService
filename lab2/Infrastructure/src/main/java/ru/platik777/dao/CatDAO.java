package ru.platik777.dao;

import ru.platik777.entity.Cat;

import java.util.List;

public interface CatDAO {
    public List<Cat> getAllCats();

    public Cat findById(int id);

    public void save(Cat cat);

    public void update(Cat cat);

    public void delete(Cat cat);

    public void makeFriendsById(int cat1Id, int cat2Id);
}
