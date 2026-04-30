package ecom.icet.Service;

import ecom.icet.Model.Dto.LoginRequest;
import ecom.icet.Model.Dto.UserDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);
    Page<UserDto> getAllUsers(int page, int size);
    UserDto getUserById(String id);
    Boolean validateUser(String username, String password);
    UserDto updateUser(String id, UserDto userDto);
    void deleteUser(String id);

    String login(LoginRequest loginRequest);
}
