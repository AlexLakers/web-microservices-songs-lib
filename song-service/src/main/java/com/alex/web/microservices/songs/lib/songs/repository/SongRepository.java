package com.alex.web.microservices.songs.lib.songs.repository;

import com.alex.web.microservices.songs.lib.songs.exception.SongCreationException;
import com.alex.web.microservices.songs.lib.songs.search.PageDto;
import com.alex.web.microservices.songs.lib.songs.dto.WriteDto;
import com.alex.web.microservices.songs.lib.songs.model.Song;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class contains all the necessary operations with database 'song'.
 * It contains own methods and default methods.
 * @see Song song-entity.
 * @see Repository
 */

@Repository
@RequiredArgsConstructor
public class SongRepository {
    private final JdbcTemplate jdbcTemplate;

    public Optional<Song> findById(Long id) {
        String query = "SELECT id, name, album, author_id FROM song WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, getRowMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Song> findAllByAuthorId(Long authorId) {
        String query = "SELECT * FROM song WHERE author_id = ?";
        return jdbcTemplate.query(query, getRowMapper(), authorId);
    }

    public List<Song> findAllByIds(List<Long> ids) {
        List<Object[]> argsIds = ids.stream().map(id -> new Object[]{id}).collect(Collectors.toList());
        jdbcTemplate.execute("CREATE TEMPORARY TABLE tmp_song_ids(id BIGINT)");
        jdbcTemplate.batchUpdate("INSERT INTO tmp_song_ids(id) VALUES (?)", argsIds);
        String query = "SELECT * FROM song WHERE id IN(SELECT id FROM tmp_song_ids)";
        List<Song> songs = jdbcTemplate.query(query, getRowMapper());
        jdbcTemplate.update("DELETE FROM tmp_song_ids");
        return songs;

    }

    public boolean existByNameAndAuthorId(String name, Long authorId) {
        String query = "SELECT EXISTS(SELECT 1 FROM song WHERE name = ? AND author_id = ?)";
        return jdbcTemplate.queryForObject(query, Boolean.class, name, authorId);
    }

    public PageDto findAll(int page, Integer size, String orderColumn, String orderDirection) {
        int countElements = jdbcTemplate.queryForObject("SELECT COUNT(id) FROM song", Integer.class);
        int countPages = (int) Math.ceil(countElements * 1.0 / size);
        int offset = page > 0 ? page * size : 0;
        String query = "SELECT id, name, album, author_id FROM song ORDER BY " +
                       orderColumn + " " + orderDirection + " LIMIT ? OFFSET ?";
        List<Song> content = jdbcTemplate.query(query, getRowMapper(), size, offset);
        return new PageDto(page, size, countPages, countElements, content);
    }

    public boolean delete(Long id) {
        String query = "DELETE FROM song WHERE id = ?";
        return jdbcTemplate.update(query, id) > 0;
    }

    public Song save(Song song) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO song(name, album, author_id) VALUES(?,?,?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query,
                    new String[]{Song.Fields.id, Song.Fields.name, Song.Fields.album, "author_id"});
            ps.setObject(1, song.getName());
            ps.setObject(2, song.getAlbum());
            ps.setObject(3, song.getAuthorId());
            return ps;
        }, keyHolder);
        return map(keyHolder);
    }

    public Song update(Long songId, Song song) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "UPDATE song SET name=?, album=?, author_id=? WHERE id=?";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, song.getName());
            ps.setObject(2, song.getAlbum());
            ps.setObject(3, song.getAuthorId());
            ps.setObject(4, songId);
            return ps;
        }, keyHolder);
        return map(keyHolder);
    }

    private Song map(KeyHolder keyHolder) {
        return keyHolder.getKeyList().stream()
                .map((map) -> Song.builder()
                        .id((Long) map.get(Song.Fields.id))
                        .name((String) map.get(Song.Fields.name))
                        .album((String) map.get(Song.Fields.album))
                        .authorId((Long) map.get("author_id"))
                        .build())
                .findFirst().orElseThrow();
    }

    private RowMapper<Song> getRowMapper() {
        return (rs, rc) ->
                Song.builder()
                        .id(rs.getLong(Song.Fields.id))
                        .name(rs.getString(Song.Fields.name))
                        .album(rs.getString(Song.Fields.album))
                        .authorId(rs.getLong("author_id"))
                        .build();
    }
}
