package org.tool.aj0422.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.tool.aj0422.dao.model.Tool;
import org.tool.aj0422.dao.model.ToolPricing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class RentalAgreement {
    private String toolCode;
    private String toolType;
    private String toolBrand;
    private int rentalDays;
    private LocalDateTime checkOutDate;
    private LocalDateTime dueDate;
    private BigDecimal dailyRentalCharge;
    private int chargeDays;
    private BigDecimal preDiscountCharge;
    private BigDecimal discountPercentage;
    private BigDecimal discountAmount;
    private BigDecimal finalCharge;

    public RentalAgreement() { }
    public RentalAgreement(Tool tool, ToolPricing toolPricing) {
        setToolCode(tool.getToolCode());
        setToolType(tool.getToolType().getToolType());
        setToolBrand(tool.getBrand());
        setDailyRentalCharge(toolPricing.getToolRate());
    }

    public void calculateCharges() {
        BigDecimal preDiscountCharge = dailyRentalCharge.multiply(BigDecimal.valueOf(chargeDays)).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal discountAmount = discountPercentage.multiply(preDiscountCharge).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal finalCharge = preDiscountCharge.subtract(discountAmount);
        setPreDiscountCharge(preDiscountCharge);
        setDiscountAmount(discountAmount);
        setFinalCharge(finalCharge);
    }

    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        String discountPercent = getDiscountPercentage()
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_EVEN)
                .toPlainString();
        return
                "Tool code: " + toolCode + "\n" +
                "Tool type: " + toolType + "\n" +
                "Tool brand: " + toolBrand + "\n" +
                "Rental days: " + rentalDays + "\n" +
                "Check out date: " + dateTimeFormatter.format(checkOutDate) + "\n" +
                "Due date: " + dateTimeFormatter.format(dueDate) + "\n" +
                "Daily rental charge: $" + dailyRentalCharge.setScale(2, RoundingMode.HALF_EVEN) + "\n" +
                "Charge days: " + chargeDays + "\n" +
                "Pre-discount charge: $" + preDiscountCharge + "\n" +
                "Discount percent: " + discountPercent + "%\n" +
                "Discount amount: $" + discountAmount + "\n" +
                "Final charge: $" + finalCharge;
    }
}
