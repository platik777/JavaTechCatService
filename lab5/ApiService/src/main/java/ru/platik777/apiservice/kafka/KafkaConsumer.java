package ru.platik777.apiservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.Cat;
import models.OwnerDetails;
import models.OwnerDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import requestModels.OperationSuccessRequest;
import requestModels.UpdateOwnerRequest;
import ru.platik777.apiservice.security.SimpleGrantedAuthorityDeserializer;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class KafkaConsumer {
    private CompletableFuture<Optional<OwnerDto>> ownerById;
    private CompletableFuture<Optional<OwnerDto>> ownerByEmail;
    private CompletableFuture<Optional<List<Cat>>> ownerCatsById;
    private CompletableFuture<Optional<List<OwnerDto>>> allOwners;
    private CompletableFuture<Optional<OwnerDto>> saveOwner;
    private CompletableFuture<Optional<OwnerDto>> updateOwner;
    private CompletableFuture<Optional<OperationSuccessRequest>> deleteOwner;
    private CompletableFuture<Optional<Cat>> catById;
    private CompletableFuture<Optional<List<Cat>>> allCats;
    private CompletableFuture<Optional<List<Cat>>> catFriends;
    private CompletableFuture<Optional<List<Cat>>> catsByBreed;
    private CompletableFuture<Optional<List<Cat>>> catsByName;
    private CompletableFuture<Optional<List<Cat>>> catsByNameAndBreed;
    private CompletableFuture<Optional<OperationSuccessRequest>> makeFriend;
    private CompletableFuture<Optional<OperationSuccessRequest>> removeFriend;
    private CompletableFuture<Optional<Cat>> createCatWithOwner;
    private CompletableFuture<Optional<Cat>> updateCat;
    private CompletableFuture<Optional<OperationSuccessRequest>> deleteCatById;
    private final ObjectMapper objectMapper;

    public KafkaConsumer() {
        this.objectMapper = new ObjectMapper();
        SimpleModule simpleGrantedAuthorityModule = new SimpleModule();
        simpleGrantedAuthorityModule.addDeserializer(GrantedAuthority.class, new SimpleGrantedAuthorityDeserializer());
        objectMapper.registerModule(simpleGrantedAuthorityModule);

    }

    @KafkaListener(topics = "get_owner_by_id_response", groupId = "group")
    public void getOwnerById(String ownerString) {
        if (ownerById != null) {
            OwnerDto ownerDto;
            try {
                ownerDto = objectMapper.readValue(ownerString, OwnerDto.class);
                ownerById.complete(Optional.ofNullable(ownerDto));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @KafkaListener(topics = "get_owner_by_email_response", groupId = "group")
    public void getOwnerByEmail(String ownerString) {
        if (ownerByEmail != null) {
            OwnerDto ownerDto;
            try {
                ownerDto = objectMapper.readValue(ownerString, OwnerDto.class);
                ownerByEmail.complete(Optional.ofNullable(ownerDto));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @KafkaListener(topics = "get_owner_cats_by_id_response", groupId = "group")
    public void getOwnerCatsById(String ownerCats) {
        if (ownerCatsById != null) {
            List<Cat> ownerCatsList;
            try {
                System.out.println(ownerCats);
                ownerCatsList = objectMapper.readValue(ownerCats, new TypeReference<>() {});
                System.out.println(ownerCatsList);
                ownerCatsById.complete(Optional.ofNullable(ownerCatsList));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @KafkaListener(topics = "get_all_owners_response", groupId = "group")
    public void getAllOwners(String ownersString) {
        if (allOwners != null) {
            try {
                List<OwnerDto> ownerList = objectMapper.readValue(ownersString, new TypeReference<>() {});
                allOwners.complete(Optional.ofNullable(ownerList));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @KafkaListener(topics = "update_owner_response", groupId = "group")
    public void updateOwner(String updateRequest) {
        if (updateOwner != null) {
            OwnerDto ownerDto;
            try {
                System.out.println(updateRequest);
                ownerDto = objectMapper.readValue(updateRequest, OwnerDto.class);
                System.out.println(ownerDto);
                updateOwner.complete(Optional.of(ownerDto));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @KafkaListener(topics = "delete_owner_by_id_response", groupId = "group")
    public void deleteOwnerById(String result) {
        if (deleteOwner != null) {
            try {
                OperationSuccessRequest operationSuccessRequest = objectMapper.readValue(result, OperationSuccessRequest.class);
                deleteOwner.complete(Optional.ofNullable(operationSuccessRequest));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @KafkaListener(topics = "get_cat_by_id_response", groupId = "group")
    public void getCatById(String catString) {
        if (catById != null) {
            Cat catDto;
            try {
                catDto = objectMapper.readValue(catString, Cat.class);
                catById.complete(Optional.ofNullable(catDto));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @KafkaListener(topics = "get_all_cats_response", groupId = "group")
    public void getAllCats(String catsString) {
        if (allCats != null) {
            try {
                List<Cat> catList = objectMapper.readValue(catsString, new TypeReference<>() {});
                allCats.complete(Optional.ofNullable(catList));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @KafkaListener(topics = "get_cat_friends_response", groupId = "group")
    public void getCatFriends(String catFriendsString) {
        if (catFriends != null) {
            try {
                List<Cat> catFriendsList = objectMapper.readValue(catFriendsString, new TypeReference<>() {});
                catFriends.complete(Optional.ofNullable(catFriendsList));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @KafkaListener(topics = "get_friends_by_breed_response", groupId = "group")
    public void getCatsByBreed(String catFoundByBreed) {
        if (catsByBreed != null) {
            try {
                List<Cat> cats = objectMapper.readValue(catFoundByBreed, new TypeReference<>() {});
                catsByBreed.complete(Optional.ofNullable(cats));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @KafkaListener(topics = "get_friends_by_name_response", groupId = "group")
    public void getCatsByName(String catFoundByName) {
        if (catsByName != null) {
            try {
                List<Cat> cats = objectMapper.readValue(catFoundByName, new TypeReference<>() {});
                catsByName.complete(Optional.ofNullable(cats));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @KafkaListener(topics = "get_friends_by_name_and_breed_response", groupId = "group")
    public void getCatsByNameAndBreed(String catFoundByNameAndBreed) {
        if (catsByNameAndBreed != null) {
            try {
                List<Cat> cats = objectMapper.readValue(catFoundByNameAndBreed, new TypeReference<>() {});
                catsByNameAndBreed.complete(Optional.ofNullable(cats));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @KafkaListener(topics = "make_friends_response", groupId = "group")
    public void makeFriend(String makeResult) {
        if (makeFriend != null) {
            try {
                OperationSuccessRequest operationSuccessRequest = objectMapper.readValue(makeResult, OperationSuccessRequest.class);
                makeFriend.complete(Optional.ofNullable(operationSuccessRequest));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @KafkaListener(topics = "remove_friends_response", groupId = "group")
    public void removeFriend(String removeResult) {
        if (removeFriend != null) {
            try {
                OperationSuccessRequest operationSuccessRequest = objectMapper.readValue(removeResult, OperationSuccessRequest.class);
                removeFriend.complete(Optional.ofNullable(operationSuccessRequest));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @KafkaListener(topics = "create_cat_response", groupId = "group")
    public void createCat(String createResult) {
        if (createCatWithOwner != null) {
            Cat cat;
            try {
                cat = objectMapper.readValue(createResult, Cat.class);
                createCatWithOwner.complete(Optional.ofNullable(cat));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @KafkaListener(topics = "update_cat_response", groupId = "group")
    public void updateCat(String catString) {
        if (updateCat != null) {
            System.out.println(catString);
            Cat catDto;
            try {
                catDto = objectMapper.readValue(catString, Cat.class);
                updateCat.complete(Optional.ofNullable(catDto));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @KafkaListener(topics = "delete_cat_by_id_response", groupId = "group")
    public void deleteCatById(String deleteResult) {
        if (deleteCatById != null) {
            try {
                OperationSuccessRequest operationSuccessRequest = objectMapper.readValue(deleteResult, OperationSuccessRequest.class);
                deleteCatById.complete(Optional.ofNullable(operationSuccessRequest));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public CompletableFuture<Optional<OwnerDto>> getOwnerById() {
        ownerById = new CompletableFuture<>();
        return ownerById;
    }

    public CompletableFuture<Optional<OwnerDto>> getOwnerByEmail() {
        ownerByEmail = new CompletableFuture<>();
        return ownerByEmail;
    }

    public CompletableFuture<Optional<List<Cat>>> getOwnerCatsById() {
        ownerCatsById = new CompletableFuture<>();
        return ownerCatsById;
    }

    public CompletableFuture<Optional<List<OwnerDto>>> getAllOwners() {
        allOwners = new CompletableFuture<>();
        return allOwners;
    }

    public CompletableFuture<Optional<OwnerDto>> getUpdateOwner() {
        updateOwner = new CompletableFuture<>();
        return updateOwner;
    }

    public CompletableFuture<Optional<OperationSuccessRequest>> getDeleteOwner() {
        deleteOwner = new CompletableFuture<>();
        return deleteOwner;
    }

    public CompletableFuture<Optional<Cat>> getCatById() {
        catById = new CompletableFuture<>();
        return catById;
    }

    public CompletableFuture<Optional<List<Cat>>> getAllCats() {
        allCats = new CompletableFuture<>();
        return allCats;
    }

    public CompletableFuture<Optional<List<Cat>>> getCatFriends() {
        catFriends = new CompletableFuture<>();
        return catFriends;
    }

    public CompletableFuture<Optional<List<Cat>>> getCatsByBreed() {
        catsByBreed = new CompletableFuture<>();
        return catsByBreed;
    }

    public CompletableFuture<Optional<List<Cat>>> getCatsByName() {
        catsByName = new CompletableFuture<>();
        return catsByName;
    }

    public CompletableFuture<Optional<List<Cat>>> getCatsByNameAndBreed() {
        catsByNameAndBreed = new CompletableFuture<>();
        return catsByNameAndBreed;
    }

    public CompletableFuture<Optional<OperationSuccessRequest>> getMakeFriend() {
        makeFriend = new CompletableFuture<>();
        return makeFriend;
    }

    public CompletableFuture<Optional<OperationSuccessRequest>> getRemoveFriend() {
        removeFriend = new CompletableFuture<>();
        return removeFriend;
    }

    public CompletableFuture<Optional<Cat>> getCreateCatWithOwner() {
        createCatWithOwner = new CompletableFuture<>();
        return createCatWithOwner;
    }

    public CompletableFuture<Optional<Cat>> getUpdateCat() {
        updateCat = new CompletableFuture<>();
        return updateCat;
    }

    public CompletableFuture<Optional<OperationSuccessRequest>> getDeleteCatById() {
        deleteCatById = new CompletableFuture<>();
        return deleteCatById;
    }
}
