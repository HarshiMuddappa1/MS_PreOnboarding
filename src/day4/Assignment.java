package day4;

import com.sun.source.tree.UsesTree;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Assignment {

    public static void main(String[] args) {
        List<Employee> employees = getEmployees();

        // 1. Print FirstName of Employees who joined in year 2023
        int yearOfJoining = 2023;
        System.out.println("FirstName of employees who joined in year 2023:");
        printFirstNameOfEmpBasedOnYearOfJoining(employees, yearOfJoining);

        // 2. Print count, min, max, sum, average of all salary's of employees in each department
        countEmployeesInEachDep(employees);
        findMinSalaryOfEmployeesInEachDep(employees);
        findMaxSalaryOfEmployeesInEachDep(employees);
        findSumOfSalaryOfEmployeesInEachDep(employees);
        findAvgSalaryOfEmployeesInEachDep(employees);

        // 3. Print employees in sorted order according to their FirstName, except HR department.
        sortEmployeesByFirstNameExceptHRDepEmployees(employees);

        // 4. Increment salary of employee by 10% in IT department
        String department = "IT";
        incrementSalOfGivenDepEmployees(employees, department);

        // 5. Print 50 odd numbers after 100
        int seed = 100;
        int limit = 50;
        printOddNumbers(seed, limit);

        // 6. Create comma separated list of FirstName of employees ordered by dateOfBirth
        getCommaSeparatedFirstNameOfEmployeesOrderedByDateOfBirth(employees);

    }

    private static void getCommaSeparatedFirstNameOfEmployeesOrderedByDateOfBirth
            (List<Employee> employees) {

        System.out.println("Comma separated FirstName of Employees ordered by dateOfBirth:");
        employees
                .stream()
                .sorted((emp1, emp2) ->
                        emp1.getDateOfBirth().compareTo(emp2.getDateOfBirth())
                )
                .map(employee -> employee.getFirstName() + ",")
                .forEach(name -> System.out.print(name + " "));
    }


    private static void printOddNumbers(int seed, int limitVal) {

        System.out.println("Odd numbers:");
        Stream<Integer> oddNumbers =
                Stream
                .iterate(seed+1, x -> x % 2 != 0, x -> x+2)
                .limit(limitVal);
        oddNumbers.forEach(x -> System.out.print(x + " "));
        System.out.println();
    }

    private static void incrementSalOfGivenDepEmployees(List<Employee> employees, String department) {
        List<Employee> resEmployees =
                employees
                        .stream()
                        .filter(employee -> employee.getDepartment().equals(department))
                        .peek(employee -> employee.setSalary(employee.getSalary().add(
                                BigDecimal.valueOf(employee.getSalary().longValue() * 10))
                                )
                        )
                        .toList();
        System.out.println("Employees of IT department after 10% salary increment:");
        resEmployees.forEach(System.out::println);
        System.out.println();
    }

    private static void sortEmployeesByFirstNameExceptHRDepEmployees(List<Employee> employees) {
        System.out.println("Employees sorted acc. to their FirstName except HR department employees:");
        employees
                .stream()
                .filter(employee -> !employee.getDepartment().equals("HR"))
                .sorted(
                        Comparator.comparing(Employee::getFirstName)
                )
                .forEach(System.out::println);
        System.out.println();
    }

    private static void findAvgSalaryOfEmployeesInEachDep(List<Employee> employees) {
        Map<String, Double> avgSalaryOfEmpFromEachDepart =
                employees
                        .stream()
                        .collect(
                                Collectors.groupingBy(
                                        Employee::getDepartment,
                                        Collectors.averagingLong(
                                                employee -> employee.getSalary().longValue()
                                        )
                                )
                        );
        System.out.println("Employees avg salary in each department:");
        avgSalaryOfEmpFromEachDepart.forEach((k, v) ->
                System.out.println("Department: " + k + " ,Avg salary in the Department: " + v));
        System.out.println();
    }

    private static void findSumOfSalaryOfEmployeesInEachDep(List<Employee> employees) {
        Map<String, Long> sumOfSalaryOfEmpFromEachDepart =
                employees
                        .stream()
                        .collect(
                                Collectors.groupingBy(
                                        Employee::getDepartment,
                                        Collectors.summingLong(
                                                employee -> employee.getSalary().longValue()
                                        )
                                )
                        );
        System.out.println("Employees total salary in each department:");
        sumOfSalaryOfEmpFromEachDepart.forEach((k, v) ->
                System.out.println("Department: " + k + " ,Total Department salary: " + v));
        System.out.println();
    }

    private static void findMaxSalaryOfEmployeesInEachDep(List<Employee> employees) {
        Map<String, Optional<Employee>> maxSalariedEmployees =
                employees
                        .stream()
                        .collect(
                                Collectors.groupingBy(
                                        Employee::getDepartment,
                                        Collectors.maxBy(
                                                Comparator.comparingLong(
                                                        (emp) -> emp.getSalary().longValue()
                                                )
                                        )
                                )
                        );

        System.out.println("Employees with highest salary in each department:");
        maxSalariedEmployees.forEach((k, v) ->
                System.out.println("Department: " + k + " ,Employee: "
                        + v.orElseThrow(() -> new RuntimeException("No Employee present"))));
        System.out.println();
    }

    private static void findMinSalaryOfEmployeesInEachDep(List<Employee> employees) {
        Map<String, Optional<Employee>> minSalariedEmployees =
            employees
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Employee::getDepartment,
                                Collectors.minBy(
                                        Comparator.comparingLong(
                                                (emp) -> emp.getSalary().longValue()
                                        )
                                )
                        )
                );
        System.out.println("Employees with lowest salary in each department:");
        minSalariedEmployees.forEach((k, v) ->
                System.out.println("Department: " + k + " ,Employee: "
                        + v.orElseThrow(() -> new RuntimeException("No Employee present"))));
        System.out.println();
    }

    private static void countEmployeesInEachDep(List<Employee> employees) {
        Map<String, Long> map =
            employees
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Employee::getDepartment,
                                Collectors.counting()
                        )
                );
        System.out.println("Employees in each department:");
        map.forEach((k, v) ->
                System.out.println("Department: " + k + " ,NoOfEmployees: " + v));
        System.out.println();
    }

    private static void printFirstNameOfEmpBasedOnYearOfJoining
            (List<Employee> employees, int yearOfJoining) {

        employees
                .stream()
                .filter(employee -> employee.getDateOfJoining().getYear() == yearOfJoining)
                .map(Employee::getFirstName)
                .forEach(System.out::println);
        System.out.println();
    }

    private static List<Employee> getEmployees(){
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(147L, "Tom", "cat",
                LocalDate.of(1980, 6, 21),
                LocalDate.of(2023, 8, 12),
                "HR",
                new BigDecimal("400000000000000000")));
        employees.add(new Employee(189L, "Jerry", "mouse",
                LocalDate.of(1985, 8, 12),
                LocalDate.of(2021, 1, 9),
                "HR",
                new BigDecimal("300000000000000000")));
        employees.add(new Employee(4589L, "Simba", "Mufasa",
                LocalDate.of(1990, 1, 1),
                LocalDate.of(2024, 12, 25),
                "IT",
                new BigDecimal("300000000000000000")));
        employees.add(new Employee(673L, "Tanjiro", "Kamado",
                LocalDate.of(2000, 2, 21),
                LocalDate.of(2023, 8, 30),
                "IT",
                new BigDecimal("200000000000000000")));
        employees.add(new Employee(234L, "Giyu", "Tomioka",
                LocalDate.of(1990, 5, 30),
                LocalDate.of(2014, 12, 20),
                "IT",
                new BigDecimal("600000000000000000")));
        return employees;
    }
}


