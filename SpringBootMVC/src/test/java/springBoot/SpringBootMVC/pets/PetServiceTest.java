package springBoot.SpringBootMVC.pets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import springBoot.SpringBootMVC.mapper.PetMapper;
import springBoot.SpringBootMVC.repository.PetRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;



import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PetServiceTest {
    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetMapper petMapper;
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();


        @Test
        void shouldCreatePet() throws Exception {
            long userId = createUser(
                    "Иван Иванов",
                    "ivan@example.com",
                    30
            );

            String petJson = """
                    {
                      "name": "Бобик",
                      "userId": %d
                    }
                    """.formatted(userId);

            mockMvc.perform(
                            post("/pets")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(petJson)
                    )
                    .andExpect(status().isCreated())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", notNullValue()))
                    .andExpect(jsonPath("$.name", is("Бобик")))
                    .andExpect(jsonPath("$.userId", is((int) userId)));
        }

        @Test
        void shouldGetAllPets() throws Exception {
            long userId = createUser(
                    "Иван Иванов",
                    "ivan@example.com",
                    30
            );

            createPet("Бобик", userId);
            createPet("Мурзик", userId);

            mockMvc.perform(get("/pets"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", notNullValue()))
                    .andExpect(jsonPath("$[0].name", notNullValue()))
                    .andExpect(jsonPath("$[0].userId", is((int) userId)));
        }

        @Test
        void shouldGetPetById() throws Exception {
            long userId = createUser(
                    "Иван Иванов",
                    "ivan@example.com",
                    30
            );

            long petId = createPet("Бобик", userId);

            mockMvc.perform(get("/pets/{id}", petId))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is((int) petId)))
                    .andExpect(jsonPath("$.name", is("Бобик")))
                    .andExpect(jsonPath("$.userId", is((int) userId)));
        }

        @Test
        void shouldReturn404WhenPetDoesNotExist() throws Exception {
            mockMvc.perform(get("/pets/{id}", 999999L))
                    .andExpect(status().isNotFound());
        }


        @Test
        void shouldDeletePet() throws Exception {
            long userId = createUser(
                    "Иван Иванов",
                    "ivan@example.com",
                    30
            );

            long petId = createPet("Бобик", userId);

            mockMvc.perform(delete("/pets/{id}", petId))
                    .andExpect(status().isNoContent());
        }

        @Test
        void shouldReturn404WhenDeletingUnknownPet() throws Exception {
            mockMvc.perform(delete("/pets/{id}", 999999L))
                    .andExpect(status().isNotFound());
        }
    private long createUser(
            String name,
            String email,
            int age
    ) throws Exception {
        String userJson = """
                            {
                              "name": "%s",
                              "email": "%s",
                              "age": %d,
                              "petList": []
                            }
                            """.formatted(name, email, age);

        MvcResult result = mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson)
                )
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode response = objectMapper.readTree(
                result.getResponse().getContentAsString()
        );

        return response.get("id").asLong();
    }
        private long createPet(
                String name,
                long userId
        ) throws Exception {
            String petJson = """
                    {
                      "name": "%s",
                      "userId": %d
                    }
                    """.formatted(name, userId);

            MvcResult result = mockMvc.perform(
                            post("/pets")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(petJson)
                    )
                    .andExpect(status().isCreated())
                    .andReturn();

            JsonNode response = objectMapper.readTree(
                    result.getResponse().getContentAsString()
            );

            return response.get("id").asLong();
        }
}
