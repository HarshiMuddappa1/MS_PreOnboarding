package day3;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Assignment {

    public static void main(String[] args) {
        List<Employee> employees = getEmployees();
        int salary = 2000;

        // 1. Write consumer to print employee details
        System.out.println("Employee details using Consumer:");
        employees.forEach(Assignment::writeConsumerToPrintEmployeeDetails);
        employees.forEach(
                emp -> writeConsumerToPrintEmployeeDetails(
                        employee -> System.out.println(employee.toString()), emp));

        // 2. Write predicate to print employees whose salary is more than 2000
        System.out.println("Employees with salary > 2000");
        employees.forEach(Assignment::writePredicateToPrintEmployeesWithSalaryMoreThan2k);

        // 3. Write predicate to print employees whose salary is more than given salary
        System.out.println("Employees with salary > " + salary);
        employees.forEach(
                emp -> writePredicateToPrintEmployeesWithSalMoreThanGivenSal(emp, salary)
        );

        // 4. Write supplier to generate 16 character random password as string.
        String password = getRandomPass();
        System.out.println("Generated password: " + password);

        // 5. Create List of Users from List of Employees
        System.out.println("Users created from Employees:");
        getUsersFromEmployees(employees).forEach(System.out::println);

        // 6. Sort employees using month of their birth
        System.out.println("Employees sorted based on the birth month:" );
        sortEmployeeByBirthMonth(employees);

        // 7. Define functional interface for username generator and threads to print employees and users
        System.out.println("Printing Employees and Users using Thread:");
        List<User> users = generateUserName(employees);
        printEmployeesAndUsers(employees, users);

    }

    private static void printEmployeesAndUsers(List<Employee> employees, List<User> users){
        // Print Employees
        Runnable printEmp =
                () -> employees.forEach(System.out::println);
        Thread threadToPrintEmp = new Thread(printEmp);
        threadToPrintEmp.start();

        // Print Users
        Runnable printUsers =
                () -> users.forEach(System.out::println);
        Thread threadToPrintUsers = new Thread(printUsers);
        threadToPrintUsers.start();
    }

    private static List<User> generateUserName(List<Employee> employees) {

        interface UserNameGenerator {
            User generate(String firstName, String lastName, int birthYear, long id);
        }

        UserNameGenerator userNameGenerator =
                (firstName, lastName, birthYear, id) -> {
                    String userName = id + firstName + lastName + birthYear;
                    return new User(userName, id, getRandomPass());
                };

        Function<List<Employee>, List<User>> empToUser =
                listOfEmp -> {
                    return listOfEmp.stream()
                            .map(emp -> {
                                return userNameGenerator.generate(emp.getFirstName(), emp.getLastName(),
                                        emp.getDateOfBirth().getMonth().getValue(), emp.getId());
                            })
                            .toList();
                };
        return empToUser.apply(employees);
    }

    private static void sortEmployeeByBirthMonth(List<Employee> employees) {
        System.out.println("Employees sorted acc. to their birth month:");
        List<Employee> tempEmployees = new ArrayList<>(employees);
        tempEmployees.sort(
                (emp1, emp2) ->
                        emp1.getDateOfBirth().getMonth().compareTo(emp2.getDateOfBirth().getMonth()));
        tempEmployees.forEach(System.out::println);
    }

    private static List<User> getUsersFromEmployees(List<Employee> employees){

        Function<List<Employee>, List<User>> empToUser =
                listOfEmp -> {
                    return listOfEmp.stream()
                            .map(emp -> {
                                String userName = emp.getId() + emp.getFirstName() + emp.getLastName()
                                        + emp.getDateOfBirth().getYear();
                                return new User(userName, emp.getId(), getRandomPass());
                            })
                            .toList();
                };

        return empToUser.apply(employees);
    }

    private static String getRandomPass(){
        SecureRandom random = new SecureRandom();
        Supplier<String> randomPass =
                () -> {
                    StringBuilder password = new StringBuilder();
                    for(int i = 0; i < 16; i++){
                        password.append((char)random.nextInt(33,Byte.MAX_VALUE-1));
                    }
                    return password.toString();
                };
        return randomPass.get();
    }

    private static void writePredicateToPrintEmployeesWithSalMoreThanGivenSal(Employee emp, int sal){

        Predicate<Employee> employeesWithSalMoreThanGivenSal =
                employee -> employee.getSalary().compareTo(new BigDecimal(sal)) > 0;
        boolean isSalMoreThan2K = employeesWithSalMoreThanGivenSal.test(emp);
        if (isSalMoreThan2K)
            System.out.println(emp);
    }

    private static void writePredicateToPrintEmployeesWithSalaryMoreThan2k(Employee emp){

        Predicate<Employee> employeesWithSalMoreThan2k =
                employee -> employee.getSalary().compareTo(new BigDecimal("2000")) > 0;
        boolean isSalMoreThan2K = employeesWithSalMoreThan2k.test(emp);
        if (isSalMoreThan2K)
            System.out.println(emp);
    }

    private static void writeConsumerToPrintEmployeeDetails
            (Consumer<Employee> employeeDetailsPrinter, Employee emp){
        employeeDetailsPrinter.accept(emp);
    }

    private static void writeConsumerToPrintEmployeeDetails(Employee emp){

        Consumer<Employee> employeeDetailsPrinter =
                employee -> System.out.println(employee.toString());
        employeeDetailsPrinter.accept(emp);
    }

    private static List<Employee> getEmployees(){
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(147L, "Tom", "Tom",
                LocalDate.of(1980, 6, 21),
                new BigDecimal("100000000000000000")));
        employees.add(new Employee(189L, "Jerry", "Tom",
                LocalDate.of(1985, 8, 12),
                new BigDecimal("200000000000000000")));
        return employees;
    }
}
