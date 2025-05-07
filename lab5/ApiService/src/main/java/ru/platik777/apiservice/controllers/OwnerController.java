package ru.platik777.apiservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import models.Cat;
import models.OwnerDetails;
import models.OwnerDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import requestModels.OperationSuccessRequest;
import requestModels.UpdateOwnerRequest;
import ru.platik777.apiservice.kafka.KafkaConsumer;
import ru.platik777.apiservice.kafka.KafkaProducer;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/owner")
@RequiredArgsConstructor
public class OwnerController {
    private final KafkaConsumer kafkaConsumer;
    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OwnerDto>> getAllOwners() {
        kafkaProducer.getAllOwners();
        try {
            Optional<List<OwnerDto>> ownerDtos = kafkaConsumer.getAllOwners().get(8, TimeUnit.SECONDS);
            return ownerDtos.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OwnerDto> getOwner(@PathVariable("id") Integer id, Principal principal) {
        try {
            kafkaProducer.getOwnerById(id.toString());
            Optional<OwnerDto> ownerDto = kafkaConsumer.getOwnerById().get(8, TimeUnit.SECONDS);
            if (ownerDto.isPresent() && ownerDto.get().getEmail().equals(principal.getName())) {
                return ResponseEntity.ok(ownerDto.get());
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping("/{id}/cats")
    public ResponseEntity<List<Cat>> getOwnedCats(@PathVariable("id") Integer id, Principal principal) {
        try {
            kafkaProducer.getOwnerById(id.toString());
            Optional<OwnerDto> ownerDto = kafkaConsumer.getOwnerById().get(8, TimeUnit.SECONDS);
            if (ownerDto.isPresent() && principal.getName().equals(ownerDto.get().getEmail())) {
                kafkaProducer.getOwnerCatsById(ownerDto.get().getId().toString());
                Optional<List<Cat>> catList = kafkaConsumer.getOwnerCatsById().get(8, TimeUnit.SECONDS);
                return ResponseEntity.ok(catList.get());
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OwnerDto> updateOwner(@PathVariable("id") Integer id, @RequestBody OwnerDetails ownerDetails, Principal principal) {
        try {
            kafkaProducer.getOwnerById(id.toString());
            Optional<OwnerDto> ownerDto = kafkaConsumer.getOwnerById().get(8, TimeUnit.SECONDS);
            if (ownerDto.isPresent() && principal.getName().equals(ownerDto.get().getEmail())) {
                UpdateOwnerRequest updateRequest = new UpdateOwnerRequest(id, new OwnerDto(ownerDetails));
                String json = objectMapper.writeValueAsString(updateRequest);

                System.out.println(json);

                kafkaProducer.updateOwner(json);
                Optional<OwnerDto> updatedOwnerDetails = kafkaConsumer.getUpdateOwner().get(8, TimeUnit.SECONDS);
                return updatedOwnerDetails.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOwner(@PathVariable("id") Integer id, Principal principal) {
        try {
            kafkaProducer.getOwnerById(id.toString());
            Optional<OwnerDto> ownerDto = kafkaConsumer.getOwnerById().get(8, TimeUnit.SECONDS);
            if (ownerDto.isPresent() && principal.getName().equals(ownerDto.get().getEmail())) {
                kafkaProducer.deleteOwnerById(id.toString());
                Optional<OperationSuccessRequest> deleteResponse = kafkaConsumer.getDeleteOwner().get(8, TimeUnit.SECONDS);
                if (deleteResponse.isPresent() && deleteResponse.get().getResult()) {
                    return ResponseEntity.ok().build();
                } else {
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                }
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
}
