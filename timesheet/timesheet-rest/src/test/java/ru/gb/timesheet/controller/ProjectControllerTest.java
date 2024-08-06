
package ru.gb.timesheet.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.gb.timesheet.model.Project;
import ru.gb.timesheet.repository.ProjectRepository;

import static org.junit.jupiter.api.Assertions.*;

//@ActiveProfiles("test")
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureWebTestClient
//class ProjectControllerTest {
//
//    @Autowired
//    ProjectRepository projectRepository;
//
//    @Autowired
//    WebTestClient webTestClient;
//
//    @LocalServerPort
//    private int port;
//
//    @Test
//    void getById() {
//        Project project = new Project();
//        project.setName("projectName");
//       Project expected = projectRepository.save(project);
//
//        webTestClient.get()
//                .uri("/projects/" + expected.getId())
//                .exchange() // то же самое что retrieve()
//                .expectStatus().isOk() // assertEquals(HttpStatus.OK, actual.getStatusCode());
//                .expectBody(Project.class)
//                .value(actual -> {
//                    assertEquals(expected.getId(), actual.getId());
//                    assertEquals(expected.getName(), actual.getName());
//                });
//    }
//}

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProjectControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    ProjectRepository projectRepository;

    @Test
    void getByIdNotFound() {
        RestClient restClient = RestClient.create("http://localhost:" + port);
        assertThrows(HttpClientErrorException.NotFound.class, () -> {
            restClient.get()
                    .uri("/projects/-2")
                    .retrieve()
                    .toBodilessEntity();
        });
//        RestClient restClient = RestClient.create("http://localhost:" + port);
//        ResponseEntity<Void> response =  restClient.get()
//                .uri("/projects/-2")
//                .retrieve()
//                .toBodilessEntity();
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getByIdAllOk() {
        Project expected = new Project();
        expected.setName("projectName");
        expected = projectRepository.save(expected);

        RestClient restClient = RestClient.create("http://localhost:" + port);
        ResponseEntity<Project> actual = restClient.get()
                .uri("/projects/" + expected.getId())
                .retrieve()
                .toEntity(Project.class);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        Project responseBody = actual.getBody();
        assertNotNull(responseBody);
        assertEquals(expected.getId(), responseBody.getId());
        assertEquals(expected.getName(), responseBody.getName());
    }

    @Test
    void testCreate() {
        Project toCreate = new Project();
        toCreate.setName("newName");
        RestClient restClient = RestClient.create("http://localhost:" + port);
        ResponseEntity<Project> response = restClient.post()
                .uri("/projects")
                .body(toCreate)
                .retrieve()
                .toEntity(Project.class);
        // Проверка ручки HTTP-сервера
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Project responseBody = response.getBody();
        assertNotNull(responseBody);
        assertNotNull(responseBody.getId());
        assertEquals(responseBody.getName(), toCreate.getName());
        //Проверка записи в БД
        assertTrue(projectRepository.existsById(responseBody.getId()));
    }

    @Test
    void testDeleteById() {
        Project toDelete = new Project();
        toDelete.setName("newName");
        toDelete = projectRepository.save(toDelete);
        RestClient restClient = RestClient.create("http://localhost:" + port);
        ResponseEntity<Void> response = restClient.delete()
                .uri("/projects/" + toDelete.getId())
                .retrieve()
                .toBodilessEntity();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

// Проверка на отсутствие записи
        assertFalse(projectRepository.existsById(toDelete.getId()));

    }
}