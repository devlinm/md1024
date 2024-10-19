The design doc is in [this doc](./design.md)

# Notes

1. For logging the RentalAgreement to the console see ```com.toolrental.domain.entity.RentalAgreement.printToConsole```


# Examples 
1. Run the web app in one terminal window via: ```mvn spring-boot:run -Dspring-boot.run.profiles=test```
2. Execute the examples script. ```./runExamples.sh```

```
Ensure the webapp has been started in background using: 
   mvn spring-boot:run -Dspring-boot.run.profiles=test

# Rent Ladder for one week at start of September. Labor day will be free.
curl -s -X POST 'localhost:8080/rental/checkout?toolCode=LADW&rentalDayCount=7&discount=0&checkoutDate=2024-09-01'
{
  "toolCode": "LADW",
  "toolType": "Ladder",
  "toolBrand": "Werner",
  "rentalDays": 7,
  "checkoutDate": "2024-09-01",
  "dueDate": "2024-09-08",
  "dailyRentalCharge": 1.99,
  "chargeDays": 6,
  "discountPercent": 0,
  "id": "17a54878-a064-4b47-8cf4-885a6635a49d",
  "digitalSignature": null,
  "state": "UNSIGNED",
  "preDiscountCharge": 11.94,
  "discountAmount": 0,
  "finalCharge": 11.94
}

# Same case but with 10% discount
curl -s -X POST 'localhost:8080/rental/checkout?toolCode=LADW&rentalDayCount=7&discount=10&checkoutDate=2024-09-01'
{
  "toolCode": "LADW",
  "toolType": "Ladder",
  "toolBrand": "Werner",
  "rentalDays": 7,
  "checkoutDate": "2024-09-01",
  "dueDate": "2024-09-08",
  "dailyRentalCharge": 1.99,
  "chargeDays": 6,
  "discountPercent": 10,
  "id": "ee3b0dcc-6cb7-44ad-98a3-c4907a9c6556",
  "digitalSignature": null,
  "state": "UNSIGNED",
  "preDiscountCharge": 11.94,
  "discountAmount": 1.19,
  "finalCharge": 10.75
}

# Rent chainsaw for one week at start of September. Labor day will be charged but weekends are free
curl -s -X POST 'localhost:8080/rental/checkout?toolCode=CHNS&rentalDayCount=7&discount=0&checkoutDate=2024-09-01'
{
  "toolCode": "CHNS",
  "toolType": "Chainsaw",
  "toolBrand": "Stihl",
  "rentalDays": 7,
  "checkoutDate": "2024-09-01",
  "dueDate": "2024-09-08",
  "dailyRentalCharge": 1.49,
  "chargeDays": 5,
  "discountPercent": 0,
  "id": "21609d35-8fd8-4a39-9ed2-86f0d61cebe7",
  "digitalSignature": null,
  "state": "UNSIGNED",
  "preDiscountCharge": 7.45,
  "discountAmount": 0,
  "finalCharge": 7.45
}

# Same case but with 10% discount
curl -s -X POST 'localhost:8080/rental/checkout?toolCode=CHNS&rentalDayCount=7&discount=10&checkoutDate=2024-09-01'
{
  "toolCode": "CHNS",
  "toolType": "Chainsaw",
  "toolBrand": "Stihl",
  "rentalDays": 7,
  "checkoutDate": "2024-09-01",
  "dueDate": "2024-09-08",
  "dailyRentalCharge": 1.49,
  "chargeDays": 5,
  "discountPercent": 10,
  "id": "684a5bcc-38dd-4a98-8ff6-9b539737adc6",
  "digitalSignature": null,
  "state": "UNSIGNED",
  "preDiscountCharge": 7.45,
  "discountAmount": 0.75,
  "finalCharge": 6.7
}

# Same case but with 50% discount - note the rounding of the discount.
curl -s -X POST 'localhost:8080/rental/checkout?toolCode=CHNS&rentalDayCount=7&discount=50&checkoutDate=2024-09-01'
{
  "toolCode": "CHNS",
  "toolType": "Chainsaw",
  "toolBrand": "Stihl",
  "rentalDays": 7,
  "checkoutDate": "2024-09-01",
  "dueDate": "2024-09-08",
  "dailyRentalCharge": 1.49,
  "chargeDays": 5,
  "discountPercent": 50,
  "id": "9dc713c0-1ea2-4962-8121-016dd5ae41c5",
  "digitalSignature": null,
  "state": "UNSIGNED",
  "preDiscountCharge": 7.45,
  "discountAmount": 3.73,
  "finalCharge": 3.72
}

# Rent jackhammer for one week at start of September. Labor day and weekends are free
curl -s -X POST 'localhost:8080/rental/checkout?toolCode=JAKD&rentalDayCount=7&discount=0&checkoutDate=2024-09-01'
{
  "toolCode": "JAKD",
  "toolType": "Jackhammer",
  "toolBrand": "DeWalt",
  "rentalDays": 7,
  "checkoutDate": "2024-09-01",
  "dueDate": "2024-09-08",
  "dailyRentalCharge": 2.99,
  "chargeDays": 4,
  "discountPercent": 0,
  "id": "07b28947-b6a8-4f28-b515-cfbdee5e872b",
  "digitalSignature": null,
  "state": "UNSIGNED",
  "preDiscountCharge": 11.96,
  "discountAmount": 0,
  "finalCharge": 11.96
}

# Same case but with 10% discount
curl -s -X POST 'localhost:8080/rental/checkout?toolCode=JAKD&rentalDayCount=7&discount=10&checkoutDate=2024-09-01'
{
  "toolCode": "JAKD",
  "toolType": "Jackhammer",
  "toolBrand": "DeWalt",
  "rentalDays": 7,
  "checkoutDate": "2024-09-01",
  "dueDate": "2024-09-08",
  "dailyRentalCharge": 2.99,
  "chargeDays": 4,
  "discountPercent": 10,
  "id": "0526695f-324c-4e50-b146-92e414ce56cf",
  "digitalSignature": null,
  "state": "UNSIGNED",
  "preDiscountCharge": 11.96,
  "discountAmount": 1.2,
  "finalCharge": 10.76
}

# Same case but with 50% discount
curl -s -X POST 'localhost:8080/rental/checkout?toolCode=JAKD&rentalDayCount=7&discount=50&checkoutDate=2024-09-01'
{
  "toolCode": "JAKD",
  "toolType": "Jackhammer",
  "toolBrand": "DeWalt",
  "rentalDays": 7,
  "checkoutDate": "2024-09-01",
  "dueDate": "2024-09-08",
  "dailyRentalCharge": 2.99,
  "chargeDays": 4,
  "discountPercent": 50,
  "id": "9cd59e22-45ca-4cf7-9111-207cd6a021f8",
  "digitalSignature": null,
  "state": "UNSIGNED",
  "preDiscountCharge": 11.96,
  "discountAmount": 5.98,
  "finalCharge": 5.98
}

# Rent jackhammer for one week at start of July. July 4th and weekends are free
curl -s -X POST 'localhost:8080/rental/checkout?toolCode=JAKD&rentalDayCount=7&discount=0&checkoutDate=2024-07-01'
{
  "toolCode": "JAKD",
  "toolType": "Jackhammer",
  "toolBrand": "DeWalt",
  "rentalDays": 7,
  "checkoutDate": "2024-07-01",
  "dueDate": "2024-07-08",
  "dailyRentalCharge": 2.99,
  "chargeDays": 4,
  "discountPercent": 0,
  "id": "f65e4a99-ebeb-4791-a502-87b4e973cbb0",
  "digitalSignature": null,
  "state": "UNSIGNED",
  "preDiscountCharge": 11.96,
  "discountAmount": 0,
  "finalCharge": 11.96
}

# Rent ladder for one week at start of July in 2027. July 4th is on a Sunday (but observed on Monday) and weekends are free
curl -s -X POST 'localhost:8080/rental/checkout?toolCode=JAKD&rentalDayCount=7&discount=0&checkoutDate=2027-07-01'
{
  "toolCode": "JAKD",
  "toolType": "Jackhammer",
  "toolBrand": "DeWalt",
  "rentalDays": 7,
  "checkoutDate": "2027-07-01",
  "dueDate": "2027-07-08",
  "dailyRentalCharge": 2.99,
  "chargeDays": 4,
  "discountPercent": 0,
  "id": "956464c2-cd34-4c8f-8179-7d6874f57602",
  "digitalSignature": null,
  "state": "UNSIGNED",
  "preDiscountCharge": 11.96,
  "discountAmount": 0,
  "finalCharge": 11.96
}

# Rent ladder for two days on Saturday July 3rd 2027 - July 4th is on a Sunday (but observed on Monday) and weekends are free. This will be completely free.
curl -s -X POST 'localhost:8080/rental/checkout?toolCode=JAKD&rentalDayCount=2&discount=0&checkoutDate=2027-07-03'
{
  "toolCode": "JAKD",
  "toolType": "Jackhammer",
  "toolBrand": "DeWalt",
  "rentalDays": 2,
  "checkoutDate": "2027-07-03",
  "dueDate": "2027-07-05",
  "dailyRentalCharge": 2.99,
  "chargeDays": 0,
  "discountPercent": 0,
  "id": "ff4bb838-ba20-4282-81b7-bb29547e3ab6",
  "digitalSignature": null,
  "state": "UNSIGNED",
  "preDiscountCharge": 0,
  "discountAmount": 0,
  "finalCharge": 0
}

# Rent ladder for three days on Saturday July 3rd 2027 - July 4th is on a Sunday (but observed on Monday) and weekends are free. Only charged for single day.
curl -s -X POST 'localhost:8080/rental/checkout?toolCode=JAKD&rentalDayCount=3&discount=0&checkoutDate=2027-07-03'
{
  "toolCode": "JAKD",
  "toolType": "Jackhammer",
  "toolBrand": "DeWalt",
  "rentalDays": 3,
  "checkoutDate": "2027-07-03",
  "dueDate": "2027-07-06",
  "dailyRentalCharge": 2.99,
  "chargeDays": 1,
  "discountPercent": 0,
  "id": "73dc14b8-d1bd-497d-be5d-f62c8e829395",
  "digitalSignature": null,
  "state": "UNSIGNED",
  "preDiscountCharge": 2.99,
  "discountAmount": 0,
  "finalCharge": 2.99
}

# Bad discount percentage > 100%  produces error message
curl -s -X POST 'localhost:8080/rental/checkout?toolCode=LADW&rentalDayCount=7&discount=120&checkoutDate=2024-09-01'
{
  "error": "Discount percentage must be a whole number in the range 0 to 100. Invalid value: 120"
}

# Bad discount percentage < 0  produces error message
curl -s -X POST 'localhost:8080/rental/checkout?toolCode=LADW&rentalDayCount=7&discount=-1&checkoutDate=2024-09-01'
{
  "error": "Discount percentage must be a whole number in the range 0 to 100. Invalid value: -1"
}

# Bad rental day count = 0 produces error message
curl -s -X POST 'localhost:8080/rental/checkout?toolCode=LADW&rentalDayCount=0&discount=0&checkoutDate=2024-09-01'
{
  "error": "Rental day count must be 1 or greater. Invalid value: 0"
}

# Bad rental day count < 0 produces error message
curl -s -X POST 'localhost:8080/rental/checkout?toolCode=LADW&rentalDayCount=-2&discount=0&checkoutDate=2024-09-01'
{
  "error": "Rental day count must be 1 or greater. Invalid value: -2"
}

# Bad discount percentage - not a whole number 
curl -s -X POST 'localhost:8080/rental/checkout?toolCode=LADW&rentalDayCount=7&discount=2.2&checkoutDate=2024-09-01'
{
  "error": "Bad value for 'discount': Failed to convert value of type 'java.lang.String' to required type 'int'; For input string: \"2.2\""
}

# Bad toolcode
curl -s -X POST 'localhost:8080/rental/checkout?toolCode=HAMMER&rentalDayCount=7&discount=0&checkoutDate=2024-09-01'
{
  "error": "There is no Tool with code: HAMMER"
}

# Invalid date
curl -s -X POST 'localhost:8080/rental/checkout?toolCode=HAMMER&rentalDayCount=7&discount=0&checkoutDate=2024-13-01'
{
  "error": "Bad value for 'checkoutDate': Failed to convert value of type 'java.lang.String' to required type 'java.time.LocalDate'; Failed to convert from type [java.lang.String] to type [@org.springframework.web.bind.annotation.RequestParam @org.springframework.format.annotation.DateTimeFormat java.time.LocalDate] for value [2024-13-01]"
}

# Read back the first rental agreement
curl -s -X GET 'localhost:8080/rental/rentalAgreement/17a54878-a064-4b47-8cf4-885a6635a49d'
{
  "toolCode": "LADW",
  "toolType": "Ladder",
  "toolBrand": "Werner",
  "rentalDays": 7,
  "checkoutDate": "2024-09-01",
  "dueDate": "2024-09-08",
  "dailyRentalCharge": 1.99,
  "chargeDays": 6,
  "discountPercent": 0,
  "id": "17a54878-a064-4b47-8cf4-885a6635a49d",
  "digitalSignature": null,
  "state": "UNSIGNED",
  "preDiscountCharge": 11.94,
  "discountAmount": 0,
  "finalCharge": 11.94
}

# Sign the first rental agreement
curl -s -X POST -H "Content-Type: application/octet-stream" --data-binary @digitalSignature.txt 'localhost:8080/rental/sign?rentalAgreementId=17a54878-a064-4b47-8cf4-885a6635a49d'

# Read back the first rental agreement
curl -s -X GET 'localhost:8080/rental/rentalAgreement/17a54878-a064-4b47-8cf4-885a6635a49d'
{
  "toolCode": "LADW",
  "toolType": "Ladder",
  "toolBrand": "Werner",
  "rentalDays": 7,
  "checkoutDate": "2024-09-01",
  "dueDate": "2024-09-08",
  "dailyRentalCharge": 1.99,
  "chargeDays": 6,
  "discountPercent": 0,
  "id": "17a54878-a064-4b47-8cf4-885a6635a49d",
  "digitalSignature": "ZmFrZURpZ2l0YWxTaWduYXR1cmUK",
  "state": "ACTIVE",
  "preDiscountCharge": 11.94,
  "discountAmount": 0,
  "finalCharge": 11.94
}

# Read back the 2nd rental agreement
curl -s -X GET 'localhost:8080/rental/rentalAgreement/ee3b0dcc-6cb7-44ad-98a3-c4907a9c6556'
{
  "toolCode": "LADW",
  "toolType": "Ladder",
  "toolBrand": "Werner",
  "rentalDays": 7,
  "checkoutDate": "2024-09-01",
  "dueDate": "2024-09-08",
  "dailyRentalCharge": 1.99,
  "chargeDays": 6,
  "discountPercent": 10,
  "id": "ee3b0dcc-6cb7-44ad-98a3-c4907a9c6556",
  "digitalSignature": null,
  "state": "UNSIGNED",
  "preDiscountCharge": 11.94,
  "discountAmount": 1.19,
  "finalCharge": 10.75
}

# Cancel the 2nd rental agreement
curl -s -X POST 'localhost:8080/rental/cancel?rentalAgreementId=ee3b0dcc-6cb7-44ad-98a3-c4907a9c6556'

# Read back the 2nd rental agreement
curl -s -X GET 'localhost:8080/rental/rentalAgreement/ee3b0dcc-6cb7-44ad-98a3-c4907a9c6556'
{
  "toolCode": "LADW",
  "toolType": "Ladder",
  "toolBrand": "Werner",
  "rentalDays": 7,
  "checkoutDate": "2024-09-01",
  "dueDate": "2024-09-08",
  "dailyRentalCharge": 1.99,
  "chargeDays": 6,
  "discountPercent": 10,
  "id": "ee3b0dcc-6cb7-44ad-98a3-c4907a9c6556",
  "digitalSignature": null,
  "state": "CANCELLED",
  "preDiscountCharge": 11.94,
  "discountAmount": 1.19,
  "finalCharge": 10.75
}

```
