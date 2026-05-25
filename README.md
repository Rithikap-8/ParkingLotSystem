Parking Lot System — Project Documentation
This canvas was generated using AI, which can produce inaccurate or harmful responses. Review for accuracy and safety before using.

Overview

A Spring Boot-based Parking Lot Management System built with Java 21, demonstrating core Object-Oriented Programming principles and classic design patterns.
This system simulates a real-world parking lot that supports:

* Multiple vehicle types (Car, Bike, Truck)
* Multiple parking floors and spots
* Vehicle entry and exit through dedicated gates
* Flexible pricing strategies (Time-Based / Event-Based)
* Multiple payment modes (Cash, Card, UPI)
* Thread-safe spot allocation


Tech Stack

Tool
	Version

Java
	21

Spring Boot
	3.5.14

Build Tool
	Maven

Testing
	JUnit 5

Utility
	Lombok



Project Structure

src/main/java/
├── com/rith/ParkingLotSystem/
│   └── ParkingLotSystemApplication.java   # Spring Boot entry point
│
├── enums/
│   ├── VechicleType.java
│   ├── PaymentMode.java
│   ├── PaymentStatus.java
│   ├── GateType.java
│   └── PricingStrategyType.java
│
├── model/
│   ├── Vechicle.java
│   ├── Car.java / Bike.java / Truck.java
│   ├── ParkingSpot.java
│   ├── ParkingFloor.java
│   ├── Ticket.java
│   ├── Gate.java
│   ├── EntryGate.java
│   └── ExitGate.java
│
├── service/
│   ├── ParkingLot.java
│   └── PaymentProcessor.java
│
├── factory/
│   ├── VechicleFactory.java
│   ├── PricingStrategyFactory.java
│   └── PaymentStrategyFactory.java
│
├── strategy/
│   ├── pricing/
│   │   ├── PricingStrategy.java
│   │   ├── TimeBasedPricing.java
│   │   └── EventBasedPricing.java
│   └── payment/
│       ├── PaymentStrategy.java
│       ├── CashPayment.java
│       ├── CardPayment.java
│       └── UpiPayment.java
│
├── utils/
│   └── DateTimeParser.java
│
└── Main.java                               # Demo runner


Design Patterns

1. Singleton — ParkingLot

Only one ParkingLot instance exists throughout the application. It acts as the central controller, managing floors, tickets, and strategy selection.

2. Strategy — Pricing & Payment

* PricingStrategy interface allows swapping TimeBasedPricing and EventBasedPricing without changing calling code.
* PaymentStrategy interface allows swapping CashPayment, CardPayment, and UpiPayment at runtime.

3. Factory — Object Creation

* VechicleFactory creates the right vehicle subclass from a VechicleType enum.
* PricingStrategyFactory returns the correct pricing implementation.
* PaymentStrategyFactory returns the correct payment implementation.

4. Abstract Base Classes

* Vechicle is the abstract parent of Car, Bike, and Truck.
* Gate is the abstract parent of EntryGate and ExitGate.


File-by-File Explanation

Enums

File
	Purpose

VechicleType.java
	Enum: CAR, BIKE, TRUCK

PaymentMode.java
	Enum: CASH, CARD, UPI

PaymentStatus.java
	Enum: PENDING, SUCCESS, FAILURE

GateType.java
	Enum: ENTRY, EXIT

PricingStrategyType.java
	Enum: TIME_BASED, EVENT_BASED



Models

Vechicle.java — Abstract base class for all vehicles. Holds registrationNumber and VechicleType. Extended by Car, Bike, and Truck.
Car.java / Bike.java / Truck.java — Concrete vehicle types. Each passes its specific VechicleType to the parent constructor.
ParkingSpot.java — Represents a single parking spot on a floor.

* Tracks spotId, vehicleType it supports, and whether it is occupied.
* Uses AtomicBoolean + compareAndSet() for thread-safe spot allocation via tryOccupy().

ParkingFloor.java — Groups multiple ParkingSpot objects on a single floor. Has a findAvailableSpot(VechicleType) method that iterates spots and returns the first free matching spot.
Ticket.java — Generated when a vehicle enters. Stores:

* Unique ticket ID
* Entry timestamp
* Vehicle reference
* Assigned ParkingSpot and ParkingFloor
* PaymentStatus (starts as PENDING)

