package com.alex.web.microservices.songs.lib.author.mapper;

import com.alex.web.microservices.songs.lib.author.dto.WriteDto;
import com.alex.web.microservices.songs.lib.author.model.Author;
import com.alex.web.microservices.songs.lib.author.model.Name;
import org.mapstruct.*;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * This class as a mapper for author-entity by map-struct framework.
 * @see Author author.
 * @see WriteDto dto.
 */
@Mapper(builder = @Builder(disableBuilder = true),
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface AuthorMapper {

    @Mapping(target = "name", expression = "java(toName(writeDto.firstname(), writeDto.lastname()))")
    Author toAuthor(WriteDto writeDto);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void update(WriteDto source, @MappingTarget Author target);


    default Name toName(String firstname, String lastname) {
        if (firstname == null && lastname == null) {
            return null;
        }
        return new Name(firstname, lastname);
    }

    @AfterMapping
    default void updateName(WriteDto source, @MappingTarget Author target) {
        if (source.firstname() != null && !source.firstname().isBlank()) {
            target.getName().setFirstname(source.firstname());
        }
        if (source.lastname() != null && !source.lastname().isBlank()) {
            target.getName().setLastname(source.lastname());
        }
    }
}
