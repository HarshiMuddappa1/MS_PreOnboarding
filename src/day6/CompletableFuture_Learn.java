package day6;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompletableFuture_Learn {

    /*
    Future interface provides the result of asynchronous computation, but it didn't have
        any methods to combine the computations or handle the possible errors.
    CompletableFuture implements Future interface and CompletionStage interface (this interface
        define a contract for an asynchronous computation step that we can combine with other steps).

    Using CompletableFuture asynchronous programming we can write non-blocking code
        where concurrently we can run multiple task in separate threads without blocking main thread,
        when task gets completed it notifies to the main thread.

    Why we need CompletableFuture:
        There are multiple options available to implement async programming which are Future,
        Callable, ExecutorService, ThreadPools etc.
        The drawbacks with those are:
        - Manual completion is not possible
        - Multiple Futures cannot be chained or combined.
        - No proper Exception Handling mechanism

        if we call get() method on CompletableFuture still it'll block the main thread for that
        we can use complete() method on the CompletableFuture object to return the value mentioned
        or the value provided by the supplier, if the computation is not yet completed.


     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        checkRunAsyncAndSupplyAsync();
        checkOtherMethodsOfCompletableFuture();
    }

    private static void checkOtherMethodsOfCompletableFuture() throws ExecutionException, InterruptedException {
        /*
        thenApply(): can be used to process the result of computation,
            it takes the Function interface, and it accepts the value of the computation as arg
            and returns a Future that holds a value of the Function.
        thenAccept(): It takes the Consumer interface, and it accepts the value of the computation
            as arg and returns nothing.
        thenRun(): takes Runnable interface, takes nothing and returns nothing.
        thenCompose(): it takes a function which returns CompletableFuture, it takes previous
            computed future as an argument.
        thenCombine(): execute two Futures independently and combine their result,
            it accepts Future and BiFunction
        allOf(future1, futureN): waits until all the futures provided are completed, and then combine
            the result of all the future args.
            It returns CompletableFuture<Void>, to combine the result we have to get the result
            manually.
        join(): it is similar to get() but it throws Unchecked Exception if the Future is not
            completed normally.
        handle(): handle the exception occurred, similar to catch block.
        completeExceptionally(): complete the execution with exception.
         */

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "hello");
        CompletableFuture<String> resThenApply = future.thenApply(s -> s + " world");
        System.out.println("thenApply():" + resThenApply.get());

        CompletableFuture<String> resThenApplyAsync = future.thenApplyAsync(s -> s + " world");
        System.out.println("thenApplyAsync():" + resThenApplyAsync.join());

        CompletableFuture<Void> resThenAccept =
                future.thenAccept(s -> System.out.println(s + " World!"));
        System.out.println("thenAccept():" + resThenAccept.join());

        CompletableFuture<String> resThenCompose =
                future.thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " World!."));
        System.out.println("thenCompose():" + resThenCompose.join());

        CompletableFuture<String> resThenCombine =
                future.thenCombine(CompletableFuture.supplyAsync(() -> " World!."),
                        (s1, s2) -> s1 + s2);
        System.out.println("thenCombine():" + resThenCombine.join());

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(
                () -> "Hello "
        );
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(
                () -> "To "
        );
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(
                () -> "Java "
        );
        CompletableFuture<String> future4 = CompletableFuture.supplyAsync(
                () -> "Programming."
        );

        CompletableFuture<Void> res =
            CompletableFuture.allOf(future1, future2, future3, future4);
        String combinedStr = Stream.of(future1, future2, future3, future4)
                .map(CompletableFuture::join)
                .collect(Collectors.joining(" "));
        System.out.println("allOf(): " + combinedStr);

        CompletableFuture<String> resHandle = future.handle((x, e) -> x + "World!");
        System.out.println("resHandle: " + resHandle.get());

        future.completeExceptionally(new RuntimeException("Exception"));
        future.get();
    }

    private static void checkRunAsyncAndSupplyAsync() throws ExecutionException, InterruptedException {
        /*
            runAsync() : used to run some background task asynchronously when we don't want to return
                anything from that task.
                CompletableFuture.runAsync(Runnable): get the thread from ForkJoin Global pool
                CompletableFuture.runAsync(Runnable, Executor): get the thread from Executor
            supplyAsync(): used to run some background task asynchronously when we want something
                to return from that task.
                CompletableFuture.runAsync(Supplier<T>)
                CompletableFuture.runAsync(Supplier<T>, Executor)
         */
        CompletableFuture<Void> printHello = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Hello world!");
        });

        printHello.get();

        CompletableFuture<Integer> getRandomNum = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return (int) (Math.random() * 10);
        });

        System.out.println(getRandomNum.get());
        System.out.println(getRandomNum.complete(90));
        System.out.println("Done !.");

    }
}
