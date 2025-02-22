package com.mindex.challenge.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);
    private static final String FILE_PATH = "src/main/resources/static/compensation_database.json";

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /*
     * Find employee in compensationRepository based on employeeId
     * If not null, returns compensation [employeeId, employee{}, salary, effectiveDate]
     */
    @Override
    public Compensation read(String id) {
        LOG.debug("Acquiring compensation data for employee with id [{}]", id);

        Compensation compensation = compensationRepository.findByEmployeeId(id);

        if (compensation == null) {
            throw new RuntimeException("No compensation data found with id [{}]: " + id);
        }

        LOG.debug("Verified employee with id [{}], acquiring employee compensation", id);

        return compensation;
    }

    /*
     * Creates new compensation based on input
     * Requires ID of employee existing in employeeRepository, persisiting or not
     * Calls persistCompensationData to persist compensation data,
     * data will persist and will still be GET-able as employee persists in compensation_database.json even if employee no longer persists in employeeRepository
     */
    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

        Employee employee = employeeRepository.findByEmployeeId(compensation.getEmployeeId());
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + compensation.getEmployeeId());
        }

        compensation.setEmployee(employee);
        compensationRepository.insert(compensation);

        persistCompensationData();

        return compensation;
    }

    /*
     * Rewrites all compensations in compensationRepository to compensation_database.json for persistence
     */
    private void persistCompensationData() {
        try {
            List<Compensation> compensations = compensationRepository.findAll();
            objectMapper.writeValue(new File(FILE_PATH), compensations);
            LOG.info("Persisted compensation data to JSON file.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
