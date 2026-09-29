package springBoot.SpringBootMVC.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PetCreateDto {
    @NotBlank
    private String name;

    @NotNull
    private Long userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public PetCreateDto(String name, Long userId) {
        this.name = name;
        this.userId = userId;
    }
}
