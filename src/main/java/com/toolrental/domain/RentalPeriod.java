package com.toolrental.domain;

import com.toolrental.domain.entity.Tool;
import com.toolrental.domain.entity.ToolType;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Represents the days from {@link #checkoutDate} thru {@link #dueDate}. Used to calculate
 * {@link #getNumberOfChargeDays(ToolType) numberOfChargeDays} when
 * {@link com.toolrental.domain.entity.RentalAgreement#createRentalAgreement(Tool, ToolType, LocalDate, int, int) creating a rental agreement}
 *
 * <h2>Implementation Notes:</h2>
 * This is an {@link Iterable} which produces a stream of {@link LocalDate}s for each day that is potentially chargeable.
 *
 * @see com.toolrental.domain.entity.RentalAgreement
 */
public class RentalPeriod implements Iterable<LocalDate> {

    private final LocalDate checkoutDate;
    private final int numberOfRentalDays;
    private final LocalDate dueDate;

    public RentalPeriod(LocalDate checkoutDate, int numberOfRentalDays){
        this.checkoutDate = checkoutDate;
        this.numberOfRentalDays = numberOfRentalDays;
        this.dueDate = checkoutDate.plusDays(numberOfRentalDays);
    }

    /**
     * @return the total number of days that should be charged for this toolType.
     */
    public int getNumberOfChargeDays(ToolType toolType){
        ChargeDayRule chargeDayRule = new ChargeDayRule(toolType);
        return (int) stream(this).filter(chargeDayRule::isChargeDay).count();
    }

    public LocalDate getCheckoutDate(){
        return checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public int getNumberOfRentalDays(){
        return numberOfRentalDays;
    }

    /**
     * @return an iterator which returns all the days of the rental period up to and including the {@link #dueDate}
     * Note that the initial {@link #checkoutDate} is excluded. i.e. the first date returned is the day after the {@link #checkoutDate}
     */
    @Override
    public Iterator<LocalDate> iterator() {
        return new Iterator<>() {
            int currentDay = 0;
            @Override
            public boolean hasNext() {
                return currentDay < numberOfRentalDays;
            }
            @Override
            public LocalDate next() {
                currentDay++;
                return checkoutDate.plusDays(currentDay);
            }
        };
    }

    /**
     * @return {@link Stream} version of an {@link Iterable}
     */
    private static <T> Stream<T> stream(Iterable<T> iterable){
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
