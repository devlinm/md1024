package com.toolrental.service;

import com.toolrental.TestData;
import com.toolrental.ToolRentalApplication;
import com.toolrental.domain.entity.RentalAgreement;
import com.toolrental.domain.entity.Tool;
import com.toolrental.domain.entity.ToolType;
import com.toolrental.domain.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.toolrental.domain.entity.RentalAgreement.round;
import static java.time.LocalDate.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = ToolRentalApplication.class)
@ActiveProfiles("test")  // enable the canned test data in com.toolrental.TestData
public class RentalServiceTest {

    private static final LocalDate CHECKOUT_DATE = of(2024, Month.OCTOBER, 10);
    public static final byte[] DIGITAL_SIGNATURE = new byte[5];

    @Autowired
    RentalService rentalService;

    @Autowired
    private Repository<Tool, String> toolRepository;

    @Autowired
    private Repository<ToolType, String> toolTypeRepository;

    @Test
    public void testRentalService_invalidRentalDay(){
        // invalid cases
        assertInvalidRentalDayRule(0, true);
        assertInvalidRentalDayRule(-1, true);
        assertInvalidRentalDayRule(-2, true);
        // valid cases - positive non zero integer
        assertInvalidRentalDayRule(1, false);
        assertInvalidRentalDayRule(5, false);
    }

    @Test
    public void testRentalService_invalidDiscountPercentage(){
        // invalid cases
        assertInvalidDiscountRule(-1, true);
        assertInvalidDiscountRule(101, true);
        // valid cases - between zero and 100
        assertInvalidDiscountRule(0, false);
        assertInvalidDiscountRule(10, false);
        assertInvalidDiscountRule(100, false);
    }

    @Test
    public void testRentalOverLaborDay() throws ValidationException {
        // Ladder is free on Labor Day
        //
        // checkout the Wednesday before Labor day, for one week
        assertRentalAgreement("LADW", 7, 0, "8/28/2024", "9/4/2024", 6 * 1.99, 0);
        assertRentalAgreement("LADW", 7, 50, "8/28/2024", "9/4/2024", 6 * 1.99 * 0.5, 6 * 1.99 * 0.5);
        assertRentalAgreement("LADW", 7, 100, "8/28/2024", "9/4/2024", 0, 6 * 1.99);
        // checkout the day before labor day for one week
        assertRentalAgreement("LADW", 7, 0, "9/1/2024", "9/8/2024", 6 * 1.99,0);
        assertRentalAgreement("LADW", 7, 50, "9/1/2024", "9/8/2024", 6 * 1.99 * 0.5, 6 * 1.99 * 0.5);
        assertRentalAgreement("LADW", 7, 100, "9/1/2024", "9/8/2024", 0, 6 * 1.99);
        // checkout on labor day for one week
        assertRentalAgreement("LADW", 7, 0, "9/2/2024", "9/9/2024", 7 * 1.99, 0);
        assertRentalAgreement("LADW", 7, 50, "9/2/2024", "9/9/2024", 6.96, 6.97); // Note the rounding
        assertRentalAgreement("LADW", 7, 100, "9/2/2024", "9/9/2024", 0, 7 * 1.99);
        // not over labor day - so all 7 days are charged
        assertRentalAgreement("LADW", 7, 0, "9/4/2024", "9/11/2024", 7 * 1.99, 0);
///*
        // Chainsaw is not free on Labor day. But it is free on the weekend
        //
        // checkout the Wednesday before Labor day, for one week
        assertRentalAgreement("CHNS", 7, 0, "8/28/2024", "9/4/2024", 5 * 1.49, 0);
        assertRentalAgreement("CHNS", 7, 50, "8/28/2024", "9/4/2024", 3.72, 3.73); // Note the rounding
        assertRentalAgreement("CHNS", 7, 100, "8/28/2024", "9/4/2024", 0, 7.45);
        // checkout the day before labor day for one week
        assertRentalAgreement("CHNS", 7, 0, "9/1/2024", "9/8/2024", 5 * 1.49, 0);
        assertRentalAgreement("CHNS", 7, 50, "9/1/2024", "9/8/2024", 3.72, 3.73);
        assertRentalAgreement("CHNS", 7, 100, "9/1/2024", "9/8/2024", 0, 7.45);
        // checkout on labor day for one week
        assertRentalAgreement("CHNS", 7, 0, "9/2/2024", "9/9/2024", 5 * 1.49, 0);
        assertRentalAgreement("CHNS", 7, 50, "9/2/2024", "9/9/2024", 3.72, 3.73);
        assertRentalAgreement("CHNS", 7, 100, "9/2/2024", "9/9/2024", 0, 7.45);
        // not over labor day
        assertRentalAgreement("CHNS", 7, 0, "9/4/2024", "9/11/2024", 5 * 1.49, 0);

        // Jackhammer is free on weekend and labor day
        //
        // checkout the Wednesday before Labor day, for one week
        assertRentalAgreement("JAKR", 7, 0, "8/28/2024", "9/4/2024", 4 * 2.99, 0);
        assertRentalAgreement("JAKR", 7, 50, "8/28/2024", "9/4/2024", 4 * 2.99 * 0.5, 4 * 2.99 * 0.5);
        assertRentalAgreement("JAKR", 7, 100, "8/28/2024", "9/4/2024", 0, 4 * 2.99);
        // checkout the day before labor day for one week
        assertRentalAgreement("JAKR", 7, 0, "9/1/2024", "9/8/2024", 4 * 2.99, 0);
        assertRentalAgreement("JAKR", 7, 50, "9/1/2024", "9/8/2024", 4 * 2.99 * 0.5,  4 * 2.99 * 0.5);
        assertRentalAgreement("JAKR", 7, 100, "9/1/2024", "9/8/2024", 0, 4 * 2.99);
        // checkout on labor day for one week
        assertRentalAgreement("JAKR", 7, 0, "9/2/2024", "9/9/2024", /* 5 * 2.99 */ 14.95, 0);
        assertRentalAgreement("JAKR", 7, 50, "9/2/2024", "9/9/2024", 7.47, 7.48);
        assertRentalAgreement("JAKR", 7, 100, "9/2/2024", "9/9/2024", 0, 14.95);
        // not over labor day
        assertRentalAgreement("JAKR", 7, 0, "9/4/2024", "9/11/2024", /* 5 * 2.99 */ 14.95,  0);
    }

