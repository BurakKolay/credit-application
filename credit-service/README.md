
# Credit Application System

Credit application system lets users to create an account and login to that account. After that, applicants can apply to Credit. After application system sends a message to applicants phone number who already given while creating an account.


Admins using this application can delete/update/create applicants. They can view every detail of credit applications and applicant profiles. System automatically checks if applicant is suitable for the credit and updates the application on the database.
## API Reference

#### Sign in

```http
  POST /users/signin
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `UserDTO`   | `Model` | **Required** Body for userDTO |

#### Sign up
```http
  POST /users/signup
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `UserDataDTO` | `Model` | **Required** Body for userDataDTO |


#### Get all applicants

```http
  GET /api/v1/applicant/all

```

#### Get applicant by Identification number

```http
  GET /api/v1/applicant/${id}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `identificationNumber`      | `Long` | **Required** identificationNumber of applicant|


```http
  PUT /api/v1/applicant/apply/${id}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `Long` | **Required** id of applicant|

## Database Design

![DB design for credit project](https://user-images.githubusercontent.com/77891949/184190932-ccd738ab-350b-4b7a-9dcf-6f08bd8b8542.png)
## Use Case Diagram
![use case for final project](https://user-images.githubusercontent.com/77891949/184191109-c31ce17e-cdd4-4796-9366-d2de0e02da62.PNG)
## Postman

You can directly reach to my Postman collection using this button:

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/8c29889c1298c5e5328b?action=collection%2Fimport)
## Documentation

Base URL:localhost:8091/v2/api-docs
![swagger final project](https://user-images.githubusercontent.com/77891949/184195752-eebf474a-0d54-47d7-b58d-b6b0416a20c6.PNG)


