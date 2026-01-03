package com.teamproject.meeting.infrastructure.redis;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;



@Component
public class RedisUtil {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisUtil(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // key
    private String createRefreshKey(String email, String uuid) {
        return "RT:" + email + ":" + uuid;
    }

    // 저장
    public void saveRefreshToken(String email, String uuid, String refreshToken, long expiredTime) {
        String key = createRefreshKey(email, uuid);
        redisTemplate.opsForValue().set(key, refreshToken, expiredTime, TimeUnit.MILLISECONDS);
    }

    // 조회
    public String getRefreshToken(String email, String uuid) {
        return redisTemplate.opsForValue().get(createRefreshKey(email, uuid));
    }

    // 삭제
    public void deleteRefreshToken(String email, String uuid) {
        redisTemplate.delete(createRefreshKey(email, uuid));
    }

    // 존재 여부
    public boolean hasRefreshToken(String email, String uuid) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(createRefreshKey(email, uuid)));
    }

    private String createInviteKey(String token){
        return "RT:" + token;   // 초대링크 토큰 생성
    }

    public void saveInviteToken(String token, Long meetingId, long duration) {
          String key = createInviteKey(token);
             redisTemplate.opsForValue().set(key,String.valueOf(meetingId),duration); //토큰  저장
    }
    public Long     getMeetingIdByToken(String token) {
        String key = createInviteKey(token);
        String value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            return null; // 토큰이 없거나 만료됨
        }
        return Long.parseLong(value);
    }


}
