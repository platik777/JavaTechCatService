package ru.platik777.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic getAllCatsRequest() {
        return new NewTopic("get_all_cats_request", 1, (short) 1);
    }

    @Bean
    public NewTopic getAllCatsResponse() {
        return new NewTopic("get_all_cats_response", 1, (short) 1);
    }

    @Bean
    public NewTopic getCatByIdRequest() {
        return new NewTopic("get_cat_by_id_request", 1, (short) 1);
    }

    @Bean
    public NewTopic getCatByIdResponse() {
        return new NewTopic("get_cat_by_id_response", 1, (short) 1);
    }

    @Bean
    public NewTopic createCatRequest() {
        return new NewTopic("create_cat_request", 1, (short) 1);
    }

    @Bean
    public NewTopic createCatResponse() {
        return new NewTopic("create_cat_response", 1, (short) 1);
    }

    @Bean
    public NewTopic deleteCatByIdRequest() {
        return new NewTopic("delete_cat_by_id_request", 1, (short) 1);
    }

    @Bean
    public NewTopic deleteCatByIdResponse() {
        return new NewTopic("delete_cat_by_id_response", 1, (short) 1);
    }

    @Bean
    public NewTopic getCatByBreedRequest() {
        return new NewTopic("get_cat_by_breed_request", 1, (short) 1);
    }

    @Bean
    public NewTopic getCatByBreedResponse() {
        return new NewTopic("get_cat_by_breed_response", 1, (short) 1);
    }

    @Bean
    public NewTopic getCatsByNameRequest() {
        return new NewTopic("get_cats_by_name_request", 1, (short) 1);
    }

    @Bean
    public NewTopic getCatsByNameResponse() {
        return new NewTopic("get_cats_by_name_response", 1, (short) 1);
    }

    @Bean NewTopic getCatsByNameAndBreedRequest() {
        return new NewTopic("get_cats_by_name_and_breed_request", 1, (short) 1);
    }

    @Bean
    public NewTopic getCatsByNameAndBreedResponse() {
        return new NewTopic("get_cats_by_name_and_breed_response", 1, (short) 1);
    }

    @Bean
    public NewTopic makeFriendsRequest() {
        return new NewTopic("make_friends_request", 1, (short) 1);
    }

    @Bean
    public NewTopic makeFriendsResponse() {
        return new NewTopic("make_friends_response", 1, (short) 1);
    }

    @Bean NewTopic removeFriendsRequest() {
        return new NewTopic("remove_friends_request", 1, (short) 1);
    }

    @Bean
    public NewTopic removeFriendsResponse() {
        return new NewTopic("remove_friends_response", 1, (short) 1);
    }

    @Bean
    public NewTopic updateCatRequest() {
        return new NewTopic("update_cat_request", 1, (short) 1);
    }

    @Bean
    public NewTopic updateCatResponse() {
        return new NewTopic("update_cat_response", 1, (short) 1);
    }

    @Bean
    public NewTopic createOwnerRequest() {
        return new NewTopic("create_owner_request", 1, (short) 1);
    }

    @Bean
    public NewTopic createOwnerResponse() {
        return new NewTopic("create_owner_response", 1, (short) 1);
    }

    @Bean
    public NewTopic updateOwnerRequest() {
        return new NewTopic("update_owner_request", 1, (short) 1);
    }

    @Bean
    public NewTopic updateOwnerResponse() {
        return new NewTopic("update_owner_response", 1, (short) 1);
    }

    @Bean
    public NewTopic getOwnerByEmailResponse() {
        return new NewTopic("get_owner_by_email_response", 1, (short) 1);
    }

    @Bean
    public NewTopic getOwnerByEmailRequest() {
        return new NewTopic("get_owner_by_email_request", 1, (short) 1);
    }

    @Bean NewTopic getOwnerCatsByIdRequest() {
        return new NewTopic("get_owner_cats_by_id_request", 1, (short) 1);
    }

    @Bean 
    public NewTopic getOwnerCatsByIdResponse() {
        return new NewTopic("get_owner_cats_by_id_response", 1, (short) 1);
    }

    @Bean
    public NewTopic getAllOwnersRequest() {
        return new NewTopic("get_all_owners_request", 1, (short) 1);
    }

    @Bean
    public NewTopic getAllOwnersResponse() {
        return new NewTopic("get_all_owners_response", 1, (short) 1);
    }

    @Bean
    public NewTopic getOwnerByIdRequest() {
        return new NewTopic("get_owner_by_id_request", 1, (short) 1);
    }

    @Bean
    public NewTopic getOwnerByIdResponse() {
        return new NewTopic("get_owner_by_id_response", 1, (short) 1);
    }

    @Bean
    public NewTopic deleteOwnerByIdRequest() {
        return new NewTopic("delete_owner_by_id_request", 1, (short) 1);
    }

    @Bean
    public NewTopic deleteOwnerByIdResponse() {
        return new NewTopic("delete_owner_by_id_response", 1, (short) 1);
    }
}
