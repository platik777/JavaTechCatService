package ru.platik777.apiservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void createOwner(String ownerDetails) {
        kafkaTemplate.send("create_owner_request", ownerDetails);
    }

    public void updateOwner(String ownerDetailsWithId) {
        kafkaTemplate.send("update_owner_request", String.valueOf(ownerDetailsWithId));
    }

    public void getOwnerById(String id) {
        kafkaTemplate.send("get_owner_by_id_request", id);
    }

    public void getOwnerByEmail(String email) {
        kafkaTemplate.send("get_owner_by_email_request", email);
    }

    public void getOwnerCatsById(String id) {
        kafkaTemplate.send("get_owner_cats_by_id_request", id);
    }

    public void getAllOwners() {
        kafkaTemplate.send("get_all_owners_request", "");
    }

    public void deleteOwnerById(String id) {
        kafkaTemplate.send("delete_owner_by_id_request", id);
    }

    public void getCatById(String id) {
        kafkaTemplate.send("get_cat_by_id_request", id);
    }

    public void getAllCats() {
        kafkaTemplate.send("get_all_cats_request", "");
    }

    public void getCatFriends(String id) {
        kafkaTemplate.send("get_cat_friends_request", id);
    }

    public void getCatByBreed(String breed) {
        kafkaTemplate.send("get_cat_by_breed_request", breed);
    }

    public void getCatByName(String name) {
        kafkaTemplate.send("get_cat_by_name_request", name);
    }

    public void getCatByBreedAndName(String searchCatParameters) {
        kafkaTemplate.send("get_cat_by_breed_and_name_request", searchCatParameters);
    }

    public void makeFriend(String friendIdRequest) {
        kafkaTemplate.send("make_friend_request", friendIdRequest);
    }

    public void removeFriend(String friendIdRequest) {
        kafkaTemplate.send("remove_friend_request", friendIdRequest);
    }

    public void createCat(String createCatWithOwnerRequest) {
        kafkaTemplate.send("create_cat_request", createCatWithOwnerRequest);
    }

    public void updateCat(String updateCatRequest) {
        kafkaTemplate.send("update_cat_request", updateCatRequest);
    }

    public void deleteCat(String id) {
        kafkaTemplate.send("delete_cat_request", id);
    }
}
