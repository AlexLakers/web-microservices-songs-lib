package com.alex.web.microservices.songs.lib.songs.validator;


import com.alex.web.microservices.songs.lib.songs.annoataion.OrderDirectionValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


import java.util.Arrays;

/**
 * This class helps to check transferred direction of order('ASC,'DESC').
 */
public class OrderDirectionValidConstraintValidator implements ConstraintValidator<OrderDirectionValid,String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        return s==null || s.isBlank() || Arrays.stream(DIRECTION.values()).anyMatch(dir->s.equalsIgnoreCase(dir.name()));
    }
    private enum DIRECTION{
        ASC,
        DESC
    }
}
