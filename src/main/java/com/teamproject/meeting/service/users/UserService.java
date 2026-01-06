package com.teamproject.meeting.service.users;

import com.teamproject.meeting.dto.users.JoinDto;
import com.teamproject.meeting.entity.Users;
import com.teamproject.meeting.enums.users.Provider;
import com.teamproject.meeting.enums.users.Role;
import com.teamproject.meeting.port.UsersRepositoryPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UsersRepositoryPort usersRepositoryPort;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UsersRepositoryPort usersRepositoryPort, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usersRepositoryPort = usersRepositoryPort;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    @Transactional
    public void joinProcess(JoinDto joinDto) {

        String email = joinDto.getEmail();
        String password = joinDto.getPassword();
        String nickname = joinDto.getNickname();

        Boolean isExist = usersRepositoryPort.existsByEmail(email);

        if(isExist) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        Users user = new Users();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setNickname(nickname);
        user.setProvider(Provider.LOCAL);
        user.setRole(Role.USERS);

        usersRepositoryPort.saveUsers(user);
    }

    @Transactional
    public void changePW(Long userId, String currentPW, String newPW) {

        String storedHash = usersRepositoryPort.findPasswordHashByUserId(userId);

        if(!bCryptPasswordEncoder.matches(currentPW, storedHash)) {
            throw new IllegalArgumentException("현재 비밀번호 불일치");
        }

        String encodedPW = bCryptPasswordEncoder.encode(newPW);

        usersRepositoryPort.changePW(userId, encodedPW);
    }

     @Transactional
    public void changeNickname(String name, Long userId) {
        usersRepositoryPort.changeNickname(name, userId);
    }
}
