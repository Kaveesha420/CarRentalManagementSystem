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
        Optional<User> user = userRepository.findByName(username);
        if (user.isPresent()) {
            return user.get().getPassword().equals(password);
        }
        return false;
    }
}