package springBoot.SpringBootMVC.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import springBoot.SpringBootMVC.dto.UserDto;

import springBoot.SpringBootMVC.mapper.PetMapper;
import springBoot.SpringBootMVC.mapper.UserMapper;
import springBoot.SpringBootMVC.repository.PetRepository;
import springBoot.SpringBootMVC.repository.UserRepository;
//import tools.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
//import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.JsonNode;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;


            @Test
            void shouldCreateUser() throws Exception {
                String userJson = """
                        {
                          "name": "Иван Иванов",
                          "email": "ivan@example.com",
                          "age": 30,
                          "petList": []
                        }
                        """;

                mockMvc.perform(
                                post("/users")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(userJson)
                        )
                        .andExpect(status().isCreated())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id", notNullValue()))
                        .andExpect(jsonPath("$.name", is("Иван Иванов")))
                        .andExpect(jsonPath("$.email", is("ivan@example.com")))
                        .andExpect(jsonPath("$.age", is(30)))
                        .andExpect(jsonPath("$.petList", hasSize(0)));
            }

            @Test
            void shouldGetAllUsers() throws Exception {
                createUser(
                        "Иван Иванов",
                        "ivan@example.com",
                        30
                );

                createUser(
                        "Пётр Петров",
                        "petr@example.com",
                        25
                );

                mockMvc.perform(get("/users"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$", hasSize(5)))
                        .andExpect(jsonPath("$[0].id", notNullValue()))
                        .andExpect(jsonPath("$[0].name", notNullValue()))
                        .andExpect(jsonPath("$[0].email", notNullValue()));
            }

            @Test
            void shouldGetUserById() throws Exception {
                long userId = createUser(
                        "Иван Иванов",
                        "ivan@example.com",
                        30
                );

                mockMvc.perform(get("/users/{id}", userId))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id", is((int) userId)))
                        .andExpect(jsonPath("$.name", is("Иван Иванов")))
                        .andExpect(jsonPath("$.email", is("ivan@example.com")))
                        .andExpect(jsonPath("$.age", is(30)))
                        .andExpect(jsonPath("$.petList", hasSize(0)));
            }

            @Test
            void shouldReturn204WhenUserDoesNotExist() throws Exception {
                mockMvc.perform(get("/users/{id}", 999999L))
                        .andExpect(status().isNotFound());
            }

            @Test
            void shouldUpdateUser() throws Exception {
                long userId = createUser(
                        "Старое имя",
                        "old@example.com",
                        20
                );

                String updatedUserJson = """
                        {
                          "name": "Новое имя",
                          "email": "new@example.com",
                          "age": 35,
                          "petList": []
                        }
                        """;

                mockMvc.perform(
                                put("/users/{id}", userId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(updatedUserJson)
                        )
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        //.andExpect(jsonPath("$.id", is((int) userId)))
                        .andExpect(jsonPath("$.name", is("Новое имя")))
                        .andExpect(jsonPath("$.email", is("new@example.com")))
                        .andExpect(jsonPath("$.age", is(35)));
            }

            @Test
            void shouldDeleteUser() throws Exception {
                long userId = createUser(
                        "Иван Иванов",
                        "ivan@example.com",
                        30
                );

                mockMvc.perform(delete("/users/{id}", userId))
                        .andExpect(status().isNotFound());
            }

            @Test
            void shouldReturn404WhenDeletingUnknownUser() throws Exception {
                mockMvc.perform(delete("/users/{id}", 999999L))
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

}



