package com.mindex.challenge.data;

import com.mindex.challenge.dao.EmployeeRepository;
import java.util.List;

public class ReportingStructure {

    private Employee employee;
    private int numberOfReports;

    /*
     * Employee passed based on ID in request
     * numberOfReports dynamically calculated based on the employee found through request ID
     */
    public ReportingStructure(Employee employee, EmployeeRepository employeeRepository) {
        this.employee = employee;
        this.numberOfReports = calculateNumberOfReports(employeeRepository);
    }

    public Employee getEmployee() {
        return employee;
    }

    public int getNumberOfReports() {
        return numberOfReports;
    }

    /*
     * Calculates the number of reports for given employee
     * Handles if no direct reports are found, returning 0
     *
     * Handles if the given employee's direct report has direct report(s) of their own,
     * with recursion to add to original requested employee
     */
    private int calculateNumberOfReports(EmployeeRepository employeeRepository) {
        List<Employee> directReports = this.employee.getDirectReports();

        int numReports = 0;

        if(directReports != null){

            for (Employee e : directReports){
                Employee fullEmployee = employeeRepository.findByEmployeeId(e.getEmployeeId());
                ReportingStructure report = new ReportingStructure(fullEmployee, employeeRepository);

                numReports += 1 + report.getNumberOfReports();
            }
        }

        return numReports;
    }
}
