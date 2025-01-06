package day2;

import day1.BigDecimal_Learn;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;

public class Optional_Learn {

    /*
        Optional class is introduced in java 8.
        It's a container object which may or may not contain non-null value.
     */
    public static void main(String[] args) {
        String name = "Harsh";
        checkOptionalCreationWays(name);
        checkOptionalHandlingWays(name);
    }

    private static void checkOptionalHandlingWays(String name) {

        Optional<Employee> employee = getEmployeeByName(name);
        Employee newEmp = new Employee((long)(Math.random() * 10000), name, "IT", new BigDecimal("78998"));

        /* 1. orElse(T) -> returns the value present in orElse block if Optional is empty.
            This is Eager method because it will execute the code in orElseGet() even if Value is present.
        */
        System.out.println("orElse(T): " + employee.orElse(newEmp));

        /* 2. orElseGet(Supplier) -> returns the value from the supplier FI if Optional is empty.
            This is Lazy method because it won't execute the code in orElseGet() if Value is present.
        */
        System.out.println("orElseGet(): " + employee.orElseGet(() -> newEmp));

        // 3. orElseThrow(Supplier) -> throws the Exception provided by the supplier if Optional is empty.
        //System.out.println("orElseThrow(): " + employee.orElseThrow(() -> new RuntimeException("Employee Not found")));

        /* 4. filter(Predicate) -> If the value is present and matches the predicate
             then returns the value, else Optional empty
        */
        System.out.println("filter(): " + employee.filter(e -> e.getDept().equals("IT")));

        /* 5. map(Function) ->If the value is present and then provides the map value
             , else Optional empty
         */
        System.out.println("map(): " + employee.map(e -> e.getDept()));

        /* 6. flatMap(Function) ->If the value is present and then provides the map value
             , else Optional empty
         */
        System.out.println("flatMap(): " + employee.flatMap(e -> Optional.of(e.getDept())));

    }

    private static void checkOptionalCreationWays(String name){

        // 1. Optional.empty() -> Returns empty Optional Instance.
        // 2. Optional.of() -> Returns Optional with Specified Non-Null value.
        /* 3. Optional.ofNullable() -> returns Optional describing the specified value if non-null,
            else empty Optional.
         */
        Optional<Object> emptyVal = empty();
        Optional<Employee> employee = getEmployeeByName(name);
        // isPresent() and get() should be avoided instead use orElse(), orElseGet(), etc methods.
        if(employee.isPresent()){
            System.out.println("Employee found: " + employee.get());
        }else{
            System.out.println("Employee not found with name: " + name);
        }

    }

    private static List<Employee> getEmployees(){
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(147L, "Harsh", "IT",
                new BigDecimal("100000000000000000")));
        employees.add(new Employee(189L, "Manju", "Non-IT",
                new BigDecimal("200000000000000000")));
        return employees;
    }

    private static Optional<Employee> getEmployeeByName(String name){
        // One way to use Optional.of() and Optional.empty() and another way Optional.ofNullable()
        List<Employee> employees = getEmployees();
        Employee res = null;
        for(Employee employee : employees){
            if(name.equals(employee.getName())){
                //return Optional.of(employee);
                res = employee;
            }
        }
        //return Optional.empty();
        return Optional.ofNullable(res);
    }
}
