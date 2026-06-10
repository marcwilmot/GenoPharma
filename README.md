# Genopharma

**Genopharma** is a Java/Spring Boot Batch project focused on processing genetic sample data and preparing it for pharmacogenetic analysis.

The project reads genetic information, extracts variants, stores them in PostgreSQL, and builds the foundation for future drug-gene recommendation logic.

---

## What is Genopharma?

Genopharma is a backend-oriented project for learning and demonstrating how a pharmacogenetic data pipeline can be built using Java and Spring technologies.

The main idea is simple:

```

Genetic sample file
|
v
Variant extraction
|
v
PostgreSQL persistence
|
v
Gene-range matching
|
v
Pharmacogenetic analysis

```

The current version focuses on the technical foundation: batch processing, database persistence, variant storage, and integration testing.

---

## Main Features

* Java 17 backend application.
* Spring Boot Batch architecture.
* PostgreSQL persistence layer.
* Variant and sample data model.
* Idempotent insert/update logic for variants.
* SQL scripts organized by domain.
* DAO and service separation.
* Integration tests using Testcontainers.
* Initial support for pharmacogenetic gene ranges.
* Project structure prepared for future recommendation logic.

---

## Technology Stack

| Technology       | Purpose                       |
| ---------------- | ----------------------------- |
| Java 17          | Main language                 |
| Spring Boot      | Application framework         |
| Spring Batch     | Batch processing              |
| Spring Data JDBC | Database access               |
| PostgreSQL       | Main database                 |
| HTSJDK           | Genetic file processing       |
| Testcontainers   | Integration testing           |
| JUnit 5          | Testing                       |
| Maven            | Build tool                    |
| Docker           | Test and local infrastructure |

---

## Project Structure

```

src/main/java/io/whiteking/GenoPharma
|
|-- config       # Application and infrastructure configuration
|-- constant     # Constants and enum-like values
|-- dao          # Database access layer
|-- dto          # Data transfer objects
|-- infra        # Reusable infrastructure helpers
|-- job          # Spring Batch readers, processors, writers and tasklets
|-- service      # Business logic
`-- GenoPharmaApplication.java

```

Resources are organized like this:

```

src/main/resources
|
|-- sql
|   |-- geneRange
|   |-- infra
|   |-- sample
|   `-- variants
| |-- application.properties
`-- log4j2.xml

```

---

## Core Concepts

### Sample

A `sample` represents a genetic file processed by the system.

It contains metadata such as:

* file name;
* file path;
* file hash;
* file size;
* reference genome;
* processing status.

---

### Variant

A `variant` represents a genetic difference found in a sample.

A variant is usually identified by:

```

genome_build + chromosome + position + reference allele + alternative allele

```

Example:

```

GRCh38 | chr1 | 123456 | A | T

```

---

### Sample Variant Call

A `sample_variant_call` links a sample with a variant.

It can store information such as:

* genotype;
* zygosity;
* read depth;
* genotype quality;
* variant quality;
* filter status;
* additional VCF information as JSON.

---

### Gene Range

A `pgx_range` represents a genomic region related to a pharmacogenetic gene.

This allows the system to detect whether a variant is located inside a gene that may be relevant for drug response.

---

## Database Model

Simplified model:

```
sample
|
| 1..N
v
sample_variant_call
^
| N..1
variant

pgx_range
|
`-- pharmacogenetic gene regions

```
---

## Batch Processing Flow

The intended processing flow is:

###

1. Detect genetic sample file
2. Register sample metadata
3. Read genetic variants
4. Normalize variant data
5. Store variants
6. Store sample-variant calls
7. Match variants against pharmacogenetic gene ranges

###

---

## Testing

The project includes integration testing with PostgreSQL using Testcontainers.

This means the database tests run against a real PostgreSQL container instead of an in-memory database.

Current test coverage focuses especially on:

* variant insertion;
* idempotent variant upsert;
* duplicate prevention;
* RSID update behavior;
* batch insertion;
* sample-variant call insertion;
* sample-variant call idempotency.

This is important because batch jobs must be safe to restart without creating duplicated data.

---

## Running the Project

Clone the repository:

```

git clone https://github.com/WhiteKingio/genopharma.git
cd genopharma

```

Build the project:

```
./mvnw clean package

```
Run the application:

```
./mvnw spring-boot:run

```

Run tests:

```

./mvnw test

```

Docker must be running when executing integration tests, because Testcontainers starts a PostgreSQL container.

---

## Current Status

Genopharma already has the main technical foundation in place:

* Spring Boot project configured.
* Spring Batch dependencies included.
* PostgreSQL persistence layer started.
* Core DTOs created.
* DAO layer implemented for variant persistence.
* SQL scripts externalized.
* Integration test infrastructure configured with Testcontainers.
* Basic pharmacogenetic range model included.
* Project packages organized for future batch steps.

The project is still evolving, but the base architecture is already prepared for a real batch-processing pipeline.

---

## Roadmap

### Completed / In Progress

* [x] Create Spring Boot project structure.
* [x] Configure Java 17 and Maven.
* [x] Add Spring Batch foundation.
* [x] Add PostgreSQL support.
* [x] Create sample, variant and sample-variant domain model.
* [x] Externalize SQL scripts by domain.
* [x] Implement DAO layer for variant persistence.
* [x] Add idempotent variant upsert logic.
* [x] Add integration tests with Testcontainers.
* [x] Add initial pharmacogenetic gene range support.
* [~] Build the first complete batch flow.
* [~] Improve sample registration and processing status handling.
* [~] Expand sample-variant call update behavior.

### Next Steps

* [ ] Complete the VCF reading step with HTSJDK.
* [ ] Connect the reader, processor and writer into a complete Spring Batch job.
* [ ] Add example VCF files for local testing.
* [ ] Improve error handling and restart behavior.
* [ ] Add more tests around sample processing.
* [ ] Add a simple demo execution flow.
* [ ] Add Docker Compose for local PostgreSQL.
* [ ] Add GitHub Actions CI.

### Future Ideas

* [ ] Map variants to pharmacogenetic genes.
* [ ] Add allele and diplotype interpretation.
* [ ] Add phenotype calculation.
* [ ] Add basic drug-gene recommendation rules.
* [ ] Generate a structured pharmacogenetic report.

---

## Disclaimer

This project is for educational, academic, and software engineering purposes only.

**Genopharma is not intended for real medical use.**

It is not a certified medical device, clinical decision support system, diagnostic tool, treatment recommendation system, or substitute for professional medical advice.

The information processed or generated by this project must not be used to diagnose, prevent, monitor, treat, prescribe medication, adjust dosage, detect contraindications, evaluate adverse reactions, or make any real clinical decision.

Any pharmacogenetic recommendation system intended for real-world clinical or medical use must be reviewed, validated, and approved by qualified healthcare professionals and must comply with all applicable medical, legal, regulatory, privacy, and safety requirements.

---

## Author

Marc Wilmot

---

## License

MIT LICENSE