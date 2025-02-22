package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
public class CompensationController {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    @Autowired
    private CompensationService compensationService;

    /*
     * POST for compensation requires:
     * - employeeId (String UUID of employee existing in employeeRepository
     *           new employee can be made first in employeeRepository
     *           NOTE: new employees in employeeRepo do not persist, only compensation is persisted)
     * - salary (int)
     * - effectiveDate (String)
     */
    @PostMapping("/compensation")
    public Compensation create(
            @RequestParam String employeeId,
            @RequestParam int salary,
            @RequestParam String effectiveDate) {
        LOG.debug("Received compensation create request for employeeId [{}], salary [{}], effectiveDate [{}]",
                employeeId, salary, effectiveDate);

        Compensation compensation = new Compensation();
        compensation.setEmployeeId(employeeId);
        compensation.setSalary(salary);
        compensation.setEffectiveDate(LocalDate.parse(effectiveDate));
        return compensationService.create(compensation);
    }

    /*
     * GET for existing compensation for employee
     * Requires employeeId
     */
    @GetMapping("/compensation/{id}")
    public Compensation read(@PathVariable String id) {
        LOG.debug("Received compensation create request for id [{}]", id);

        return compensationService.read(id);
    }
}
