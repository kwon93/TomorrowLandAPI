package com.aaa.api.redis;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class RedisTest {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Test
    @DisplayName("embeddedRedis must be run")
    void embeddedRedisTest() {
        String key = "test: key";
        String value = "embeddedRedis";

        redisTemplate.opsForValue().set(key,value);
        String valueFromRedis = redisTemplate.opsForValue().get(key);

        Assertions.assertThat(valueFromRedis).isEqualTo(value);
    }
}
