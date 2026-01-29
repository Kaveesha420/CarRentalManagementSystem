package ecom.icet.Service;

import ecom.icet.Model.Dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);
    List<UserDto> getAllUsers();
    UserDto getUserById(String id);
    Boolean validateUser(String username, String password);
    UserDto updateUser(String id, UserDto userDto);
    void deleteUser(String id);
}
