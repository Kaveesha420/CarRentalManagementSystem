package ecom.icet.Model.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
        private Long id;
        private String name;
        private String password;
        private String email;
        private String role;
}
