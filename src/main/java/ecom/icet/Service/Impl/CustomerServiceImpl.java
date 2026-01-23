package ecom.icet.Service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import ecom.icet.Model.Dto.CustomerDto;
import ecom.icet.Model.Entity.Customer;
import ecom.icet.Repository.CustomerRepository;
import ecom.icet.Service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ObjectMapper mapper;

    @Override
    public CustomerDto addCustomer(CustomerDto customerDto) {
        Customer customer = mapper.convertValue(customerDto, Customer.class);
        Customer savedCustomer = customerRepository.save(customer);
        return mapper.convertValue(savedCustomer, CustomerDto.class);
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        List<Customer> customerList = customerRepository.findAll();
        List<CustomerDto> dtoList = new ArrayList<>();

        for (Customer customer : customerList){
            dtoList.add(mapper.convertValue(customer, CustomerDto.class));
        }
        return dtoList;
    }

    @Override
    public CustomerDto getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(customer -> mapper.convertValue(customer, CustomerDto.class))
                .orElse(null);
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        Optional<Customer> existing = customerRepository.findById(id);
        if (existing.isPresent()){
            Customer customer = existing.get();
            customer.setName(customerDto.getName());
            customer.setAddress(customerDto.getAddress());
            customer.setContactNo(customerDto.getContactNumber());
            customer.setNic(customerDto.getNic());

            Customer updated = customerRepository.save(customer);
            return mapper.convertValue(updated, CustomerDto.class);
        }
        return null;
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
