package com.alex.web.microservices.songs.lib.songs.search;

import com.alex.web.microservices.songs.lib.songs.model.Song;

import java.util.List;

public record PageDto(int currentPage,
                      int pageSize,
                      int totalPages,
                      int totalElements,
                      List<Song> content) {
}
