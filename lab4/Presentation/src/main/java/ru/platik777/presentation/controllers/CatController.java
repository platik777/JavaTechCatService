package ru.platik777.presentation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.platik777.application.models.Cat;
import ru.platik777.application.models.OwnerDetails;
import ru.platik777.application.services.CatService;
import ru.platik777.application.services.OwnerService;
import ru.platik777.infrastructure.entities.CatEntity;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cat")
public class CatController {
    private final CatService catService;
    private final OwnerService ownerService;

    @Autowired
    public CatController(CatService catService, OwnerService ownerService) {
        this.catService = catService;
        this.ownerService = ownerService;
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public List<Cat> getFriends(@PathVariable Integer id) {
        try {
            return catService.getCatFriends(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @PostMapping("/{id1}/friends/{id2}")
    @PreAuthorize("hasRole('ADMIN')")
    public void addFriend(@PathVariable Integer id1, @PathVariable Integer id2) {
        try {
            catService.makeFriend(id1, id2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DeleteMapping("/{id1}/friends/{id2}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteFriend(@PathVariable Integer id1, @PathVariable Integer id2) {
        try {
            catService.removeFriend(id1, id2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cat> getCat(@PathVariable Integer id, Principal principal) {
        try {
            Cat cat = catService.getCatById(id);
            if (ownerService.getOwnerById(cat.getOwner_id()).getUsername().equals(principal.getName())) {
                return ResponseEntity.ok(cat);
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<Cat> createCat(@RequestBody Cat cat, Principal principal) {
        OwnerDetails ownerDetails = ownerService.getOwnerByEmail(principal.getName());
        return ResponseEntity.ok(catService.createCatWithOwner(cat, ownerDetails));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cat> updateCat(@PathVariable Integer id, @RequestBody Cat cat, Principal principal) {
        try {
            Cat catInDB = catService.getCatById(id);
            if (principal.getName().equals(ownerService.getOwnerById(catInDB.getOwner_id()).getUsername())) {
                return ResponseEntity.ok(catService.updateCat(id, cat));
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCat(@PathVariable Integer id) {
        catService.deleteCat(id);
    }
}
