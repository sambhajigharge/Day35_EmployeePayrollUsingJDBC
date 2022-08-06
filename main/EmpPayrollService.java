package EmpPayrollUsingJDBC.main;

import java.util.List;
import java.util.Scanner;

import java.util.ArrayList;

public class EmpPayrollService {

    private List<EmpPayrollData> employeePayrollList;
    private EmpPayrollDBService empPayrollDBService;

    public enum IOService {
        CONSOLE_IO, FILE_IO, DB_IO, REST_IO
    }

    public EmpPayrollService() {
        empPayrollDBService = EmpPayrollDBService.getInstance();
    }

    public EmpPayrollService(List<EmpPayrollData> employeePayrollList) {
        this();
        this.employeePayrollList = employeePayrollList;
    }

    public void printData(IOService fileIo) {
        if (fileIo.equals(IOService.FILE_IO))
            new EmpPayrollFileIOService().printData();
    }

    public long countEntries(IOService fileIo) {
        if (fileIo.equals(IOService.FILE_IO))
            return new EmpPayrollFileIOService().countEntries();
        return 0;
    }

    private void readEmployeePayrollData(Scanner consoleInputReader) {

        System.out.println("Enter the Employee Id : ");
        int id = consoleInputReader.nextInt();
        System.out.println("Enter the Employee Name : ");
        String name = consoleInputReader.next();
        System.out.println("Enter the Employee Salary : ");
        double salary = consoleInputReader.nextDouble();

        employeePayrollList.add(new EmpPayrollData(id, name, salary));
    }

    private EmpPayrollData getEmployeePayrollData(String name) {

        return this.employeePayrollList.stream()
                .filter(EmployeePayrollDataItem -> EmployeePayrollDataItem.employeeName.equals(name)).findFirst()
                .orElse(null);
    }

    public long readDataFromFile(IOService fileIo) {

        List<String> employeePayrollFromFile = new ArrayList<String>();
        if (fileIo.equals(IOService.FILE_IO)) {
            System.out.println("Employee Details from payroll-file.txt");
            employeePayrollFromFile = new EmpPayrollFileIOService().readDataFromFile();

        }
        return employeePayrollFromFile.size();
    }

    public List<EmpPayrollData> readEmployeePayrollData(IOService ioService) {

        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollList = empPayrollDBService.readData();
        return this.employeePayrollList;

    }

    public void writeEmployeePayrollData(IOService ioService) {
        if (ioService.equals(IOService.CONSOLE_IO))
            System.out.println("\nWriting Employee Payroll Roster to Console\n" + employeePayrollList);

        else if (ioService.equals(IOService.FILE_IO))
            new EmpPayrollFileIOService().writeData(employeePayrollList);
    }

    public void updateEmployeeSalary(String name, double salary) {

        int result = empPayrollDBService.updateEmployeeData(name, salary);
        if (result == 0)
            return;

        EmpPayrollData employeePayrollData = this.getEmployeePayrollData(name);
        if (employeePayrollData != null)
            employeePayrollData.employeeSalary = salary;

    }

    public boolean checkEmployeePayrollInSyncWithDB(String name) {

        List<EmpPayrollData> employeePayrollDataList = empPayrollDBService.getEmployeePayrollData(name);
        return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
    }

    public List<EmpPayrollData> getEmployeeDetailsBasedOnStartDate(IOService ioService, String startDate) {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollList = empPayrollDBService
                    .getEmployeeDetailsBasedOnStartDateUsingStatement(startDate);
        return this.employeePayrollList;
    }

    public List<EmpPayrollData> getEmployeeDetailsBasedOnStartDateUsingPreparedStatement(IOService ioService,
                                                                                         String startDate) {

        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollList = empPayrollDBService
                    .getEmployeeDetailsBasedOnStartDateUsingPreparedStatement(startDate);
        return this.employeePayrollList;
    }

    public List<EmpPayrollData> getEmployeeDetailsBasedOnName(IOService ioService, String name) {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollList = empPayrollDBService.getEmployeeDetailsBasedOnNameUsingStatement(name);
        return this.employeePayrollList;
    }

    public static void main(String[] args) {

        System.out.println("---------- Welcome To Employee Payroll Application ----------\n");
        ArrayList<EmpPayrollData> employeePayrollList = new ArrayList<EmpPayrollData>();
        EmpPayrollService employeePayrollService = new EmpPayrollService(employeePayrollList);
        Scanner consoleInputReader = new Scanner(System.in);

        employeePayrollService.readEmployeePayrollData(consoleInputReader);
        employeePayrollService.writeEmployeePayrollData(IOService.CONSOLE_IO);
    }

}
