package springBoot.SpringBootMVC.dto;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import springBoot.SpringBootMVC.model.Pet;

import java.util.List;

public class UserDto {
    private Long id;

    @NotBlank
    private String name;

    @Email
    private String email;

    @Min(value = 0)
    private Integer age;

    @NotNull
    private List<Pet> petList;
    public UserDto(Long id, String name, String email, Integer age, List<Pet> petList) {
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
