package springBoot.SpringBootMVC.mapper;

import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import springBoot.SpringBootMVC.dto.PetDto;
import springBoot.SpringBootMVC.dto.UserDto;
import springBoot.SpringBootMVC.model.Pet;
import springBoot.SpringBootMVC.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class PetMapper {
    public abstract PetDto map(Pet pet);

    public abstract Pet map(PetDto petDto);

    public abstract void update(PetDto petDto, @MappingTarget Pet pet);
   //public abstract List<Pet> map(List<PetDto> petDtoList);

}
