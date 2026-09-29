package springBoot.SpringBootMVC.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springBoot.SpringBootMVC.dto.PetCreateDto;
import springBoot.SpringBootMVC.mapper.PetMapper;
import springBoot.SpringBootMVC.model.Pet;
import springBoot.SpringBootMVC.service.PetService;
import springBoot.SpringBootMVC.dto.PetDto;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/pets")
@RestController
public class PetController {
    private final PetService petService;
    private final PetMapper petMapper;

    public PetController(PetService petService, PetMapper petMapper) {
        this.petService = petService;
        this.petMapper = petMapper;
    }

    @PostMapping
    public ResponseEntity<PetDto> createPet(@Valid @RequestBody PetCreateDto petCreateDto) {
        PetDto pet = petService.createPet(petCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pet);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PetDto> getPet(@PathVariable Long id) {
        Pet pet = petService.findPetById(id);
        return ResponseEntity.ok(petMapper.map(pet));
    }
    @GetMapping
    public ResponseEntity<List<PetDto>> getAll() {
        List<Pet> currentPet = petService.getAll();
        List<PetDto> petDtoList = currentPet.stream()
                .map(petMapper::map)
                .toList();
        return ResponseEntity.ok(petDtoList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetDto> update(@PathVariable Long id, @RequestBody PetDto petDto) {
        Pet pet = petService.findPetById(id);
        petService.updatePate((id), petDto);
        return ResponseEntity.ok(petMapper.map(pet));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Valid @PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }
}

