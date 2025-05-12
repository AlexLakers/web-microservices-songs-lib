package com.alex.web.microservices.songs.lib.author.mapper;

import com.alex.web.microservices.songs.lib.author.dto.WriteDto;
import com.alex.web.microservices.songs.lib.author.model.Author;
import com.alex.web.microservices.songs.lib.author.model.Name;
import org.mapstruct.*;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(builder = @Builder(disableBuilder = true),
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface AuthorMapper {

    @Mapping(target = "name",expression = "java(toName(writeDto.firstname(), writeDto.lastname()))")
    Author toAuthor(WriteDto writeDto);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void update (WriteDto source,@MappingTarget Author target);


    default Name toName(String firstname,String lastname) {
        if(firstname==null &&  lastname==null) {
            return null;
        }
        return new Name(firstname, lastname);
    }
}
