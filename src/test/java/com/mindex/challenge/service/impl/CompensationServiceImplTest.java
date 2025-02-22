package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String employeeUrl;
    private String compensationUrl;

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        compensationUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void test() {
        //Create test employee for compensation
        Employee testEmployee = new Employee();

        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        //Create POST compensation to check, with created test employee
        Compensation testCompensation = new Compensation();

        testCompensation.setEmployee(createdEmployee);
        testCompensation.setEmployeeId(createdEmployee.getEmployeeId());
        testCompensation.setSalary(120000);
        testCompensation.setEffectiveDate(LocalDate.parse("2024-12-15"));

        Compensation createdCompensation = compensationService.create(testCompensation);

        assertNotNull(createdCompensation);
        assertCompensationEquivalence(testCompensation, createdCompensation);

        //Read GET compensation to check
        Compensation readCompensation = restTemplate.getForEntity(compensationUrl, Compensation.class, createdCompensation.getEmployeeId()).getBody();

        assertCompensationEquivalence(createdCompensation, readCompensation);
    }

    //Checks for equivalence for id, salary, and effective date
    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getEmployeeId(), actual.getEmployeeId());
        assertEquals(expected.getSalary(), actual.getSalary());
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }
}
