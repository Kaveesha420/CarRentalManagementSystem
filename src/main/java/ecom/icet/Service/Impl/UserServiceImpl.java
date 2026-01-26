package ecom.icet.Service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import ecom.icet.Model.Dto.UserDto;
import ecom.icet.Model.Entity.User;
import ecom.icet.Repository.UserRepository;
import ecom.icet.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ObjectMapper mapper;

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = mapper.convertValue(userDto, User.class);
        User savedUser = userRepository.save(user);
        return mapper.convertValue(savedUser, UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> userList = userRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            userDtoList.add(mapper.convertValue(user, UserDto.class));
        }
        return userDtoList;
    }

    @Override
    public UserDto getUserById(Long id) {
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
    public UserDto updateUser(Long id, UserDto userDto) {
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
            return mapper.convertValue(savedUser, UserDto.class);
        }
        return null;
    }

    @Override
    public void deleteUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setIsActive(false);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }
}