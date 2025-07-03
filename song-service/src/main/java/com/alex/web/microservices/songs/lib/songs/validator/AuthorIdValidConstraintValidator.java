package com.alex.web.microservices.songs.lib.songs.validator;

import com.alex.web.microservices.songs.lib.songs.annoataion.AuthorIdValid;
import com.alex.web.microservices.songs.lib.songs.client.AuthorClient;
import com.alex.web.microservices.songs.lib.songs.client.model.Author;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This class allows to check author id during other operations in this service.
 * Occurs interaction with 'Author-service' using some HttpClient.
 */
@RequiredArgsConstructor
@Slf4j
public class AuthorIdValidConstraintValidator implements ConstraintValidator<AuthorIdValid, Long> {

    private final AuthorClient authorClient;

    @Override
    public boolean isValid(Long aLong, ConstraintValidatorContext constraintValidatorContext) {

        Author author = null;
        try {
            author = authorClient.getAuthor(aLong).get();
        } catch (Exception e) {
            return false;
        }
        log.info("The author :{} was founded by id: {}", author, aLong);
        return author.getId().equals(aLong);
    }
}
