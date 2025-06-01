package com.alex.web.microservices.songs.lib.songs.annoataion;

import com.alex.web.microservices.songs.lib.songs.validator.OrderColumnValidConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Positive;
import org.mapstruct.TargetType;

import java.lang.annotation.*;
import java.util.List;

@Constraint(validatedBy = OrderColumnValidConstraintValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderColumnValid {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
