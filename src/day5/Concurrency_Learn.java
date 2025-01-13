package day5;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.*;
import java.util.stream.Stream;

public class Concurrency_Learn {

    /*
        java.util.concurrent package contains all necessary classes and interfaces for multithreading and concurrency.

        Single threaded program:
            -> By default, programming languages are sequential in nature, code execution happens line by line
            -> In single threaded program instructions will be executed one by one in the order, the time-consuming sections of the code can freeze the entire application.
            -> Solution to the single threaded program can be provided by figuring out the time-consuming tasks and running such tasks in separate thread if possible.

        Multithreading:
            -> It's the ability of CPU to perform different tasks concurrently.

        Concurrency:
            -> It's doing multiple things all at once by quickly switching b/w tasks.
            -> it refers to the ability of a system to execute multiple tasks at the same time
                or nearly overlapping times so they seem like being executed at the same time.
            -> in concurrent systems tasks may start execute and complete independently of each other,
                but they may not necessarily be executing simultaneously at any given moment
                ex: multitasking achieved through single processor switching between executing multiple tasks, multithreading or multiprocessing

        Parallelism:
            -> It's doing multiple things at once by having different parts of the task been done simultaneously.
            -> Tasks are truly executed simultaneously either on multiple processors or processors
            -> It is breaking down the tasks which are not related and increasing performance.

        Process:
            -> Process is an instance of program execution.
            -> when you enter an application, it's a process, the OS assigns its own stack & heap memory area.

        Thread:
            -> Thread is a lightweight process, it is a unit of execution within a given program.
            -> A single process may contain multiple threads, each thread in the process share resources.

        Time Slicing algorithm:
            -> When a process contains multiple threads, CPU has to ensure that all threads are given a fair chance to execute and one such approach is time slicing.
            -> certain time is assigned to each thread

        Pros and cons of multithreading:
            -> Pros: we can build responsive applications, better resource utilization, better performant applications
            -> cons: Synchronization is tricky, difficult to design and test multithreaded apps, thread context switch is expensive.

        Thread Lifecycle:
        NEW state: Thread will be in this state until we call start() on it.
        ACTIVE state: Thread will be in this state after we start() on it, it has two states runnable and running.
        BLOCKED state: Thread will be in this state when it's waiting for some thread to finish.
        TERMINATED state: Thread will be in this state after it's done doing its required task.

     */

    private static int counter = 0;
    private static int counter1 = 0;
    private static final Object LOCK = new Object();

    public static void main(String[] args) throws InterruptedException {
        //createThreadsUsingRunnableI();
        //createThreadsUsingThreadC();
        //checkJoinOperationOnThread();
        //checkUserAndDaemonThread();
        //checkOnThreadPriority();
        //checkOnThreadSynchronization();
        //checkOnWaitAndNotify();
        //checkOnProducerConsumerProblem();
        //checkExecutorService();
        //checkTheIdealPoolSize();
        //checkCallableAndFuture();
        //checkSynchronizedCollections();
        //checkCountDownLatch();
        //checkBlockingQueue();
        //checkConcurrentMap();
        //checkCyclicBarrier();
        //checkExchanger();
        //checkCopyOnWriteArray();
        //checkOnLocks();
        //checkReentrantLock();
        //checkReadWriteLock();
        //checkVisibilityProblem();
        //checkDeadlock();
        //checkAtomicVariables();
        //checkSemaphores();
        //checkMutex();
        checkForkJoinPool();
    }

    private static void checkForkJoinPool() {
        /*
            ForkJoin Framework is a concurrency framework introduced in java 7, to simplify
            the process of parallel programming, it is designed to take advantage of multicore
            processors by dividing tasks into multiple smaller tasks, executing them parallel
            and combining the result together, it is similar to ExecutorService and differs in
            creating the subtask which is not the case in ExecutorService.

            Task producing subtask:
                In ForkJoin Framework a task is capable of producing a subtask
                in divide and conquer approach.
            Per Thread queuing and work stealing:
                A thread pick the task from its own queue, also there's a load balancer
                which allows to pick the thread from other queue.

            Use of ForkJoin Framework:
            -> utilization of multicore processor.
            -> simplified parallelism
            -> Efficient work stealing algorithm

            Forking: Dividing the task into subtasks using fork() method.
            Joining: Combine the results together.
            RecursiveTask: Return the result from the task.
            RecursiveAction: It doesn't return anything from the task.

            ForkJoin pool:
                It's a specialized pool in java which is a specialized implementation of
                ExecutorService, it is designed to support ForkJoin framework.
                It uses work stealing algorithm to efficiently manage and balance the workload among
                the threads, when a thread finishes its task it can steal the task from a work queue
                of other threads, making all threads productive.
                It manages the Fork join tasks.

         */

        class SearchOccurrenceTask extends RecursiveTask<Integer> {

            final int[] arr;
            final int start;
            final int end;
            final int searchElement;

            public SearchOccurrenceTask(int[] arr, int start, int end, int searchElement) {
                this.arr = arr;
                this.start = start;
                this.end = end;
                this.searchElement = searchElement;
            }

            @Override
            protected Integer compute() {
                int size = end-start+1;
                if(size > 50){
                    int mid = start + end/2;
                    SearchOccurrenceTask task1 = new SearchOccurrenceTask(arr, start, mid, searchElement);
                    SearchOccurrenceTask task2 = new SearchOccurrenceTask(arr, mid+1, end, searchElement);
                    task1.fork();
                    task2.fork();
                    return task1.join() + task2.join();
                }else
                    return search();
            }

            private Integer search() {
                int count = 0;
                for(int i = start; i < end; i++){
                    if(arr[i] == searchElement){
                        count++;
                    }
                }
                return count;
            }
        }

        int[] arr = new int[100];
        Random random = new Random();
        for(int i = 0; i < 100; i++){
            arr[i] = random.nextInt(10) + 1;
        }

        int searchElement = random.nextInt(10) + 1;

        ForkJoinPool fkPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        SearchOccurrenceTask task = new SearchOccurrenceTask(arr, 0, 100, searchElement);
        Integer occurrence = fkPool.invoke(task);
        System.out.println("Array is : " + Arrays.toString(arr));
        System.out.printf("%d found %d times ", searchElement, occurrence);
        System.out.println();

        class WorkLoadSplitter extends RecursiveAction{

            private final long workLoad;

            WorkLoadSplitter(long workLoad) {
                this.workLoad = workLoad;
            }

            @Override
            protected void compute() {
                if(workLoad > 16){
                    System.out.println("Workload too big so splitting " + workLoad);
                    long firstWorkLoad = workLoad/2;
                    long secondWorkLoad = workLoad - firstWorkLoad;

                    WorkLoadSplitter firstSplit = new WorkLoadSplitter(firstWorkLoad);
                    WorkLoadSplitter secondSplit = new WorkLoadSplitter(secondWorkLoad);

                    firstSplit.fork();
                    secondSplit.fork();
                }else{
                    System.out.println("Workload within limits, task being executed is: " + workLoad);
                }
            }
        }

        WorkLoadSplitter splitTask = new WorkLoadSplitter(64);
        ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        pool.invoke(splitTask);

    }

