package models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.platik777.entities.CatEntity;
import ru.platik777.entities.Color;

import java.time.LocalDate;

@NoArgsConstructor
@Data
public class Cat {
    private Integer id;
    private String name;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
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
