# Effective Java Summary Guide
## With Java 21 Examples

This guide summarizes the key principles from Joshua Bloch's "Effective Java" using modern Java 21 features. Each section includes a principle, explanation, and practical code example.

## Table of Contents
1. [Creating and Destroying Objects](#creating-and-destroying-objects)
2. [Methods Common to All Objects](#methods-common-to-all-objects)
3. [Classes and Interfaces](#classes-and-interfaces)
4. [Generics](#generics)
5. [Enums and Annotations](#enums-and-annotations)
6. [Lambdas and Streams](#lambdas-and-streams)
7. [Concurrency](#concurrency)
8. [Exception Handling](#exception-handling)
9. [Java 21 Specific Features](#java-21-specific-features)

## Creating and Destroying Objects

### Item 1: Consider static factory methods instead of constructors

**Explanation:** Static factory methods provide more flexibility than constructors. They can have meaningful names, control object creation, and return subtypes.

**Example:**
```java
public class User {
    private final String name;
    private final String email;
    private final boolean isAdmin;

    private User(String name, String email, boolean isAdmin) {
        this.name = name;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    // Static factory methods with meaningful names
    public static User createRegularUser(String name, String email) {
        return new User(name, email, false);
    }

    public static User createAdminUser(String name, String email) {
        return new User(name, email, true);
    }

    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public boolean isAdmin() { return isAdmin; }
}

// Usage
User regularUser = User.createRegularUser("John", "john@example.com");
User adminUser = User.createAdminUser("Admin", "admin@example.com");
```

### Item 2: Consider a builder when faced with many constructor parameters

**Explanation:** When a class has many parameters, especially optional ones, consider using the Builder pattern instead of constructors or telescoping constructors.

**Example:**
```java
public class Pizza {
    private final String size;
    private final boolean cheese;
    private final boolean pepperoni;
    private final boolean mushrooms;
    private final boolean olives;

    private Pizza(Builder builder) {
        this.size = builder.size;
        this.cheese = builder.cheese;
        this.pepperoni = builder.pepperoni;
        this.mushrooms = builder.mushrooms;
        this.olives = builder.olives;
    }

    public static class Builder {
        // Required parameter
        private final String size;

        // Optional parameters with default values
        private boolean cheese = false;
        private boolean pepperoni = false;
        private boolean mushrooms = false;
        private boolean olives = false;

        public Builder(String size) {
            this.size = size;
        }

        public Builder cheese(boolean value) {
            cheese = value;
            return this;
        }

        public Builder pepperoni(boolean value) {
            pepperoni = value;
            return this;
        }

        public Builder mushrooms(boolean value) {
            mushrooms = value;
            return this;
        }

        public Builder olives(boolean value) {
            olives = value;
            return this;
        }

        public Pizza build() {
            return new Pizza(this);
        }
    }
}

// Usage
Pizza pizza = new Pizza.Builder("large")
    .cheese(true)
    .pepperoni(true)
    .olives(true)
    .build();
```

### Item 3: Enforce the singleton property with a private constructor or an enum type

**Explanation:** Singletons ensure a class has only one instance. Java 21 offers cleaner ways to implement them.

**Example:**
```java
// Singleton with enum (preferred approach)
public enum DatabaseConnection {
    INSTANCE;
    
    private String connectionString;
    
    public void configure(String connectionString) {
        this.connectionString = connectionString;
    }
    
    public void connect() {
        System.out.println("Connected to: " + connectionString);
    }
}

// Usage
DatabaseConnection.INSTANCE.configure("jdbc:mysql://localhost:3306/mydb");
DatabaseConnection.INSTANCE.connect();

// Singleton with private constructor
public class ConfigManager {
    private static final ConfigManager INSTANCE = new ConfigManager();
    
    private Map<String, String> settings = new HashMap<>();
    
    private ConfigManager() {
        // Private constructor prevents instantiation
    }
    
    public static ConfigManager getInstance() {
        return INSTANCE;
    }
    
    public void setSetting(String key, String value) {
        settings.put(key, value);
    }
    
    public String getSetting(String key) {
        return settings.get(key);
    }
}

// Usage
ConfigManager config = ConfigManager.getInstance();
config.setSetting("theme", "dark");
```

### Item 4: Enforce noninstantiability with a private constructor

**Explanation:** For utility classes that contain only static methods and fields, prevent instantiation with a private constructor.

**Example:**
```java
public class MathUtils {
    // Private constructor to prevent instantiation
    private MathUtils() {
        throw new AssertionError("Utility class - cannot be instantiated");
    }
    
    public static int add(int a, int b) {
        return a + b;
    }
    
    public static int multiply(int a, int b) {
        return a * b;
    }
}
```

## Methods Common to All Objects

### Item 10: Obey the general contract when overriding equals

**Explanation:** When overriding equals, ensure it's reflexive, symmetric, transitive, consistent, and returns false for null.

**Example:**
```java
public class Point {
    private final int x;
    private final int y;
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public boolean equals(Object o) {
        // Reflexive check
        if (this == o) return true;
        
        // Null check and class check
        if (o == null || getClass() != o.getClass()) return false;
        
        // Cast and compare fields
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
```

### Item 11: Always override hashCode when you override equals

**Explanation:** Objects equal according to equals must have the same hashCode value to avoid issues with HashMap, HashSet, etc.

**Example:**
```java
public class Employee {
    private final int id;
    private final String name;
    
    public Employee(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id && Objects.equals(name, employee.name);
    }
    
    @Override
    public int hashCode() {
        // Must be consistent with equals
        return Objects.hash(id, name);
    }
}
```

### Item 12: Always override toString

**Explanation:** A good toString implementation makes your class more pleasant to use and aids debugging.

**Example:**
```java
public class Product {
    private final String name;
    private final double price;
    private final String category;
    
    public Product(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }
    
    @Override
    public String toString() {
        return "Product{" +
               "name='" + name + '\'' +
               ", price=" + price +
               ", category='" + category + '\'' +
               '}';
    }
}
```

## Classes and Interfaces

### Item 15: Minimize the accessibility of classes and members

**Explanation:** Make each class or member as inaccessible as possible. Use private for implementation details and provide minimal public APIs.

**Example:**
```java
public class BankAccount {
    // Private fields - implementation details
    private String accountNumber;
    private double balance;
    private final List<Transaction> transactions = new ArrayList<>();
    
    // Package-private utility method
    static boolean isValidAccountNumber(String number) {
        return number != null && number.matches("\\d{10}");
    }
    
    // Public API
    public BankAccount(String accountNumber) {
        if (!isValidAccountNumber(accountNumber)) {
            throw new IllegalArgumentException("Invalid account number");
        }
        this.accountNumber = accountNumber;
        this.balance = 0.0;
    }
    
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        balance += amount;
        recordTransaction("DEPOSIT", amount);
    }
    
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (amount > balance) {
            throw new IllegalStateException("Insufficient funds");
        }
        balance -= amount;
        recordTransaction("WITHDRAW", amount);
    }
    
    public double getBalance() {
        return balance;
    }
    
    // Private helper method
    private void recordTransaction(String type, double amount) {
        transactions.add(new Transaction(type, amount));
    }
    
    // Private inner class
    private static class Transaction {
        private final String type;
        private final double amount;
        private final LocalDateTime timestamp;
        
        Transaction(String type, double amount) {
            this.type = type;
            this.amount = amount;
            this.timestamp = LocalDateTime.now();
        }
    }
}
```

### Item 17: Minimize mutability

**Explanation:** Immutable objects are simpler, thread-safe, and can be shared freely. Make classes immutable when possible.

**Example:**
```java
public final class ImmutablePerson {
    private final String name;
    private final LocalDate birthDate;
    private final List<String> hobbies;
    
    public ImmutablePerson(String name, LocalDate birthDate, List<String> hobbies) {
        this.name = Objects.requireNonNull(name);
        this.birthDate = Objects.requireNonNull(birthDate);
        // Defensive copy of mutable input
        this.hobbies = List.copyOf(hobbies);
    }
    
    public String getName() {
        return name;
    }
    
    public LocalDate getBirthDate() {
        return birthDate;
    }
    
    public List<String> getHobbies() {
        // Return unmodifiable view
        return hobbies;
    }
    
    // Instead of setters, create new instances
    public ImmutablePerson withName(String newName) {
        return new ImmutablePerson(newName, this.birthDate, this.hobbies);
    }
    
    public ImmutablePerson withBirthDate(LocalDate newBirthDate) {
        return new ImmutablePerson(this.name, newBirthDate, this.hobbies);
    }
    
    public ImmutablePerson withHobby(String newHobby) {
        List<String> newHobbies = new ArrayList<>(this.hobbies);
        newHobbies.add(newHobby);
        return new ImmutablePerson(this.name, this.birthDate, newHobbies);
    }
}
```

### Item 20: Prefer interfaces to abstract classes

**Explanation:** Interfaces are more flexible than abstract classes because they allow multiple inheritance and can be retrofitted to existing classes.

**Example:**
```java
// Interface defines a contract
public interface Playable {
    void play();
    void pause();
    void stop();
    
    // Default method provides implementation
    default void replay() {
        stop();
        play();
    }
}

// Multiple interfaces can be implemented
public class AudioPlayer implements Playable, Recordable {
    private boolean isPlaying = false;
    private boolean isPaused = false;
    
    @Override
    public void play() {
        isPlaying = true;
        isPaused = false;
        System.out.println("Audio playing");
    }
    
    @Override
    public void pause() {
        if (isPlaying) {
            isPaused = true;
            isPlaying = false;
            System.out.println("Audio paused");
        }
    }
    
    @Override
    public void stop() {
        isPlaying = false;
        isPaused = false;
        System.out.println("Audio stopped");
    }
    
    @Override
    public void startRecording() {
        System.out.println("Recording started");
    }
    
    @Override
    public void stopRecording() {
        System.out.println("Recording stopped");
    }
}

// Another interface
public interface Recordable {
    void startRecording();
    void stopRecording();
}
```

## Generics

### Item 28: Prefer lists to arrays

**Explanation:** Lists are reifiable and provide compile-time type safety that arrays don't.

**Example:**
```java
// Problematic with arrays
Object[] objectArray = new Long[1];
objectArray[0] = "I don't fit in"; // ArrayStoreException at runtime

// Safe with generics
List<Long> longList = new ArrayList<>();
// longList.add("I don't fit in"); // Won't compile - type safety!

// Using generics properly in Java 21
public class Stack<E> {
    private final List<E> elements = new ArrayList<>();
    
    public void push(E element) {
        elements.add(element);
    }
    
    public E pop() {
        if (elements.isEmpty()) {
            throw new EmptyStackException();
        }
        return elements.remove(elements.size() - 1);
    }
    
    public boolean isEmpty() {
        return elements.isEmpty();
    }
    
    // Using Java 21 pattern matching
    public boolean contains(Object o) {
        if (o instanceof E e) { // Pattern matching (conceptual, this specific syntax doesn't work with type parameters)
            return elements.contains(e);
        }
        return false;
    }
}
```

### Item 31: Use bounded wildcards to increase API flexibility

**Explanation:** Use bounded wildcards like `? extends E` (producer) and `? super E` (consumer) to make APIs more flexible.

**Example:**
```java
public class CollectionUtils {
    // Producer uses extends (PECS: Producer-Extends, Consumer-Super)
    public static <T> void copy(List<? extends T> source, List<? super T> dest) {
        for (T item : source) {
            dest.add(item);
        }
    }
    
    // Example with bounded wildcards
    public static <T extends Comparable<? super T>> T max(List<? extends T> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("Empty list");
        }
        
        T result = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            T item = list.get(i);
            if (item.compareTo(result) > 0) {
                result = item;
            }
        }
        return result;
    }
}

// Usage example
public void example() {
    List<Integer> integers = List.of(1, 2, 3);
    List<Number> numbers = new ArrayList<>();
    
    // Works because Integer extends Number
    CollectionUtils.copy(integers, numbers);
    
    // Using max with different types
    List<Integer> ints = List.of(1, 5, 3, 8, 2);
    Integer maxInt = CollectionUtils.max(ints);
}
```

## Enums and Annotations

### Item 34: Use enums instead of int constants

**Explanation:** Enums provide type safety, namespace, and can include methods and fields.

**Example:**
```java
public enum PaymentStatus {
    PENDING("Payment is being processed"),
    COMPLETED("Payment was successful"),
    FAILED("Payment failed"),
    REFUNDED("Payment was refunded");
    
    private final String description;
    
    PaymentStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isTerminal() {
        return this == COMPLETED || this == FAILED || this == REFUNDED;
    }
    
    // Strategy pattern with enum
    public enum Operation {
        PLUS("+") {
            public double apply(double x, double y) { return x + y; }
        },
        MINUS("-") {
            public double apply(double x, double y) { return x - y; }
        },
        TIMES("*") {
            public double apply(double x, double y) { return x * y; }
        },
        DIVIDE("/") {
            public double apply(double x, double y) { return x / y; }
        };
        
        private final String symbol;
        
        Operation(String symbol) {
            this.symbol = symbol;
        }
        
        public abstract double apply(double x, double y);
        
        @Override
        public String toString() {
            return symbol;
        }
    }
}
```

### Item 39: Prefer annotations to naming patterns

**Explanation:** Annotations make code clearer and provide compiler-checked metadata.

**Example:**
```java
// Custom annotation
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
    // No elements needed
}

// More complex annotation with elements
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Timing {
    long timeoutMillis() default 1000;
}

// Using annotations
public class TestClass {
    @Test
    public void testMethod() {
        // Test code
    }
    
    @Test
    @Timing(timeoutMillis = 5000)
    public void longRunningTest() {
        // Test that might take longer
    }
    
    // Without annotation, this won't be recognized as a test
    public void helperMethod() {
        // Not a test
    }
}
```

## Lambdas and Streams

### Item 42: Prefer lambdas to anonymous classes

**Explanation:** Lambdas are more concise and clearer than anonymous classes for functional interfaces.

**Example:**
```java
import java.util.function.*;

public class LambdaExample {
    public static void main(String[] args) {
        // Old way with anonymous class
        Runnable oldWay = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello, world!");
            }
        };
        
        // New way with lambda
        Runnable newWay = () -> System.out.println("Hello, world!");
        
        // Example with parameters
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        
        // Lambda with block
        Function<String, Integer> wordCount = s -> {
            if (s == null || s.isEmpty()) {
                return 0;
            }
            return s.split("\\s+").length;
        };
        
        // Method reference (even cleaner than lambda in some cases)
        Consumer<String> printer = System.out::println;
    }
}
```

### Item 45: Use streams judiciously

**Explanation:** Streams make bulk operations concise but can reduce readability if overused. Use them when they improve clarity.

**Example:**
```java
import java.util.*;
import java.util.stream.*;

public class StreamExample {
    // Example data
    private static final List<String> WORDS = List.of(
        "stream", "lambda", "optional", "functional", "interface",
        "immutable", "collection", "iterate", "mapping"
    );
    
    public static void main(String[] args) {
        // Filtering and collecting
        List<String> longWords = WORDS.stream()
                                     .filter(word -> word.length() > 6)
                                     .collect(Collectors.toList());
        
        // Transforming
        List<String> upperCaseWords = WORDS.stream()
                                         .map(String::toUpperCase)
                                         .collect(Collectors.toList());
        
        // Finding unique characters
        Set<Character> uniqueChars = WORDS.stream()
                                        .flatMap(word -> word.chars().mapToObj(c -> (char) c))
                                        .collect(Collectors.toSet());
        
        // Grouping
        Map<Integer, List<String>> wordsByLength = WORDS.stream()
                                                     .collect(Collectors.groupingBy(String::length));
        
        // Statistics
        IntSummary stats = WORDS.stream()
                              .mapToInt(String::length)
                              .summaryStatistics();
        
        System.out.println("Average word length: " + stats.getAverage());
        System.out.println("Longest word length: " + stats.getMax());
    }
    
    // When streams improve code vs traditional loops
    public static Map<String, Long> wordFrequency(String text) {
        return Arrays.stream(text.split("\\s+"))
                   .map(String::toLowerCase)
                   .collect(Collectors.groupingBy(
                       word -> word.replaceAll("[^a-zA-Z]", ""),
                       Collectors.counting()
                   ));
    }
}
```

## Concurrency

### Item 78: Synchronize access to shared mutable data

**Explanation:** Synchronization is needed to prevent thread interference and memory inconsistency errors.

**Example:**
```java
public class Counter {
    private long count = 0;
    
    // Bad - not thread safe
    public void incrementUnsafe() {
        count++;
    }
    
    // Good - thread safe with synchronization
    public synchronized void incrementSafe() {
        count++;
    }
    
    // Thread safe using AtomicLong
    private final AtomicLong atomicCount = new AtomicLong(0);
    
    public void incrementAtomic() {
        atomicCount.incrementAndGet();
    }
    
    // Thread safe with lock
    private final ReentrantLock lock = new ReentrantLock();
    
    public void incrementWithLock() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }
    
    public synchronized long getCount() {
        return count;
    }
    
    public long getAtomicCount() {
        return atomicCount.get();
    }
}
```

### Item 81: Prefer concurrent utilities to wait and notify

**Explanation:** Modern Java provides higher-level concurrency utilities that are easier to use correctly.

**Example:**
```java
import java.util.concurrent.*;

public class ConcurrencyExample {
    // Using ExecutorService instead of manually creating threads
    public static void executeAsyncTasks() {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        
        try {
            // Submit tasks
            Future<String> future1 = executor.submit(() -> fetchDataFromUrl("https://api.example.com/data1"));
            Future<String> future2 = executor.submit(() -> fetchDataFromUrl("https://api.example.com/data2"));
            
            // Process results when available
            String result1 = future1.get(2, TimeUnit.SECONDS);
            String result2 = future2.get(2, TimeUnit.SECONDS);
            
            System.out.println("Combined results: " + result1 + ", " + result2);
        } catch (TimeoutException e) {
            System.out.println("Operation timed out");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
    
    // Using CountDownLatch for coordination
    public static void coordinateWork() throws InterruptedException {
        int numberOfWorkers = 3;
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(numberOfWorkers);
        
        for (int i = 0; i < numberOfWorkers; i++) {
            int workerId = i;
            new Thread(() -> {
                try {
                    startSignal.await(); // Wait until all workers are ready
                    doWork(workerId);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneSignal.countDown(); // Signal this worker is done
                }
            }).start();
        }
        
        // Let all threads proceed
        startSignal.countDown();
        // Wait for all to finish
        doneSignal.await();
        System.out.println("All workers have completed their tasks");
    }
    
    // Helper methods
    private static String fetchDataFromUrl(String url) {
        // Simulate network request
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Data from " + url;
    }
    
    private static void doWork(int workerId) {
        // Simulate work
        System.out.println("Worker " + workerId + " performing task");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Worker " + workerId + " completed task");
    }
}
```

## Exception Handling

### Item 69: Use exceptions only for exceptional conditions

**Explanation:** Exceptions should be used for exceptional conditions, not for ordinary control flow.

**Example:**
```java
public class ExceptionUsage {
    // Bad - using exceptions for control flow
    public int getPositionBad(List<String> list, String item) {
        try {
            return list.indexOf(item);
        } catch (ArrayIndexOutOfBoundsException e) {
            return -1;
        }
    }
    
    // Good - proper control flow
    public int getPositionGood(List<String> list, String item) {
        return list.indexOf(item);
    }
    
    // Bad - exceptions for expected cases
    public void processFileBad(String path) {
        try {
            // Process the file
            Files.readAllLines(Path.of(path)).forEach(System.out::println);
        } catch (NoSuchFileException e) {
            System.out.println("File not found: " + path);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
    
    // Good - check preconditions
    public void processFileGood(String path) {
        Path filePath = Path.of(path);
        
        if (!Files.exists(filePath)) {
            System.out.println("File not found: " + path);
            return;
        }
        
        try {
            Files.readAllLines(filePath).forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
```

### Item 70: Use checked exceptions for recoverable conditions and runtime exceptions for programming errors

**Explanation:** Use checked exceptions for conditions that the caller can reasonably be expected to recover from. Use runtime exceptions for programming errors.

**Example:**
```java
public class BankTransferService {
    // Checked exception - recoverable condition
    public class InsufficientFundsException extends Exception {
        private final BigDecimal available;
        private final BigDecimal required;
        
        public InsufficientFundsException(BigDecimal available, BigDecimal required) {
            super(String.format("Insufficient funds: %.2f available, %.2f required", 
                               available, required));
            this.available = available;
            this.required = required;
        }
        
        public BigDecimal getAvailable() {
            return available;
        }
        
        public BigDecimal getRequired() {
            return required;
        }
    }
    
    // Using checked exception for recoverable condition
    public void transfer(Account from, Account to, BigDecimal amount) 
                         throws InsufficientFundsException {
        if (from.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(from.getBalance(), amount);
        }
        
        from.debit(amount);
        to.credit(amount);
    }
    
    // Runtime exception for programming error
    public void validateAccount(Account account) {
        if (account == null) {
            throw new NullPointerException("Account cannot be null");
        }
        
        if (!account.isActive()) {
            throw new IllegalArgumentException("Account is not active");
        }
    }
    
    // Main method showing proper exception handling
    public void executeBankTransfer(Account from, Account to, BigDecimal amount) {
        try {
            validateAccount(from);
            validateAccount(to);
            transfer(from, to, amount);
            System.out.println("Transfer successful");
        } catch (InsufficientFundsException e) {
            // Recoverable - offer alternatives
            System.out.println(e.getMessage());
            System.out.println("Would you like to:");
            System.out.println("1. Transfer a smaller amount");
            System.out.println("2. Add funds to your account first");
        } catch (IllegalArgumentException e) {
            // Programming error - log and handle gracefully
            System.err.println("Invalid account: " + e.getMessage());
        }
    }
}
```

## Java 21 Specific Features

### Record Classes (Java 16+)

**Explanation:** Records provide a concise way to define immutable data classes with automatic getters, equals, hashCode, and toString.

**Example:**
```java
// Before records
public final class PersonOld {
    private final String name;
    private final int age;

    public PersonOld(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() { return name; }
    public int getAge() { return age; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonOld person = (PersonOld) o;
        return age == person.age && Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}

// With records (Java 16+)
public record Person(String name, int age) {
    // Compact constructor for validation
    public Person {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
    }
    
    // You can add methods
    public boolean isAdult() {
        return age >= 18;
    }
}

// Usage
Person person = new Person("John", 30);
String name = person.name(); // Accessor method
int age = person.age(); // Accessor method
System.out.println(person); // toString() is generated
```

### Pattern Matching for instanceof (Java 16+)

**Explanation:** Pattern matching for instanceof combines the instanceof check and cast into a single operation.

**Example:**
```java
// Old way
public boolean isLongString(Object obj) {
    if (obj instanceof String) {
        String s = (String) obj;
        return s.length() > 10;
    }
    return false;
}

//
