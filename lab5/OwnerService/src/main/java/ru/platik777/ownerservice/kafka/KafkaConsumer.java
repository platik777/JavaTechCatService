package ru.platik777.ownerservice.kafka;

import lombok.extern.slf4j.Slf4j;
import models.Cat;
import models.OwnerDetails;
import models.OwnerDto;
import org.apache.kafka.common.protocol.types.Field;
import requestModels.OperationSuccessRequest;
import requestModels.UpdateOwnerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.platik777.ownerservice.services.OwnerService;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class KafkaConsumer {
    private final OwnerService ownerService;
    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaConsumer(OwnerService ownerService, KafkaProducer kafkaProducer, ObjectMapper objectMapper) {
        this.ownerService = ownerService;
        this.kafkaProducer = kafkaProducer;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "create_owner_request", groupId = "group")
    public void createOwner(String ownerString) {
        OwnerDetails ownerDetails;
        String ownerDetailsResultString;
        try {
            ownerDetails = objectMapper.readValue(ownerString, OwnerDetails.class);
            OwnerDetails ownerDetailsResult = ownerService.saveOwner(ownerDetails);
            ownerDetailsResultString = objectMapper.writeValueAsString(ownerDetailsResult);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            return;
        }

        kafkaProducer.createOwner(ownerDetailsResultString);
    }

    @KafkaListener(topics = "update_owner_request", groupId = "group")
    public void updateOwner(String ownerDetailsWithId) {
        String ownerDtoJson = null;
        try {
            UpdateOwnerRequest message = objectMapper.readValue(ownerDetailsWithId, UpdateOwnerRequest.class);
            OwnerDetails ownerDetails = ownerService.updateOwner(message.getUpdateOwnerDetails(), message.getId());
            ownerDtoJson = objectMapper.writeValueAsString(new OwnerDto(ownerDetails));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        kafkaProducer.updateOwner(ownerDtoJson);
    }

    @KafkaListener(topics = "get_owner_by_email_request", groupId = "group")
    public void getOwnerByEmail(String email) {
        String ownerDetailsJson = null;
        try {
            ownerDetailsJson = objectMapper.writeValueAsString(new OwnerDto(ownerService.getOwnerByEmail(email)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        kafkaProducer.getOwnerByEmail(ownerDetailsJson);
    }

    @KafkaListener(topics = "get_owner_cats_by_id_request", groupId = "group")
    public void getOwnerCatsById(String id) {
        List<Cat> cats;
        String result = null;
        try {
            cats = ownerService.getOwnerCatsById(Integer.valueOf(id));
            System.out.println(cats);
            result = objectMapper.writeValueAsString(cats);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        kafkaProducer.getOwnerCatsById(result);
    }

    @KafkaListener(topics = "get_all_owners_request", groupId = "group")
    public void getAllOwners() {
        List<OwnerDetails> ownerList = ownerService.getAllOwners();
        List<OwnerDto> ownerDtoList = new ArrayList<>();
        for (OwnerDetails ownerDetails : ownerList) {
            OwnerDto ownerDto = new OwnerDto(ownerDetails);
            ownerDtoList.add(ownerDto);
        }
        String ownerListResult = "";
        try {
            ownerListResult = objectMapper.writeValueAsString(ownerDtoList);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }

        kafkaProducer.getAllOwners(ownerListResult);
    }

    @KafkaListener(topics = "get_owner_by_id_request", groupId = "group")
    public void getOwnerById(String id) {
        OwnerDetails ownerDetails;
        String ownerDtoJson = null;
        try {
            ownerDetails = ownerService.getOwnerById(Integer.valueOf(id));
            OwnerDto ownerDto = new OwnerDto(ownerDetails);
            ownerDtoJson = objectMapper.writeValueAsString(ownerDto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        kafkaProducer.getOwnerById(ownerDtoJson);
    }

    @KafkaListener(topics = "delete_owner_by_id_request", groupId = "group")
    public void deleteOwnerById(String id) {
        ownerService.deleteOwner(Integer.valueOf(id));
        OperationSuccessRequest operationSuccessRequest = new OperationSuccessRequest(true);
        String operationSuccessRequestJson = null;
        try {
            operationSuccessRequestJson = objectMapper.writeValueAsString(operationSuccessRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        kafkaProducer.deleteOwnerById(operationSuccessRequestJson);
    }


}