package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    //Employee repository to access data on employee based on ID received
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure read(String id) {

        Employee employee = employeeRepository.findByEmployeeId(id);

        //Throw error if no employee is found or invalid UUID
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        LOG.debug("Verified employee with id [{}], acquiring employee reporting structure", id);

        //Pass employeeRepository to later use in finding employee data based on direct reports IDs
        return new ReportingStructure(employee, employeeRepository);
    }
}
