package ru.platik777.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "cat")
public class CatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "breed")
    private String breed;

    @Column(name = "age")
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(name = "color")
    private Color color;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private OwnerEntity ownerEntity;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "friends",
            joinColumns = @JoinColumn(name = "first_friend_id"),
            inverseJoinColumns = @JoinColumn(name = "second_friend_id"))
    private List<CatEntity> friends;

    public CatEntity(String name, LocalDate birthday, String breed, Integer age, Color color) {
        this.name = name;
        this.birthday = birthday;
        this.breed = breed;
        this.age = age;
        this.color = color;
        friends = new ArrayList<>();
        ownerEntity = null;
    }

    public void addFriend(CatEntity catEntity) {
        if (!friends.contains(catEntity)) {
            friends.add(catEntity);
            catEntity.getFriends().add(this);
        }
    }

    public void removeFriend(CatEntity catEntity) {
        friends.remove(catEntity);
        catEntity.getFriends().remove(this);
    }

    @Override
    public String toString() {
        return "Cat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", breed='" + breed + '\'' +
                ", age=" + age +
                ", color=" + color +
                '}';
    }
}
