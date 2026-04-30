package ecom.icet.Service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import ecom.icet.Model.Dto.LoginRequest;
import ecom.icet.Model.Dto.UserDto;
import ecom.icet.Model.Entity.Customer;
import ecom.icet.Model.Entity.User;
import ecom.icet.Repository.CustomerRepository;
import ecom.icet.Repository.UserRepository;
import ecom.icet.Service.AuditLogService;
import ecom.icet.Service.UserService;
import ecom.icet.Util.IdGenerator;
import ecom.icet.Util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final ObjectMapper mapper;
    private final AuditLogService auditLogService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto addUser(UserDto userDto) {

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole() != null ? userDto.getRole() : "CUSTOMER");
        user.setIsActive(true);


        User lastUser = userRepository.findFirstByOrderByIdDesc();
        user.setId(IdGenerator.generateNextId(lastUser != null ? lastUser.getId() : null, "USR"));

        User savedUser = userRepository.save(user);

        Customer customer = new Customer();
        customer.setName(userDto.getName());
        customer.setAddress(userDto.getAddress());
        customer.setContactNo(userDto.getContactNo());
        customer.setEmail(userDto.getEmail());
        customer.setNic(userDto.getNic());
        customer.setUser(savedUser);

        customer.setId(IdGenerator.generateNextId(customerRepository.findFirstByOrderByIdDesc() != null ? customerRepository.findFirstByOrderByIdDesc().getId() : null, "CUS"));

        customerRepository.save(customer);

        auditLogService.logAction("CREATE", "Registered new User & Customer: " + savedUser.getUsername());
        return mapper.convertValue(savedUser, UserDto.class);
    }

    @Override
    public Page<UserDto> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return userRepository.findAll(pageable).map(user -> mapper.convertValue(user, UserDto.class));
    }

    @Override
    public UserDto getUserById(String id) {
        return userRepository.findById(id)
                .map(value -> mapper.convertValue(value, UserDto.class)).orElse(null);
    }

    @Override
    public Boolean validateUser(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            if (Boolean.FALSE.equals(user.get().getIsActive())) return false;
            return passwordEncoder.matches(password, user.get().getPassword());
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
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }

            User savedUser = userRepository.save(user);
            auditLogService.logAction("UPDATE", "Updated User profile: " + savedUser.getUsername());
            return mapper.convertValue(savedUser, UserDto.class);
        }
        return null;
    }

    @Override
    public void deleteUser(String id) {
        userRepository.findById(id).ifPresent(u -> {
            u.setIsActive(false);
            userRepository.save(u);
            auditLogService.logAction("DELETE", "Deactivated User: " + u.getUsername());
        });
    }

    @Override
    public String login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities(user.getRole())
                    .build();


            return jwtUtil.generateToken(userDetails, user.getId());
        } else {
            throw new IllegalArgumentException("Invalid password");
        }
    }
}