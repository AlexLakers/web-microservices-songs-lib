package com.alex.web.microservices.songs.lib.songs.mapper;

import com.alex.web.microservices.songs.lib.songs.client.model.Author;
import com.alex.web.microservices.songs.lib.songs.dto.WriteDto;
import com.alex.web.microservices.songs.lib.songs.model.Song;
import org.mapstruct.*;

import java.util.List;

/**
 * This is mapper for {@link Song songEntity}.
 */
@Mapper(builder = @Builder(disableBuilder = true),
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface SongMapper {
    Song toSong(WriteDto writeDto);

    /**
     * Performs mapping(copy) from entity to dto using our own map method.
     * @param writeDto dto.
     * @param song entity.
     */
    @Mapping(target = "authorId", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "name",expression = "java(map(writeDto.name(),song.getName()))")
    @Mapping(target = "album",expression = "java(map(writeDto.album(),song.getAlbum()))")
    void updateSong(WriteDto writeDto, @MappingTarget Song song);

    default String map(String dtoName, String entityName){
        if (dtoName != null && !dtoName.isBlank()) {
            return dtoName;
        }
        return entityName;
       /* if (album != null && !album.isBlank()) {
            song.setAlbum(album);
        }*/
    }

 /*   @BeforeMapping
    default void beforeUpdate(WriteDto source, @MappingTarget Song song) {
        if (source.name() != null && source.name().isBlank()) {
            song.setName(source.name());
        }
        if (source.album() != null && !source.album().isBlank()) {
            song.setAlbum(source.name());
        }
    }*/

}

