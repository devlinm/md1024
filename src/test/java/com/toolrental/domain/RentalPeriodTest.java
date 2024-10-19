package com.toolrental.domain;

import com.toolrental.domain.entity.ToolType;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class RentalPeriodTest {

    LocalDate oct1 = LocalDate.of(2024, 10, 1);
    LocalDate oct2 = LocalDate.of(2024, 10, 2);
    LocalDate oct3 = LocalDate.of(2024, 10, 3);
    LocalDate oct4 = LocalDate.of(2024, 10, 4);

    @Test
    public void testRentalPeriod(){
        // test the Iterator which produces rental days
        assertThat( new RentalPeriod(oct1, 1 )).containsExactly(oct2);
        assertThat( new RentalPeriod(oct1, 1 ).getDueDate()).isEqualTo(oct2);

        assertThat( new RentalPeriod(oct1, 2 )).containsExactly(oct2, oct3);
        assertThat( new RentalPeriod(oct1, 2 ).getDueDate()).isEqualTo(oct3);

        assertThat( new RentalPeriod(oct1, 3 )).containsExactly(oct2, oct3, oct4);
        assertThat( new RentalPeriod(oct1, 3).getDueDate()).isEqualTo(oct4);
    }

    @Test
    public void testGetNumberOfChargeDays(){
        // charge every day
        ToolType chargeEveryDayTool = new ToolType("chargeEveryDay", 10, true, true, true);
        IntStream.range(1, 100).forEach(numberOfRentalDays -> {
            assertThat( new RentalPeriod(oct1, numberOfRentalDays).getNumberOfChargeDays(chargeEveryDayTool) ).isEqualTo(numberOfRentalDays);
        });

        // charge only weekdays i.e. weekends are free
        assertThat(oct1.getDayOfWeek()).isEqualTo(DayOfWeek.TUESDAY);
        ToolType weekendsFree = new ToolType("weekendsFree", 10, true, false, true);
        assertRentalDaysAndChargeDays(1, 1, weekendsFree, oct1); // Tues -> Wed
        assertRentalDaysAndChargeDays(2, 2, weekendsFree, oct1); // Tues -> Thurs
        assertRentalDaysAndChargeDays(3, 3, weekendsFree, oct1); // Tues -> Friday
        assertRentalDaysAndChargeDays(4, 3, weekendsFree, oct1); // Tues -> Saturday
        assertRentalDaysAndChargeDays(5, 3, weekendsFree, oct1); // Tues -> Sunday
        assertRentalDaysAndChargeDays(6, 4, weekendsFree, oct1); // Tues -> Monday
        assertRentalDaysAndChargeDays(7, 5, weekendsFree, oct1); // Tues -> Tuesday
    }

    private void assertRentalDaysAndChargeDays(int rentalDays, int expectedChargeDays, ToolType toolType, LocalDate checkoutDate){
        RentalPeriod rentalPeriod = new RentalPeriod(checkoutDate, rentalDays);
        assertThat( rentalPeriod.getNumberOfChargeDays(toolType) ).isEqualTo(expectedChargeDays);
        assertThat( rentalPeriod.getNumberOfRentalDays() ).isEqualTo(rentalDays);
    }
}
