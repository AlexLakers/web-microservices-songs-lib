package com.alex.web.microservices.songs.lib.songs.validator;

import com.alex.web.microservices.songs.lib.songs.annoataion.OrderColumnValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class OrderColumnValidConstraintValidator implements ConstraintValidator<OrderColumnValid,String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        String[] columns=new String[]{"id","name","album","author_id"};
        return  s==null || s.isBlank() || Arrays.stream(columns).anyMatch(s::equalsIgnoreCase);

    }
}
