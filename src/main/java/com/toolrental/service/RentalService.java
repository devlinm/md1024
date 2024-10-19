package com.toolrental.service;

import com.toolrental.domain.entity.RentalAgreement;
import com.toolrental.domain.entity.Tool;
import com.toolrental.domain.entity.ToolType;
import com.toolrental.domain.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Service for checking out rental tools. See {@link #checkout(String, int, int, LocalDate)}.
 * A {@link RentalAgreement} previously created can be fetched using {@link #getRentalAgreement(String)}
 */
@RestController
@RequestMapping("/rental")
public class RentalService {

    @Autowired
    private Repository<Tool, String> toolRepository;

    @Autowired
    private Repository<ToolType, String> toolTypeRepository;

    @Autowired
    private Repository<RentalAgreement, UUID> rentalAgreementRepository;

    /**
     * @param toolCode
     * @param rentalDayCount must be non-zero.
     * @param discountPercent must be between 0 and 100 inclusive.
     * @param checkoutDate the day the tool is checked out.
     * @return a RentalAgreement
     * @throws ValidationException if there is a validation rule violation. Or if there is no tool matching toolCode or no matching toolType for this 'toolCode'.
     */
    @PostMapping("/checkout")
    @ResponseBody
    public RentalAgreement checkout(@RequestParam("toolCode") String toolCode,
                                    @RequestParam("rentalDayCount") int rentalDayCount,
                                    @RequestParam("discount") int discountPercent,
                                    @RequestParam("checkoutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkoutDate) throws ValidationException {
        validateRentalDayCount(rentalDayCount);
        validateDiscountPercent(discountPercent);

        Tool tool = toolRepository.get(toolCode);
        validateNotNull(tool, "There is no Tool with code: " + toolCode);

        ToolType toolType = toolTypeRepository.get(tool.getType());
        validateNotNull(toolCode, "There is Tool Type with type: " + tool.getType());

        RentalAgreement rentalAgreement = RentalAgreement.createRentalAgreement(tool, toolType, checkoutDate, rentalDayCount, discountPercent);
        rentalAgreementRepository.create(rentalAgreement);

        return rentalAgreement;
    }

    @GetMapping("/rentalAgreement/{rentalAgreementId}")
    @ResponseBody
    public RentalAgreement getRentalAgreement(@PathVariable String rentalAgreementId) throws ValidationException {
        RentalAgreement rentalAgreement = rentalAgreementRepository.get(UUID.fromString(rentalAgreementId));
        validateNotNull(rentalAgreement, "Rental Agreement does not exist - uuid:" + rentalAgreementId);
        return rentalAgreement;
    }

    @PostMapping(value="/sign", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void sign(@RequestParam("rentalAgreementId") String uuid, @RequestBody byte[] digitalSignature) throws ValidationException {
        RentalAgreement rentalAgreement = getRentalAgreement(uuid);
        rentalAgreement.setDigitalSignature(digitalSignature);
        rentalAgreement.setState(RentalAgreement.State.ACTIVE);
    }

    @PostMapping("/cancel")
    public void cancel(@RequestParam("rentalAgreementId") String uuid) throws ValidationException {
        RentalAgreement rentalAgreement = getRentalAgreement(uuid);
        rentalAgreement.setState(RentalAgreement.State.CANCELLED);
    }

    private void validateNotNull(Object obj, String errorMessage) throws ValidationException {
        if (obj == null){
            throw new ValidationException(errorMessage);
        }
    }

    private void validateDiscountPercent(int discountPercent) throws ValidationException {
        if (discountPercent < 0 || discountPercent > 100){
            throw new ValidationException("Discount percentage must be a whole number in the range 0 to 100. Invalid value: " + discountPercent);
        }
    }

    private void validateRentalDayCount(int rentalDayCount) throws ValidationException {
        if (rentalDayCount < 1){
            throw new ValidationException("Rental day count must be 1 or greater. Invalid value: " + rentalDayCount);
        }
    }
}
