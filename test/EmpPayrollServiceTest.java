package EmpPayrollUsingJDBC.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import EmpPayrollUsingJDBC.main.EmpPayrollData;
import EmpPayrollUsingJDBC.main.EmpPayrollService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class EmpPayrollServiceTest {
    @Test
    public void given3Employees_WhenWrittenToFile_ShouldMatchEmployeeEntries() {
        EmpPayrollData[] arrayOfEmployees = {new EmpPayrollData(1, "Jeff Bezos", 100000.0, LocalDate.now()),
                new EmpPayrollData(2, "Bill Gates", 200000.0, LocalDate.now()),
                new EmpPayrollData(3, "Mark Zuckerberg", 300000.0, LocalDate.now())};
        EmpPayrollService employeePayrollService;
        employeePayrollService = new EmpPayrollService(Arrays.asList(arrayOfEmployees));
        employeePayrollService.writeEmployeePayrollData(EmpPayrollService.IOService.FILE_IO);

        employeePayrollService.printData(EmpPayrollService.IOService.FILE_IO);
        long entries = employeePayrollService.countEntries(EmpPayrollService.IOService.FILE_IO);
        assertEquals(3, entries);

    }

    @Test
    public void givenFile_WhenRead_ShouldReturnNumberOfEntries() {
        EmpPayrollService employeePayrollService = new EmpPayrollService();
        long entries = employeePayrollService.readDataFromFile(EmpPayrollService.IOService.FILE_IO);
        assertEquals(3, entries);
    }

    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {

        EmpPayrollService employeePayrollService = new EmpPayrollService();
        List<EmpPayrollData> employeePayrollData = employeePayrollService
                .readEmployeePayrollData(EmpPayrollService.IOService.DB_IO);
        System.out.println(employeePayrollData.size());
        assertEquals(0, employeePayrollData.size());
    }

    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() {

        EmpPayrollService employeePayrollService = new EmpPayrollService();
        List<EmpPayrollData> employeePayrollData = employeePayrollService
                .readEmployeePayrollData(EmpPayrollService.IOService.DB_IO);
        employeePayrollService.updateEmployeeSalary("Bill", 7000000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Bill");
        Assertions.assertTrue(result);

    }

    @Test
    public void givenName_WhenFound_ShouldReturnEmployeeDetails() {

        EmpPayrollService employeePayrollService = new EmpPayrollService();
        String name = "Rosa Diaz";
        List<EmpPayrollData> employeePayrollData = employeePayrollService
                .getEmployeeDetailsBasedOnName(EmpPayrollService.IOService.DB_IO, name);
        String resultName = employeePayrollData.get(0).employeeName;
        Assertions.assertEquals(name, resultName);
    }

    @Test
    public void givenStartDateRange_WhenMatches_ShouldReturnEmployeeDetails() {

        String startDate = "2013-01-01";
        EmpPayrollService employeePayrollService = new EmpPayrollService();
        List<EmpPayrollData> employeePayrollData = employeePayrollService
                .getEmployeeDetailsBasedOnStartDate(EmpPayrollService.IOService.DB_IO, startDate);
        System.out.println(employeePayrollData.size());
        assertEquals(0, employeePayrollData.size());
    }
}
