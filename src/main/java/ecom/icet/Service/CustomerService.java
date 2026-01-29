package ecom.icet.Service;

import ecom.icet.Model.Dto.CustomerDto;

import java.util.List;

public interface CustomerService {
    CustomerDto addCustomer(CustomerDto customerDto);
    List<CustomerDto> getAllCustomers();
    CustomerDto getCustomerById(String id);
    CustomerDto updateCustomer(String id , CustomerDto customerDto);
    void deleteCustomer(String id);
}
