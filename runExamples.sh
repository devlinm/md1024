#!/bin/bash
# Invoke the RentalService rest endpoint using curl
#
echo "Ensure the webapp has been started in background using: "
echo "   mvn spring-boot:run -Dspring-boot.run.profiles=test"
echo

SERVER="localhost:8080"
RENTAL_AGREEMENT_IDS=()

checkout(){
  echo "# $1"
  URL="$SERVER/rental/checkout?${2}"
  COMMAND="curl -s -X POST '$URL'"
  echo "$COMMAND"
  eval "$COMMAND" > response.js
  RENTAL_AGREEMENT_ID=`jq .id response.js`
  # echo the whole response message
  jq . response.js
  RENTAL_AGREEMENT_ID=`jq -r .id response.js`
  if [[ "$RENTAL_AGREEMENT_ID" != "null" ]]; then
    RENTAL_AGREEMENT_IDS+=($RENTAL_AGREEMENT_ID)
  fi
  rm response.js
  echo
}

getRentalAgreement(){
  echo "# $1"
  RENTAL_AGREEMENT_ID=$2
  URL="$SERVER/rental/rentalAgreement/${RENTAL_AGREEMENT_ID}"
  COMMAND="curl -s -X GET '$URL'"
  echo "$COMMAND"
  eval "$COMMAND" | jq
  echo
}

signRentalAgreement(){
  echo "# $1"
  RENTAL_AGREEMENT_ID=$2
  URL="$SERVER/rental/sign?rentalAgreementId=$RENTAL_AGREEMENT_ID"
  echo "fakeDigitalSignature" > digitalSignature.txt
  COMMAND="curl -s -X POST -H \"Content-Type: application/octet-stream\" --data-binary @digitalSignature.txt '$URL'"
  echo "$COMMAND"
  eval "$COMMAND"
  rm digitalSignature.txt
  echo
}

cancelRentalAgreement(){
  echo "# $1"
  RENTAL_AGREEMENT_ID=$2
  URL="$SERVER/rental/cancel?rentalAgreementId=$RENTAL_AGREEMENT_ID"
  COMMAND="curl -s -X POST '$URL'"
  echo "$COMMAND"
  eval "$COMMAND"
  echo
}

checkout 'Rent Ladder for one week at start of September. Labor day will be free.' 'toolCode=LADW&rentalDayCount=7&discount=0&checkoutDate=2024-09-01'
checkout 'Same case but with 10% discount' 'toolCode=LADW&rentalDayCount=7&discount=10&checkoutDate=2024-09-01'

checkout 'Rent chainsaw for one week at start of September. Labor day will be charged but weekends are free' 'toolCode=CHNS&rentalDayCount=7&discount=0&checkoutDate=2024-09-01'
checkout 'Same case but with 10% discount' 'toolCode=CHNS&rentalDayCount=7&discount=10&checkoutDate=2024-09-01'
checkout 'Same case but with 50% discount - note the rounding of the discount.' 'toolCode=CHNS&rentalDayCount=7&discount=50&checkoutDate=2024-09-01'

checkout 'Rent jackhammer for one week at start of September. Labor day and weekends are free' 'toolCode=JAKD&rentalDayCount=7&discount=0&checkoutDate=2024-09-01'
checkout 'Same case but with 10% discount' 'toolCode=JAKD&rentalDayCount=7&discount=10&checkoutDate=2024-09-01'
checkout 'Same case but with 50% discount' 'toolCode=JAKD&rentalDayCount=7&discount=50&checkoutDate=2024-09-01'

checkout 'Rent jackhammer for one week at start of July. July 4th and weekends are free' 'toolCode=JAKD&rentalDayCount=7&discount=0&checkoutDate=2024-07-01'

checkout 'Rent ladder for one week at start of July in 2027. July 4th is on a Sunday (but observed on Monday) and weekends are free' 'toolCode=JAKD&rentalDayCount=7&discount=0&checkoutDate=2027-07-01'

checkout 'Rent ladder for two days on Saturday July 3rd 2027 - July 4th is on a Sunday (but observed on Monday) and weekends are free. This will be completely free.' 'toolCode=JAKD&rentalDayCount=2&discount=0&checkoutDate=2027-07-03'
checkout 'Rent ladder for three days on Saturday July 3rd 2027 - July 4th is on a Sunday (but observed on Monday) and weekends are free. Only charged for single day.' 'toolCode=JAKD&rentalDayCount=3&discount=0&checkoutDate=2027-07-03'


# error cases
checkout "Bad discount percentage > 100%  produces error message" 'toolCode=LADW&rentalDayCount=7&discount=120&checkoutDate=2024-09-01'
checkout "Bad discount percentage < 0  produces error message" 'toolCode=LADW&rentalDayCount=7&discount=-1&checkoutDate=2024-09-01'
checkout "Bad rental day count = 0 produces error message" 'toolCode=LADW&rentalDayCount=0&discount=0&checkoutDate=2024-09-01'
checkout "Bad rental day count < 0 produces error message" 'toolCode=LADW&rentalDayCount=-2&discount=0&checkoutDate=2024-09-01'


# other error cases related to bad input data
checkout "Bad discount percentage - not a whole number " 'toolCode=LADW&rentalDayCount=7&discount=2.2&checkoutDate=2024-09-01'
checkout "Bad toolcode" 'toolCode=HAMMER&rentalDayCount=7&discount=0&checkoutDate=2024-09-01'
checkout "Invalid date" 'toolCode=HAMMER&rentalDayCount=7&discount=0&checkoutDate=2024-13-01'

# sign a rental agreements
rentalId=${RENTAL_AGREEMENT_IDS[0]} 
getRentalAgreement "Read back the first rental agreement" $rentalId
signRentalAgreement "Sign the first rental agreement" $rentalId
getRentalAgreement "Read back the first rental agreement" $rentalId

# cancel the 2nd rental agreement
rentalId=${RENTAL_AGREEMENT_IDS[1]} 
getRentalAgreement "Read back the 2nd rental agreement" $rentalId
cancelRentalAgreement "Cancel the 2nd rental agreement" $rentalId
getRentalAgreement "Read back the 2nd rental agreement" $rentalId
