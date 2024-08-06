package ru.gb.timesheet.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;
import ru.gb.timesheet.model.Timesheet;
import ru.gb.timesheet.repository.TimesheetRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TimesheetControllerTest {


    @LocalServerPort
    private int port;

    private LocalDate createdAt;
    private RestClient restClient;

    @Autowired
    TimesheetRepository timesheetRepository;


    @BeforeEach
    void beforeEach() {
        timesheetRepository.deleteAll();
        restClient = RestClient.create("http://localhost:" + port);
        createdAt = LocalDate.now();
    }

    @Test
    void getById() {
        Timesheet expectTimesheet = new Timesheet();
        expectTimesheet.setEmployeeId(ThreadLocalRandom.current().nextLong(1, 8));
        expectTimesheet.setProjectId(ThreadLocalRandom.current().nextLong(1, 6));
        expectTimesheet.setCreatedAt(createdAt);
        expectTimesheet.setMinutes(ThreadLocalRandom.current().nextInt(100, 1000));
        expectTimesheet = timesheetRepository.save(expectTimesheet);
        ResponseEntity<Timesheet> actual = restClient.get()
                .uri("/timesheets/" + expectTimesheet.getId())
                .retrieve()
                .toEntity(Timesheet.class);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        Timesheet responseBody = actual.getBody();
        assertNotNull(responseBody);
        assertEquals(expectTimesheet.getId(), responseBody.getId());
        assertEquals(expectTimesheet.getProjectId(), responseBody.getProjectId());
        assertEquals(expectTimesheet.getEmployeeId(), responseBody.getEmployeeId());
        assertEquals(expectTimesheet.getMinutes(), responseBody.getMinutes());
        assertEquals(expectTimesheet.getCreatedAt(), responseBody.getCreatedAt());
    }


    @Test
    void getAllOk() {
        List<Timesheet> expectListTimesheet = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            createdAt = createdAt.plusDays(1);
            Timesheet timesheet = new Timesheet();
            timesheet.setEmployeeId(ThreadLocalRandom.current().nextLong(1, 8));
            timesheet.setProjectId(ThreadLocalRandom.current().nextLong(1, 6));
            timesheet.setCreatedAt(createdAt);
            timesheet.setMinutes(ThreadLocalRandom.current().nextInt(100, 1000));
            timesheet = timesheetRepository.save(timesheet);
            expectListTimesheet.add(timesheet);
        }
        ResponseEntity<List<Timesheet>> actualTimesheets = restClient.get()
                .uri("/timesheets")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<Timesheet>>() {
                });

        assertEquals(HttpStatus.OK, actualTimesheets.getStatusCode());
        assertNotNull(actualTimesheets);
        for (int i = 0; i < 5; i++) {
            Optional<Timesheet> expected = Optional.of(expectListTimesheet.get(i));
            Optional<Timesheet> actual = Optional.of(actualTimesheets.getBody().get(i));
            assertEquals(expected, actual);
        }
    }

    @Test
    void create() {
        Timesheet toCreate = new Timesheet();
        toCreate.setEmployeeId(ThreadLocalRandom.current().nextLong(1, 8));
        toCreate.setProjectId(ThreadLocalRandom.current().nextLong(1, 6));
        toCreate.setMinutes(ThreadLocalRandom.current().nextInt(100, 1000));
        toCreate.setCreatedAt(createdAt);

        ResponseEntity<Timesheet> response = restClient.post()
                .uri("/timesheets")
                .body(toCreate)
                .retrieve()
                .toEntity(Timesheet.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Timesheet responseBody = response.getBody();
        assertNotNull(responseBody);
        assertNotNull(responseBody.getId());

        assertEquals(responseBody.getEmployeeId(), toCreate.getEmployeeId());
        assertEquals(responseBody.getMinutes(), toCreate.getMinutes());
        assertEquals(responseBody.getCreatedAt(), toCreate.getCreatedAt());
        assertEquals(responseBody.getProjectId(), toCreate.getProjectId());

        assertTrue(timesheetRepository.existsById(responseBody.getId()));
    }

    @Test
    void testDeleteById() {
        Timesheet toDelete = new Timesheet();
        toDelete.setCreatedAt(createdAt);
        toDelete.setEmployeeId(ThreadLocalRandom.current().nextLong(1, 8));
        toDelete.setProjectId(ThreadLocalRandom.current().nextLong(1, 6));
        toDelete.setMinutes(ThreadLocalRandom.current().nextInt(100, 1000));
        toDelete = timesheetRepository.save(toDelete);

        ResponseEntity<Void> response = restClient.delete()
                .uri("/timesheets/" + toDelete.getId())
                .retrieve()
                .toBodilessEntity();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        assertFalse(timesheetRepository.existsById(toDelete.getId()));
    }

    @Test
    void changeById() {
        Timesheet firstTimesheet = new Timesheet();
        firstTimesheet = timesheetRepository.save(firstTimesheet);
        System.out.println("Before request: " + firstTimesheet);
        Timesheet changedTimesheet = new Timesheet();
        changedTimesheet.setEmployeeId(ThreadLocalRandom.current().nextLong(1, 8));
        changedTimesheet.setProjectId(ThreadLocalRandom.current().nextLong(1, 6));
        changedTimesheet.setCreatedAt(createdAt);
        changedTimesheet.setMinutes(ThreadLocalRandom.current().nextInt(100, 1000));

        ResponseEntity<Timesheet> actual = restClient.put()
                .uri("/timesheets/" + firstTimesheet.getId())
                .body(changedTimesheet)
                .retrieve()
                .toEntity(Timesheet.class);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        Timesheet responseBody = actual.getBody();
        System.out.println("After request: " + responseBody);
        assertNotNull(responseBody);
        assertEquals(firstTimesheet.getId(), responseBody.getId()); // Id должен быть тотже, остальное изменено
        assertNotNull(responseBody.getProjectId());
        assertNotNull(responseBody.getEmployeeId());
        assertNotNull(responseBody.getMinutes());
        assertNotNull(responseBody.getCreatedAt());
    }
}