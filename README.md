# Algorithmic Comparison: Pattern Searching

A comparative study of substring search algorithms implemented in Java.
This project analyzes the time complexity and operational efficiency of four distinct approaches to finding pattern occurrences within large texts.

## 🎯 Objectives
* Implement standard string-searching algorithms from scratch.
* Benchmark performance (execution time & operation count) on various datasets.
* Analyze behavior on **random texts** vs. **repetitive texts** (worst-case scenarios).

## 🧮 Implemented Algorithms

### 1. Naive Algorithm (Brute Force)
* **Concept:** Compares the pattern character by character at every possible position.
* **Complexity:** $O(n \times m)$ in the worst case.
* **Use case:** Baseline for performance comparison.

### 2. Knuth-Morris-Pratt (KMP)
* **Concept:** Preprocesses the pattern to build a "Partial Match Table" (prefix function).
* **Optimization:** Skips unnecessary comparisons by utilizing known matching prefixes, avoiding backtracking in the text.
* **Complexity:** Linear time $O(n + m)$.

### 3. Rabin-Karp
* **Concept:** Uses a **Rolling Hash** function (based on ASCII values) to compare the hash of the pattern with substrings of the text.
* **Optimization:** Rapidly filters out non-matching positions using hash arithmetic.
* **Feature:** Implemented with a base-101 rolling hash formula.

### 4. Boyer-Moore
* **Concept:** Scans the pattern from **right to left**.
* **Optimization:** Uses two heuristics to skip sections of text:
  * **Bad Character Rule:** Shifts alignment based on mismatched characters.
  * **Good Suffix Rule:** Shifts based on matching suffixes found elsewhere in the pattern.
* **Performance:** Often sub-linear in practice (fastest for standard text).

## Technical Stack
* **Language:** Java 8+
* **Data Structures:** `ArrayList<Character>` for text storage, `ArrayList<Integer>` for result indices.
* **Metrics:** Automated counting of elementary operations (`cpt`) and nanosecond-precision timing.

## Benchmarking Strategy
* The project includes a dedicated efficiency test suite that:
  * Generates large datasets (Random text and Repetitive patterns like AAAA...).
  * Measures execution time.
  * Counts fundamental operations (comparisons/loops).
  * Outputs data for complexity graph plotting.
