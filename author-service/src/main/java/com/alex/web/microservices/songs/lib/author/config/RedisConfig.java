package com.alex.web.microservices.songs.lib.author.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
        //I used default RedisTemplate.It is not used.
   /* @Bean
    public RedisTemplate<String, Song> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Song> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }*/
}
//For Jedis-client. Spring boot starter redis use Lettuse-redis client by default.
  /*  @Bean
    public JedisConnectionFactory jedisConnectionFactory(ServiceConfig serviceConfig) {
        String hostname = serviceConfig.getHostRedis();
        int port = Integer.parseInt(serviceConfig.getPortRedis());
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(hostname, port);
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }*/

//For Lettuce-client. Spring boot starter redis use Lettuse-redis client by default.
 /*   @Bean
    public LettuceConnectionFactory redisConnectionFactory(*//*ServiceConfig serviceConfig*//*) {
        return new LettuceConnectionFactory("localhost"*//*serviceConfig.getHostRedis()*//*,6379*//*Integer.parseInt(serviceConfig.getPortRedis())*//*);
    }*/
       /* @Bean
        public LettuceConnectionFactory redisConnectionFactory() {
            return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379));
        }*/
