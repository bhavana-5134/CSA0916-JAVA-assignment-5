# Part A – Design & Pseudocode

## A1 – File format and operations

**Delimited text layout:** `passId,registerNo,name,routeNo,boardingStop,validityDate,amount`. UTF-8; one pass per line. Escape commas/newlines in fields or use a proper CSV library when such values are permitted.

**Binary DataOutputStream layout:** write fields in fixed order: `writeUTF(passId)`, `writeUTF(registerNo)`, `writeUTF(name)`, `writeUTF(routeNo)`, `writeUTF(boardingStop)`, `writeUTF(validityDate)`, `writeDouble(amount)`. Add a format version and record count/header in a production format.

**ADD PASS**
1. Validate every field and amount.
2. Open file in append mode using UTF-8.
3. Encode one complete record and append newline.
4. Flush and close; report success only after close succeeds.

**FIND PASS BY ID**
1. If file does not exist, report missing file.
2. Read one line at a time.
3. Split with the expected delimiter while preserving empty fields.
4. If field count or field types are invalid, record/skip the damaged line.
5. If ID matches, return the parsed pass; otherwise continue.
6. Return NOT FOUND if no valid match exists.

**LIST PASSES ON ROUTE**
1. Read each line.
2. Validate and parse it.
3. Add valid records whose route matches.
4. Skip and report damaged lines.

Damaged lines should be quarantined/logged with line numbers. Missing files should be handled explicitly (create only for add; report for find/list). To reduce partial writes, write a complete new file to a temporary path, flush it, then atomically replace the original where supported. A simple append can still be truncated by power loss.

**Comparison:** delimited text is human-readable and easy to inspect but parsing is required and records have variable length. Fixed-length `RandomAccessFile` records support direct offset access (`recordIndex × recordSize`) but waste padding space and require strict field-size limits. Text scanning is O(n); indexed/fixed-offset lookup can be O(1) when the record position is known.

## A2 – Buffered I/O

A 50 MiB file read one byte at a time requests approximately 52,428,800 byte reads at the Java stream API level. Actual OS calls may be fewer due to runtime and OS buffering, but the repeated method-call overhead is still high. `BufferedInputStream` reads larger blocks and serves many individual reads from memory.

```text
OPEN file using BufferedReader
SET lineCount = 0
WHILE readLine() is not EOF
    lineCount = lineCount + 1
CLOSE reader
PRINT lineCount
```

Buffering may add little benefit when the source is already an in-memory byte array, or when each read is already a large block. `Files.readAllLines()` is convenient but stores all lines in memory (O(file size)); streaming buffered reading uses O(buffer size) additional memory.

## A3 – DAO pattern

StudentDB, CourseDB and MarksDB repeat connection acquisition, parameter binding, execution, result mapping, exception handling and resource cleanup. Their SQL, table/column names, keys and object mapping differ.

```java
interface Dao<T, K> {
  T insert(T value) throws SQLException;
  Optional<T> findById(K id) throws SQLException;
  List<T> findAll() throws SQLException;
  boolean update(T value) throws SQLException;
  boolean delete(K id) throws SQLException;
}
```

Student mapping: `Student.studentId → student.student_id` (primary key), `name → student_name`, `email → email`. A generic DAO centralizes common persistence behaviour and improves testability; SQL embedded in GUI/service classes tightly couples UI/business logic to storage and duplicates code.

## A4 – Serialization

Mark `password` transient because credentials should not be written to a serialized snapshot; preferably do not keep plaintext passwords at all. Mark live `Connection` transient because it is a runtime resource, not serializable state, and must be re-created. `Room` and `Resident` are serialized recursively as part of the Hostel object graph if they implement `Serializable`. Cycles are tracked by Java serialization handles.

Adding a field changes the class schema. With a stable `serialVersionUID`, an older stream can often be read and the new field receives its default value (`null`, `0`, or `false`); custom migration/defaulting may be needed. `serialVersionUID` detects incompatible class versions. Java serialization is Java-specific and can be unsafe for untrusted input; CSV is simple and interoperable but weak for nested objects; JSON is readable and widely supported.

## A5 – Transactions and SQL injection

```text
GET connection
SET autoCommit false
TRY
  UPDATE wallet SET balance = balance - 500
    WHERE wallet_id = 1 AND balance >= 500
  CHECK exactly one row updated
  UPDATE wallet SET balance = balance + 500 WHERE wallet_id = 2
  CHECK exactly one row updated
  COMMIT
CATCH error
  ROLLBACK
FINALLY restore autoCommit and close connection
```

Unsafe string concatenation can let input such as `' OR '1'='1` alter the WHERE condition and make it true. `PreparedStatement` separates SQL structure from values, so the same text is treated as data. One transaction makes both wallet updates atomic; two auto-commit updates can leave money deducted without being credited if the second update fails.
