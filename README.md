# revoluttransfer

Steps for running:
1) checkout the project
2) run mvn clean install
3) open target folder
4) run the next command in console: java -jar revolut-service.jar

You must provide json message as request.

Request format:
```json
{
    "fromAccountId": 123,
    "toAccountId": 124,
    "amount": 1000
}
```

Response format for successful operation:
```json
{
    "message": "The operation was successfully carried out!",
    "fromUserId": 123,
    "fromUserBalance": 11900.22,
    "toUserId": 124,
    "toUserBalance": 46540.43,
    "transferredAmount": 1000,
    "transactionId": "a359e6b2-dedb-46c2-b109-a42407d8c16c"
}
```

Error response:
```json
{
    "errorMessage": "The operation is not permissible for account.",
    "userId": 123
}
```

Note that for the moment available 3 users:
>id: 123, name: 'Pavel', balance: 12900.22

>id: 124, name: 'Eugen', balance: 45540.43

>id: 125, name: 'Andrey', balance: 23450.34

You can add more accounts into initial_data.sql file.
