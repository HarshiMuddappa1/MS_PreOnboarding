package day3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.*;

public class FunctionalInterface_Learn {

    /*
        -> Functional Interface is introduced in Java 8, and it can be implemented by lambda and method reference.
           and Functional Programming in java is possible because of it
        -> Functional Interfaces are the Interfaces which contains single abstract method and any no. of default and static methods.
        -> Functional Interfaces can be annotated with @FunctionalInterface even though it's not mandatory to add this annotation,
           it's better to add to clear the design intent and compiler flags "Unexpected @FunctionalInterface annotation" if more than one abstract method is present.
        -> Java library has provided many functional interfaces for general purpose usage.
        -> Functional Interfaces are present in java.util.function package
        -> Consumer,Predicate,Function,Supplier are some of the FIs provided by the library.
        -> There are other FIs which are used mainly are Comparator, Comparable, Runnable, Callable, etc.
     */

    public static void main(String[] args) {
        checkConsumerFunctionalInterfaces();
        checkFunctionFunctionalInterfaces();
        checkPredicateFunctionalInterfaces();
        checkSupplierFunctionalInterfaces();
        checkUnaryOperatorFunctionalInterfaces();
        checkBinaryOperatorFunctionalInterfaces();
    }

    private static void checkBinaryOperatorFunctionalInterfaces() {
        /*  It implements BiFunction<T, T, T> Functional Interface
            1. BinaryOperator<T> -> represents operation on two operands of same type and returns same type result.
                -> It has only two methods:
                static maxBy(Comparator):
                    -> Returns a BinaryOperator which returns greater of two elements acc. to Comparator.
                static minBy(Comparator):
                    -> Returns a BinaryOperator which returns lesser of two elements acc. to Comparator.
                -> it inherits BiFunction FI methods
            2. IntBinaryOperator -> represents operation on two operands of int type & returns int valued result.
            3. LongBinaryOperator -> represents operation on two operands of long type & returns long valued result.
            4. DoubleBinaryOperator -> represents operation on two operands of double & and returns double valued result.
         */

        BinaryOperator<Integer> numberOfDigits =
                (x, y) -> (int) (Math.log10(x) + Math.log10(y) + 1);
        System.out.println("numberOfDigits: " + numberOfDigits.apply(343, 568));
    }

    private static void checkUnaryOperatorFunctionalInterfaces() {

        /*  It implements Function<T, T> Functional Interface
            1. UnaryOperator<T> -> accepts single operand and returns same type result.
                -> It has only one method which is identity() -> returns same as input.
                -> it inherits Function FI methods
            2. IntUnaryOperator -> accepts single int argument and returns int valued result.
            3. LongUnaryOperator -> accepts single long argument and returns long valued result.
            4. DoubleUnaryOperator -> accepts single double argument and returns double valued result.
         */

        UnaryOperator<String> covertFirstLetterUppercase =
                str -> str.substring(0, 1).toUpperCase() + str.substring(1);
        System.out.println("covertFirstLetterUppercase: " +
                covertFirstLetterUppercase.apply("harshitha"));
    }

    private static void checkSupplierFunctionalInterfaces() {
        /*
            1. Supplier is a functional Interface which accepts no argument and returns a result.
            It has the following methods:
            T get():
                -> returns the result.

            2. IntSupplier -> accepts no argument and returns int valued result.
            3. LongSupplier -> accepts no argument and returns long valued result.
            4. DoubleSupplier -> accepts no argument and returns double valued result.
            5. BooleanSupplier -> accepts no arguments and returns boolean valued result.
         */

        Supplier<Integer> getRandomNum = () -> (int) (Math.random() * 1000);

        System.out.println("RandomNumber: " + getRandomNum.get());
    }

    private static void checkPredicateFunctionalInterfaces() {
        /*
            1. Predicate is a functional Interface which accepts single argument and returns boolean.
            It has the following methods:
            boolean test(T):
                -> Evaluate on the given argument and return the boolean res.
            default Predicate<T> and(Predicate<? super T> other):
                -> Returns a composed predicate which represents logical And of this and other predicate.
            default Predicate<T> or(Predicate<? super T> other):
                -> Returns a composed predicate which represents logical or of this and other predicate.
            default Predicate<T> negate():
                -> Returns a predicate which represents logical negation of this predicate.
            static Predicate<T> isEqual(Object target):
                -> Returns a predicate which test if two arguments are equal acc. to Object.equal(obj, obj)

            2. IntPredicate -> accepts single int primitive argument and returns the result of the predicate.
            3. LongPredicate -> accepts single long primitive argument and returns the result of the predicate.
            4. DoublePredicate -> accepts single double primitive argument and returns the result of the predicate.
            5. BiPredicate<T, U> -> accepts two input arguments and returns the result of the predicate.
         */

        Predicate<Integer> isEven = x -> x % 2 == 0;
        Predicate<Integer> isPowerOfTwo = x -> x != 0 && (x & (x-1)) == 0;
        boolean isEvenAndPowerOfTwo = isEven.and(isPowerOfTwo).test(4);
        boolean isEvenOrPowerOfTwo = isEven.or(isPowerOfTwo).test(6);
        boolean isOdd = isEven.negate().test(3);
        Predicate<Object> isEqualToTwo = Predicate.isEqual(2);
        boolean isEqualsToTwo = isEqualToTwo.test(2);
        System.out.println("isEvenAndPowerOfTwo: " + isEvenAndPowerOfTwo);
        System.out.println("isEvenOrPowerOfTwo: " + isEvenOrPowerOfTwo);
        System.out.println("isOdd: " + isOdd);
        System.out.println("isEqualsToTwo:" + isEqualsToTwo);
    }

