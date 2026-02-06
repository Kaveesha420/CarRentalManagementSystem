package ecom.icet.Service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import ecom.icet.Model.Dto.UserDto;
import ecom.icet.Model.Entity.User;
import ecom.icet.Repository.UserRepository;
import ecom.icet.Service.AuditLogService;
import ecom.icet.Service.UserService;
import ecom.icet.Util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ObjectMapper mapper;
    private final AuditLogService auditLogService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = mapper.convertValue(userDto, User.class);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User lastUser = userRepository.findFirstByOrderByIdDesc();
        String lastId = (lastUser != null) ? lastUser.getId() : null;
        user.setId(IdGenerator.generateNextId(lastId, "USR"));

        user.setIsActive(true);
        User savedUser = userRepository.save(user);
        auditLogService.logAction("CREATE", "Registered new User: " + savedUser.getUsername());
        return mapper.convertValue(savedUser, UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers(int page,int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAll(pageable);
        List<UserDto> userDtoList = new ArrayList<>();

        for (User user : userPage){
            userDtoList.add(mapper.convertValue(user, UserDto.class));
        }
        return userDtoList;
    }

    @Override
    public UserDto getUserById(String id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> mapper.convertValue(value, UserDto.class)).orElse(null);
    }

    @Override
    public Boolean validateUser(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            if (user.get().getIsActive() == Boolean.FALSE) {
                return false;
            }
            return user.get().getPassword().equals(password);
        }
        return false;
    }

    @Override
    public UserDto updateUser(String id, UserDto userDto) {
        Optional<User> existingUserOptional = userRepository.findById(id);

        if (existingUserOptional.isPresent()) {
            User user = existingUserOptional.get();

            user.setUsername(userDto.getUsername());
            user.setEmail(userDto.getEmail());
            user.setRole(userDto.getRole());

            if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
                user.setPassword(userDto.getPassword());
            }

            User savedUser = userRepository.save(user);
            auditLogService.logAction("UPDATE", "Updated User profile: " + savedUser.getUsername());
            return mapper.convertValue(savedUser, UserDto.class);
        }
        return null;
    }

    @Override
    public void deleteUser(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User u = user.get();
            u.setIsActive(false);
            userRepository.save(u);

            auditLogService.logAction("DELETE", "Deactivated User: " + u.getUsername());
        }
    }
}