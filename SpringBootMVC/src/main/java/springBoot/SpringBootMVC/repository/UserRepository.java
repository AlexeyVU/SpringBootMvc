package springBoot.SpringBootMVC.repository;

import com.sun.source.tree.OpensTree;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import springBoot.SpringBootMVC.handler.ResourceNotFoundException;
import springBoot.SpringBootMVC.model.Pet;
import springBoot.SpringBootMVC.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class UserRepository {
    private final Map<Long, User> userMap = new HashMap<>();
    private final PetRepository petRepository;


    UserRepository(@Lazy PetRepository petRepository) {
        this.petRepository = petRepository;
    }
    public Map<Long, User> save(Long id, User user) {
        userMap.put(id, user);
        return userMap;
    }
    public User findUserById(Long id) {
        return userMap.get(id);
    }
    public void delete(Long id) {
        userMap.remove(id);
        List<Pet> petList = petRepository.getAll();
        for (Pet pet : petList) {
            if (pet.getId().equals(id)) {
                petRepository.delete(pet.getId());
            } else throw new ResourceNotFoundException("User not found");
        }
    }
    public List<User> getAll() {
        return new ArrayList<>(userMap.values());
    }
}
