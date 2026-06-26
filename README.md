# Supply Chain Management System

> A Java Swing desktop application that simulates a complete supply chain — from raw-material producers to factories, markets, and end customers. Developed as a term project for **CS102 (Object-Oriented Programming)**.

[![Java](https://img.shields.io/badge/Java-Swing-007396?logo=openjdk&logoColor=white)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![Paradigm](https://img.shields.io/badge/Paradigm-OOP-blue)](#object-oriented-design)
[![Architecture](https://img.shields.io/badge/Architecture-MVC-success)](#architecture)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

---

## Overview

The system models four types of business entities that interact within a supply chain, each with its own funds, storage capacity, and inventory:

| Actor | Role |
|-------|------|
| **Raw Material Producer** | Produces raw materials and supplies them to factories |
| **Factory** | Buys raw materials, manufactures products (and byproducts), and sells to markets |
| **Market** | Buys products from factories, sets retail prices, and sells to customers |
| **Customer** | Browses markets and purchases products |

Each transaction validates funds, storage capacity, and resource availability, throwing custom exceptions when a business rule is violated. The whole simulation is driven through a Swing graphical interface.

## Supply Chain Flow

```
Raw Material Producer ──raw materials──▶ Factory ──products──▶ Market ──sales──▶ Customer
                                            │
                                            └──▶ Byproducts
```

## Object-Oriented Design

This project was built to demonstrate the core pillars of object-oriented programming:

- **Inheritance** — `BusinessEntity` and `Item` are base classes; `Factory`, `Market`, `Customer`, `RawMaterialProducer` extend `BusinessEntity`, while `Product`, `ManufacturedProduct`, `RawMaterial`, and `Byproduct` extend `Item`.
- **Abstraction** — `BusinessEntity` and `Item` are `abstract` classes that share common state (name, funds, capacity, inventory / name, quantity) and cannot be instantiated directly.
- **Polymorphism** — entities are handled through their base types and safely down-cast where needed (e.g. `entity instanceof Factory`, `item instanceof RawMaterial`).
- **Interfaces** — the `Producer` interface defines the `produce()` contract implemented by producing entities.
- **Exception Handling** — four custom exceptions enforce business rules:
  `InsufficientFundsException`, `InsufficientResourcesException`, `InsufficientStorageException`, `InvalidInputException`.
- **Encapsulation** — fields are private/protected and exposed through getters/setters and controlled mutators (`increaseFunds`, `addToInventory`, …).

## Architecture

The codebase follows the **Model–View–Controller (MVC)** pattern, with each layer in its own package:

| Layer | Package | Responsibility |
|-------|---------|----------------|
| **Model** | `Main` | Domain entities and business rules (`BusinessEntity`, `Item`, actors, products, exceptions) |
| **View** | `GUI` | Swing frames per actor — list, detail, and form windows |
| **Controller** | `User_Interaction` | "Interplay" classes that parse and validate GUI input, invoke model logic, handle exceptions, and update the view |

```
src/
├── Main/                  # Model — domain entities & business rules
│   ├── BusinessEntity.java        (abstract base for all actors)
│   ├── Item.java                  (abstract base for all inventory items)
│   ├── Customer.java / Factory.java / Market.java / RawMaterialProducer.java
│   ├── Product.java / ManufacturedProduct.java / RawMaterial.java / Byproduct.java
│   ├── Producer.java              (interface)
│   └── *Exception.java            (4 custom exceptions)
├── User_Interaction/      # Controller — GUI ↔ Model bridge
│   ├── CustomerInterplay.java / FactoryInterplay.java
│   └── MarketInterplay.java / RawMaterialInterplay.java
└── GUI/                   # View — Swing user interface
    ├── MainFrame.java             (application entry point)
    ├── Customer/  Factory/  Market/  RawMaterial/
```

## Getting Started

### Prerequisites

- JDK 8 or newer (no external libraries — Swing is part of the JDK)

### Run from an IDE (recommended)

1. Open the project and mark `src` as the sources root.
2. Run the `main` method in [`GUI/MainFrame.java`](src/GUI/MainFrame.java).

### Run from the command line

```bash
# Compile all sources into an "out" directory
javac -d out $(find src -name "*.java")

# Launch the application
java -cp out GUI.MainFrame
```

> On Windows PowerShell, compile with:
> `javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName`

## Usage

Launch the app to open the main menu, then choose an actor type (**Raw Material**, **Factories**, **Markets**, **Customers**). From each window you can create entities, view inventories and details, and perform supply-chain actions — producing raw materials, buying inputs, manufacturing products, setting market prices, and customer shopping.

## Author

**Batu Önlükuş** — [@batuonlukus](https://github.com/batuonlukus)
First-year CS102 term project.

## License

Released under the [MIT License](LICENSE).
