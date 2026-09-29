package springBoot.SpringBootMVC.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import springBoot.SpringBootMVC.controller.UserController;
import springBoot.SpringBootMVC.dto.PetCreateDto;
import springBoot.SpringBootMVC.dto.PetDto;
import springBoot.SpringBootMVC.handler.ResourceNotFoundException;
import springBoot.SpringBootMVC.mapper.PetMapper;
import springBoot.SpringBootMVC.model.Pet;
import springBoot.SpringBootMVC.model.User;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.ILoggerFactory;
import springBoot.SpringBootMVC.repository.PetRepository;
import springBoot.SpringBootMVC.repository.UserRepository;


@Service
public class PetService {
    private final UserService userService;
    private final List<Pet> petList;
    private final UserRepository userRepository;
    private final PetRepository petRepository;


    @Autowired
    private PetMapper petMapper;


    private Long idCountPet;

    private static final Logger log = LoggerFactory.getLogger(PetService.class);

    public PetService(UserService userService, UserRepository userRepository, PetRepository petRepository) {
        this.userService = userService;
        this.idCountPet = 0L;
        this.petList = new ArrayList<>();
        this.userRepository = userRepository;
        this.petRepository = petRepository;

    }
    //создание питомца (хранение в Map И List не синхронизированны. Исправить)
    public PetDto createPet(PetCreateDto petCreateDto) {
        idCountPet++;
        long id = idCountPet;
        Pet pet = new Pet(id, petCreateDto.getName(), petCreateDto.getUserId());
        petRepository.save(pet);
        User user = userRepository.findUserById(petCreateDto.getUserId());
        List<Pet> pets = getAll();
        List<Pet> userPets = new ArrayList<>();
        for (Pet currentPet : pets) {
            if (currentPet.getUserId().equals(user.getId())) {
                userPets.add(currentPet);
            }
        }
        user.setPetList(userPets);
        return petMapper.map(pet);
    }

    //поиск питомца по id
    public Pet findPetById(Long id) {
        Pet pet = petRepository.findPetById(id);
        if (pet == null) {
            throw new ResourceNotFoundException("Pet not found");
        }
        return pet;
    }

    //обновление питомца
    public Pet updatePate(Long petId, PetDto petDto) {
        Pet currentPet = findPetById(petId);
        petMapper.update(petDto, currentPet);
        log.info("petId " + currentPet.getId());
        petList.add(Math.toIntExact(petId), currentPet);
        return currentPet;
    }

    //удаление питомца
    public void deletePet(Long id) {
        petRepository.delete(id);
    }
    //получение списка питомцев
    public List<Pet> getAll() {
       return petRepository.getAll();
    }
    public List<PetDto> findPetListByUser(Pet pet) {
        List<Pet> userPetList = new ArrayList<>();
        List<Pet> findAll = getAll();
        for (Pet currentPet : findAll) {
            if (currentPet.getUserId().equals(pet.getUserId())) {
                userPetList.add(pet);
            }
        }
        return userPetList.stream().map(petMapper::map).toList();
    }

}
