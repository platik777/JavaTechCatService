package ru.platik777.presentation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.platik777.application.models.Cat;
import ru.platik777.application.models.Owner;
import ru.platik777.application.services.OwnerService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/lab3/owner")
public class OwnerController {
    private final OwnerService ownerService;

    @Autowired
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping("/")
    public List<Owner> getAllOwners() {
        return ownerService.getAllOwners();
    }

    @GetMapping("/{id}")
    public Owner getOwner(@PathVariable("id") Integer id) {
        try {
            return ownerService.getOwnerById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/{id}/cats")
    public List<Cat> getCatsByOwnerId(@PathVariable("id") Integer id) {
        try {
            return ownerService.getCatsByOwnerId(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @PostMapping("/create")
    public Owner createOwner(@RequestBody Owner owner) {
        return ownerService.createOwner(owner);
    }

    @PutMapping("/{id}")
    public Owner updateOwner(@PathVariable("id") Integer id, @RequestBody Owner owner) {
        try {
            return ownerService.updateOwner(owner, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteOwner(@PathVariable("id") Integer id) {
        ownerService.deleteOwner(id);
    }
}
