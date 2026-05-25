1 # Parking Lot System
    2 
    3 A Spring Boot-based Parking Lot Management System built with Java 21, demonstrating core Object-Oriented Programming principles and classic design patterns.
    4
    5 ---
    6
    7 ## Table of Contents
    8
    9 - [Overview](#overview)
   10 - [Tech Stack](#tech-stack)
   11 - [Project Structure](#project-structure)
   12 - [Design Patterns](#design-patterns)
   13 - [File-by-File Explanation](#file-by-file-explanation)
   14 - [Core Flows](#core-flows)
   15 - [How to Run](#how-to-run)
   16
   17 ---
   18
   19 ## Overview
   20
   21 This system simulates a real-world parking lot that supports:
   22
   23 - Multiple vehicle types (Car, Bike, Truck)
   24 - Multiple parking floors and spots
   25 - Vehicle entry and exit through dedicated gates
   26 - Flexible pricing strategies (Time-Based / Event-Based)
   27 - Multiple payment modes (Cash, Card, UPI)
   28 - Thread-safe spot allocation
   29
   30 ---
   31
   32 ## Tech Stack
   33
   34 | Tool | Version |
   35 |---|---|
   36 | Java | 21 |
   37 | Spring Boot | 3.5.14 |
   38 | Build Tool | Maven |
   39 | Testing | JUnit 5 |
   40 | Utility | Lombok |
   41
   42 ---
   43
   44 ## Project Structure
   45
   46 ```
   47 src/main/java/
   48 ├── com/rith/ParkingLotSystem/
   49 │   └── ParkingLotSystemApplication.java   # Spring Boot entry point
   50 │
   51 ├── enums/
   52 │   ├── VechicleType.java
   53 │   ├── PaymentMode.java
   54 │   ├── PaymentStatus.java
   55 │   ├── GateType.java
   56 │   └── PricingStrategyType.java
   57 │
   58 ├── model/
   59 │   ├── Vechicle.java
   60 │   ├── Car.java
   61 │   ├── Bike.java
   62 │   ├── Truck.java
   63 │   ├── ParkingSpot.java
   64 │   ├── ParkingFloor.java
   65 │   ├── Ticket.java
   66 │   ├── Gate.java
   67 │   ├── EntryGate.java
   68 │   └── ExitGate.java
   69 │
   70 ├── service/
   71 │   ├── ParkingLot.java
   72 │   └── PaymentProcessor.java
   73 │
   74 ├── factory/
   75 │   ├── VechicleFactory.java
   76 │   ├── PricingStrategyFactory.java
   77 │   └── PaymentStrategyFactory.java
   78 │
   79 ├── strategy/
   80 │   ├── pricing/
   81 │   │   ├── PricingStrategy.java
   82 │   │   ├── TimeBasedPricing.java
   83 │   │   └── EventBasedPricing.java
   84 │   └── payment/
   85 │       ├── PaymentStrategy.java
   86 │       ├── CashPayment.java
   87 │       ├── CardPayment.java
   88 │       └── UpiPayment.java
   89 │
   90 ├── utils/
   91 │   └── DateTimeParser.java
   92 │
   93 └── Main.java                              # Demo runner
   94 ```
   95
   96 ---
   97
   98 ## Design Patterns
   99
  100 ### 1. Singleton — `ParkingLot`
  101 Only one `ParkingLot` instance exists throughout the application. It acts as the central controller, managing floors, tickets, and strategy selection.
  102
  103 ### 2. Strategy — Pricing & Payment
  104 - `PricingStrategy` interface allows swapping `TimeBasedPricing` and `EventBasedPricing` without changing calling code.
  105 - `PaymentStrategy` interface allows swapping `CashPayment`, `CardPayment`, and `UpiPayment` at runtime.
  106
  107 ### 3. Factory — Object Creation
  108 - `VechicleFactory` creates the right vehicle subclass from a `VechicleType` enum.
  109 - `PricingStrategyFactory` returns the correct pricing implementation.
  110 - `PaymentStrategyFactory` returns the correct payment implementation.
  111
  112 ### 4. Abstract Base Classes
  113 - `Vechicle` is the abstract parent of `Car`, `Bike`, and `Truck`.
  114 - `Gate` is the abstract parent of `EntryGate` and `ExitGate`.
  115
  116 ---
  117
  118 ## File-by-File Explanation
  119
  120 ### Enums
  121
  122 | File | Purpose |
  123 |---|---|
  124 | `VechicleType.java` | Enum: `CAR`, `BIKE`, `TRUCK` |
  125 | `PaymentMode.java` | Enum: `CASH`, `CARD`, `UPI` |
  126 | `PaymentStatus.java` | Enum: `PENDING`, `SUCCESS`, `FAILURE` |
  127 | `GateType.java` | Enum: `ENTRY`, `EXIT` |
  128 | `PricingStrategyType.java` | Enum: `TIME_BASED`, `EVENT_BASED` |
  129
  130 ---
  131
  132 ### Models
  133
  134 #### `Vechicle.java`
  135 Abstract base class for all vehicles. Holds `registrationNumber` and `VechicleType`. Extended by `Car`, `Bike`, and `Truck`.
  136
  137 #### `Car.java` / `Bike.java` / `Truck.java`
  138 Concrete vehicle types. Each passes its specific `VechicleType` to the parent constructor.
  139
  140 #### `ParkingSpot.java`
  141 Represents a single parking spot on a floor.
  142 - Tracks `spotId`, `vehicleType` it supports, and whether it is occupied.
  143 - Uses `AtomicBoolean` + `compareAndSet()` for **thread-safe** spot allocation via `tryOccupy()`.
  144
  145 #### `ParkingFloor.java`
  146 Groups multiple `ParkingSpot` objects on a single floor.
  147 - Has a `findAvailableSpot(VechicleType)` method that iterates spots and returns the first free matching spot.
  148
  149 #### `Ticket.java`
  150 Generated when a vehicle enters. Stores:
  151 - Unique ticket ID
  152 - Entry timestamp
  153 - Vehicle reference
  154 - Assigned `ParkingSpot` and `ParkingFloor`
  155 - `PaymentStatus` (starts as `PENDING`)
  156
  157 #### `Gate.java`
  158 Abstract base class for gates. Holds `gateId` and `GateType`.
  159
  160 #### `EntryGate.java`
  161 Handles vehicle arrival. Calls `ParkingLot.parkVechicle()` to find a spot and returns a `Ticket`.
  162
  163 #### `ExitGate.java`
  164 Handles vehicle departure. Calls `ParkingLot.unparkVechicle()` with the ticket and payment mode to calculate fees and process payment.
  165
  166 ---
  167
  168 ### Services
  169
  170 #### `ParkingLot.java` *(Singleton)*
  171 The core controller of the system. Responsibilities:
  172 - Holds a list of `ParkingFloor` objects.
  173 - Maintains a map of active tickets by ticket ID.
  174 - Selects and stores the active `PricingStrategy`.
  175 - `parkVechicle()` — scans floors to find an available spot, creates and stores a ticket.
  176 - `unparkVechicle()` — retrieves ticket, calculates fee via strategy, triggers payment, frees the spot.
  177 - `printLotStatus()` — prints current spot occupancy across all floors.
  178
  179 #### `PaymentProcessor.java`
  180 Handles the payment step during exit. Uses the injected `PaymentStrategy` to process the fee and updates the ticket's `PaymentStatus` to `SUCCESS` or `FAILURE`.
  181
  182 ---
  183
  184 ### Factories
  185
  186 #### `VechicleFactory.java`
  187 ```java
  188 VechicleFactory.createVechicle(VechicleType.CAR, "KA-01-HH-1234");
  189 ```
  190 Returns the correct `Vechicle` subclass instance.
  191
  192 #### `PricingStrategyFactory.java`
  193 ```java
  194 PricingStrategyFactory.getStrategy(PricingStrategyType.TIME_BASED);
  195 ```
  196 Returns either `TimeBasedPricing` or `EventBasedPricing`.
  197
  198 #### `PaymentStrategyFactory.java`
  199 ```java
  200 PaymentStrategyFactory.getStrategy(PaymentMode.UPI);
  201 ```
  202 Returns `CashPayment`, `CardPayment`, or `UpiPayment`.
  203
  204 ---
  205
  206 ### Pricing Strategies
  207
  208 #### `PricingStrategy.java`
  209 Interface with one method: `calculateFee(Ticket ticket, LocalDateTime exitTime)`.
  210
  211 #### `TimeBasedPricing.java`
  212 Applies dynamic rates based on the hour of exit:
  213 - **Peak hours (8 AM – 5 PM):** CAR = ₹30/hr, BIKE = ₹15/hr, TRUCK = ₹50/hr
  214 - **Off-peak:** CAR = ₹20/hr, BIKE = ₹10/hr, TRUCK = ₹30/hr
  215
  216 #### `EventBasedPricing.java`
  217 Applies flat rates regardless of time:
  218 - CAR = ₹50/hr, BIKE = ₹30/hr, TRUCK = ₹70/hr
  219
  220 ---
  221
  222 ### Payment Strategies
  223
  224 #### `PaymentStrategy.java`
  225 Interface with one method: `processPayment(double amount)`.
  226
  227 #### `CashPayment.java` / `CardPayment.java` / `UpiPayment.java`
  228 Each simulates payment processing and returns `PaymentStatus.SUCCESS`. Can be extended to integrate real payment gateways.
  229
  230 ---
  231
  232 ### Utilities
  233
  234 #### `DateTimeParser.java`
  235 Parses date-time strings in the format `"dd MMM yyyy h:mm a"`.
  236
  237 Example: `"25 May 2026 7:30 AM"` → `LocalDateTime`
  238
  239 ---
  240
  241 ### Entry Points
  242
  243 #### `Main.java`
  244 Console-based demo that:
  245 1. Creates a `ParkingLot` with one floor and 4 spots (2 cars, 1 bike, 1 truck).
  246 2. Parks a car entering at `"25 May 2026 7:30 AM"` using `EVENT_BASED` pricing.
  247 3. Exits the car at `"25 May 2026 4:15 PM"` paying via `CASH`.
  248 4. Prints lot status before and after the operations.
  249
  250 #### `ParkingLotSystemApplication.java`
  251 Standard Spring Boot application launcher. Currently not used by the demo — `Main.java` is the active runner.
  252
  253 ---
  254
  255 ## Core Flows
  256
  257 ### Vehicle Entry
  258
  259 ```
  260 EntryGate.parkVehicle(vehicle)
  261     └── ParkingLot.parkVechicle(vehicle)
  262             └── ParkingFloor.findAvailableSpot(vehicleType)
  263                     └── ParkingSpot.tryOccupy()       ← thread-safe
  264             └── new Ticket(vehicle, spot, entryTime)
  265             └── store ticket in activeTickets map
  266         return Ticket
  267 ```
  268
  269 ### Vehicle Exit
  270
  271 ```
  272 ExitGate.unparkVehicle(ticket, paymentMode, exitTime)
  273     └── ParkingLot.unparkVechicle(ticket, paymentMode, exitTime)
  274             └── PricingStrategy.calculateFee(ticket, exitTime)
  275             └── PaymentProcessor.processPayment(fee, paymentMode)
  276                     └── PaymentStrategy.processPayment(fee)
  277                     └── ticket.setPaymentStatus(SUCCESS)
  278             └── spot.setOccupied(false)
  279             └── remove ticket from activeTickets map
  280 ```
  281
  282 ---
  283
  284 ## How to Run
  285
  286 ### Prerequisites
  287 - Java 21+
  288 - Maven 3.8+
  289
  290 ### Steps
  291
  292 ```bash
  293 # Clone the repository
  294 git clone <repo-url>
  295 cd ParkingLotSystem
  296
  297 # Build the project
  298 mvn clean install
  299
  300 # Run the demo
  301 mvn exec:java -Dexec.mainClass="Main"
  302 ```
  303
  304 Or run `Main.java` directly from your IDE.
  305
  306 ### Expected Output
  307
  308 ```
  309 === Parking Lot Status ===
  310 Floor 0: Spot 1 (CAR) - Available
  311 Floor 0: Spot 2 (CAR) - Available
  312 Floor 0: Spot 3 (BIKE) - Available
  313 Floor 0: Spot 4 (TRUCK) - Available
  314
  315 Vehicle KA-01-HH-1234 parked. Ticket ID: <uuid>
  316
  317 === After Parking ===
  318 Floor 0: Spot 1 (CAR) - Occupied
  319 ...
  320
  321 Payment of ₹450.0 processed via CASH
  322 Vehicle KA-01-HH-1234 exited. Fee: ₹450.0
  323 ```
  324
  325 ---
  324
  325 ---
  326
  327 ## Key Concepts Demonstrated
  328 
  329 - OOP: Abstraction, Inheritance, Polymorphism, Encapsulation
  330 - SOLID Principles: Single Responsibility, Open/Closed (strategies), Dependency Inversion
  331 - Concurrency Safety: `AtomicBoolean` for spot allocation
  332 - Java 21: Enhanced switch expressions, modern API usage
