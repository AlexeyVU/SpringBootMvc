package springBoot.SpringBootMVC.model;

import java.util.List;

public class User {

    public Long id;

    public String name;

    public String email;

    public Integer age;

    public List<Pet> petList;
    public User(Long id, String name, String email, Integer age, List<Pet> petList) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.petList = petList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Pet> getPetList() {
        return petList;
    }

    public void setPetList(List<Pet> petList) {
        this.petList = petList;
    }
}
