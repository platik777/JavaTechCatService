package ru.platik777.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cat")
public class Cat {
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
    private Owner owner;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "friends",
    joinColumns = @JoinColumn(name = "first_friend_id"),
    inverseJoinColumns = @JoinColumn(name = "second_friend_id"))
    private List<Cat> friends;

    public Cat() { }

    public Cat (String name, LocalDate birthday, String breed, Integer age, Color color) {
        this.name = name;
        this.birthday = birthday;
        this.breed = breed;
        this.age = age;
        this.color = color;
        friends = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<Cat> getFriends() {
        return friends;
    }

    public void setFriends(List<Cat> friends) {
        this.friends = friends;
    }

    public void addFriend(Cat cat) {
        if (!friends.contains(cat)) {
            friends.add(cat);
            cat.getFriends().add(this);
        }
    }

    public void removeFriend(Cat cat) {
        friends.remove(cat);
        cat.getFriends().remove(this);
    }

    @Override
    public String toString() {
        return "ru.platik777.entity.Cat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", breed='" + breed + '\'' +
                ", age=" + age +
                ", color=" + color +
                '}';
    }
}