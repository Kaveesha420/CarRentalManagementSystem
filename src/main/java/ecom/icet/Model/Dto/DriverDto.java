package ecom.icet.Model.Dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DriverDto {
    private Long id;
    private String name;
    private String licenseNo;
    private String contactNo;
    private String status;
}
