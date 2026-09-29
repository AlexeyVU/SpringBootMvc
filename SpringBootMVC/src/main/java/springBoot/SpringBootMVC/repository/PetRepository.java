package springBoot.SpringBootMVC.repository;

import lombok.Getter;
import org.springframework.stereotype.Component;
import springBoot.SpringBootMVC.handler.ResourceNotFoundException;
import springBoot.SpringBootMVC.model.Pet;
import springBoot.SpringBootMVC.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PetRepository {
    @Getter
    private final List<Pet> petList = new ArrayList<>();
    private final UserRepository userRepository;


    PetRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<Pet> save(Pet pet) {
        petList.add(pet);
        return petList;
    }
    public Pet findPetById(Long id) {
        List<Pet> currentPet = getAll();
        for (Pet pet : currentPet) {
            if (pet.getId().equals(id)) {
                return pet;
            }
        }
        return null;
    }
    public List<Pet> getAll() {
         return new ArrayList<>(getPetList());

    }
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID не может быть null");
        }

        // Удаляем из общего списка
        boolean removedFromGlobal = petList.removeIf(pet -> pet.getId().equals(id));
        if (!removedFromGlobal) {
            throw new ResourceNotFoundException("Pet с id=" + id + " не найден в глобальном списке");
        }
        // Удаляем из списков пользователей
        boolean removedFromUsers = false;
        for (User user : userRepository.getAll()) {
            List<Pet> userPets = user.getPetList();
            if (userPets.removeIf(pet -> pet.getId().equals(id))) {
                removedFromUsers = true;
            }
        }

        if (!removedFromUsers) {
            // По желанию можно выбросить предупреждение или игнорировать:
            // например, если питомец был удалён из глобального списка, но не найден у пользователей.
            // throw new ResourceNotFoundException("Pet с id=" + id + " не найден ни у одного пользователя");
        }
    }


}
