package ru.platik777.kafka;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void getCatById(String cat) {
        kafkaTemplate.send("get_cat_by_id_response", cat);
    }

    public void getAllCats(String cats) {
        kafkaTemplate.send("get_all_cats_response", cats);
    }

    public void getCatFriends(String friends) {
        kafkaTemplate.send("get_cat_friends_response", friends);
    }

    public void getCatByBreed(String cats) {
        kafkaTemplate.send("get_cat_by_breed_response", cats);
    }

    public void getCatByName(String cat) {
        kafkaTemplate.send("get_cat_by_name_response", cat);
    }

    public void getCatByBreedAndName(String cat) {
        kafkaTemplate.send("get_cat_by_breed_and_name_response", cat);
    }

    public void makeFriend(String result) {
        kafkaTemplate.send("make_friend_response", result);
    }

    public void removeFriend(String result) {
        kafkaTemplate.send("remove_friend_response", result);
    }

    public void createCat(String cat) {
        kafkaTemplate.send("create_cat_response", cat);
    }

    public void updateCat(String cat) {
        kafkaTemplate.send("update_cat_response", cat);
    }

    public void deleteCat(String result) {
        kafkaTemplate.send("delete_cat_response", result);
    }
}
