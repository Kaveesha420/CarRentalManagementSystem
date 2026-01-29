package ecom.icet.Model.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerDto {
        private String id;

        @NotBlank(message = "Name is required")
        private String name;

        @NotBlank(message = "Address is required")
        private String address;

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        private String email;

        @Pattern(regexp = "^\\d{10}$", message = "Contact number must be exactly 10 digits")
        private String contactNumber;

        @NotBlank(message = "Nic is required")
        private String nic;

        private UserDto user;
}

