package ru.platik777.application.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.platik777.infrastructure.entities.CatEntity;
import ru.platik777.infrastructure.entities.Color;

import java.time.LocalDate;

@NoArgsConstructor
@Data
public class Cat {
    private Integer id;
    private String name;
    private LocalDate birthday;
    private String breed;
    private Integer age;
    private Color color;
    private Integer owner_id;

    public Cat(CatEntity catEntity) {
        this.id = catEntity.getId();
        this.name = catEntity.getName();
        this.birthday = catEntity.getBirthday();
        this.breed = catEntity.getBreed();
        this.age = catEntity.getAge();
        this.color = catEntity.getColor();
        this.owner_id = catEntity.getOwnerEntity().getId();
    }

    public Cat(String name, LocalDate birthday, String breed, Integer age, Color color) {
        this.name = name;
        this.birthday = birthday;
        this.breed = breed;
        this.age = age;
        this.color = color;
    }
}
