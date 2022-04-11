package org.tool.aj0422.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tool.aj0422.dao.model.ToolPricing;
import org.tool.aj0422.dao.model.Tool;
import org.tool.aj0422.dao.repository.ToolRepository;
import org.tool.aj0422.exception.InvalidDiscountException;
import org.tool.aj0422.exception.InvalidRentalDaysException;
import org.tool.aj0422.exception.ToolPricingNotFoundException;
import org.tool.aj0422.model.RentalAgreement;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
public class RentalAgreementService {

    private ToolRepository toolRepository;

    public RentalAgreement getRentalAgreement(String toolCode, int rentalDayCount, double discount,
                                              LocalDateTime checkOutDate) {
        if (discount < 0.0 || discount > 1.0)
            throw new InvalidDiscountException(discount);
        if (rentalDayCount < 0)
            throw new InvalidRentalDaysException(rentalDayCount);
        Tool tool = toolRepository.findToolPricingByToolCodeAndCheckOutDate(toolCode, checkOutDate);
        if (tool == null)
            throw new ToolPricingNotFoundException(toolCode, checkOutDate);

        ToolPricing toolPricing = tool.getToolType().getToolTypePricingMaps().get(0).getToolPricing();
        LocalDateTime returnDate = checkOutDate.plusDays(rentalDayCount);
        long chargeDays = calculateChargeDays(checkOutDate, returnDate, rentalDayCount, toolPricing);

        RentalAgreement rentalAgreement = new RentalAgreement(tool, toolPricing);
        rentalAgreement.setRentalDays(rentalDayCount);
        rentalAgreement.setCheckOutDate(checkOutDate);
        rentalAgreement.setDueDate(returnDate);
        rentalAgreement.setChargeDays((int) chargeDays);
        rentalAgreement.setDiscountPercentage(BigDecimal.valueOf(discount));
        rentalAgreement.calculateCharges();
        System.out.println(rentalAgreement);
        return rentalAgreement;
    }

    /**
     * Finds the number of days to charge the daily rental charge for the rental.
     * @param checkOutDate - day rental is checked out
     * @param returnDate - day rental should be returned
     * @param days - total number of days the tool will be rented
     * @param toolPricing - tool pricing for the tool being rented
     * @return - number of days to charge the daily rental charge for the rental
     */
    public long calculateChargeDays(LocalDateTime checkOutDate,
                                    LocalDateTime returnDate,
                                    int days,
                                    ToolPricing toolPricing) {
        long weekendDays = getWeekendDays(checkOutDate, returnDate, days);
        int numMemorialDays = findNumMemorialDayInTimeRange(checkOutDate, returnDate);
        int numIndependenceDays = findIndependenceDayInRange(checkOutDate, returnDate);
        int numHolidays = numMemorialDays + numIndependenceDays;
        long weekDays = days - weekendDays - numMemorialDays - numHolidays;

        // add whichever types of days should be charged to the return value
        long chargeDays = (toolPricing.isWeekday() ? weekDays : 0);
        if (toolPricing.isWeekend()) {
            chargeDays += weekendDays;
        }
        if (toolPricing.isHoliday()) {
            chargeDays += numHolidays;
        }
        return chargeDays;
    }

    /**
     * Calculates the number of weekend days the tool is rented. Begin by counting two weekend days for every Sunday the
     * tool is rented. Next, add a day if the tool is returned on a Saturday. Lastly, if the tool rental began on a
     * weekend, deduct one for each day of the weekend up to and including the checkout day.
     * @param checkOutDate - time of the tool rental
     * @param returnDate - time the tool should be returned
     * @param days - total number of days the tool is rented
     * @return - number of weekend days the tool is rented
     */
    public long getWeekendDays(LocalDateTime checkOutDate, LocalDateTime returnDate, int days) {
        DayOfWeek checkOutDateDayOfWeek = checkOutDate.getDayOfWeek();
        DayOfWeek returnDateDayOfWeek = returnDate.getDayOfWeek();

        int sundaysRented = ((days + checkOutDateDayOfWeek.getValue())/DayOfWeek.SUNDAY.getValue());
        int weekendDays = 2 * sundaysRented;
        if (returnDateDayOfWeek == DayOfWeek.SATURDAY)
            weekendDays++;
        // if the checkOutDate date is after Friday, deduct one weekend day for each day after Friday
        int currentWeekAdjustment = Math.max(0, checkOutDateDayOfWeek.getValue() - DayOfWeek.FRIDAY.getValue());
        return weekendDays - currentWeekAdjustment;
    }


