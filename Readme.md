/**
-- API documentation for EWalletApplication 
This is a EWallet Application using a micro-services architecture where there are  User, Wallet, Transaction and Notification services.

1. Create User API for User Service - /createNewUser
--This endpoint will create a new user and queue the creation status to the Kafka Topic
--The wallet service will dequeue the Kafka Topic and create a new Wallet for the User
--New Wallet creation will trigger the Transaction to credit intro amount to the new use wallet 
--Notification service will send emails on user creation and amount credit to the new user.

2. TODO Delete User API
3. TODO Update User API
4. TODO Transactions between Users API

A thanks to Pranav Miglani (https://www.linkedin.com/in/pranavmiglaniii/) who mentored to help create this application. 
*/