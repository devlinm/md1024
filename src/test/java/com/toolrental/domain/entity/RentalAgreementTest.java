package com.toolrental.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

public class RentalAgreementTest {

    Tool tool;
    ToolType toolType;
    final LocalDate checkoutDate = LocalDate.of(2024, Month.OCTOBER, 2);

    @BeforeEach
    public void setup(){
        final String TOOL_TYPE = "toolType";
        tool = new Tool("toolCode", TOOL_TYPE, "toolBrand");
        toolType = new ToolType(TOOL_TYPE, 10, true, true, false);
    }

    @Test
    public void testCalculations(){
        RentalAgreement agreement = RentalAgreement.createRentalAgreement(tool, toolType, checkoutDate, 2, 5);
        // chargeDays = 2  discount = 5%
        assertThat( agreement.getPreDiscountCharge() ).isEqualTo(20);
        assertThat( agreement.getDiscountAmount() ).isEqualTo(1);
        assertThat( agreement.getFinalCharge() ).isEqualTo(19);

        // chargeDays = 4   discount = 10%
        agreement = RentalAgreement.createRentalAgreement(tool, toolType, checkoutDate, 4, 10);
        assertThat( agreement.getPreDiscountCharge() ).isEqualTo(40);
        assertThat( agreement.getDiscountAmount() ).isEqualTo(4);
        assertThat( agreement.getFinalCharge() ).isEqualTo(36);

        // chargeDays = 4   discount =  0
        agreement = RentalAgreement.createRentalAgreement(tool, toolType, checkoutDate, 4, 0);
        assertThat( agreement.getPreDiscountCharge() ).isEqualTo(40);
        assertThat( agreement.getDiscountAmount() ).isEqualTo(0);
        assertThat( agreement.getFinalCharge() ).isEqualTo(40);

        // test that the rounding of discountCharge and finalCharge is correct when we have fractional cents
        // - it should be rounded up to 2 decimal places.
        toolType.setDailyCharge(1.99);
        agreement = RentalAgreement.createRentalAgreement(tool, toolType, checkoutDate, 5, 10);
        assertThat( agreement.getPreDiscountCharge() ).isEqualTo(9.95);
        assertThat( agreement.getDiscountAmount() ).isEqualTo(1.00); // 0.995 gets rounded up to 1.00
        assertThat( agreement.getFinalCharge() ).isEqualTo(8.95); // we use the rounded amount of discount when calculating final charge
    }



    @Test
    public void testToString(){
        RentalAgreement agreement = RentalAgreement.createRentalAgreement(tool, toolType, checkoutDate, 2, 5);
        assertThat( agreement.toString() ).isEqualTo("""
                Tool code: toolCode
                Tool type: toolType
                Tool brand: toolBrand
                Rental Days: 2
                Checkout Date: 10/02/24
                Due Date: 10/04/24
                Daily Rental Charge: $10.00
                Charge Days: 2
                Pre-discount Charge: $20.00
                Discount Percent: 5%
                Discount Amount: $1.00
                Final Charge: $19.00
                """);

        // ensure commas are used when money amounts are in the thousands
        toolType.setDailyCharge(1000);
        agreement = RentalAgreement.createRentalAgreement(tool, toolType, checkoutDate, 2, 5);
        assertThat( agreement.toString() ).isEqualTo("""
                Tool code: toolCode
                Tool type: toolType
                Tool brand: toolBrand
                Rental Days: 2
                Checkout Date: 10/02/24
                Due Date: 10/04/24
                Daily Rental Charge: $1,000.00
                Charge Days: 2
                Pre-discount Charge: $2,000.00
                Discount Percent: 5%
                Discount Amount: $100.00
                Final Charge: $1,900.00
                """);

        agreement.printToConsole();
    }
}
