package com.toolrental.domain;

import com.toolrental.domain.entity.ToolType;

import java.time.LocalDate;
import java.time.Month;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;

/**
 * Business logic rule for determining which days get charged within a {@link RentalPeriod} for a given {@link ToolType}
 * @see RentalPeriod#getNumberOfChargeDays(ToolType)
 */
public class ChargeDayRule {

    private final ToolType toolType;

    public ChargeDayRule(ToolType toolType){
        this.toolType = toolType;
    }

    public boolean isChargeDay(LocalDate date){
        // Note that the holiday rule takes precedence. i.e. Holiday could be on any week day
        if (isHoliday(date)){
            return toolType.isHolidayCharge();
        }
        return isWeekDay(date) ? toolType.isWeekdayCharge() : toolType.isWeekendCharge();
    }

    ///////////
    // helper functions below are static pure functions to make them easy to unit test
    ////////////

    static boolean isHoliday(LocalDate date){
        return isLaborDay(date) || isEffectiveJuly4th(date);
    }

    static boolean isLaborDay(LocalDate date){
        return date.getMonth() == Month.SEPTEMBER && date.getDayOfWeek() == MONDAY && date.getDayOfMonth() <= 7;
    }

    /**
     * Treat Friday July 3rd or Monday July 5th as being the "effective july 4th" in the case where July 4th falls
     * on Saturday or Sunday respectively. In that case, the actual July 4th (on the Saturday or Sunday) will return false.
     * Otherwise, return true when July 4th falls on a week day.
     * @return true if this day is the effective July 4th
     */
    static boolean isEffectiveJuly4th(LocalDate date){
        if (date.getMonth() != Month.JULY) {
            return false;
        }
        final int dayOfMonth = date.getDayOfMonth();
        if (dayOfMonth == 3 && date.getDayOfWeek() == FRIDAY){
            return true;
        }
        if (dayOfMonth == 5 && date.getDayOfWeek() == MONDAY){
            return true;
        }
        return dayOfMonth == 4 && isWeekDay(date);
    }

    static boolean isWeekDay(LocalDate day){
        int dayOfWeek = day.getDayOfWeek().getValue();
        return dayOfWeek >= MONDAY.getValue() && dayOfWeek <= FRIDAY.getValue();
    }
}
