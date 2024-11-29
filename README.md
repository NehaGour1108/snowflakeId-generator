Implemented Snowflake on API, and
Database as stored procedure
benchmark pagination approaches
Limit Offset
ID Limit

# Snowflake ID Generator and Pagination Benchmark

This project demonstrates the integration of **Snowflake ID Generation**, lightweight database operations using **H2 in-memory database**, and a **benchmark for pagination strategies**. It showcases how to generate distributed unique IDs, test stored procedures in H2, and evaluate database pagination techniques.

---

## Features

1. **Snowflake ID Generator**:
   - Generates globally unique, time-ordered 64-bit IDs.
   - Composed of timestamp, data center ID, worker ID, and sequence number.
   - Ensures no collisions, even in distributed environments.

<img width="861" alt="Screenshot 2024-11-30 at 12 34 02 AM" src="https://github.com/user-attachments/assets/f4ad2169-a567-4760-a2fc-5c8c83f1fd3a">


2. **H2 Database Integration**:
   - Uses H2 in-memory database for lightweight and quick database operations.
   - Implements a stored procedure to generate Snowflake IDs directly via SQL.
     
<img width="852" alt="Screenshot 2024-11-30 at 12 32 02 AM" src="https://github.com/user-attachments/assets/476bcf81-b33d-407f-9ae5-b3be8152ab2d">

3. **Pagination Benchmark**:
   - Compares **Limit-Offset** and **ID-Limit** pagination strategies.
   - Demonstrates the performance impact of each method on large datasets.

<img width="745" alt="Screenshot 2024-11-30 at 12 32 57 AM" src="https://github.com/user-attachments/assets/01685b68-b77b-4c21-b143-c89a2bfd3a60">
---

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── org/example/
│   │       ├── H2DatabaseExample.java        # Demonstrates Snowflake ID generation in H2
│   │       ├── PaginationBenchmark.java      # Benchmarks Limit-Offset vs. ID-Limit pagination
│   │       └── SnowflakeIdGenerator.java     # Implements Snowflake ID generation
```

---

## How It Works

1. **Snowflake ID Generator**:
   - Generates IDs using:
     - **Timestamp** (current time in milliseconds since epoch).
     - **Worker ID** (machine-specific identifier).
     - **Data Center ID** (data center-specific identifier).
     - **Sequence Number** (ensures uniqueness within the same millisecond).
   - Provides a stored procedure for database usage.

2. **H2 Integration**:
   - Creates an in-memory database with tables.
   - Defines a SQL alias for calling the Snowflake ID generator as a stored procedure.

3. **Pagination Benchmark**:
   - Populates a `users` table with 1 million rows.
   - Benchmarks:
     - **Limit-Offset**: Skips rows using `OFFSET` for pagination.
     - **ID-Limit**: Fetches rows using the last seen `id`.

---

## Setup Instructions

### Prerequisites

- Java Development Kit (JDK 8 or later)
- Maven (for build and dependency management)

### Build and Run

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/snowflake-id-generator.git
   cd snowflake-id-generator
   ```

2. Build the project:
   ```bash
   mvn clean package
   ```

3. Run the main classes:

   - **Snowflake ID Example**:
     ```bash
     java -cp target/snowflake-id-generator-1.0-SNAPSHOT.jar org.example.H2DatabaseExample
     ```

   - **Pagination Benchmark**:
     ```bash
     java -cp target/snowflake-id-generator-1.0-SNAPSHOT.jar org.example.PaginationBenchmark
     ```

---

## Outputs

### Snowflake ID Generation
Demonstrates generating a Snowflake ID as a stored procedure in H2.

**Example Output**:
```
Generated Snowflake ID: 143983289839616
```

### Pagination Benchmark
Compares two pagination strategies on a dataset of 1 million rows.

**Example Output**:
```
Limit-Offset Total Time: 600 ms
ID-Limit Total Time: 300 ms
```

---

## Key Learnings

1. **Efficient ID Generation**:
   - Snowflake IDs are highly scalable and globally unique, ideal for distributed systems.

2. **H2 Database as a Testing Tool**:
   - H2’s in-memory mode is perfect for quick prototyping and stored procedure integration.

3. **Pagination Strategies**:
   - **Limit-Offset** is simple but inefficient for large datasets.
   - **ID-Limit** performs better as it avoids row skipping and leverages indexed queries.
