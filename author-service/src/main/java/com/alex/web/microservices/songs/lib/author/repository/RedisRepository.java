package com.alex.web.microservices.songs.lib.author.repository;

import com.alex.web.microservices.songs.lib.author.client.song.model.Song;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class as a repository and it contains my own  methods for interaction with redis-storage.
 * It contains different data structs:set,hash,key-value....
 */
@Repository
@RequiredArgsConstructor
public class RedisRepository {


    private  final RedisTemplate redisTemplate;

    private static final String KEY_PATTERN ="Song:%1$s";
    private static final String KEY_PATTERN_SET ="Author:%1$s:SongS:";
    private static final String KEY_PATTERN_HASH ="Author:%1$s:SongH";


    //key,value id,song
    public Song getSong(Long songId) {
        String keyPattern= KEY_PATTERN.formatted(songId);
        return (Song) redisTemplate.opsForValue().get(keyPattern);
    }
    public void saveSong(Song song) {
        String keyPattern= KEY_PATTERN.formatted(String.valueOf(song.getId()));
        redisTemplate.opsForValue().set(keyPattern,song);
    }
    public void deleteSong(Long songId){
        String key= KEY_PATTERN.formatted(String.valueOf(songId));
        redisTemplate.delete(key);
    }

    //set song
    public Set<Song> getSongSet( Long authorId) {
        String keyPattern= KEY_PATTERN_SET.formatted(String.valueOf(authorId));
        return (LinkedHashSet<Song>)redisTemplate.opsForSet().members(keyPattern);
    }
    public void saveSongSet(Song song) {
        String keySet= KEY_PATTERN_SET.formatted(String.valueOf(song.getAuthorId()));
        redisTemplate.opsForSet().add(keySet,song);
    }

    //hash(map) id,song
    public Map<Object,Song> getSongHash(Long authorId) {
        String keyPattern= KEY_PATTERN_HASH.formatted(String.valueOf(authorId));
        return (Map<Object,Song>)redisTemplate.opsForHash().entries(keyPattern);
    }
    public void saveSongHash(Song song) {
        String keyHash= KEY_PATTERN_HASH.formatted(String.valueOf(song.getAuthorId()));
        String key =KEY_PATTERN.formatted(String.valueOf(song.getId()));
        redisTemplate.opsForHash().put(keyHash,key,song);
    }
    public List<Song> getSongListBySetIds(Long authorId) {
        String keySet="Author:%1$d:Songs:Ids".formatted(authorId);
        Set<String> keysValue=(Set<String>)redisTemplate.opsForSet().members(keySet);

         return keysValue.stream()
                .map(key->(Song)redisTemplate.opsForValue().get(key))
                .collect(Collectors.toList());
    }
    public void saveSongSetIdAndValue(Song song){
        String keyValue=KEY_PATTERN.formatted(String.valueOf(song.getId()));
        String keySet="Author:%1$s:Songs:Ids".formatted(String.valueOf(song.getAuthorId()));
        String valueSet=KEY_PATTERN.formatted(song.getId());

        redisTemplate.opsForSet().add(keySet,valueSet);
        redisTemplate.opsForValue().set(keyValue,song);
    }
    public void deleteSongSetAndValue(Long authorId,Long songId ){
        String keyValue=KEY_PATTERN.formatted(String.valueOf(songId));
        String keySet="Author:%1$s:Songs:Ids".formatted(String.valueOf(authorId));
        redisTemplate.opsForSet().remove(keySet,keyValue);
        redisTemplate.delete(keyValue);
    }



}
