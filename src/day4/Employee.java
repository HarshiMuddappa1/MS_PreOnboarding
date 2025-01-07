package day4;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Employee {

    private final long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private LocalDate dateOfJoining;
    private String department;
    private BigDecimal salary;


    public Employee(long id, String firstName, String lastName, LocalDate dateOfBirth, LocalDate dateOfJoining, String department, BigDecimal salary) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.dateOfJoining = dateOfJoining;
        this.department = department;
        this.salary = salary;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth + '\'' +
                ", dateOfJoining=" + dateOfJoining + '\'' +
                ", department=" + department + '\'' +
                ", salary=" + salary +
                '}';
    }
}
