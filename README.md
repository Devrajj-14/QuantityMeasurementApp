
---

## Use Cases Summary (UC1 → UC9)

### UC1: Feet Measurement Equality
**Goal:** Compare two *Feet* measurements for equality.  
**What you learn:** `equals()` basics, value-based equality, null/type safety.  
**Output example:** `1.0 ft == 1.0 ft -> true`

---

### UC2: Feet and Inches Measurement Equality
**Goal:** Add a separate *Inches* equality check (still not converting feet↔inch yet, just separate comparisons).  
**What you learn:** extending tests, better method separation, avoiding main() dependency.

---

### UC3: Generic QuantityLength Class (DRY Principle)
**Goal:** Remove duplication in Feet/Inches classes by creating:
- `LengthUnit` enum (units + factors)
- `QuantityLength` class (value + unit)
  **What you learn:** DRY, abstraction, encapsulation, scalable design.  
  **Key feature:** Cross-unit equality becomes possible (ex: `1 ft == 12 in`).

---

### UC4: Extended Unit Support (Yards + Centimeters)
**Goal:** Add more length units:
- `YARDS` (1 yard = 3 feet)
- `CENTIMETERS` (1 cm = 0.393701 inch)
  **What you learn:** scaling design by only changing enum, strong conversion correctness.  
  **Key feature:** any-to-any length equality works.

---

### UC5: Unit-to-Unit Conversion (Length)
**Goal:** Provide explicit conversion API:
- `convert(value, fromUnit, toUnit)` OR `QuantityLength.convertTo(targetUnit)`
  **What you learn:** API design, base-unit normalization, precision handling.  
  **Example:** `convert(1 ft → inches) = 12`

---

### UC6: Addition of Two Length Units (Same Category)
**Goal:** Add two lengths (possibly different units) and return result in **first operand unit**.  
**Example:** `1 ft + 12 in = 2 ft`  
**What you learn:** arithmetic on value objects, immutability, conversion reuse.

---

### UC7: Addition with Target Unit Specification
**Goal:** Add two lengths but return result in **explicit target unit**.  
**Example:** `1 ft + 12 in` in `YARDS` → `~0.667 yards`  
**What you learn:** method overloading, flexible API, consistent conversion.

---

### UC8: Refactor Unit Enum to Standalone (SRP)
**Goal:** Extract `LengthUnit` out as its own file and make it responsible for conversions:
- `convertToBaseUnit()`
- `convertFromBaseUnit()`
  **What you learn:** SRP, reduced coupling, scalable architecture for multiple categories.  
  **Result:** QuantityLength becomes cleaner and delegates conversion to the unit.

---

### UC9: Weight Measurement Equality, Conversion, and Addition
**Goal:** Add **Weight category** (separate from length) using the same UC8 pattern:
- `WeightUnit` enum (KILOGRAM base, GRAM, POUND)
- `QuantityWeight` class (equality, conversion, add)
  **What you learn:** multi-category scaling, type safety (length != weight), reusable architecture.

Weight units supported:
- `KILOGRAM` (base)
- `GRAM` (1 g = 0.001 kg)
- `POUND` (1 lb ≈ 0.453592 kg)

---

## Key Design Rules
- **Base unit normalization**:
    - Length base: **FEET**
    - Weight base: **KILOGRAM**
- **Immutability**: conversion/addition return **new objects**
- **Type safety**: Length and Weight are **not comparable**
- **Enum responsibility**: units handle conversion (UC8 pattern)

---

## How to Run Tests
### Using Maven
```bash
mvn test