package ecom.icet.Service;

import ecom.icet.Model.Dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    Boolean validateUser(String username, String password);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
}