    private static void checkMutex() {
        /*
            Mutex: means mutual exclusion, it's a synchronization mechanism used to control access to
                shared resource in multithreaded environment.
            Primary purpose of mutex is to ensure that only one thread can access a critical
                section or shared resource at a given time, it prevents race condition and ensures
                data consistency.
            it's concept of locks and synchronization.
         */
    }

    private static void checkSemaphores() {
        /*
            Semaphores: It's a synchronization mechanism, used to control access to a shared
                resource, in concurrent programming it uses counters to manage the number of
                allowed accesses to the resources preventing race conditions and ensuring safe
                concurrent operations, it can be binary or counting it depends on the number of
                permitted access.
            Our application can make limited number of concurrent calls to another application,
                in order to add that restriction we can use Semaphores, it's a permit mechanism.
                if a thread wants to access the resource it has get the permit from semaphore,
                permit is like a token.
            acquire() method will get the permit on the semaphore.
            acquire(permit) method will allow to get multiple permits at a time,
                how many permits will a thread acquire same no. of permits should be released.
            release() method will release the permit acquired by the thread.
            At a time it'll allow only number of threads to run based on the number of permit.
            some other methods:
            tryAcquire(): it'll acquire the permit if it's available, else it won't be blocked.
            tryAcquire(timeout): it'll acquire the permit if it's available within the given time.
            availablePermits(): returns number of available permits.
         */

        enum ScrapeService {
            INSTANCE;

            private Semaphore semaphore = new Semaphore(3);

            public void scrape(){
                try{
                    semaphore.acquire();
                    invokeScrapeBot();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    semaphore.release();
                }
            }

            private void invokeScrapeBot() {
                try{
                    System.out.println("Scraping data ...");
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < 15; i++) {
            service.execute(ScrapeService.INSTANCE::scrape);
        }
        service.shutdown();
    }

    private static void checkAtomicVariables() throws InterruptedException {
        /*
            AtomicVariables: A feature which helps to write concurrent code.
            read-modify-write cycle: count++ will first read then modify and then write the value.
            These are the type of variables that support lock free thread safe operations on single
             variables, they ensure that any kind of read modify write operation such as incrementing
             or updating a value is performed atomically, it prevents race condition and this makes
             them crucial in concurrent programming as they help in maintaining the data consistency,
             and integrity without the overhead of traditional synchronization techniques.
            Atomic variables ensure that incrementing and updating a value is performed in single
             non-divisible step, this eliminates the need for synchronized locks which are costly due
             to potential overhead and thread contention.

            AtomicVariables are provided for different types and are classes:
                AtomicInteger, AtomicDouble, AtomicLong

            Basic operations on AtomicVariables:
                get(): fetch the value.
                set(): sets the value.
                compareAndSet(expected, update): it sets the value if it equals to the expected.
                getAndIncrement()/incrementAndGet(): get the value and increment.
                getAndDecrement()/decrementAndGet(): get the value and decrement.
         */

        final AtomicInteger count = new AtomicInteger(0);

        Thread one = new Thread(() -> {
            for(int i = 0; i < 1000; i++){
                count.getAndIncrement();
            }
        });

        Thread two = new Thread(() -> {
            for(int i = 0; i < 1000; i++){
                count.getAndIncrement();
            }
        });

        one.start();
        two.start();

        one.join();
        two.join();

        System.out.println("Count value: " + count);
    }

    private static void checkDeadlock() {
        /*
            Deadlocks: It's a situation when one thread is holding a lock and waiting for another lock
                which is acquired by another thread, and it is waiting for the lock held by current thread
                it's a cycle of dependencies.
             To spot deadlocks manually is a nearly impossible situation to understand when which thread
             gets access to the lock.
             Deadlock can be determined using programmatic way using Thread Dump (jps -l),
              or to probe the code every 5 seconds to understand if there's any deadlock.

            How to prevent Deadlock:
                -> use timeouts to acquire lock
                -> Global ordering of the locks
                -> avoid nesting of locks
                -> use thread safe alternatives

         */

        class DeadLock {
            private final Lock lockA = new ReentrantLock(true);
            private final Lock lockB = new ReentrantLock(true);

            public void workerOne(){
                lockA.lock();
                System.out.println("Worker One acquired lockA ..");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                lockB.lock();
                System.out.println("Worker One acquired lockB ..");
                lockA.unlock();
                lockB.unlock();
            }

            public void workerTwo(){
                lockB.lock();
                System.out.println("Worker Two acquired lockB ..");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                lockA.lock();
                System.out.println("Worker Two acquired lockA ..");
                lockB.unlock();
                lockA.unlock();
            }
        }

        DeadLock deadLock = new DeadLock();
        new Thread(deadLock::workerOne, "Worker One").start();
        new Thread(deadLock::workerTwo, "Worker Two").start();

        new Thread( () -> {
            ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
            while(true){
                long[] threadIds = mxBean.findDeadlockedThreads();
                if(threadIds != null){
                    System.out.println("Deadlock detected..");
                    for(long threadId : threadIds){
                        ThreadInfo threadInfo = mxBean.getThreadInfo(threadId);
                        System.out.println("Thread with id " + threadId + " is in deadlock "
                                        + threadInfo);
                    }
                    break;
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private static void checkVisibilityProblem() {

        /*
            Cores: piece of hardware which are responsible for computing, it doesn't have any storage
                capability.
            Register: every core has a dedicated storage component which holds the data temporarily
                during the execution, it has the least amount of storage capacity.
            L1 cache: is very small but very fast, it plays crucial role in improving the performance
                of the CPU by reducing the data access to and from the main memory(RAM),
                each core has a dedicate L1 cache.
            L2 cache: it's a larger capacity cache compared to L1 cache, it's role is to complement
                the smaller and faster L1 cache, it does so by providing additional storage for frequently
                accessed data and instructions, it is integrated in two ways depending on the
                architecture of the processor, multiple cores can have shared L2 cache or multiple cores
                can have dedicated L2 cache.
            L3 cache: it sits between the L2 cache and RAM, it provides larger pool of cached data,
                and instructions which can be shared among all the CPU cores.
            RAM: It's also known as main memory, and it serves as temporary storage for data and
                instructions that are actively being used by the CPU.

            Visibility problem:
                Multiple threads access the shared resource at a time, making the data read by some thread
                invalid, is known as visibility problem.
                ex: thread1 and thread2 read the data of counter which is 1, later thread1 updates the
                counter to 2 and thread2 still be using the same counter value which is 1.

            Volatile keyword:
                It ensures whenever there's a change in the value it's flushed to the main memory and
                whenever there's a value which is supposed to be read, it reads from the shared memory
                which in many cases is L3 cache.
                It ensures correct data being read by all threads but it slows down the application.
         */
    }

    private static void checkReadWriteLock() {
        /*
          ReadWriteLock:
            It's a synchronization mechanism which allows multiple threads to read a shared resource
             concurrently, but only one thread can write to a resource at a time.
            This is mostly used when the resource is read heavy rather than write heavy.
            Many reader threads can acquire the reader lock at a time, only one writer thread can
             acquire the writer thread at a time.

         */

        class SharedResource{
            int counter = 0;
            final ReadWriteLock lock = new ReentrantReadWriteLock();

            public void increment(){
                lock.writeLock().lock();
                try{
                    counter++;
                    System.out.println(Thread.currentThread().getName() + " writes : " + counter);
                } finally{
                    lock.writeLock().unlock();
                }
            }

            public void readVal(){
                lock.readLock().lock();
                try{
                    System.out.println(Thread.currentThread().getName() + " reads : " + counter);
                } finally{
                    lock.readLock().unlock();
                }
            }
        }

        SharedResource sharedResource = new SharedResource();
        for(int i = 0; i < 2; i++){
            Thread readerThread = new Thread(() -> {
                for(int j = 0; j < 3; j++){
                    sharedResource.readVal();
                }
            });
            readerThread.setName("Reader Thread: " + (i+1));
            readerThread.start();
        }

        Thread writerThread = new Thread(() -> {
            for(int j = 0; j < 5; j++){
                sharedResource.increment();
            }
        });
        writerThread.setName("Writer Thread: ");
        writerThread.start();
    }

    private static void checkReentrantLock() {
        /*
            ReentrantLock: It allows the thread to acquire the same lock multiple times,
             without causing any deadlocks, if lock is not reentrant and if same thread try to
             acquire the lock again it'll block the thread.
            hold count acquired by the lock will be incremented if same thread acquire the same lock.
             lock is only released if the hold count reaches zero.
            It is useful in situation where Thread calls multiple methods of same class which uses shared resources.
            It is unfair lock, since which thread will acquire the lock is guaranteed.

            methods:
            getHoldCount(): returns an integer in which number of times current thread has acquired the lock.
            tryLock(): if a thread is successfully acquired or not, it'll try to acquire lock and returns boolean
            tryLock(timeout, timeUnit): acquire the thread within the given timeout
            isHeldByCurrentThread(): checks whether the lock is held by current thread.
            getQueueLength(): get the length of the waiting queue.
            newCondition(): get a condition to invoke await or signal methods.

         */

        class ReentrantDemo {
            private final ReentrantLock lock = new ReentrantLock();
            private int sharedData = 0;

            public void increment(){
                lock.lock();
                try{
                    sharedData++;
                    System.out.println("Increment shared data: " + sharedData);
                    decrement();
                }finally {
                    lock.unlock();
                }
            }

            public void decrement(){
                lock.lock();
                try{
                    sharedData--;
                    System.out.println("Decrement shared data: " + sharedData);
                }finally {
                    lock.unlock();
                }
            }
        }

        ReentrantDemo reentrantDemo = new ReentrantDemo();
        for (int i = 0; i < 5; i++){
            new Thread(reentrantDemo::increment).start();
        }

    }

    private static void checkOnLocks() {
        /*
             When multiple threads running simultaneously and all are trying to access the same
              resource, without proper synchronization it could lead to inconsistency.
             Locks provide a way to access shared resource ensuring that only one thread can access
              the resource at a given time thus it helps to reduce data corruption and other concurrent
              issues.

            Synchronized blocks provides synchronization using synchronized keyword, and they provide
             intrinsic locking which means lock acquired by the object is automatically released by JVM,
             these are easy to use and avoid boilerplate code, however they have limitation such as
             lack of flexibility and lock acquisition and inability to handle interrupts.

            Locks provide more flexibility and control over locking mechanisms,
            java Lock interface and its implementations allow manually acquire and release locks,
            and locks can be acquired and released in any sequence and in any scope.

            Use synchronized blocks where simple synchronization is needed and use Locks where complex
             synchronization is needed with more flexibility and fine-grained control.

            Lock conditions:
                As soon as u acquire the lock, u will get access to the resources.
                if a thread wants some condition to meet, it'll call condition.await() and
                if any thread fulfil the condition, it'll call condition.signal() which invokes
                longest waiting thread or the signalAll which invokes all waiting threads.

         */

        class ConditionDemo {
            final Integer MAX_SIZE = 5;
            final Lock lock = new ReentrantLock();
            final Queue<Integer> buffer = new LinkedList<>();
            final Condition bufferNotFull = lock.newCondition();
            final Condition bufferNotEmpty = lock.newCondition();

            private void produce(int item) throws InterruptedException{
                lock.lock();
                try{
                    while(buffer.size() == MAX_SIZE){
                        bufferNotFull.await();
                    }
                    buffer.offer(item);
                    System.out.println("Produced item: " + item);
                    bufferNotEmpty.signal();
                }finally {
                    lock.unlock();
                }
            }

            private void consume() throws InterruptedException{
                lock.lock();
                try{
                    while(buffer.isEmpty()) {
                        bufferNotEmpty.await();
                    }
                    System.out.println("Consumed item: " + buffer.poll());
                    bufferNotFull.signal();
                }finally {
                    lock.unlock();
                }
            }
        }

        ConditionDemo conditionDemo = new ConditionDemo();
        Thread producerThread = new Thread(() -> {
            for(int i = 0; i < 10; i++){
                try {
                    conditionDemo.produce(i);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread consumerThread = new Thread(() -> {
            for(int i = 0; i < 10; i++){
                try {
                    conditionDemo.consume();
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        producerThread.start();
        consumerThread.start();

    }

    private static void checkCopyOnWriteArray() {
        /*
         CopyOnWriteArray: Creates a copy for every read.
            when multiple threads accessing and modifying data at the same time, it ensures that
            reader don't get disturbed by writers and writers don't interfere with each other
            making your program safer and more efficient.

            when a thread wants to read from the array it creates the snapshot of the array to read,
            if a thread wants to write into the array it creates the snapshot of the array to write,
            once the write operation is completed snapshot array is considered as latest version
            of the array from which snapshots should be created for read and write.

            This is Just as GIT allows parallel development without conflict.
         */
        final List<Integer> list = new CopyOnWriteArrayList<>();
        list.addAll(Arrays.asList(0, 0, 0, 0, 0, 0, 0));

        Runnable reader = () -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(list);
            }
        };

        Runnable writer = () -> {
            Random random = new Random();
            while (true) {
                try {
                    Thread.sleep(1400);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                list.set(random.nextInt(list.size()), random.nextInt(10));
            }
        };

        new Thread(writer).start();
        new Thread(writer).start();
        new Thread(writer).start();
        new Thread(reader).start();
    }

    private static void checkExchanger() {
        /*
            Exchanger: A synchronization point at which threads can pair and swap elements within a
                concurrent environment.
            Two threads can call exchange method on Exchanger object passing the object they want to
                exchange.
            Exchanger is useful when two threads needs to synchronize and exchange data before
                proceeding with their respective tasks.
            Exchanger is a standalone class, it has exchange(object) method which is used to perform
                blocking exchange operation, it waits until another thread arrives at exchange position,
                and it returns the object provided by other thread, if other thread is not arrived yet
                the current thread will block until it arrives, another method is exchangeVariation()
                which is an overloaded method which takes both object and timeout, it is same as
                exchange() except that it'll throw Timeout Exception if another thread doesn't arrive
                in the specified timeout.

            Queue                          Exchanger
            -> one to many communication   -> point to point communication
            -> Asynchronous                -> Synchronous
            -> Buffering                   -> Simplicity for two threads
            -> Non-symmetrical exchange    -> symmetrical exchange

            Exchanger is similar to synchronous queue however exchanger is bidirectional
            synchronous queue is unidirectional.
         */

        class FirstThread implements Runnable{
            private final Exchanger<Integer> exchanger;

            public FirstThread(Exchanger<Integer> exchanger){
                this.exchanger = exchanger;
            }

            @Override
            public void run() {
                int dataToSend = 17;
                System.out.println("First Thread is sending the data: " + dataToSend);

                try {
                    Integer receivedData = exchanger.exchange(dataToSend);
                    System.out.println("First Thread received the data: " + receivedData);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        class SecondThread implements Runnable{
            private final Exchanger<Integer> exchanger;

            public SecondThread(Exchanger<Integer> exchanger){
                this.exchanger = exchanger;
            }

            @Override
            public void run() {
                int dataToSend = 30;
                System.out.println("Second Thread is sending the data: " + dataToSend);

                try {
                    Thread.sleep(3000);
                    Integer receivedData = exchanger.exchange(dataToSend);
                    System.out.println("Second Thread received the data: " + receivedData);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        Exchanger<Integer> exchanger = new Exchanger<>();
        Thread firstThread = new Thread(new FirstThread(exchanger));
        Thread secondThread = new Thread(new SecondThread(exchanger));
        firstThread.start();
        secondThread.start();

    }

    private static void checkCyclicBarrier() {
        /*
            Every thread has to wait until all thread arrives at common point.
            CyclicBarrier will trip when all the given number of threads waiting for upon it,
                and it'll execute the barrier action when the barrier is tripped
                barrier action will be executed by the last entering barrier thread.
            Barrier gets reset once the barrier is tripped.
            It uses counter and condition to manage the Threads, when await() method is invoked
                internal counter will decrement the value.
         */

        final int NUM_TOURISTS = 5;
        final int NUM_STAGES = 3;
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(NUM_TOURISTS, () -> {
            System.out.println("Tour guide starts speaking..");
        });

        class Tourist implements Runnable {
            private final int touristId;

            public Tourist(int touristId){
                this.touristId = touristId;
            }

            @Override
            public void run() {
                for(int i = 0; i < NUM_STAGES; i++){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Tourist: " + touristId + " arrives at stage " + (i+1));

                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        for(int i = 0; i < NUM_TOURISTS; i++){
            new Thread(new Tourist(i)).start();
        }
    }

    private static void checkConcurrentMap() {
        /*
            It's an interface which represents a map that can be safely accessed and
             modified concurrently by multiple threads, it extends the Map interface and provides
             additional atomic operations to support concurrent access without the need for explicit synchronization.
            It is needed to handle situations where in multiple threads need to access and modify a map concurrently.

            ConcurrentMap implementationS:
            ConcurrentHashMap: lock will be applied to specific segment, multiple threads can read,
                however segment which is locked for writing isn't allowed to read for another thread.
            ConcurrentSkipListMap:
            ConcurrentLinkedHashMap:
            ConcurrentNavigableMap: Implements NavigableMap

            Internal implementation and working of concurrent map
            - Adding an element to concurrent hash map
              - Hashing and determining segment: each element will be hashed to determine the segment,
                each segment acts like a small concurrent hashmap
              - Acquiring lock: Thread will acquire the lock for the segment
              - Insertion in segment: Data will be inserted in the segment
              - Releasing lock: Thread will release the lock on the segment after insertion completes.

            - Fetching an element from concurrent hash map
              - Hashing and determining segment
              - Acquiring lock
              - Searching in segment
              - Releasing lock

         */

        final Map<String, String> cache = new ConcurrentHashMap<>();

        for(int i = 0; i < 10; i++){
            final int threadNum = i;

            new Thread(() -> {
                String key = "Key @ " + threadNum;
                for(int j = 0; j < 3; j++){
                    String value = getCachedValue(key, cache);
                    System.out.println("Thread " + Thread.currentThread().getName() +
                            ": Key = " + key + ": value = " + value);
                }
            }).start();
        }
    }

    private static String getCachedValue(final String key, final Map<String, String> cache) {
        String value = cache.get(key);

        if(value == null){
            value = compute(key);
            cache.put(key, value);
        }
        return value;
    }

    private static String compute(String key) {
        System.out.println(key + " is not present, computing ...");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "Value @ " + key;
    }

    private static void checkBlockingQueue() {
        /*
            It's DS which allows multiple threads to safely put and take items from queue,
              this is done in concurrent manner.
            There are two aspects of blocking queue:
            1. Blocking aspect: If a thread try to take an item from queue which is empty
              it'll be paused or blocked until an item becomes available.
              If a thread try to put an item onto the queue which is full
              it'll be paused or blocked until queue space becomes available.
            2. Usage of Queue: it follows FIFO order.

            BlockingQueue is the parent, and it is an interface,
            BlockingDeque and TransferQueue are the classes which implements BlockingQueue.
            BlockingDeque is the double ended queue which blocks threads when it reaches the capacity or become empty.
                here access to both ends will be provided.
            TransferQueue is a special queue where producers can block until a consumer directly receives an item
                it extends the functionality of BlockingQueue by providing a method called as transfer,
                transfer() allows one thread to transfer an item directly to another waiting thread
                potentially avoiding the need for blocking if there are no waiting threads transfer
                behaves like put and blocks until there is a space available for the item.
                transfer queue ensures a strong hand of coordination.

            Major implementations are:
             ArrayBlockingQueue: It implements bounded blocking queue, is backed by array DS.
             LinkedBlockingQueue: It implements bounded or unbounded blocking queue, is backed by LL.
             PriorityBlockingQueue: It implements a blocking queue that orders elements based
                                    on their natural ordering or acc. to the specified comparator.
             DelayQueue: It implements a blocking queue of delayed elements where an element can be taken out
                         when its delay has expired, it's useful for scheduling tasks to be executed
                         after a certain delay or specific time .
             SynchronousQueue: It implements a zero capacity blocking queue where each insert operation
                                must wait for a corresponding remove operation by another thread and vice versa.

           Blocking queue operations:
            put(E e): adds element e to the queue, if queue is full, operation blocks until the space become available.
            take(): retrieves and remove the head of the element, if queue is empty, operation blocks until the element available.
            offer(E e): adds element e to the queue, if its added returns true, if queue is full returns false.
            poll(): retrieves and remove the head of the element, if queue is empty, returns null.
            peek(): retrieves the head of the element if present else returns null.
         */

        final int QUEUE_CAPACITY = 10;
        BlockingQueue<Integer> taskQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

        Thread producer = new Thread(() ->{
            try {
                for(int i = 1; i <= 20; i++){
                    taskQueue.put(i);
                    System.out.println("Task produced: " + i);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread consumerOne = new Thread(() ->{
            try {
                while(true){
                    int task = taskQueue.take();
                    processTask(task, "ConsumerOne");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread consumerTwo = new Thread(() ->{
            try {
                while(true){
                    int task = taskQueue.take();
                    processTask(task, "ConsumerTwo");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        producer.start();
        consumerOne.start();
        consumerTwo.start();

    }

    private static void processTask(int task, String consumerName) throws InterruptedException {
        System.out.println("Task is being processed by " + consumerName + ":" + task);
        Thread.sleep(1000);
        System.out.println("Task consumed by " + consumerName + ":" + task);
    }

    private static void checkCountDownLatch() throws InterruptedException {
        /* CountDownLatch:
            -> It's a synchronization utility which allows one or more thread to wait
               until a set of operations which is being performed in another threads completes.
            -> It maintains a count that starts from a specific number and decreases each time the
               countdown method is called, threads that wait for countdown to reach zero can call the
               await method which will block until the count becomes zero.
           When to use CountDownLatch:
            -> An organizer will divide the task among multiple threads and organizer calls await method
               and each thread has to completes the task and each thread will call countDown() method
               once the thread completes its task, organizer has to wait until countdown of the latch
               reaches zero, that means all threads have completed their task.
            -> It helps in coordinating multiple threads or tasks to synchronize their work,
                it ensures that they all reach a certain point before proceeding further.
            -> resetting the count is not possible
         */

        class Biker implements Runnable{

            final String bikerName;
            LocalTime startTime;
            LocalTime endTime;
            LocalTime totalTime;
            final CountDownLatch latch;

            public Biker(String bikerName, CountDownLatch latch) {
                this.bikerName = bikerName;
                this.latch = latch;
            }

            @Override
            public void run() {
                this.startTime = LocalTime.now();
                System.out.println(bikerName + " finished the race");
                this.endTime = LocalTime.now();
                this.totalTime = LocalTime.of((int) ChronoUnit.HOURS.between(startTime, endTime),
                        (int) ChronoUnit.MINUTES.between(startTime, endTime),
                        (int) ChronoUnit.SECONDS.between(startTime, endTime),
                        (int) ChronoUnit.MILLIS.between(startTime, endTime)
                );
                latch.countDown();
            }
        }

        CountDownLatch latch = new CountDownLatch(5);

        List<Biker> bikers = new ArrayList<>();
        bikers.add(new Biker("Biker1", latch));
        bikers.add(new Biker("Biker2", latch));
        bikers.add(new Biker("Biker3", latch));
        bikers.add(new Biker("Biker4", latch));
        bikers.add(new Biker("Biker5", latch));

        for(int i = 0; i < bikers.size(); i++){
            new Thread(bikers.get(i)).start();
        }

        latch.await();
        bikers.forEach(biker -> {
            System.out.println("Biker: " + biker.bikerName +
                    ", Start Time: " + biker.startTime +
                    ", End Time: " + biker.endTime +
                    ", Total time taken: " + biker.totalTime);
        });
    }

    private static void checkSynchronizedCollections() throws InterruptedException {
        /*
            Most of the java collections are not thread safe.
            Ways to make collections thread safe:
             -> use Collections.synchronize() method.
             -> use the concurrent collections which are synchronized.

           Drawbacks of using Collections.synchronize() approach:
            -> Coarse grained locking: uses single lock to synchronize all methods of the collection.
            -> Limited functionality: it doesn't have any fine-grained or custom locking methods.
            -> No Fail fast iterators: collections returned by Collections.synchronize() doesn't support FailFast iterators
               which throw concurrentModification exception if the collection is structurally modified when an iterator is iterating over it.
               with FailFast iterators, it's possible for concurrent modifications to go unnoticed.
            -> Performance overhead: synchronization introduces overhead due to lock acquisition and release,
                this overhead can degrade performance in high throughput or latency sensitive applications.
         */
        // It produces unexpected result
        // List<Integer> list = new ArrayList<>();

        // use of synchronized method on collection
        List<Integer> list = Collections.synchronizedList(new ArrayList<>());

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++){
                list.add(i);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++){
                list.add(i);
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(list.size());
    }

    private static void checkCallableAndFuture() {
        /*
            Callable:
                Runnable.run() doesn't return anything,
                if we want anything to be returned from a thread we can use Callable
                For callable we have to submit, but for Runnable we can use submit or execute.
            Future:
                Result from the Callable is collected in the Future.
                use get() method to get the value from future.
                It's a placeholder, and it is empty till the time processing is not completed by thread.
                execution time could be depending on the task, in this time if we call Future.get()
                but it doesn't have any value so it'll block the main thread until it gets the value.
                Future is a blocking operation hence main thread execution will resume once future gets the value.
                Future.get() as an overloaded method which waits for certain time to get the value,
                if it doesn't get the value within the provided time, TimeoutException will be thrown.

                Future.cancel(boolean):
                    if u pass true - it may interrupt the thread if the Thread is running
                    if u pass false - it will not interrupt the thread

                Future.isCancelled():
                    checks whether Future is cancelled.

                Future.isDone():
                    it'll return true if thread is successfully completed, or it got interrupted, and exited.
         */

        Callable<Integer> task = () -> {
            Thread.sleep(5000);
            return (int)(Math.random() * 100);
        };

        ExecutorService service = Executors.newFixedThreadPool(2);
        Future<Integer> result = service.submit(task);

        try {
            //System.out.println("Result: " + result.get());
            System.out.println("Result: " + result.get(6, TimeUnit.SECONDS));
            result.cancel(true);
            boolean cancelled = result.isCancelled();
            boolean done = result.isDone();
            System.out.println("isCancelled: " + cancelled + ", isDone: " + done);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Main thread execution completed..");
    }

    private static void checkTheIdealPoolSize() {
        /* Ideal Thread Pool size?
            Check whether it is CPU intensive task or IO intensive task.
            There's no Ideal thread pool size, it's better to analyze the task patterns
            and based on their type utilize a combination of CPU intensive or IO intensive task.
         */

        // CPU intensive task
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(cores);
        System.out.println("Created thread pool with : " + cores + " cores");

        Thread threadPoolTask = new Thread(() -> {
            System.out.println("CPU intensive task being done by: " + Thread.currentThread().getName());
        });

        for(int i = 0; i < 20; i++)
            service.execute(threadPoolTask);

        /* IO intensive task: inherently fire and forgetting nature bec IO operation such as reading from files
            or making network calls often involve waiting for external resources to respond
            during this waiting period the CPU is idle and having more threads allows other tasks to execute
            while one thread is waiting for the io operation to complete.
        */
    }

    private static void checkExecutorService() {
        /*
            We can create multiple thread however creating thousands of threads is not a scalable approach.
                since creating a thread in java is equal to creating OS level thread.
            We can create Fixed thread pool and use them repeatedly, this can be achieved through ExecutorService.
            ExecutorService is a tool in java for managing and running tasks concurrently across multiple threads
                instead of developer handling all the creation and management work.
            ExecutorService creates a bunch of threads called as thread pool and threads are not killed once they're executing the task.
                rather they're used for executing another task.
            With the help of ExecutorService, time needed to create the thread is saved, and it makes more efficient and manageable.

            4 types of executor provided by ExecutorService:
                1. SingleThreadExecutor
                2. FixedThreadExecutor
                3. CachedThreadPoolExecutor
                4. ScheduledExecutor

            Each type of ExecutorService will be having a BlockingQueue and ThreadPool.
            New task will be keep on adding to the BlockingQueue(priority queue).
            New thread which is available can pick the task from the priority queue.
         */

        class Task implements Runnable{
            private final int taskId;

            public Task(int taskId){
                this.taskId = taskId;
            }

            @Override
            public void run() {
                System.out.println("Task with Id: " + taskId + " being executed by Thread: " +
                        Thread.currentThread().getName());
                try{
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // singleThreadExecutor: here size of the thread will be one and tasks are executed sequentially by the same thread.
        ExecutorService service = Executors.newSingleThreadExecutor();

        // fixedThreadPool: here size of the pool will be as mentioned and those threads are used to execute the tasks.
        service = Executors.newFixedThreadPool(2);

        /* cachedThreadPool: Task queue contain one task at max, if all threads are busy a new thread is created and task is assigned to that thread.
            If a thread is idle for more than 60 seconds, it's killed.
            here the queue is called synchronous queue.
        */
        service = Executors.newCachedThreadPool();

        for (int i = 0; i < 5; i++) {
            service.execute(new Task(i));
        }

        /* scheduledExecutor: It will be having a DelayQueue and each task will be picked up by thread after the delay between each task.
            It will be executed continuously, and we can stop it using awaitTermination()
            shutdownNow() will ends all the tasks even the tasks which are executing.
            shutdown() will not allow new tasks to execute, but it'll wait the executing task to complete.
         */
        ScheduledExecutorService serviceSch = Executors.newScheduledThreadPool(2);
        serviceSch.scheduleAtFixedRate(new Task(1), 0, 1, TimeUnit.SECONDS);
        try {
            if(!serviceSch.awaitTermination(10, TimeUnit.SECONDS)){
                serviceSch.shutdown();
            }
        } catch (InterruptedException e) {
            serviceSch.shutdownNow();
        }
    }

    private static void checkOnProducerConsumerProblem() {
        /*
            Producer-consumer problem is a synchronization scenario where one or more producer threads
            generate data and put it into a shared buffer, while one or more consumer threads retrieve the
            data from the buffer concurrently.
         */

        class Worker{

            private int sequence = 0;
            private final Integer top;
            private final Integer bottom;
            private final LinkedList<Integer> container;
            private final Object LOCK = new Object();

            public Worker(Integer top, Integer bottom) {
                this.top = top;
                this.bottom = bottom;
                this.container = new LinkedList<>();
            }

            public void produce() throws InterruptedException{
                synchronized (LOCK){
                    while(true){
                        if(container.size() == top){
                            System.out.println("Container full, waiting for items to be removed..");
                            LOCK.wait();
                        }else{
                            System.out.println(sequence + " added to the container");
                            container.add(sequence++);
                            LOCK.notify();
                        }
                        Thread.sleep(500);
                    }
                }
            }

            public void consume() throws InterruptedException{
                synchronized (LOCK){
                    while(true){
                        if(container.size() == bottom){
                            System.out.println("Container is empty, waiting for items to be added..");
                            LOCK.wait();
                        }else{
                            System.out.println(container.removeFirst() + " removed from the container");
                            LOCK.notify();
                        }
                        Thread.sleep(500);
                    }
                }
            }
        }

        Worker worker = new Worker(5, 0);

        Thread producer = new Thread( () -> {
            try {
                worker.produce();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread consumer = new Thread( () -> {
            try {
                worker.consume();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        producer.start();
        consumer.start();
    }

    private static void checkOnWaitAndNotify() {
        /*
           wait:
            when a thread calls wait on the lock then thread is suspended from its execution, and it enters into the waiting state.
            when an executing thread enters into a waiting state, other threads which were already waiting will get a chance of execution.

           notify:
            notify is a message to all the waiting threads that object lock is available to acquire.
            notify() will notify single waiting thread
            notifyAll() will notify all waiting threads.
        */

        Thread t1 = new Thread( () -> {
            try{
                task1();
            } catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        });

        Thread t2 = new Thread( () -> {
            try{
                task2();
            } catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        });

        t1.start();
        t2.start();
    }

    private static void task1() throws InterruptedException {
        synchronized (LOCK){
            System.out.println("Running task1....");
            LOCK.wait();
            System.out.println("Back again in task1 after waiting");
        }
    }

    private static void task2() throws InterruptedException {
        synchronized (LOCK){
            System.out.println("Running task2....");
            LOCK.notify();
            System.out.println("Back again in task2 after notifying");
        }
    }

    private static void checkOnThreadSynchronization() {

        Thread threadOne = new Thread(
                () -> {
                    Stream
                            .iterate(0, i -> i + 1)
                            .limit(10000)
                            //.forEach(i -> counter++);
                            .forEach(i -> increment());
                }
        );

        Thread threadTwo = new Thread(
                () -> {
                    Stream
                            .iterate(0, i -> i + 1)
                            .limit(10000)
                            //.forEach(i -> counter++);
                            //.forEach(i -> increment());
                            .forEach(i -> incrementAnother());
                }
        );

        threadOne.start();
        threadTwo.start();

        try{
            threadOne.join();
            threadTwo.join();
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        System.out.println("Counter1 value: " + counter1);
        System.out.println("Counter value: " + counter);

        /*
            Above code(counter++) will not give expected counter value which is 2000 in this case.
            Because counter++ is not a single operation instead it's three operations as below.
            1. Load
            2. Increment
            3. Set back the value
            counter = 0; incrementValue = 1 -> Thread 1
            counter = 0; incrementValue = 1 -> Thread 2

            This is called Race condition, Here counter is a shared resource.
            Multiple threads are accessing the same shared resource which led into race condition.
            We can avoid this by making the threads to access the shared resource one at a time.
            This can be achieved with the help of Synchronized method/block.
         */

        /* Synchronization:
            Each object in java is associated with monitor which is a mutual exclusion mechanism used for synchronization.
            When a thread enters a Synchronized block or method, it attempts to acquire the monitor/intrinsic lock associated with the object on which synchronization is applied.
            Thread will acquire the lock and starts running if the lock is available which means if the lock is not acquired by any thread.
            Thread will go to blocked state if the lock is acquired by any other state.
            When the thread completes the task/exit the synchronized block, it has to release the monitor lock.
            Synchronized keyword will acquire/release the lock of the object.
         */

        /* Problems with synchronized method:
            -> Synchronized method takes coarse gain locking which means lock is applied to entire method body instead of the critical section.
                it'll block other threads from entering the method.
            -> We will lose fine-grained control needed in more complex scenarios.
            -> When subclass is overriding the synchronized super class method it must explicitly declare the method as synchronized.
                and failure to do so can lead to unexpected behavior and potential synchronization issues.
            -> If one thread acquires the class level lock on the method, other thread has to wait even if it has to perform different operation.
         */

        /* synchronized block:
            create synchronized blocks by providing the different locks to solve the above problem
            static Object lock1 = new Object();
            private static void increment(){
                synchronized(lock1){
                    counter++;
                }
            }
         */
    }

    private synchronized static void increment(){
        counter++;
    }
    
    private synchronized static void incrementAnother(){
        counter1++;
    }

    private static void checkOnThreadPriority() throws InterruptedException {

        /*
            If there are n threads and only one available CPU, one thread can run at given time and other threads has to wait.
            Which thread has to run first will be decided by the Thread Scheduler.
            Each thread will have its own priority and under normal circumstance the thread with higher priority will run first.
            Priority value 1-10 can be assigned to the thread, 1 being low priority, 10 being high priority and 5 being normal priority.
            When any thread gets created it'll have the priority as 5.
            Threads with same priority execute in FIFO order and Scheduler will store threads in a queue.
         */

        Thread thread = new Thread(
                () -> {
                    Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                    System.out.println("Run-Priority: " + Thread.currentThread().getPriority());
                }
        );

        thread.start();
        thread.join();
        System.out.println("Priority: " + thread.getPriority());
    }

    private static void checkUserAndDaemonThread() {

        /* Thread can be of two types - Daemon Thread and User Thread.
            When java program starts the main thread(main() method thread) starts running immediately.
            child threads can be started from main thread and main thread will be the last thread to finish since it hs to perform various shutdown operation.
            Daemon thread are helper threads which can run in background and are of low priority, Eg: GC thread.
            Daemon thread will be terminated by the JVM when all other user threads are terminated.
         */

        Runnable daemonHelper = () -> {
            Stream
                    .iterate(0, c -> c < 500, c -> c + 1)
                    .forEach(c -> {
                        try {
                            Thread.sleep(1000);
                            System.out.println("Running Daemon thread: " + c);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
        };

        Thread daemonThread = new Thread(daemonHelper);
        daemonThread.setDaemon(true);
        daemonThread.start();

        Runnable userHelper = () -> {
            try{
                Thread.sleep(5000);
            } catch (InterruptedException e){
                throw new RuntimeException(e);
            }
            System.out.println("User thread completed execution");
        };

        Thread userThread = new Thread(userHelper);
        userThread.start();

    }

    private static void checkJoinOperationOnThread() throws InterruptedException {

        // join() methods makes the main thread to wait until it completes the thread that method is called on.

        Runnable threadOne = () -> Stream
                .iterate(1, i -> i + 1)
                .limit(5)
                .forEach(i -> System.out.println("Running ThreadOne: " + i));

        Runnable threadTwo = () -> Stream
                .iterate(1, i -> i + 1)
                .limit(5)
                .forEach(i -> System.out.println("Running threadTwo: " + i));

        Thread t1 = new Thread(threadOne);
        Thread t2 = new Thread(threadTwo);
        t1.start();
        t1.join();
        t2.start();
        System.out.println("Done with threads execution");
    }

    private static void createThreadsUsingThreadC() {
        // This class has a drawback since it can't extend any other class.
        class ThreadOne extends Thread{
            @Override
            public void run(){
                for (int i = 0; i < 10; i++){
                    System.out.println("ThreadOne: " + i);
                }
            }
        }

        class ThreadTwo extends Thread{
            @Override
            public void run(){
                for (int i = 0; i < 10; i++){
                    System.out.println("ThreadTwo: " + i);
                }
            }
        }

        new ThreadOne().start();
        new ThreadTwo().start();
    }

    private static void createThreadsUsingRunnableI() {

        // Implementing a Runnable is the best way to create thread because a class will have a chance to extend other class and lambda, stream features can be used.

        // old way
        class ThreadOne implements Runnable{

            @Override
            public void run() {
                for(int i = 0; i < 10; i++){
                    System.out.println("Running Thread: " + i);
                }
            }
        }
        Thread t1 = new Thread(new ThreadOne());
        t1.start();

        // new way
        Runnable threadOne = () -> Stream
                .iterate(1, i -> i + 1)
                .limit(5)
                .forEach(i -> System.out.println("Running ThreadOne: " + i));

        Runnable threadTwo = () -> Stream
                .iterate(1, i -> i + 1)
                .limit(5)
                .forEach(i -> System.out.println("Running threadTwo: " + i));

        new Thread(threadOne).start();
        new Thread(threadTwo).start();
    }

}