    private static void checkFunctionFunctionalInterfaces() {
        /* 1. Function<T, R> -> accepts single argument input and returns an output.
            It has four methods:
            R apply(T): -> it's an abstract method
            default <V> Function<T, V> andThen(Function< ? super R, ? extend V> after):
                -> R is the return type of this and V is the return type of after & composed function.
                -> first this will be executed and then after
                -> If any of this or after consumer throws exception then it is relayed to the caller of composed operation.
                -> If after is null, it will throw NullPointerException
                -> If this throws the exception, after will not be executed.
            default <V> Function<V, R> compose(Function< ? super V, ? extend T> before):
                -> R is the return type of this and V is the return type of before
                -> first before will be executed and then this
            default <T> Function<T, T> identity():
                -> returns its only argument.
          2. IntFunction -> accepts single int primitive argument and returns an output.
          3. LongFunction -> accepts single long primitive argument and returns an output.
          4. DoubleFunction -> accepts single double primitive argument and returns an output.
          5. BiFunction<T, U, R> -> accepts two input arguments and returns an output.
          6. DoubleToIntFunction -> accepts double valued arg and returns int valued res.
          7. DoubleToLongFunction -> accepts double valued arg and returns long valued res.
          8. IntToDoubleFunction -> accepts int valued arg and returns double valued res.
          9. IntToLongFunction -> accepts int valued arg and returns long valued res.
          10. LongToIntFunction -> accepts long valued arg and returns int valued res.
          11. LongToDoubleFunction -> accepts long valued arg and returns double valued res.
          12. ToDoubleBiFunction<T, U> -> accepts two args and returns double valued res.
          13. ToDoubleFunction<T> -> accepts an arg and returns double valued res.
          14. ToIntBiFunction<T, U> -> accepts two args and returns int valued res.
          15. ToIntFunction<T> -> accepts an arg and returns int valued res.
          16. ToLongBiFunction<T, U> -> accepts two args and returns long valued res.
          17. ToLongFunction<T> -> accepts an arg and returns long valued res.
        */

        Function<Integer, Double> getDoubledVal = x -> x * 2.0;
        Double res = getDoubledVal.apply(2);
        System.out.println(res);

        Function<List<String>, String> concat =
                list -> list.stream().reduce("", (concatRes, str) -> concatRes + " " + str);

        List<String> listOfStr = new ArrayList<>();
        listOfStr.add("Hello");
        listOfStr.add("To");
        listOfStr.add("World");

        System.out.println(concat.apply(listOfStr));

        Function<String, String> lowerCaseStr = String::toLowerCase;
        Function<String, String> trimStr = String::trim;
        Function<String, Integer> strLen = String::length;

        int length = lowerCaseStr.compose(trimStr).andThen(strLen).apply(" Harshitha    ");
        System.out.println(length);

    }

    private static void checkConsumerFunctionalInterfaces() {
        /* 1. Consumer<T> -> accepts single argument input and returns no output.
            It has two methods:
            void accept(T): -> it's an abstract method
            default Consumer<T> andThen(Consumer< ? super T> after):
                -> first this will be executed and then after
                -> If any of this or after consumer throws exception then it is relayed to the caller of composed operation.
                -> If after is null, it will throw NullPointerException
                -> If this throws the exception, after will not be executed.
          2. IntConsumer -> accepts single int primitive argument and returns no output.
          3. LongConsumer -> accepts single long primitive argument and returns no output.
          4. DoubleConsumer -> accepts single double primitive argument and returns no output.
          5. BiConsumer<T, U> -> accepts two input arguments and returns no result.
        */
        System.out.println("Predefined consumer: ");
        Consumer<String> display = str -> {
            str = str.toLowerCase(Locale.ROOT);
            System.out.print(str + " ");
        };
        //display.accept("HARSHITHA");

        Consumer<List<String>> modify =
                list -> list.sort(String::compareTo);
        Consumer<List<String>> dispList =
                list -> list.forEach(str -> System.out.print(str + " "));

        // List.of() -> provides Immutable/Unmodifiable List.
        // List.of("Harshitha", "Manjula", "Muddappa", "Arun", "Akash", "Rakesh");
        List<String> listOfNames = new ArrayList<>();
        listOfNames.add("Harshitha");
        listOfNames.add("Manjula");
        listOfNames.add("Muddappa");
        listOfNames.add("Arun");
        listOfNames.add("Rakesh");
        listOfNames.add("Akash");

        modify.andThen(dispList).accept(listOfNames);

        System.out.println();
        System.out.println("Custom Consumer:");
        interface ConsumerCustom<T> {
            void print(T t);
        }

        ConsumerCustom<List<Integer>> printConsumer =
            list -> list.forEach(i -> System.out.print(i + " "));
        List<Integer> listOfInt = List.of(1, 2, 3, 4, 5, 6);
        printConsumer.print(listOfInt);
        System.out.println();
    }




}
