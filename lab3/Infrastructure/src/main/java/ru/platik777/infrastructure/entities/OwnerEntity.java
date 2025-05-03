package ru.platik777.infrastructure.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "owner")
public class OwnerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "birthday")
    private LocalDate birthday;

    @OneToMany(mappedBy = "ownerEntity")
    private List<CatEntity> catEntities;

    public OwnerEntity(String name, LocalDate birthday) {
        this.name = name;
        this.birthday = birthday;
    }

    public void addCat(CatEntity catEntity) {
        if (catEntities == null)
            catEntities = new ArrayList<>();
        catEntities.add(catEntity);
        catEntity.setOwnerEntity(this);
    }

    @Override
    public String toString() {
        return "Owner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}

