package com.toolrental.domain.entity;

import com.toolrental.domain.RentalPeriod;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

public class RentalAgreement implements Entity<UUID> {

    private String toolCode;
    private String toolType;
    private String toolBrand;
    private int rentalDays;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private double dailyRentalCharge;
    private int chargeDays;
    private int discountPercent;
    private UUID id;
    private byte[] digitalSignature;

    public enum State {
        UNSIGNED,
        ACTIVE,
        CANCELLED
    }
    private State state;

    public static RentalAgreement createRentalAgreement(Tool tool, ToolType toolType, LocalDate checkoutDate, int rentalDayCount, int discountPercent){
        RentalAgreement r = new RentalAgreement();
        r.toolCode = tool.getCode();
        r.toolType = tool.getType();
        r.toolBrand = tool.getBrand();
        RentalPeriod rentalPeriod = new RentalPeriod(checkoutDate, rentalDayCount);
        r.rentalDays = rentalPeriod.getNumberOfRentalDays();
        r.checkoutDate = rentalPeriod.getCheckoutDate();
        r.dueDate = rentalPeriod.getDueDate();
        r.dailyRentalCharge = toolType.getDailyCharge();
        r.chargeDays = rentalPeriod.getNumberOfChargeDays(toolType);
        r.discountPercent = discountPercent;
        r.id = UUID.randomUUID();
        r.state = State.UNSIGNED;
        return r;
    }

    public double getPreDiscountCharge(){
        return round(chargeDays * dailyRentalCharge); // initially I thought the rounding here wasn't needed but unit test prove in fact it is needed.
                                                         // e.g. 1.99 * 5 = 14.950000000000001
    }

    public double getDiscountAmount(){
        return round(getPreDiscountCharge() * (discountPercent / 100.0));
    }

    public double getFinalCharge(){
        return round(getPreDiscountCharge() - getDiscountAmount());
    }

    /**
     * @return round up to two decimal places
     */
    public static double round(double d){
        return BigDecimal.valueOf(d).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    @Override
    public String toString() {
        return String.format("""
                Tool code: %s
                Tool type: %s
                Tool brand: %s
                Rental Days: %s
                Checkout Date: %tD
                Due Date: %tD
                Daily Rental Charge: $%,.2f
                Charge Days: %s
                Pre-discount Charge: $%,.2f
                Discount Percent: %s%%
                Discount Amount: $%,.2f
                Final Charge: $%,.2f
                """, toolCode, toolType, toolBrand, rentalDays, checkoutDate, dueDate, dailyRentalCharge, chargeDays,
                     getPreDiscountCharge(), discountPercent, getDiscountAmount(), getFinalCharge());
    }

    public void printToConsole(){
        System.out.println(this);
    }

    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getToolCode() {
        return toolCode;
    }

    public void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }

    public String getToolType() {
        return toolType;
    }

    public void setToolType(String toolType) {
        this.toolType = toolType;
    }

    public String getToolBrand() {
        return toolBrand;
    }

    public void setToolBrand(String toolBrand) {
        this.toolBrand = toolBrand;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public void setRentalDays(int rentalDays) {
        this.rentalDays = rentalDays;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public double getDailyRentalCharge() {
        return dailyRentalCharge;
    }

    public void setDailyRentalCharge(double dailyRentalCharge) {
        this.dailyRentalCharge = dailyRentalCharge;
    }

    public int getChargeDays() {
        return chargeDays;
    }

    public void setChargeDays(int chargeDays) {
        this.chargeDays = chargeDays;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public byte[] getDigitalSignature() {
        return digitalSignature;
    }

    public void setDigitalSignature(byte[] digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
