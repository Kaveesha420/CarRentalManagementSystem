package ecom.icet.Service;

import ecom.icet.Model.Dto.CustomerDto;

import java.util.List;

public interface CustomerService {
    CustomerDto addCustomer(CustomerDto customerDto);
    List<CustomerDto> getAllCustomers();
    CustomerDto getCustomerById(Long id);
    CustomerDto updateCustomer(Long id , CustomerDto customerDto);
    void deleteCustomer(Long id);
}
