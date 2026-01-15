package ecom.icet.Model.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerDto {
        private Long id;
        private String name;
        private String address;
        private String email;
        private String contactNumber;
        private String nic;
        private UserDto user;
}

