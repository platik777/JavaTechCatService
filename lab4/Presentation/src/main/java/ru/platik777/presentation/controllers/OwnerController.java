package ru.platik777.presentation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.platik777.application.models.Cat;
import ru.platik777.application.models.OwnerDetails;
import ru.platik777.application.services.OwnerService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/owner")
public class OwnerController {
    private final OwnerService ownerService;

    @Autowired
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public List<OwnerDetails> getAllOwners() {
        return ownerService.getAllOwners();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OwnerDetails> getOwner(@PathVariable("id") Integer id, Principal principal) {
        try {
            OwnerDetails ownerDetails = ownerService.getOwnerById(id);
            if (principal.getName().equals(ownerDetails.getUsername())) {
                return ResponseEntity.ok(ownerDetails);
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping("/{id}/cats")
    public ResponseEntity<List<Cat>> getOwnedCats(@PathVariable("id") Integer id, Principal principal) {
        try {
            OwnerDetails ownerDetails = ownerService.getOwnerById(id);
            if (principal.getName().equals(ownerDetails.getUsername())) {
                return ResponseEntity.ok(ownerService.getOwnerCatsById(id));
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OwnerDetails> updateOwner(@PathVariable("id") Integer id, @RequestBody OwnerDetails ownerDetails, Principal principal) {
        try {
            OwnerDetails ownerDetailsInDatabase = ownerService.getOwnerById(id);
            if (principal.getName().equals(ownerDetailsInDatabase.getUsername())) {
                return ResponseEntity.ok(ownerService.updateOwner(ownerDetails, id));
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOwner(@PathVariable("id") Integer id, Principal principal) {
        try {
            OwnerDetails ownerDetails = ownerService.getOwnerById(id);
            if (principal.getName().equals(ownerDetails.getUsername())) {
                ownerService.deleteOwner(id);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
}