    @Test
    public void testRounding(){
        assertThat( RentalAgreement.round(1.9) ).isEqualTo(1.9);
        assertThat( RentalAgreement.round(1.93) ).isEqualTo(1.93);
        assertThat( RentalAgreement.round(1.932) ).isEqualTo(1.93);
        assertThat( RentalAgreement.round(1.933) ).isEqualTo(1.93);
        assertThat( RentalAgreement.round(1.934) ).isEqualTo(1.93);
        assertThat( RentalAgreement.round(1.935) ).isEqualTo(1.94);
        assertThat( RentalAgreement.round(1.936) ).isEqualTo(1.94);
    }

    private void assertRentalAgreement(String toolCode, int rentalDayCount, int discount, String checkoutDate, String expectedDueDate, double expectedCharge, double expectedDiscount) {
        RentalAgreement ra = null;
        try {
            ra = rentalService.checkout(toolCode, rentalDayCount, discount, date(checkoutDate));
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
        assertThat(ra.getDueDate()).isEqualTo(date(expectedDueDate));
        assertThat(ra.getFinalCharge()).isEqualTo(expectedCharge);
        assertThat(ra.getDiscountAmount()).isEqualTo(expectedDiscount);
        // ensure with the rounding that the numbers add up correctly
        assertThat(ra.getPreDiscountCharge()).isEqualTo( expectedCharge + expectedDiscount);

        // test that reading back the RentalAgreement works
        RentalAgreement ra2 = null;
        try {
            ra2 = rentalService.getRentalAgreement(ra.getId().toString());
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
        assertThat(ra).isSameAs(ra2); // for now use object comparison but needs to be converted to semantic comparison once we move to persistent store

        // sign the rental agreement
        try {
            assertThat(ra.getState()).isEqualTo(RentalAgreement.State.UNSIGNED);
            rentalService.sign(ra.getId().toString(), DIGITAL_SIGNATURE);
            ra = rentalService.getRentalAgreement(ra.getId().toString());
            assertThat(ra.getState()).isEqualTo(RentalAgreement.State.ACTIVE);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }

        // cancel the rental agreement
        // - normally this would happen isntead of signing but the logic is all the same
        try {
            rentalService.cancel(ra.getId().toString());
            ra = rentalService.getRentalAgreement(ra.getId().toString());
            assertThat(ra.getState()).isEqualTo(RentalAgreement.State.CANCELLED);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    private void assertInvalidDiscountRule(int discountPercentage, boolean expectedInvalid) {
        if (expectedInvalid) {
            // ensure it throws a validation exception
            ValidationException ex = assertThrows(ValidationException.class,
                    () -> rentalService.checkout("CHNS", 3, discountPercentage, CHECKOUT_DATE)
            );
            assertThat(ex).hasMessageContaining("Discount percentage must be a whole number in the range 0 to 100");
        } else {
            // ensure it does not throw a validation exception
            assertDoesNotThrow(() -> rentalService.checkout("CHNS", 3, discountPercentage, CHECKOUT_DATE));
        }
    }

    private void assertInvalidRentalDayRule(int rentalDayCount, boolean expectedInvalid) {
        if (expectedInvalid) {
            // ensure it throws a validation exception
            ValidationException ex = assertThrows(ValidationException.class,
                    () -> rentalService.checkout("CHNS", rentalDayCount, 10, CHECKOUT_DATE)
            );
            assertThat(ex).hasMessageContaining("Rental day count must be 1 or greater");
        } else {
            // ensure it does not throw a validation exception
            assertDoesNotThrow(() -> rentalService.checkout("CHNS", rentalDayCount, 10, CHECKOUT_DATE));
        }
    }

    // make the unit tests easier to read by using date strings
    private static LocalDate date(String dateStr){
        String[] parts = dateStr.split("/");
        assertThat(parts.length).isEqualTo(3);
        List<Integer> list = Arrays.stream(parts).map(Integer::valueOf).toList();
        return LocalDate.of(list.get(2), list.get(0), list.get(1));
    }
}
