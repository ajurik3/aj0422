package org.tool.aj0422.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.tool.aj0422.Application;
import org.tool.aj0422.LocalDateTimeConverter;
import org.tool.aj0422.exception.InvalidDiscountException;
import org.tool.aj0422.model.RentalAgreement;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {Application.class})
@SpringBootTest
public class RentalAgreementServiceTests {
    private RentalAgreementService rentalAgreementService;

    @ParameterizedTest
    @CsvFileSource(
            resources = "/rentalAgreementTests.csv",
            lineSeparator = ">"
    )
    void requiredTests(String toolCode, int rentalDayCount, double discount,
                       @ConvertWith(LocalDateTimeConverter.class) LocalDateTime checkout, String expectedOutput) {
        expectedOutput = expectedOutput.replace("\r\n", "\n");

        if (expectedOutput.equals("InvalidDiscountException")) {
            assertThrows(InvalidDiscountException.class, () ->
                    rentalAgreementService.getRentalAgreement(toolCode, rentalDayCount, discount, checkout));
        } else {
            RentalAgreement rentalAgreement =
                    rentalAgreementService.getRentalAgreement(toolCode, rentalDayCount, discount, checkout);
            assertEquals(rentalAgreement.toString(), expectedOutput);
        }
    }

    @Autowired
    public void setSaleService(RentalAgreementService rentalAgreementService) {
        this.rentalAgreementService = rentalAgreementService;
    }

}
