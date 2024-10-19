package com.toolrental.domain;

import com.toolrental.domain.entity.ToolType;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.toolrental.domain.ChargeDayRule.*;
import static java.time.DayOfWeek.*;
import static java.time.Month.*;
import static org.assertj.core.api.Assertions.*;

public class ChargeDayRuleTest {

    final static List<LocalDate> laborDays = Arrays.asList(
            LocalDate.of(2019, SEPTEMBER, 2),
            LocalDate.of(2020, SEPTEMBER, 7),
            LocalDate.of(2021, SEPTEMBER, 6),
            LocalDate.of(2022, SEPTEMBER, 5),
            LocalDate.of(2023, SEPTEMBER, 4),
            LocalDate.of(2024, SEPTEMBER, 2),
            LocalDate.of(2025, SEPTEMBER, 1),
            LocalDate.of(2026, SEPTEMBER, 7),
            LocalDate.of(2027, SEPTEMBER, 6),
            LocalDate.of(2028, SEPTEMBER, 4),
            LocalDate.of(2029, SEPTEMBER, 3)
    );

    final static List<LocalDate> july4ths = Arrays.asList(
            LocalDate.of(2023, JULY, 4),
            LocalDate.of(2024, JULY, 4),
            LocalDate.of(2025, JULY, 4),
            LocalDate.of(2026, JULY, 3), // FRIDAY 3rd - July 4th on Saturday
            LocalDate.of(2027, JULY, 5), // MONDAY 5th - July 4th on Sunday
            LocalDate.of(2028, JULY, 4)
    );

    final static List<LocalDate> holidays = new ArrayList<>();
    static {
        holidays.addAll(laborDays);
        holidays.addAll(july4ths);
    }

    private static final List<LocalDate> daysInAugust = new ArrayList<>();
    static {
        IntStream.range(1, 32).forEach(day -> daysInAugust.add(LocalDate.of(2025, AUGUST, day)));
    }

    @Test
    public void testIsChargeDay(){
        // there are 8 permutations of possibilities for the 3 boolean flags

        // the three example cases from the requirements
        testChargeDay("Ladder", true, true, false);  // 6
        testChargeDay("Chainsaw", true, false, true);// 5
        testChargeDay("Jackhammer", true, false, false); // 4

        // never free
        testChargeDay(null, true, true, true); // 7

        // always free
        testChargeDay(null, false, false, false);// 0

        // only on holidays
        testChargeDay(null, false, false, true); // 1

        // only on weekends
        testChargeDay(null, false, true, false); // 2

        // only on weekends and holidays
        testChargeDay(null, false, true, true); // 3
    }

    private void testChargeDay(String toolType, boolean weekdayCharge, boolean weekendCharge, boolean holidayCharge){
        ChargeDayRule rule = new ChargeDayRule(new ToolType(toolType, 1, weekdayCharge, weekendCharge, holidayCharge));
        for (LocalDate day : daysInAugust){
            if (day.getDayOfWeek() == SATURDAY || day.getDayOfWeek() == SUNDAY){
                assertThat( rule.isChargeDay(day) ).isEqualTo(weekendCharge);
            } else {
                // week day
                assertThat( rule.isChargeDay(day) ).isEqualTo(weekdayCharge);
            }
        }
        for (LocalDate day: holidays){
            assertThat( rule.isChargeDay(day) ).isEqualTo(holidayCharge);
        }
    }

