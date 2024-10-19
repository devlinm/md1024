# Design Specification

## Use Cases
I'm making some assumptions not explicitly stated in the original functional spec:

1. A customer may decide to change their mind and *not* rent the tool once they see the total price.
2. The customer must digitally Sign the Rental Agreement after seeing it, to make the Rental Agreement "active".
3. There is a need to render a previously generated Rental Agreement e.g. in an email / on a paper receipt etc.

(Of course in real life these assumptions would be agreed with product mgmt in a Requirements Review before this
design spec is finalized)


### UC1 POSTerminal creates RentalAgreement Invoice and Renders the Data on POS Device
Primary Actor: Customer
1. Customer scans the Rental Tool at POSTerminal
2. Customer enters the number of days to rent
3. POSTerminal Renders the days charged and Total Cost (and maybe a more detailed breakdown of the calculation)
4. Customer signs the POSTerminal
    1. OR customer hits Cancel button because they no longer want to rent the tool.
    2. POSTerminal marks the RentalAgreement as deleted.
5. POSTerminal submits digital signature to RentalService (the RentalAgreement is now ACTIVE)

### UC2 Customer Receipt Generation e.g. in email (i.e. Fetch previously created RentalAgreement
Primary Actor: Receipt Generator Agent (e.g. this could be POSTerminal or EmailGenerator agent)
1. Based on RentalAgreementId, download the RentalAgreement details
2. Render these details on a document to be displayed on screen (and/or printed)


## Design
Implement a restful web service using spring-boot.
- json over http

### Domain Model:
There are three main entities in the domain model:

- Tool
  - primary key is the 'tool code'
- ToolType
  - primary key is the 'tool type' 
- RentalAgreement
  - has three states:
    - UNSIGNED (when first created)
    - ACTIVE (when signed)
    - CANCELLED (if the customer decides to cancel the rental upon seeing the total price)
  - primary key is a UUID

The properties of these entities are clearly specified in the functional spec.

The initial implementation will no durable persistence so we will use in a simple in-memory repository for storing
these entities.

The domain model may contain other helper classes for the business rules around Charge Days logic. i.e. whether the customer
should be charged on holidays or weekends etc. 


### Service layer contains a single "RentalService". It has these http operations:
- POST /rental/checkout
  - creates and returns a new RentalAgreement 
  - parameters:
    - toolCode: String
    - rentalDayCount: int
    - discountPercent: int
    - checkoutDate: String yyyy-mm-dd 
  - Validation Rules: 
    - rentalDay: must be non-zero integer
    - discountPercent: must be 0 - 100
    - Return http response code 412 (Precondition Failed)
- GET /rental/rentalAgreement/{rentalAgreementId}
  - returns RentalAgreement that was created previously.
  - path parameter:
    - rentalAgreementId: String of the uuid
  - Validation Rules:
    - If the rental agreement does not exist return an error message "Does Not Exist" with http response code 412 (Precondition Failed)
- POST /rental/cancel 
  - marks an existing RentalAgreement as CANCELLED 
  - parameters:
      - rentalAgreementId: String of the uuid
  - Validation Rules:
      - If the rental agreement does not exist return an error message "Does Not Exist" with http response code 412 (Precondition Failed)
- POST 'sign' 
  - marks an existing RentalAgreement as ACTIVE
  - parameters:
      - rentalAgreementId: String of the uuid
      - digitalSignature: byte[]
  - Validation Rules:
      - If the rental agreement does not exist return an error message "Does Not Exist" with http response code 412 (Precondition Failed)


## Testing
Write comprehensive unit tests and integration tests for the use cases described above. Pay particular attention to the 
business rules around holidays (July 4th and Labor Day) as described in the functional specification.



## Other Assumptions & Notes:
1. Not dealing with security requirements in this prototype.

2. Not dealing with idempotency. Normally for a Point of Sale service where monetary transactions are involved this
may be a requirement. We are making the assumption that this "RentalService" is only for generating a RentalAgreement
document which is to be signed by the customer, or linked to in a customer receipt email so there is we are assuming 
there is no need for that level of fault tolerance. i.e. if the tx failed, it would simply be retired by the client.

