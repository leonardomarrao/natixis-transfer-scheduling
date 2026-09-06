# Transfer Scheduling API

This project was developed for the Natixis Back-End Java Developer technical challenge.

## Requirements

- Java 21
- Maven

## How to run the application

Clone the repository:

```bash
git clone <REPOSITORY_URL>
```

Go to the project folder:

```bash
cd natixis-transfer-scheduling
```

Run the application:

```bash
mvn spring-boot:run
```

The application will be available at:

```text
http://localhost:8080
```

## H2 Database

The project uses an in-memory H2 database.

H2 Console:

```text
http://localhost:8080/h2-console
```

Connection settings:

```text
JDBC URL: jdbc:h2:mem:bankdb
Username: sa
Password:
```

Some initial data is inserted automatically through `data.sql`.

## How to test the API

A Postman collection is included in the repository.

Import the collection into Postman and make sure the `baseUrl` variable is:

```text
http://localhost:8080
```

The base endpoint for transfers is:

```text
http://localhost:8080/api/transfers
```

### 1. Create a transfer

```http
POST /api/transfers
```

Example:

```text
http://localhost:8080/api/transfers
```

In Postman, select **Body > raw > JSON** and send:

```json
{
  "sourceAccount": "123456",
  "destinationAccount": "654321",
  "amount": 500.00,
  "transferDate": "YYYY-MM-DD"
}
```

For this example, `transferDate` should be the current date because a transfer of €500 uses Fee A, which is only valid for same-day transfers.

Example:

```json
{
  "sourceAccount": "123456",
  "destinationAccount": "654321",
  "amount": 500.00,
  "transferDate": "2026-09-06"
}
```

Expected status:

```text
201 Created
```

Example response:

```json
{
  "id": 7,
  "sourceAccount": "123456",
  "destinationAccount": "654321",
  "amount": 500.00,
  "fee": 18.00,
  "scheduledDate": "2026-09-06",
  "transferDate": "2026-09-06"
}
```

Keep the returned `id`, as it can be used in the next requests.

---

### 2. Get all transfers

```http
GET /api/transfers
```

Example:

```text
http://localhost:8080/api/transfers
```

No request body is required.

Expected status:

```text
200 OK
```

This returns all scheduled transfers.

---

### 3. Get a transfer by ID

```http
GET /api/transfers/{id}
```

Replace `{id}` with an existing transfer ID.

Example:

```text
http://localhost:8080/api/transfers/7
```

No request body is required.

Expected status:

```text
200 OK
```

If the ID does not exist:

```text
404 Not Found
```

---

### 4. Update a transfer

```http
PUT /api/transfers/{id}
```

Replace `{id}` with the ID of an existing transfer.

Example:

```text
http://localhost:8080/api/transfers/7
```

In **Body > raw > JSON**, send the new transfer information:

```json
{
  "sourceAccount": "123456",
  "destinationAccount": "999999",
  "amount": 800.00,
  "transferDate": "2026-09-06"
}
```

Expected status:

```text
200 OK
```

The transfer information is updated and the fee is recalculated.

If the ID does not exist:

```text
404 Not Found
```

---

### 5. Search transfers by account

```http
GET /api/transfers/by-account?account={account}
```

Replace `{account}` with the account number you want to search for.

Example:

```text
http://localhost:8080/api/transfers/by-account?account=123456
```

No request body is required.

Expected status:

```text
200 OK
```

This returns transfers where `123456` is either the source account or the destination account.

---

### 6. Search transfers by date range

```http
GET /api/transfers/by-date-range?startDate={startDate}&endDate={endDate}
```

Dates must use the format:

```text
YYYY-MM-DD
```

Example:

```text
http://localhost:8080/api/transfers/by-date-range?startDate=2026-09-01&endDate=2026-09-30
```

No request body is required.

Expected status:

```text
200 OK
```

This returns transfers whose transfer date is between the provided start and end dates.

The start date cannot be after the end date.

---

### 7. Delete a transfer

```http
DELETE /api/transfers/{id}
```

Replace `{id}` with an existing transfer ID.

Example:

```text
http://localhost:8080/api/transfers/7
```

No request body is required.

Expected status:

```text
204 No Content
```

After deleting it, you can call:

```http
GET /api/transfers/7
```

and the expected result should be:

```text
404 Not Found
```

## Transfer fee examples

Some valid examples that can be used when testing the create endpoint:

### Fee A

Same-day transfer up to €1000:

```json
{
  "sourceAccount": "111111",
  "destinationAccount": "222222",
  "amount": 500.00,
  "transferDate": "CURRENT_DATE"
}
```

Fee:

```text
3% of €500 + €3 = €18
```

### Fee B

Transfer between €1000 and €2000, scheduled 1 to 10 days ahead:

```json
{
  "sourceAccount": "111111",
  "destinationAccount": "222222",
  "amount": 1500.00,
  "transferDate": "CURRENT_DATE_PLUS_5_DAYS"
}
```

Fee:

```text
9% of €1500 = €135
```

### Fee C

Transfer greater than €2000, scheduled 11 to 20 days ahead:

```json
{
  "sourceAccount": "111111",
  "destinationAccount": "222222",
  "amount": 3000.00,
  "transferDate": "CURRENT_DATE_PLUS_15_DAYS"
}
```

Fee:

```text
8.2% of €3000 = €246
```

Replace the example date values with real dates using the `YYYY-MM-DD` format.

## Running the tests

Run all unit tests with:

```bash
mvn test
```

The current test suite contains 52 tests.

## Build

To build the application:

```bash
mvn clean package
```