    /**
     * Checks if Memorial Day of the year of the check-out date falls during the tool rental period. Next, if it is
     * different, checks if memorial day from the year of the return date falls within the tool rental period. Lastly,
     * if the difference between the check-out date and return date is more than a year, add a Memorial Day
     * for each year between the years of each date.
     * @param checkOutDate - time of the tool rental
     * @param returnDate - time the tool should be returned
     * @return - number of Memorial Days within the tool rental period
     */
    private int findNumMemorialDayInTimeRange(LocalDateTime checkOutDate, LocalDateTime returnDate) {
        LocalDateTime memorialDay =
                LocalDateTime.of(checkOutDate.getYear(), Month.MAY, 1, 0, 0, 0)
                .with(TemporalAdjusters.lastInMonth(DayOfWeek.MONDAY));

        int numMemorialDay = 0;
        if (memorialDay.isAfter(checkOutDate) && isBeforeOrEqual(memorialDay, returnDate))
            numMemorialDay++;
        if (checkOutDate.getYear() == returnDate.getYear()) {
            // return early since this is most likely scenario
            return numMemorialDay;
        }
        memorialDay =
                LocalDateTime.of(returnDate.getYear(), Month.MAY, 1, 0, 0, 0)
                        .with(TemporalAdjusters.lastInMonth(DayOfWeek.MONDAY));
        if (isBeforeOrEqual(memorialDay, returnDate))
            numMemorialDay++;

        numMemorialDay += Math.max(0, returnDate.getYear() - checkOutDate.getYear() - 1);

        return numMemorialDay;
    }

    /**
     * Checks if Independence Day of the year of the check-out date falls during the tool rental period. Next, if it is
     * different, checks if Independence Day from the year of the return date falls within the tool rental period. Lastly,
     * if the difference between the check-out date and return date is more than a year, add the Independence Day
     * for each year between the years of each date.
     * @param checkOutDate - time of the tool rental
     * @param returnDate - time the tool should be returned
     * @return - number of Independence Days within the tool rental period
     */
    private int findIndependenceDayInRange(LocalDateTime checkOutDate, LocalDateTime returnDate) {
        LocalDateTime independenceDay = getIndependenceDayObserved(checkOutDate.getYear());
        int numIndependenceDay = 0;
        if (independenceDay.isAfter(checkOutDate) && isBeforeOrEqual(independenceDay, returnDate))
            numIndependenceDay++;
        if (checkOutDate.getYear() == returnDate.getYear()) {
            // return early since this is most likely scenario
            return numIndependenceDay;
        }

        independenceDay = getIndependenceDayObserved(returnDate.getYear());
        if (isBeforeOrEqual(independenceDay, returnDate))
            numIndependenceDay++;

        numIndependenceDay += Math.max(0, returnDate.getYear() - checkOutDate.getYear() - 1);

        return numIndependenceDay;
    }

    private LocalDateTime getIndependenceDayObserved(int year) {
        LocalDateTime independenceDay = LocalDateTime.of(year, Month.JULY, 4, 0, 0, 0);
        if (independenceDay.getDayOfWeek() == DayOfWeek.SATURDAY) {
            return independenceDay.plusDays(-1);
        }
        if (independenceDay.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return independenceDay.plusDays(1);
        }
        return independenceDay;
    }

    private boolean isBeforeOrEqual(LocalDateTime date, LocalDateTime returnDate) {
        return date.isBefore(returnDate) || date.isEqual(returnDate);
    }

    @Autowired
    public void setToolRepository(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }
}