Gate.java — Abstract base class for gates. Holds gateId and GateType.
EntryGate.java — Handles vehicle arrival. Calls ParkingLot.parkVechicle() to find a spot and returns a Ticket.
ExitGate.java — Handles vehicle departure. Calls ParkingLot.unparkVechicle() with the ticket and payment mode to calculate fees and process payment.

Services

ParkingLot.java (Singleton) — The core controller of the system.

* Holds a list of ParkingFloor objects.
* Maintains a map of active tickets by ticket ID.
* Selects and stores the active PricingStrategy.
* parkVechicle() — scans floors to find an available spot, creates and stores a ticket.
* unparkVechicle() — retrieves ticket, calculates fee via strategy, triggers payment, frees the spot.
* printLotStatus() — prints current spot occupancy across all floors.

PaymentProcessor.java — Handles the payment step during exit. Uses the injected PaymentStrategy to process the fee and updates the ticket's PaymentStatus to SUCCESS or FAILURE.

Factories

VechicleFactory.java

VechicleFactory.createVechicle(VechicleType.CAR, "KA-01-HH-1234");

PricingStrategyFactory.java

PricingStrategyFactory.getStrategy(PricingStrategyType.TIME_BASED);

PaymentStrategyFactory.java

PaymentStrategyFactory.getStrategy(PaymentMode.UPI);


Pricing Strategies

PricingStrategy.java — Interface with one method: calculateFee(Ticket ticket, LocalDateTime exitTime).
TimeBasedPricing.java — Applies dynamic rates based on the hour of exit:

Vehicle
	Peak (8 AM – 5 PM)
	Off-Peak

Car
	₹30/hr
	₹20/hr

Bike
	₹15/hr
	₹10/hr

Truck
	₹50/hr
	₹30/hr


EventBasedPricing.java — Applies flat rates regardless of time:

Vehicle
	Rate

Car
	₹50/hr

Bike
	₹30/hr

Truck
	₹70/hr



Payment Strategies

PaymentStrategy.java — Interface with one method: processPayment(double amount).
CashPayment.java / CardPayment.java / UpiPayment.java — Each simulates payment processing and returns PaymentStatus.SUCCESS. Can be extended to integrate real payment gateways.

Utilities

DateTimeParser.java — Parses date-time strings in the format "dd MMM yyyy h:mm a".
Example: "25 May 2026 7:30 AM" → LocalDateTime

Core Flows

Vehicle Entry

EntryGate.parkVehicle(vehicle)
└── ParkingLot.parkVechicle(vehicle)
    └── ParkingFloor.findAvailableSpot(vehicleType)
        └── ParkingSpot.tryOccupy()          ← thread-safe
            └── new Ticket(vehicle, spot, entryTime)
                └── store ticket in activeTickets map
                    return Ticket

Vehicle Exit

ExitGate.unparkVehicle(ticket, paymentMode, exitTime)
└── ParkingLot.unparkVechicle(ticket, paymentMode, exitTime)
    └── PricingStrategy.calculateFee(ticket, exitTime)
        └── PaymentProcessor.processPayment(fee, paymentMode)
            └── PaymentStrategy.processPayment(fee)
                └── ticket.setPaymentStatus(SUCCESS)
                    └── spot.setOccupied(false)
                        └── remove ticket from activeTickets map


How to Run

Prerequisites

* Java 21+
* Maven 3.8+

Steps

# Clone the repository
git clone <repo-url>
cd ParkingLotSystem

# Build the project
mvn clean install

# Run the demo
mvn exec:java -Dexec.mainClass="Main"

Or run Main.java directly from your IDE.

Expected Output

=== Parking Lot Status ===
Floor 0: Spot 1 (CAR)   - Available
Floor 0: Spot 2 (CAR)   - Available
Floor 0: Spot 3 (BIKE)  - Available
Floor 0: Spot 4 (TRUCK) - Available

Vehicle KA-01-HH-1234 parked. Ticket ID: <uuid>

=== After Parking ===
Floor 0: Spot 1 (CAR) - Occupied
...

Payment of ₹450.0 processed via CASH
Vehicle KA-01-HH-1234 exited. Fee: ₹450.0


Key Concepts Demonstrated

* OOP — Abstraction, Inheritance, Polymorphism, Encapsulation
* SOLID Principles — Single Responsibility, Open/Closed (strategies), Dependency Inversion
* Concurrency Safety — AtomicBoolean for thread-safe spot allocation
* Java 21 — Enhanced switch expressions, modern API usage

