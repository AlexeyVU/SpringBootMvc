package springBoot.SpringBootMVC.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.Mapping;
import springBoot.SpringBootMVC.dto.PetDto;
import springBoot.SpringBootMVC.dto.UserUpdateDto;
import springBoot.SpringBootMVC.dto.UserDto;
import springBoot.SpringBootMVC.dto.UserUpdateDto;
import springBoot.SpringBootMVC.model.User;
import springBoot.SpringBootMVC.model.Pet;
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {

    public abstract UserDto map(User user);

    public abstract User map(UserDto userDto);

    public abstract void update(UserUpdateDto dto, @MappingTarget User user);
}
