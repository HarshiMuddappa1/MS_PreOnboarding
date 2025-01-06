package day3;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Lambda_Learn {

    /*
        Lambda expression are introduced in java 8
        It provides clear and concise way to represent FunctionalInterface method using expression.
        Earlier to Lambda, Anonymous class were used.
        Lambda is an instance of functional interface.
        Lambda expression can be passed as an object and executed on demand.
        Lambda expression provide class independence since function can be created without class.
        Syntax: (args..) -> {body};
        Expression Lambdas:
            If the lambda body contains a single expression then the lambda body is referred as Expression body, and it is called as Expression lambda.
        Block Lambdas:
            If the lambda body contains more than single expression then the lambda body is referred as Block body, and it is called as Block lambda.

     */

    static int n = 10;
    int num = 3;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        checkLambdaUsage();
        checkLambdaSyntax();
        checkLambdaUsageWithCollection();
        checkLambdaVariableCapture();
        checkLambdaSerialization();
    }

    private static void checkLambdaSerialization() throws IOException, ClassNotFoundException {
        /*
            Serialization of lambda functions are straightforward since lambda doesn't implement Serializable interface.
            Two ways to serialize lambda are:
            1. If the class is not pre-defined/we have control to change class.
            2. If the class is defined by library or third-party /we don't have control to change class.
         */

        // 1. If the class is not pre-defined/we have control to change class.
        interface Audit<T> extends Predicate<T>, Serializable {
        }

        Audit<String> audit =
                auditId -> auditId.startsWith("AUD", 1)
                                && auditId.length() >= 8
                                && auditId.endsWith("t");

        //2. If the class is defined by library or third-party /we don't have control to change class.
        Predicate<String> auditStat =
                (Predicate<String> & Serializable)
                        auditId -> auditId.startsWith("AUD", 1)
                                && auditId.length() >= 8
                                && auditId.endsWith("t");

        ObjectOutputStream objectOut =
                new ObjectOutputStream(new FileOutputStream("lambda.ser"));
        objectOut.writeObject(audit);
        objectOut.writeObject(auditStat);
        objectOut.flush();
        objectOut.close();
        ObjectInputStream objectIn =
                new ObjectInputStream(new FileInputStream("lambda.ser"));
        Audit<String> deserializedAudit = (Audit<String>)objectIn.readObject();
        Predicate<String> deserializedAuditStat = (Predicate<String>)objectIn.readObject();
        System.out.println("1. Audit status: " + deserializedAudit.test("#AUD2025Aura8t"));
        System.out.println("2. Audit status: " + deserializedAuditStat.test("#AUD2025Aura8t"));

    }

    private static void checkLambdaVariableCapture() {
        /*
            Variable defined by enclosing scope lambda expression are accessible within lambda expression.
            Lambda expression can access instance or static variable defined by the enclosing scope.
            Local variable can be accessed from lambda expression if it is final or effective final.
            Effective final variable is the one which assigned value only once.
            Effective final local variable should be used in lambda because variable capture may introduce concurrency problems.
            Standard loop local variable should be effective final
            No restriction on For-Each loop variables because it's considered distinct.
         */
        Lambda_Learn obj = new Lambda_Learn();
        int x = 5;
        interface Test{
            void print();
        }

        Test printLambda =
                () -> System.out.println("Accessing Instance Variable: " + obj.num + "\n"
                        + "Accessing Static Variable: " + n + "\n"
                        + "Accessing Local Variable: " + x);
        printLambda.print();
        /* This below line gives compiler error because x is a local variable accessed in lambda
        expression, it results in illegal and allows variable capture.
         */
        //x = 6;
    }

    private static void checkLambdaUsageWithCollection() {
        List<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(1);
        list.add(5);
        list.add(8);
        list.add(2);
        list.add(6);
        System.out.println("List before sorting: ");
        list.forEach(l -> System.out.print(l + " "));
        System.out.println();
        list.sort((l1, l2) -> l1 - l2);
        System.out.println("List after sorting: ");
        list.forEach(l -> System.out.print(l + " "));
        System.out.println();
    }

    private static void checkLambdaSyntax() {
        /* If there's only one statement, then {} is optional.
           Body of the lambda expression can contain zero, one or more statements.
           Return type of anonymous function is same as the body of the lambda.
           If u mention 'return', curly braces and semicolon are mandatory.
           Don't mention semicolon and 'return' when curly braces are not provided.
        */
        // Lambda expression with no parameter.
        Runnable r = () -> System.out.println("Lambda with no parameter");
        new Thread(r).start();

        // Lambda expression with one input parameter.
        Consumer<String> printName = (name) -> System.out.println("Name is: " + name);
        printName.accept("Harsh");

        // Lambda expression with more than one parameter.
        BiConsumer<String, String> printNameWithDes =
                (name, des) -> System.out.println("Name is: " + name + " Designation is: " + des);
        printNameWithDes.accept("Harsh", "SE");
    }

    private static void checkLambdaUsage() {
        // 1. Implementing an interface using class
        class Test implements Runnable{

            @Override
            public void run() {
                System.out.println("Running a thread: " + Thread.currentThread().getName());
            }
        }

        Test test = new Test();
        Thread task1 = new Thread(test);
        task1.setName("ClassImpl");
        task1.start();

        // 2. Implementing an interface using anonymous class
        Runnable runnableAnonymous = new Runnable() {
            @Override
            public void run() {
                System.out.println("Running a thread: " + Thread.currentThread().getName());
            }
        };
        Thread task2 = new Thread(runnableAnonymous);
        task2.setName("AnonymousClassImpl");
        task2.start();

        // 3. Lambda as an instance of the functional interface
        Runnable runnableLambda =
                () -> System.out.println("Running a thread: " + Thread.currentThread().getName());
        Thread task3 = new Thread(runnableLambda);
        task3.setName("LambdaImpl");
        task3.start();
    }
}