    /**
     * Ensure that when July 4th falls on the weekend, it is observed on the previous Friday or next Monday instead.
     */
    @Test
    public void testIsEffectiveJuly4th(){
        // 2023
        assertThat(isEffectiveJuly4th(date(2023,  3, MONDAY))).isFalse();
        assertThat(isEffectiveJuly4th(date(2023,  4, TUESDAY))).isTrue();
        assertThat(isEffectiveJuly4th(date(2023,  5, WEDNESDAY))).isFalse();

        // 2024
        assertThat(isEffectiveJuly4th(date(2024,  3, WEDNESDAY))).isFalse();
        assertThat(isEffectiveJuly4th(date(2024,  4, THURSDAY))).isTrue();
        assertThat(isEffectiveJuly4th(date(2024,  5, FRIDAY))).isFalse();

        // 2025 - July 4th is on a Friday
        assertThat(isEffectiveJuly4th(date(2025,  3, THURSDAY))).isFalse();
        assertThat(isEffectiveJuly4th(date(2025,  4, FRIDAY))).isTrue();
        assertThat(isEffectiveJuly4th(date(2025,  5, SATURDAY))).isFalse();

        // 2026 - July 4th is on a Saturday but it should be observed on the Friday
        assertThat(isEffectiveJuly4th(date(2026,  3, FRIDAY))).isTrue(); // NOTE - this is TRUE
        assertThat(isEffectiveJuly4th(date(2026,  4, SATURDAY))).isFalse(); // NOTE this is FALSE which is correct
        assertThat(isEffectiveJuly4th(date(2026,  5, SUNDAY))).isFalse();

        // 2027 - July 4th is on a Sunday but it should be observed on the following Monday
        assertThat(isEffectiveJuly4th(date(2027,  3, SATURDAY))).isFalse();
        assertThat(isEffectiveJuly4th(date(2027,  4, SUNDAY))).isFalse(); // NOTE this is FALSE which is correct
        assertThat(isEffectiveJuly4th(date(2027,  5, MONDAY))).isTrue(); // this is TRUE becuase we observe july 4th on MOnday July 5th

        // 2028 - July 4th is on a Tuesday
        assertThat(isEffectiveJuly4th(date(2028,  3, MONDAY))).isFalse();
        assertThat(isEffectiveJuly4th(date(2028,  4, TUESDAY))).isTrue();
        assertThat(isEffectiveJuly4th(date(2028,  5, WEDNESDAY))).isFalse();

        // other 4th of months dates
        assertThat(isEffectiveJuly4th(LocalDate.of(2028,  Month.AUGUST, 4))).isFalse();
        assertThat(isEffectiveJuly4th(LocalDate.of(2028,  Month.JUNE, 4))).isFalse();
    }

    @Test
    public void testIsWeekDay(){
        // weekdays
        assertThat( isWeekDay(date(2024,  1, MONDAY))).isTrue();
        assertThat( isWeekDay(date(2024,  2, TUESDAY))).isTrue();
        assertThat( isWeekDay(date(2024,  3, WEDNESDAY))).isTrue();
        assertThat( isWeekDay(date(2024,  4, THURSDAY))).isTrue();
        assertThat( isWeekDay(date(2024,  5, FRIDAY))).isTrue();
        // weekend
        assertThat( isWeekDay(date(2024,  6, SATURDAY))).isFalse();
        assertThat( isWeekDay(date(2024,  7, SUNDAY))).isFalse();
    }

    @Test
    public void testIsLaborDay(){
        for (LocalDate date : laborDays){
           assertLaborDay(date);
        }
        // sanity check - there should only be one labor year per year
        // check every day of the year between from 2019 up to 2029
        List<LocalDate> calculatedLaborDays = new ArrayList<>();
        IntStream.range(2019, 2030).forEach(year -> {
            IntStream.range(1, 366).forEach( dayOfYear -> {
                LocalDate day = LocalDate.ofYearDay(year, dayOfYear);
                if (ChargeDayRule.isLaborDay(day)){
                    calculatedLaborDays.add(day);
                }
            });
        });
        assertThat(calculatedLaborDays).containsExactly(laborDays.toArray(new LocalDate[]{}));
    }
    
    @Test
    public void testIsHoliday(){
        for (LocalDate date : holidays){
            assertThat( isHoliday(date) ).isTrue();
        }
        assertThat( isHoliday(LocalDate.of(2026, JULY, 4))).isFalse(); // Saturday
        assertThat( isHoliday(LocalDate.of(2027, JULY, 4))).isFalse(); // Monday
        // sanity check
        assertThat( holidays.size() ).isEqualTo( july4ths.size() + laborDays.size());
    }

    /**
     * @return a date in july of the given year. Assert that it matches the 'dayOfWeek'
     */
    private static LocalDate date(int year, int dayOfMonth, DayOfWeek dayOfWeek){
        LocalDate date = LocalDate.of(year, JULY.getValue(), dayOfMonth);
        assertThat(date.getDayOfWeek()).isEqualTo(dayOfWeek);
        return date;
    }

    private static void assertLaborDay(LocalDate date){
        assertThat(ChargeDayRule.isLaborDay(date)).isTrue();

        // sanity check
        assertThat(date.getDayOfWeek()).isEqualTo(MONDAY);
        assertThat(date.getMonth()).isEqualTo(SEPTEMBER);

        LocalDate dayBefore = date.minusDays(1);
        assertThat(ChargeDayRule.isLaborDay(dayBefore)).isFalse();

        LocalDate dayAfter = date.plusDays(1);
        assertThat(ChargeDayRule.isLaborDay(dayAfter)).isFalse();

        // 2nd Monday in September
        LocalDate weekAfter = date.plusDays(7);
        assertThat(ChargeDayRule.isLaborDay(weekAfter)).isFalse();
    }
}
