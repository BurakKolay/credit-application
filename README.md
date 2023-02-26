# Credit Application System

It is a restful credit application system that receives credit application requests and returns the credit result to the customer according to the relevant criteria. It is written using the Spring Boot framework. The project has been tried to be done in accordance with Solid principles.

## Requirements
* New customers can be created in the system, existing customers can be updated or deleted.
* If the credit score is below 500, the customer will be rejected. (Credit result: Rejected)
* If the credit score is between 500 points and 1000 points and the monthly income is below 5000 TL, the loan application of the user is approved and a limit of 10.000 TL is assigned to the user. (Credit Result: Confirmation). If he has given a guarantee, 10 percent of the amount of the guarantee is added to the credit limit.
* If the credit score is between 500 points and 1000 points and the monthly income is between 5000 TL and 10,000 TL, the user's loan application is approved and a 20,000 TL limit is assigned to the user. (Credit Result: Approval) If a guarantee has been given, 20 percent of the guarantee amount is added to the credit limit.
* If the credit score is between 500 points and 1000 points and the monthly income is above 10.000 TL, the loan application of the user is approved and the user is assigned a limit of MONTHLY INCOME INFORMATION * CREDIT LIMIT MULTIPLIER/2. (Credit Result: Approval) If a guarantee is given, 25 percent of the guarantee amount is added to the credit limit.
* If the credit score is equal to or above 1000 points, the user is assigned a limit equal to MONTHLY INCOME * CREDIT LIMIT MULTIPLIER. (Credit Result: Approval) If a guarantee is given, 50 percent of the guarantee amount is added to the credit limit.
* As a result of the conclusion of the credit, the relevant application is recorded in the database. Afterwards, an informative SMS is sent to the relevant phone number and the approval status information (rejection or approval), limit information is returned from the endpoint.
* A completed loan application can only be queried with the ID number and date of birth. If the date of birth and identity information do not match, it should not be questioned.

## Swagger
[![View in Swagger](http://jessemillar.github.io/view-in-swagger-button/button.svg)](http://localhost:8080/swagger-ui/index.html)
![userController](https://user-images.githubusercontent.com/107641642/184563878-d3388b2a-145c-4dd8-8855-8c8b0bb22e09.png)
![customerController](https://user-images.githubusercontent.com/107641642/184563885-e2b69c62-2859-4968-83fa-2a7d9ad41560.png)
![creditController](https://user-images.githubusercontent.com/107641642/184563899-9935db5e-2aa4-4b14-89ad-a6d73a0365bc.png)
