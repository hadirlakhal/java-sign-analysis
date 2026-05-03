# Interprocedural Sign Analysis

A static analysis tool for Java bytecode that performs sign analysis to detect potential runtime errors such as division by zero and invalid array access.

## Overview

This project implements an **interprocedural sign analysis** framework for Java programs. It analyzes the signs of variables (positive, negative, or zero) across method boundaries to identify potential bugs and runtime errors before execution.

### Key Features

- **Sign Analysis**: Tracks the sign values (positive, negative, zero) of variables throughout the program
- **Interprocedural Analysis**: Analyzes method calls and propagates information across method boundaries
- **Error Detection**: Identifies potential runtime errors including:
  - Division by zero
  - Negative array access
  - Other sign-related violations
- **Lattice-based Framework**: Uses a complete lattice structure for sign analysis
- **Bytecode Analysis**: Operates on compiled Java bytecode using ASM

## Project Structure

```
├── src/
│   └── de/uni_passau/fim/se2/sa/
│       ├── examples/              # Example code for analysis
│       └── sign/
│           ├── SignAnalysis.java           # Core analysis interface
│           ├── SignAnalysisImpl.java        # Analysis implementation
│           ├── SignAnalysisMain.java       # CLI entry point
│           ├── AnalysisResult.java         # Analysis result representation
│           ├── interpretation/
│           │   ├── SignInterpreter.java    # Bytecode interpretation logic
│           │   ├── SignTransferRelation.java
│           │   ├── TransferRelation.java
│           │   └── SignValue.java          # Sign value representation
│           └── lattice/
│               ├── SignLattice.java        # Sign lattice implementation
│               └── Lattice.java            # Base lattice interface
├── test/                          # Unit tests
├── expected-results/              # Expected analysis results
├── pom.xml                        # Maven configuration
└── README.md                      # This file
```

## Technology Stack

- **Language**: Java 21
- **Build Tool**: Maven
- **Key Dependencies**:
  - ASM 9.8 - Java bytecode manipulation and analysis
  - Google Guava 33.3.1 - Utilities and data structures
  - JGraphT 1.5.2 - Graph algorithms
  - PicoCLI 4.7.7 - Command-line interface
- **Testing**: JUnit 5, AssertJ, Mockito

## Building the Project

### Prerequisites

- Java 21 or later
- Maven 3.6+

### Build Command

```bash
mvn clean package
```

### Running Tests

```bash
mvn test
```

## Sign Lattice

The analysis uses a complete lattice of sign values:

- **P** (Positive): Values > 0
- **N** (Negative): Values < 0
- **Z** (Zero): Value = 0
- **⊤** (Top): Unknown value (any sign is possible)
- **⊥** (Bottom): No value (unreachable code)

## Testing

Expected results for various test cases are located in the `expected-results/` directory:
- `public-functional-add.txt`
- `public-functional-div.txt`
- `public-functional-divZeroCall.txt`
- `public-functional-loop0.txt`
- And more...

Run tests with:

```bash
mvn test
```

## Note

This project is part of the Software Engineering II course at University of Passau.

## References

- ASM Documentation: https://asm.ow2.io/
- Static Program Analysis Techniques
- Lattice Theory for Program Analysis
