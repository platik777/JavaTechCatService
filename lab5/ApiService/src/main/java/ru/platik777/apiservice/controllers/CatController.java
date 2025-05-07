package ru.platik777.apiservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import models.Cat;
import models.OwnerDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import requestModels.*;
import ru.platik777.apiservice.kafka.KafkaConsumer;
import ru.platik777.apiservice.kafka.KafkaProducer;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/cat")
@RequiredArgsConstructor
public class CatController {
    private final KafkaConsumer kafkaConsumer;
    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Cat>> getCats(@RequestParam(required = false) String name,
                             @RequestParam(required = false) String breed) {
        if (name != null && breed != null) {
            CatSearchParameters catSearchParameters = new CatSearchParameters(name, breed);
            try {
                String requestJson = objectMapper.writeValueAsString(catSearchParameters);
                kafkaProducer.getCatByBreedAndName(requestJson);
                Optional<List<Cat>> getResult = kafkaConsumer.getCatsByNameAndBreed().get(8, TimeUnit.SECONDS);
                return ResponseEntity.ok(getResult.orElse(new ArrayList<>()));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } else if (name != null) {
            kafkaProducer.getCatByName(name);
            try {
                Optional<List<Cat>> getResult = kafkaConsumer.getCatsByName().get(8, TimeUnit.SECONDS);
                return ResponseEntity.ok(getResult.orElse(new ArrayList<>()));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } else if (breed != null) {
            kafkaProducer.getCatByBreed(breed);
            try {
                Optional<List<Cat>> getResult = kafkaConsumer.getCatsByBreed().get(8, TimeUnit.SECONDS);
                return ResponseEntity.ok(getResult.orElse(new ArrayList<>()));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        }

        kafkaProducer.getAllCats();
        try {
            Optional<List<Cat>> allCats = kafkaConsumer.getAllCats().get(8, TimeUnit.SECONDS);
            return ResponseEntity.ok(allCats.orElse(new ArrayList<>()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping("/{id}/friends")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Cat>> getFriends(@PathVariable Integer id) {
        try {
            kafkaProducer.getCatFriends(id.toString());
            Optional<List<Cat>> getResult = kafkaConsumer.getCatFriends().get(8, TimeUnit.SECONDS);
            return ResponseEntity.ok(getResult.orElse(new ArrayList<>()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @PostMapping("/{id1}/friends/{id2}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> makeFriend(@PathVariable Integer id1, @PathVariable Integer id2) {
        try {
            FriendIdRequest friendIdRequest = new FriendIdRequest(id1, id2);
            String requestJson = objectMapper.writeValueAsString(friendIdRequest);
            kafkaProducer.makeFriend(requestJson);
            Optional<OperationSuccessRequest> makeFriendResult = kafkaConsumer.getMakeFriend().get(8, TimeUnit.SECONDS);
            if (makeFriendResult.isPresent() && makeFriendResult.get().getResult()) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @DeleteMapping("/{id1}/friends/{id2}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeFriend(@PathVariable Integer id1, @PathVariable Integer id2) {
        try {
            FriendIdRequest friendIdRequest = new FriendIdRequest(id1, id2);
            String requestJson = objectMapper.writeValueAsString(friendIdRequest);
            kafkaProducer.removeFriend(requestJson);
            Optional<OperationSuccessRequest> removeFriendResult = kafkaConsumer.getRemoveFriend().get(8, TimeUnit.SECONDS);
            if (removeFriendResult.isPresent() && removeFriendResult.get().getResult()) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cat> getCat(@PathVariable Integer id, Principal principal) {
        try {
            kafkaProducer.getCatById(id.toString());
            Optional<Cat> cat = kafkaConsumer.getCatById().get(8, TimeUnit.SECONDS);
            if (cat.isPresent()) {
                kafkaProducer.getOwnerById(cat.get().getOwner_id().toString());
                Optional<OwnerDto> ownerDto = kafkaConsumer.getOwnerById().get(8, TimeUnit.SECONDS);
                if (ownerDto.isPresent() && ownerDto.get().getEmail().equals(principal.getName())) {
                    return ResponseEntity.ok(cat.get());
                } else {
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    /*{
        "name": "Kot",
        "birthday": "2005-07-19",
        "breed": "cat_breed",
        "age": "19",
        "color": "GREY"
    }*/

    @PostMapping("/")
    public ResponseEntity<Cat> createCat(@RequestBody Cat cat, Principal principal) {
        try {
            kafkaProducer.getOwnerByEmail(principal.getName());
            Optional<OwnerDto> ownerDto = kafkaConsumer.getOwnerByEmail().get(8, TimeUnit.SECONDS);
            if (ownerDto.isPresent()) {
                CreateCatWithOwnerRequest createRequest = new CreateCatWithOwnerRequest(ownerDto.get(), cat);
                String requestJson = objectMapper.writeValueAsString(createRequest);
                kafkaProducer.createCat(requestJson);
                Optional<Cat> createdCat = kafkaConsumer.getCreateCatWithOwner().get(8, TimeUnit.SECONDS);
                return createdCat.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cat> updateCat(@PathVariable Integer id, @RequestBody Cat cat, Principal principal) {
        try {
            kafkaProducer.getCatById(id.toString());
            Optional<Cat> updatedCat = kafkaConsumer.getCatById().get(8, TimeUnit.SECONDS);
            if (updatedCat.isPresent()) {
                System.out.println(updatedCat + " " + updatedCat.get().getOwner_id());
                kafkaProducer.getOwnerById(updatedCat.get().getOwner_id().toString());
                Optional<OwnerDto> ownerDto = kafkaConsumer.getOwnerById().get(8, TimeUnit.SECONDS);
                if (ownerDto.isPresent() && ownerDto.get().getEmail().equals(principal.getName())) {
                    UpdateCatRequest updateCatRequest = new UpdateCatRequest(id, cat);
                    String requestJson = objectMapper.writeValueAsString(updateCatRequest);
                    kafkaProducer.updateCat(requestJson);
                    Optional<Cat> resultCat = kafkaConsumer.getUpdateCat().get(8, TimeUnit.SECONDS);
                    return resultCat.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
                }
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCat(@PathVariable Integer id) {
        try {
            kafkaProducer.deleteCat(id.toString());
            Optional<OperationSuccessRequest> deleteResult = kafkaConsumer.getDeleteCatById().get(8, TimeUnit.SECONDS);
            if (deleteResult.isPresent() && deleteResult.get().getResult()) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
}
