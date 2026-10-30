# Smart Electricity Waste Detection System

A Java-based, Spring Boot web application that simulates whole-house electricity consumption, estimates electrical load, monitors appliance usage patterns, and identifies possible unnecessary electricity consumption through rule-based detection.

The system allows users to configure household appliances, calculate connected and current load, estimate daily energy consumption, and observe simulated electrical readings through an interactive dashboard.

> **Note:** This project is a software-based simulation. It does not connect to physical electricity meters, IoT devices, or smart sensors.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Objectives](#objectives)
- [Key Features](#key-features)
- [Technology Stack](#technology-stack)
- [System Architecture](#system-architecture)
- [How the System Works](#how-the-system-works)
- [Electrical Calculations](#electrical-calculations)
- [Simulation and Waste Detection](#simulation-and-waste-detection)
- [Project Structure](#project-structure)
- [Installation and Setup](#installation-and-setup)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Data Storage](#data-storage)
- [Current Limitations](#current-limitations)
- [Future Enhancements](#future-enhancements)
- [Learning Outcomes](#learning-outcomes)

---

## Project Overview

Electricity consumption in households is distributed across multiple appliances, each having different power ratings, operating durations, and usage requirements.

The **Smart Electricity Waste Detection System** provides a centralized platform to configure and analyze these appliance groups. Instead of relying on physical hardware, the application uses user-provided appliance information and software-generated readings to simulate household electricity consumption.

The application calculates the total connected load, estimates the current whole-house load, predicts daily energy consumption, and identifies potential unnecessary appliance usage.

It uses Java and Spring Boot for backend processing, HTML, CSS, and JavaScript for the frontend, and REST APIs for communication between the two layers.

## Objectives

The primary objectives of this project are:

1. To provide a centralized interface for configuring household electrical appliances.
2. To calculate the total connected electrical load of a household.
3. To estimate the current load based on the number of appliance units switched ON.
4. To estimate daily energy consumption using appliance power ratings and operating hours.
5. To simulate changing electrical readings through scheduled software processes.
6. To identify possible electricity wastage using predefined detection rules.
7. To present household electricity information through an interactive dashboard.
8. To demonstrate the practical application of Java, Spring Boot, REST APIs, and frontend technologies.

## Key Features

### 1. Dynamic Appliance Configuration

- Add or remove appliance groups dynamically.
- Configure custom appliance names such as fans, air conditioners, geysers, refrigerators, computers, lights, washing machines, and induction cooktops.
- Enter the quantity of each appliance.
- Specify rated power consumption in watts per unit.
- Enter the number of units currently switched ON.
- Specify the number of units expected to be needed.
- Configure estimated daily operating hours.
- Create separate entries for the same appliance type when their power ratings differ.

### 2. Whole-House Load Estimation

The application calculates:

- **Total Connected Load:** Combined rated power of all configured appliance units.
- **Current Load:** Estimated power demand based on the number of units currently ON.
- **Estimated Daily Energy:** Expected daily energy consumption based on configured usage hours.

### 3. Software-Based Reading Simulation

The backend periodically generates simulated appliance readings to represent changing electricity consumption.

The simulation includes:

- Simulated power consumption.
- Estimated current based on nominal voltage.
- Reading timestamps.
- Appliance-level status information.
- Rule-based usage alerts.

### 4. Electricity Waste Detection

The application compares the configured number of active appliance units with the number of units expected to be needed.

It can flag conditions such as:

- More appliance units switched ON than required.
- Simulated power consumption exceeding the configured threshold.

### 5. Interactive Dashboard

The dashboard presents:

- Total connected load.
- Current simulated whole-house load.
- Estimated daily energy consumption.
- Appliance-wise simulated readings.
- Usage status and possible waste alerts.
- Continuously refreshed dashboard information.

---

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 25 | Backend programming |
| Spring Boot 3.5.6 | Backend application framework |
| Spring Web | REST API development |
| Apache Maven | Dependency and build management |
| HTML5 | Frontend structure |
| CSS3 | User interface styling |
| JavaScript | Dynamic tables and dashboard interactions |
| JSON | Frontend-backend data exchange |
| Spring Boot Actuator | Application health monitoring |
| VS Code | Development environment |

---

## System Architecture

The application follows a layered client-server architecture.

```text
             USER
               |
               v
     FRONTEND (HTML/CSS/JS)
               |
               v
       REST API COMMUNICATION
               |
               v
     SPRING BOOT CONTROLLER
               |
               v
       ELECTRICITY SERVICE
               |
       +-------+--------+
       |                |
       v                v
  LOAD AND ENERGY   READING SIMULATOR
  CALCULATIONS           |
       |                 v
       |          WASTE DETECTION
       |                 |
       +--------+--------+
                |
                v
         DASHBOARD DATA
                |
                v
        INTERACTIVE UI
```

### Architecture Components

**Frontend Layer**

Responsible for collecting appliance information, dynamically generating table rows, validating user inputs, and displaying dashboard information.

**Controller Layer**

Receives HTTP requests from the frontend and forwards the required operations to the service layer.

**Service Layer**

Contains the main application logic, including appliance configuration, electrical calculations, reading simulation, and waste detection.

**Simulation Layer**

Generates software-based readings periodically to represent changing appliance power consumption.

**Dashboard Layer**

Displays the calculated values, simulated readings, and detection results.

---

## How the System Works

The application follows the workflow below:

1. The user opens the web application in a browser.
2. The user adds appliance groups through the configuration section.
3. JavaScript dynamically generates the required table rows.
4. The user enters appliance quantities, rated watts, active units, expected units, and daily operating hours.
5. JavaScript validates the entered configuration.
6. The configuration is converted into JSON and submitted to the backend using a REST API request.
7. The Spring Boot controller receives the request and forwards it to the `ElectricityService`.
8. The service validates and stores the configuration in memory.
9. The application calculates connected load, configured current load, and estimated daily energy consumption.
10. A scheduled process generates simulated readings at regular intervals.
11. The detection logic checks for possible unnecessary usage and unusually high simulated power.
12. The frontend periodically retrieves updated dashboard information and displays the results.

### Dynamic Table Generation

The appliance configuration table is generated dynamically using JavaScript DOM manipulation.

The frontend uses functions such as:

- `createRow()` – Creates the HTML elements required for an appliance row.
- `addRow()` – Adds a newly created row to the table.
- `readConfig()` – Reads appliance information from all available rows.
- `validateConfig()` – Checks the entered values before submission.
- `refreshDashboard()` – Retrieves and updates dashboard information.

JavaScript DOM methods such as `document.createElement()` and `appendChild()` are used to create and insert elements dynamically.

This approach allows users to configure different numbers of appliance groups without depending on a predefined fixed table size.

---

## Electrical Calculations

The system uses standard electrical and energy estimation formulas.

### 1. Total Connected Load

Connected load represents the combined rated power of all configured appliance units.

\[
P_{\text{connected}}=\sum_{i=1}^{n}(q_i \times P_i)
\]

Where:

- \(q_i\) = Quantity of appliance units.
- \(P_i\) = Rated power per unit in watts.
- \(n\) = Number of configured appliance groups.

### 2. Current Whole-House Load

The configured current load represents the estimated rated power demand of the appliance units marked as currently ON.

\[
P_{\text{current}}=\sum_{i=1}^{n}(o_i \times P_i)
\]

Where:

- \(o_i\) = Number of appliance units currently ON.
- \(P_i\) = Rated power per unit.

### 3. Estimated Daily Energy Consumption

Daily energy consumption is estimated using the number of active units, their rated power, and configured operating hours.

\[
E_{\text{daily}}=
\sum_{i=1}^{n}
\frac{o_i \times P_i \times h_i}{1000}
\]

Where:

- \(o_i\) = Number of units currently ON.
- \(P_i\) = Rated power in watts.
- \(h_i\) = Estimated operating hours per day.

The result is expressed in kilowatt-hours (kWh), commonly referred to as electricity units.

### Example Calculation

Consider the following household configuration:

| Appliance | Quantity | Units ON | Power per Unit |
|---|---:|---:|---:|
| Air Conditioner | 2 | 1 | 1500 W |
| Geyser | 1 | 1 | 2000 W |
| Computer | 2 | 1 | 200 W |

**Total Connected Load:**

\[
(2 \times 1500)+(1 \times 2000)+(2 \times 200)
=5400W
\]

**Configured Current Load:**

\[
(1 \times 1500)+(1 \times 2000)+(1 \times 200)
=3700W
\]

If the AC operates for 6 hours, the geyser for 1 hour, and the computer for 5 hours:

\[
E_{\text{daily}}=
\frac{(1\times1500\times6)+(1\times2000\times1)+(1\times200\times5)}{1000}
\]

\[
E_{\text{daily}}=12\text{ kWh}
\]

These calculations are estimates based on user-configured values and rated power.

---

## Simulation and Waste Detection

### Scheduled Reading Simulation

The application uses Spring's scheduling mechanism to generate simulated readings periodically.

The service uses:

```java
@Scheduled(fixedRate = 3000)
```

This schedules the simulation method to execute approximately every three seconds.

The simulation process:

1. Retrieves the saved appliance configuration.
2. Determines the nominal power of the active appliance units.
3. Applies controlled random variations to simulate changing power consumption.
4. Estimates current using the nominal voltage.
5. Generates a timestamped reading.
6. Evaluates the reading against the configured detection rules.
7. Stores the generated reading for dashboard retrieval.

### Current Estimation

The application uses the relationship:

\[
I=\frac{P}{V}
\]

Where:

- \(I\) = Estimated current in amperes.
- \(P\) = Simulated power in watts.
- \(V\) = Nominal voltage.

The simulation uses 230 V as its nominal voltage.

### Waste Detection Rules

The system uses predefined rule-based conditions rather than machine learning.

| Status | Detection Condition |
|---|---|
| NORMAL | No configured waste or high-usage condition is detected. |
| WASTE | The number of units currently ON exceeds the number of units expected to be needed. |
| HIGH_USAGE | Simulated power exceeds 115% of the nominal appliance-group power. |

These rules provide a basic mechanism for identifying potential unnecessary electricity consumption.

**Important:** The alerts represent software-detected conditions. They should not be interpreted as verified electrical faults or actual meter-based measurements.

---

## Project Structure

The main application follows a standard Maven and Spring Boot directory structure.

```text
smart-electricity-waste-detection/
│
├── pom.xml
│
├── README.md
│
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── example/
        │           └── electricwaste/
        │               │
        │               ├── ElectricityWasteApplication.java
        │               ├── ElectricityController.java
        │               ├── ElectricityService.java
        │               ├── ApplianceConfig.java
        │               └── Reading.java
        │
        └── resources/
            └── static/
                └── index.html
```

### Important Backend Files

| File | Responsibility |
|---|---|
| `ElectricityWasteApplication.java` | Application entry point and scheduling configuration |
| `ElectricityController.java` | REST API endpoints and request handling |
| `ElectricityService.java` | Business logic, calculations, simulation, and detection |
| `ApplianceConfig.java` | Appliance configuration data model |
| `Reading.java` | Simulated electrical reading data model |
| `index.html` | Frontend interface, styling, and JavaScript functionality |

---

## Installation and Setup

### Prerequisites

Ensure that the following software is installed:

- JDK 25 or later.
- Apache Maven 3.x.
- Visual Studio Code or another Java-compatible IDE.
- A modern web browser.

### Step 1: Clone the Repository

```bash
git clone <repository-url>
```

### Step 2: Open the Project

```bash
cd smart-electricity-waste-detection
```

Open the project folder in Visual Studio Code or your preferred Java IDE.

### Step 3: Build the Application

Run the following command:

```bash
mvn clean package
```

If Maven is not configured in the system PATH, use the full path to `mvn.cmd` on Windows.

### Step 4: Start the Spring Boot Application

```bash
mvn spring-boot:run
```

### Step 5: Open the Application

Navigate to:

```text
http://localhost:8080
```

Add or edit appliance groups under **Configure Your Home**, then select **Save Home Configuration**.

The **Load Example Setup** option can be used to populate the configuration with editable sample values.

---

## API Documentation

The application exposes REST endpoints for appliance configuration, dashboard information, simulated readings, and health monitoring.

| HTTP Method | Endpoint | Description |
|---|---|---|
| GET | `/api/appliances` | Retrieves saved appliance groups |
| PUT | `/api/appliances` | Replaces the existing appliance configuration |
| GET | `/api/summary` | Retrieves whole-house summary calculations |
| GET | `/api/readings` | Retrieves recent simulated readings |
| POST | `/api/simulator/tick` | Generates another simulation cycle |
| GET | `/actuator/health` | Checks application health |

### Example: Appliance Configuration Request

**Endpoint:**

```http
PUT /api/appliances
```

**Request Body:**

```json
[
  {
    "name": "Air Conditioner",
    "quantity": 2,
    "powerWatts": 1500,
    "unitsOn": 1,
    "expectedUnits": 1,
    "dailyHours": 6
  },
  {
    "name": "Geyser",
    "quantity": 1,
    "powerWatts": 2000,
    "unitsOn": 1,
    "expectedUnits": 0,
    "dailyHours": 1
  }
]
```

The backend validates the configuration and updates the saved appliance groups.

---

## Data Storage

The current version uses in-memory Java collections to maintain appliance configurations and simulated readings.

- Appliance configurations are stored in application memory.
- Simulated readings are maintained in memory.
- Reading history is limited to 500 entries.
- The readings endpoint returns up to 50 recent entries.
- Data is reset when the application is restarted.

**Database integration is not implemented in the current version.** JDBC, MySQL, Spring Data JPA, and Hibernate are not currently used.

---

## Current Limitations

- All electrical readings are software-generated.
- The application does not communicate with physical meters or IoT hardware.
- Current load is an estimate based on user-configured appliance information.
- Daily energy consumption depends on the configured usage hours and rated power.
- Waste detection is based on predefined rules.
- Appliance configurations and readings are not permanently stored.
- The simulation does not model detailed electrical characteristics such as power factor, voltage fluctuations, or phase imbalance.

---

## Future Enhancements

The project can be extended with the following features:

1. **Database Integration:** Introduce MySQL with JDBC or Spring Data JPA for persistent storage.
2. **Historical Consumption Analysis:** Store readings and analyze electricity usage over longer periods.
3. **Advanced Analytics:** Add statistical analysis and machine-learning-based anomaly detection.
4. **Consumption Visualization:** Introduce charts for daily, weekly, and monthly energy trends.
5. **Electricity Bill Estimation:** Estimate electricity costs using configurable tariff slabs.
6. **User Authentication:** Support multiple households and individual user accounts.
7. **IoT Integration:** Connect compatible smart meters and sensors for actual electrical readings.
8. **Custom Alert Thresholds:** Allow users to configure detection thresholds and usage limits.
9. **Appliance Scheduling:** Provide recommendations for optimizing appliance operating hours.

---

## Learning Outcomes

This project demonstrates practical implementation of:

- Object-oriented programming using Java.
- Backend development using Spring Boot.
- RESTful API design and request handling.
- Maven-based project management.
- Dynamic frontend development using JavaScript DOM manipulation.
- JSON-based frontend-backend communication.
- Electrical load and energy estimation.
- Scheduled task execution in Spring Boot.
- Rule-based condition evaluation.
- Exception handling and input validation.
- Software simulation and dashboard development.

---

## Project Scope

The **Smart Electricity Waste Detection System** is designed as a software simulation and estimation platform for household electricity usage.

Its primary purpose is to demonstrate how appliance configuration, electrical calculations, scheduled simulation, and rule-based detection can be combined into a centralized household energy monitoring application.

The current implementation establishes the foundation for future development involving persistent databases, historical analytics, and real-time hardware integration.

---

**Developed using Java, Spring Boot, Maven, HTML, CSS, and JavaScript.**