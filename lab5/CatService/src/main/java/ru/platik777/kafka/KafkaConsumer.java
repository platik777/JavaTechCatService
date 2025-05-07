package ru.platik777.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import models.Cat;
import requestModels.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.platik777.services.CatService;

import java.util.List;

@Slf4j
@Service
public class KafkaConsumer {
    private final CatService catService;
    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaConsumer(CatService catService, KafkaProducer kafkaProducer, ObjectMapper objectMapper) {
        this.catService = catService;
        this.kafkaProducer = kafkaProducer;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "get_cat_by_id_request", groupId = "group")
    public void getCatById(String id) {
        Cat cat;
        String catString = null;
        try {
            cat = catService.getCatById(Integer.valueOf(id));
            catString = objectMapper.writeValueAsString(cat);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        kafkaProducer.getCatById(catString);
    }

    @KafkaListener(topics = "get_all_cats_request", groupId = "group")
    public void getAllCats() {
        List<Cat> cats = catService.getAllCats();
        String catsString = null;
        try {
            catsString = objectMapper.writeValueAsString(cats);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        kafkaProducer.getAllCats(catsString);
    }

    @KafkaListener(topics = "get_cat_friends_request", groupId = "group")
    public void getCatFriends(String id) {
        List<Cat> catFriends;
        String catFriendsString = null;
        try {
            catFriends = catService.getCatFriends(Integer.valueOf(id));
            catFriendsString = objectMapper.writeValueAsString(catFriends);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        kafkaProducer.getCatFriends(catFriendsString);
    }

    @KafkaListener(topics = "get_cats_by_breed_request", groupId = "group")
    public void getCatsByBreed(String breed) {
        List<Cat> catsByBreed;
        String catsByBreedString = null;
        try {
            catsByBreed = catService.getCatsByBreed(breed);
            catsByBreedString = objectMapper.writeValueAsString(catsByBreed);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        kafkaProducer.getCatByBreed(catsByBreedString);
    }

    @KafkaListener(topics = "get_cats_by_name_request", groupId = "group")
    public void getCatsByName(String name) {
        List<Cat> catsByName;
        String catsByNameString = null;
        try {
            catsByName = catService.getCatsByName(name);
            catsByNameString = objectMapper.writeValueAsString(catsByName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        kafkaProducer.getCatByName(catsByNameString);
    }

    @KafkaListener(topics = "get_cats_by_name_and_breed_request", groupId = "group")
    public void getCatsByNameAndBreed(String searchParameters) {
        List<Cat> catsByNameAndBreed;
        String catsByNameAndBreedString = null;
        try {
            CatSearchParameters catSearchParameters = objectMapper.readValue(searchParameters, CatSearchParameters.class);
            if (catSearchParameters.getName() != null && catSearchParameters.getBreed() != null) {
                catsByNameAndBreed = catService.getCatsByNameAndBreed(
                        catSearchParameters.getName(), catSearchParameters.getBreed());
                catsByNameAndBreedString = objectMapper.writeValueAsString(catsByNameAndBreed);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        kafkaProducer.getCatByBreedAndName(catsByNameAndBreedString);
    }

    @KafkaListener(topics = "make_friend_request", groupId = "group")
    public void makeFriend(String friendsIdRequest) {
        FriendIdRequest friendsIdRequestObject;
        OperationSuccessRequest operationSuccessRequest = new OperationSuccessRequest();
        String operationSuccessRequestString = null;
        try {
            friendsIdRequestObject = objectMapper.readValue(friendsIdRequest, FriendIdRequest.class);
            catService.makeFriend(friendsIdRequestObject.getId1(), friendsIdRequestObject.getId2());
            operationSuccessRequest.setResult(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            operationSuccessRequest.setResult(false);
        }

        try {
            operationSuccessRequestString = objectMapper.writeValueAsString(operationSuccessRequest);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
        kafkaProducer.makeFriend(operationSuccessRequestString);
    }

    @KafkaListener(topics = "remove_friend_request", groupId = "group")
    public void removeFriend(String friendsIdRequest) {
        FriendIdRequest friendsIdRequestObject;
        OperationSuccessRequest operationSuccessRequest = new OperationSuccessRequest();
        String operationSuccessRequestString = null;
        try {
            friendsIdRequestObject = objectMapper.readValue(friendsIdRequest, FriendIdRequest.class);
            catService.removeFriend(friendsIdRequestObject.getId1(), friendsIdRequestObject.getId2());
            operationSuccessRequest.setResult(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            operationSuccessRequest.setResult(false);
        }

        try {
            operationSuccessRequestString = objectMapper.writeValueAsString(operationSuccessRequest);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
        kafkaProducer.makeFriend(operationSuccessRequestString);
    }

    @KafkaListener(topics = "create_cat_request", groupId = "group")
    public void createCat(String catRequest) {
        CreateCatWithOwnerRequest createRequest;
        Cat resultCat;
        String resultCatString = null;
        try {
            createRequest = objectMapper.readValue(catRequest, CreateCatWithOwnerRequest.class);
            resultCat = catService.createCatWithOwner(createRequest.getCat(), createRequest.getOwnerDto());
            resultCatString = objectMapper.writeValueAsString(resultCat);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        kafkaProducer.createCat(resultCatString);
    }

    @KafkaListener(topics = "update_cat_request", groupId = "group")
    public void updateCat(String catUpdateRequest) {
        Cat resultCat;
        String resultCatString = null;
        System.out.println(catUpdateRequest);
        try {
            UpdateCatRequest updateRequest = objectMapper.readValue(catUpdateRequest, UpdateCatRequest.class);
            resultCat = catService.updateCat(updateRequest.getId(), updateRequest.getCat());
            resultCatString = objectMapper.writeValueAsString(resultCat);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        kafkaProducer.updateCat(resultCatString);
    }

    @KafkaListener(topics = "delete_cat_request", groupId = "group")
    public void deleteCat(String catDeleteId) {
        catService.deleteCat(Integer.valueOf(catDeleteId));
        OperationSuccessRequest operationSuccessRequest = new OperationSuccessRequest(true);
        String operationSuccessRequestString = null;
        try {
            operationSuccessRequestString = objectMapper.writeValueAsString(operationSuccessRequest);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
        kafkaProducer.deleteCat(operationSuccessRequestString);
    }
}
