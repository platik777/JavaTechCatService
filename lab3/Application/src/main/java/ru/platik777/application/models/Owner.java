package ru.platik777.application.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.platik777.infrastructure.entities.OwnerEntity;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Owner {
    private Integer id;
    private String name;
    private LocalDate birthday;

    public Owner(OwnerEntity owner) {
        this.id = owner.getId();
        this.name = owner.getName();
        this.birthday = owner.getBirthday();
    }

    public Owner(String name, LocalDate birthday) {
        this.name = name;
        this.birthday = birthday;
    }
}
