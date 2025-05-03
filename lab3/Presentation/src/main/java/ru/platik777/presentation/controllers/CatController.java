package ru.platik777.presentation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.platik777.application.models.Cat;
import ru.platik777.application.services.CatService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/lab3/cat")
public class CatController {
    private final CatService catService;

    @Autowired
    public CatController(CatService catService) {
        this.catService = catService;
    }

    @GetMapping("/")
    public List<Cat> getCats(@RequestParam(required = false) String name,
                             @RequestParam(required = false) String breed) {
        if (name != null && breed != null) {
            return catService.getCatsByNameAndBreed(name, breed);
        } else if (name != null) {
            return catService.getCatsByName(name);
        } else if (breed != null) {
            return catService.getCatsByBreed(breed);
        }

        return catService.getAllCats();
    }

    @GetMapping("/{id}/friends")
    public List<Cat> getFriends(@PathVariable Integer id) {
        try {
            return catService.getCatFriends(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @PostMapping("/{id1}/friends/{id2}")
    public void addFriend(@PathVariable Integer id1, @PathVariable Integer id2) {
        try {
            catService.makeFriend(id1, id2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DeleteMapping("/{id1}/friends/{id2}")
    public void deleteFriend(@PathVariable Integer id1, @PathVariable Integer id2) {
        try {
            catService.removeFriend(id1, id2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/{id}")
    public Cat getCat(@PathVariable Integer id) {
        try {
            return catService.getCatById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/")
    public Cat createCat(@RequestBody Cat cat) {
        return catService.createCat(cat);
    }

    @PutMapping("/{id}")
    public Cat updateCat(@PathVariable Integer id, @RequestBody Cat cat) {
        try {
            return catService.updateCat(id, cat);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteCat(@PathVariable Integer id) {
        catService.deleteCat(id);
    }
}
