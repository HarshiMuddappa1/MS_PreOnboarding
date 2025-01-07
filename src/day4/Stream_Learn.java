package day4;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Stream_Learn {

    /*
        Stream is introduced in java 8, and present in java.util.stream package
        Stream provides an abstract layer.
        Stream is a sequence of components that can be processed sequentially.
        stream package has classes, interfaces, enum to allow functional style operations on elements.
        Streams are used to process collection of objects, and it improves performance by avoiding loops.
        Stream is not a data structure, it is used to process Collections, Arrays and i/o.
        Stream don't change the original DS, various intermediate methods will be pipelined and terminate function will mark the end of the process and return the result.
        Intermediate operation:
            Returns the stream hence multiple methods are chained together.
            ex: filter, map, iterate, flatMap
        Terminate operation:
            Returns the result of certain type but not stream.
            ex: forEach, collect, count, reduce
        ShortCircuit operation:
            Instead of processing all elements, it exits on first/desired match.
            ex: anyMatch, findFirst
        Stream follows lazy evaluation where intermediate functions are not invoked until terminate operation is called.
        Stream doesn't modify the Source and it should be operated only once.
        To create parallel stream use parallelStream() on collection or parallel on Stream.
     */

    public static void main(String[] args) {
        checkStreamCreationWays();
        checkStreamMethods();
    }

    private static void checkStreamMethods() {

        List<Integer> list = List.of(4, 8, 1, 9, 3, 2);
        List<String> listOfStr = List.of("4", "7", "1", "9", "3", "0", "2");

        /* 1. boolean allMatch(Predicate)
            - Returns whether all elements of this stream match the provided predicate.
         */
        boolean isAllElemEven = list.stream()
                .allMatch(x -> x % 2 == 0);
        System.out.println("allMatch() - isAllElementsEven: " + isAllElemEven);

        /* 2. boolean anyMatch(Predicate)
            - Returns whether any elements of this stream match the provided predicate.
         */
        boolean isAnyElemEven = list.stream()
                .anyMatch(x -> x % 2 == 0);
        System.out.println("anyMatch() - isAnyElementsEven: " + isAnyElemEven);

        /* 3. R collect(Collector<T,A,R>)
            - It is a terminal operation.
            - It allows to perform mutable reduction operations(Repacking elements to some data structure and performing additional logic).
            - It takes Collector interface implementation.
         */

        /* To collect the result into unmodifiable collection/s.
            Collectors.toUnmodifiableList(),
            Collectors.toUnmodifiableMap(Function<T, K> key, Function<T, K> val),
            Collectors.toUnmodifiableSet()
         */
        /*Collectors.toCollection(Supplier)
                -> To collect the result into any Data structure
         */
        System.out.println("Collector.toCollection():");
        LinkedList<Integer> linkedList = list.stream()
                .collect(Collectors.toCollection(LinkedList::new));
        linkedList.forEach(x -> System.out.print(x + " "));
        System.out.println();

        /* Collectors.toList(),
                -> To collect the result into List
           Collectors.toSet()
                -> To collect the result into Set
           Collectors.toMap(Function<T, K> key, Function<T, K> val)
                -> To collect the result into Map
         */
        System.out.println("Collector.toMap():");
        Map<Integer, Integer> map =
                list.stream()
                        .collect(Collectors.toMap(x -> x , y -> y));
        map.forEach((k, v) -> System.out.println("Key: " + k + " Val: " + v));

        /* Collectors.collectingAndThen(Collector, Function)
            -> performs the another action after collecting.
         */
        System.out.println("Collector.collectingAndThen():");
        Set<Integer> set =  list
                .stream()
                .collect(Collectors
                .collectingAndThen(Collectors.toSet(),
                        Collections::unmodifiableSet));

        list.forEach(x -> System.out.print(x + " "));

        /* Collectors.joining()
            -> can be used only on Stream<String> and return collector concatenating all element.
         */
        System.out.println("Collector.joining():");
        String str = listOfStr.stream().collect(Collectors.joining());
        System.out.println("String: " + str);

        /* Collectors.counting()
            -> count all the elements of the Stream
         */
        System.out.println("Collector.counting():");
        Long count = list.stream().collect(Collectors.counting());
        System.out.println("Length of the Stream elements: " + count);

        /* Collectors.summarizingInt()
            Collectors.summarizingDouble()
            Collectors.summarizingLong()
            -> returns a class containing statistical information about numerical data
         */
        System.out.println("Collectors.summarizingLong():");
        LongSummaryStatistics summaryStatistics =
                list.stream().collect(Collectors.summarizingLong(Integer::longValue));
        System.out.println(summaryStatistics.getCount());

        /* Collectors.averagingInt(ToIntFunction)/Double(.)/Long(.)
            -> returns average of extracted elements.
         */
        System.out.println("Collectors.averagingInt():");
        Double avg = list.stream().collect(Collectors.averagingDouble(x -> x + x));
        System.out.println("Average: " + avg);

        /* Collectors.summingInt(ToIntFunction)/Double()/Long()
            -> returns sum of extracted elements.
         */
        System.out.println("Collectors.summingInt():");
        Integer sum = list.stream().collect(Collectors.summingInt(x -> x+1));
        System.out.println("Sum:" + sum);

        /* Collectors.maxBy()/minBy()
            -> returns the max or min element of the stream according to Comparator provided.
         */
        System.out.println("Collectors.maxBy():");
        Optional<Integer> max =
                list.stream().collect(Collectors.maxBy((o1, o2) -> o2 - o1));
        System.out.println("Max: " + max.orElse(0));

        /* Collectors.groupingBy(Function)
            -> group object by given property and store in a Map
         */
        System.out.println("Collectors.groupingBy():");
        Map<String, List<Integer>> mapOfElem =
                list.stream().collect(Collectors.groupingBy(x -> x+ " "));
        mapOfElem.forEach((k, v) -> System.out.println("Key: " + k + " Val: " + v));

        /* 4. R collect(Supplier<R>, BiConsumer<T,R> accumulator, BiConsumer<R,R> combiner)
            -> performs a mutable operation on the elements of this stream
         */
        System.out.println("collect(): ");
        StringBuilder strBuilder = listOfStr.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append);
        System.out.println(strBuilder.toString());

        /* 5. static Stream<T> concat(Stream<T>, Stream<T>)
            - creates a lazily concatenated Stream from combining elements from 1st stream then 2nd stream.
            - resulting streams are ordered if both streams are ordered/sequential.
            - resulting streams are not ordered if any streams are parallel.
         */
        System.out.println("concat(Stream,Stream)):");
        Stream<Integer> stream1 = Stream.of(3, 7, 8, 9, 0);
        Stream<Integer> stream2 = Stream.of(1, 4, 2, 5);
        Stream<Integer> resStream = Stream.concat(stream1, stream2);
        resStream.forEach(x -> System.out.print(x + " "));
        System.out.println();

        /* 6. long count()
            -> returns count of elements in the stream
         */
        System.out.println("count():");
        long countofElem = list.stream().count();
        System.out.println("Count of Stream elements: " + countofElem);

        /* 7. Stream<T> distinct()
            -> returns a Stream consist of distinct element of the stream.
         */
        System.out.println("distinct():");
        Stream<Integer> distinctElem = list.stream().distinct();
        distinctElem.forEach(x -> System.out.print(x + " "));
        System.out.println();

        /* 8. Stream<T> filter(Predicate<T>)
            -> Returns a stream with elements which matches the given predicate.
         */
        System.out.println("filter():");
        Stream<Integer> evenStr = list.stream().filter(x -> x % 2 == 0);
        evenStr.forEach(x -> System.out.print(x + " "));
        System.out.println();

        /* 9. Optional<T> findAny()
            -> returns an optional containing some elements of the stream or empty if stream is empty.
         */
        System.out.println("findAny():");
        Optional<Integer> anyEven = list.stream().filter(x -> x % 2 == 0).findAny();
        System.out.println(anyEven.orElse(0));

        /* 10. Optional<T> findFirst()
            -> returns an optional containing first element of the stream or empty if stream is empty.
         */
        System.out.println("findFirst():");
        Optional<Integer> firstOdd = list.stream().filter(x -> x % 2 != 0).findFirst();
        System.out.println(firstOdd.orElse(0));

        /* 11. Stream<R> flatMap(Function<T, Stream<R>>)
            -> returns a stream consist of elements which is produced by applying mapping function.
           12. DoubleStream flatMapToDouble(Function<T, DoubleStream>)
             -> returns a DoubleStream consist of elements which is produced by applying mapping function.
           13. IntStream flatMapToInt(Function<T, IntStream>)
             -> returns a IntStream consist of elements which is produced by applying mapping function.
           14. LongStream flatMapToLong(Function<T, LongStream>)
             -> returns a LongStream consist of elements which is produced by applying mapping function.
         */
        System.out.println("flatMap():");
        Stream<String> flatMap = list.stream().flatMap(x -> Stream.of(x + " "));
        flatMap.forEach(x -> System.out.print(x + " "));
        System.out.println();

        /* 15. forEach(Consumer<T>)
            -> Performs an action on each element of the stream
         */
        System.out.println("forEach():");
        list.stream().forEach(x -> System.out.print(x + " "));
        System.out.println();

        /* 16. forEachOrdered(Consumer<T>)
            -> Performs an action on each element of the stream in the ordered way that it encounter.
         */
        System.out.println("forEachOrdered():");
        list.stream().forEachOrdered(x -> System.out.print(x + " "));
        System.out.println();

        /* 17. static Stream<T> limit(long maxSize)
            -> returns stream consisting of stream elements where size is not more than maxSize of limit.
         */
        /* 18. static Stream<T> generate(Supplier<T>)
            -> generate sequential unordered infinite stream
         */
        System.out.println("generate():");
        Stream<Double> unorderedStream =
                Stream.generate(() -> Math.random() * 100).limit(10);
        unorderedStream.forEach(System.out::println);

        /* 19. static Stream<T> iterate(int seed, UnaryOperator<T>)
            -> generate sequential ordered infinite stream
         */
        System.out.println("iterate():");
        Stream<Integer> orderedStream =
                Stream.iterate(10, x -> x * 10).limit(10);
        orderedStream.forEach(System.out::println);

        /* 20. static Stream<R> map(Function<T,R>)
            -> returns result stream by applying mapping function on the stream.
           21. static DoubleStream mapToDouble(ToDoubleFunction<T>)
            -> returns result DoubleStream by applying mapping function on the stream.
           22. static IntStream mapToInt(ToIntFunction<T>)
            -> returns result IntStream by applying mapping function on the stream.
           23. static LongStream mapToLong(ToLongFunction<T>)
            -> returns result LongStream by applying mapping function on the stream.
         */
        System.out.println("map():");
        Stream<Double> mapStream = list.stream().map(x -> (double) (x/2));
        mapStream.forEach(x -> System.out.print(x + " "));
        System.out.println();

        /* 24. Optional<T> max(Comparator<T>)
            -> Returns max element of the stream according to the provided comparator
         */
        System.out.println("max():");
        Optional<Integer> maxElem =
                list.stream().max((o1, o2) -> o1 - o2);
        System.out.println(maxElem.orElse(0));

        /* 25. Optional<T> min(Comparator<T>)
              -> Returns min element of the stream according to the provided comparator
         */
        System.out.println("min():");
        Optional<Integer> minElem =
                list.stream().min((o1, o2) -> o1 - o2);
        System.out.println(minElem.orElse(0));

        /* 26. boolean noneMatch(Predicate<T>)
            -> Returns true when no elements of the stream matches the given predicate.
         */
        System.out.println("noneMatch(): ");
        boolean isNoneMatching =
                list.stream().noneMatch(x -> x < 10);
        System.out.println("isNoneMatching: " + isNoneMatching);

        /* 27. Stream<T> peek(Consumer<T>)
            -> Returns the stream consisting of elements of this stream,
            additionally performing the action on the elements as elements are consumed from result.
         */
        System.out.println("peek():");
        Stream<String> strList = listOfStr.stream().peek(x -> x.endsWith("7"));
        strList.forEach(x -> System.out.print(x + " "));
        System.out.println();

        /* 28. Optional<T> reduce(BinaryOperator<T> accumulator)
                -> performs reduction on the element of the stream, using accumulator
           29. T reduce(T, BinaryOperator<T> accumulator)
                -> performs reduction on the element of the stream, using the identity and accumulator
           30. U reduce(U, BiFunction<T, U> accumulator, BinaryOperator<U> combiner)
                -> performs reduction on the element of the stream, using the identity, accumulator and combiner.
         */
        System.out.println("reduce():");
        Integer sumUsingReduce =
                list.stream().reduce(0, (x, y) -> x + y);
        System.out.println("sumUsingReduce: " + sumUsingReduce);

        /* 31. Stream<T> skip(long)
            -> returns a remaining stream by skipping the no. of elements provided.
         */
        System.out.println("skip():");
        list.stream().skip(4).forEach(x -> System.out.print(x + " "));
        System.out.println();

        /* 32. Stream<T> sorted()
            -> sort according to natural order
           33. Stream<T> sorted(Comparator<T>)
            -> sort according to comparator
         */
        System.out.println("Sorted():");
        list.stream().sorted().forEach(x -> System.out.print(x + " "));
        System.out.println();

        /* 34. Object[] toArray()
             -> returns an array containing elements of the stream
           35. A[] toArray(IntFunction<A[]>)
             -> returns an array containing elements of the stream using the function.
         */
        System.out.println("toArray():");
        Object[] objArr = list.stream().toArray();
        System.out.println(Arrays.toString(objArr));

    }

    private static void checkStreamCreationWays() {

        List<Integer> list = List.of(7, 5, 9, 12, 8, 1, 4);
        Integer[] arr = new Integer[]{7, 8, 2, 9, 1, 4};

        Stream<Integer> streamOnCollection = list.stream();
        IntStream intStream = list.stream().mapToInt(x -> x+1);
        Stream<Integer> streamOnArray = Arrays.stream(arr);
        Stream<Integer> stream = Stream.of(7, 9, 2, 3, 4, 1);
        Stream<String> emptyStream = Stream.empty();

        // Stream using builder
        Stream.Builder<Integer> builder = Stream.builder();
        Stream<Integer> streamUsingBuilder = builder.add(1).add(2).add(3).build();

        // Infinite stream using iterate() which accepts seed, Predicate, UnaryOperator
        Stream
                .iterate(0, n -> n+1)
                .limit(10)
                .forEach(System.out::println);

        // Infinite stream using generate() which accepts Supplier
        Stream
                .generate(Math::random)
                .limit(5)
                .forEach(System.out::println);



    }
}
