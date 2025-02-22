package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    @Autowired
    private ReportingStructureService reportingStructureService;

    /*
     * Create reportingStructures with ReportingStructureService.Read() GET
     * for employees with 4, 2, and 0 reports respectively
     * Assert that each reportingStructure has the expected number of reports
     */
    @Test
    public void test() {
        ReportingStructure fourReports = reportingStructureService.read("16a596ae-edd3-4847-99fe-c4518e82c86f");
        ReportingStructure twoReports = reportingStructureService.read("03aa1462-ffa9-4978-901b-7c001562cf6f");
        ReportingStructure noReports = reportingStructureService.read("62c1084e-6e34-4630-93fd-9153afb65309");

        assertNotNull(fourReports);
        assertNotNull(twoReports);
        assertNotNull(noReports);

        assertEquals(4, fourReports.getNumberOfReports());
        assertEquals(2, twoReports.getNumberOfReports());
        assertEquals(0, noReports.getNumberOfReports());
    }
}