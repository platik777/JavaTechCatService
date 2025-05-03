package ru.platik777.ownerservice.kafka;

import lombok.AllArgsConstructor;
import models.OwnerDetails;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void createOwner(String ownerDetails) {
        kafkaTemplate.send("create_owner_response", ownerDetails);
    }

    public void updateOwner(String ownerDetailsWithId) {
        kafkaTemplate.send("update_owner_response", String.valueOf(ownerDetailsWithId));
    }

    public void getOwnerById(String ownerDto) {
        kafkaTemplate.send("get_owner_by_id_response", ownerDto);
    }

    public void getOwnerByEmail(String ownerDetails) {
        kafkaTemplate.send("get_owner_by_email_response", ownerDetails);
    }

    public void getOwnerCatsById(String ownerCats) {
        kafkaTemplate.send("get_owner_by_cats_response", ownerCats);
    }

    public void getAllOwners(String owners) {
        kafkaTemplate.send("get_all_owners_response", owners);
    }

    public void deleteOwnerById(String result) {
        kafkaTemplate.send("delete_owner_by_id_response", result);
    }
}
