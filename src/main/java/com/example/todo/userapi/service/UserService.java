package com.example.todo.userapi.service;

import com.example.todo.userapi.dto.request.UserRequestSignUpDTO;
import com.example.todo.userapi.dto.response.UserSignUpResponseDTO;
import com.example.todo.userapi.entity.User;
import com.example.todo.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
//    의존성주입이 빠져있어서 (외부에서 들고오는것) 우리가 수동으로 만들어줘야함 => 설정파일을 통해서 등록한다.

    // 회원가입 처리
    public UserSignUpResponseDTO create(final UserRequestSignUpDTO dto) {

        if (dto == null || dto.getEmail().equals("")) {

            throw new RuntimeException("가입 정보가 없습니다.");

        }

        String email = dto.getEmail();

        if (userRepository.existsByEmail(email)) {
            log.warn("이메일이 중복되었습니다. = {}", email);
            throw new RuntimeException("중복된 이메일 입니다.");
        }

        // 패스워드 인코딩
        String encoded = encoder.encode(dto.getPassword());
        dto.setPassword(encoded);

        // 유저 엔터티로 변환
        User user = dto.toEntity();
        User saved = userRepository.save(user);

        log.info("회원가입 정상 수행됨", saved);

        return new UserSignUpResponseDTO(saved);
    }


}
