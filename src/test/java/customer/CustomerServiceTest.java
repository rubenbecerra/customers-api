package customer;

import com.example.customers.dto.CustomerDTO;
import com.example.customers.dto.CustomerRegistrationRequest;
import com.example.customers.dto.CustomerUpdateRequest;
import com.example.customers.entity.Customer;
import com.example.customers.entity.Role;
import com.example.customers.mapper.CustomerDTOMapper;
import com.example.customers.repository.CustomerRepository;
import com.example.customers.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerDTOMapper customerDTOMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    private CustomerService underTest;


    @BeforeEach
    void setup() {
        underTest = new CustomerService(customerRepository,customerDTOMapper,passwordEncoder);
    }
    @Test
    void canGetAllCustomersDTO() {
        underTest.getAllCustomersDTO();
        verify(customerRepository).findAll();
    }
    @Test
    void canGetCustomerDTOById() {
        int id = 10;
        Customer customer = new Customer("alex", "alex@gmail.com", 25, "Male","123", Role.ROLE_USER);
        CustomerDTO customerDTO = new CustomerDTO(id, "alex", "alex@gmail.com", 25, "Male");

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customerDTOMapper.apply(customer)).thenReturn(customerDTO);

        CustomerDTO actual = underTest.getCustomerDTOById(id);
        assertThat(actual).isEqualTo(customerDTO);
    }
    @Test
    void willThrowWhenGetCustomerDTOByIdReturnsEmptyOptional() {
        int id = 10;
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getCustomerById(id)).isInstanceOf(NoSuchElementException.class)
                .hasMessage("Customer with ID " + id + " not found");
    }
    @Test
    void willThrowWHenDeleteCustomerDoesNotExist() {
        int id = 10;
        when(customerRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() ->underTest.deleteCustomer(id)).isInstanceOf(NoSuchElementException.class)
                .hasMessage("Client with ID " + id + " doesn't exist");

        verify(customerRepository, never()).deleteById(any());
    }

    @Test
    void canUpdateACustomer() {
        int id = 10;
        Customer customer = new Customer("alex","alex@gmail.com", 25,"Male","123",Role.ROLE_USER);
        CustomerUpdateRequest update = new CustomerUpdateRequest("alexandre", 22, "Male");

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        underTest.updateCustomer(id,update);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(update.name());
        assertThat(capturedCustomer.getAge()).isEqualTo(update.age());
        assertThat(capturedCustomer.getGender()).isEqualTo(update.gender());
    }
    @Test
    void willThrowWhenUpdateHasNoChanges() {
        int id = 10;
        Customer customer = new Customer("alex","alex@gmail.com",20,"Male","123",Role.ROLE_USER);
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest update = new CustomerUpdateRequest(
                "alex",20,"Male");
        assertThatThrownBy(() -> underTest.updateCustomer(id, update)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No data changes found");

        verify(customerRepository, never()).save(any());

    }
    @Test
    void canAddCustomer() {
        int id = 10;
        CustomerRegistrationRequest customer = new CustomerRegistrationRequest("alex","alex@gmail.com",20,"Male","123");

        underTest.addCustomer(customer);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(customer.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.age());
        assertThat(capturedCustomer.getGender()).isEqualTo(customer.gender());
    }
    @Test
    void canDeleteCustomer() {
        int id = 10;
        Customer customer = new Customer("alex","alex@gmail.com",20,"Male","123",Role.ROLE_USER);

        when(customerRepository.existsById(id)).thenReturn(true);
        underTest.deleteCustomer(id);

        verify(customerRepository).deleteById(10);
    }
    @Test
    void willThrowWhenCreateUserWithEmailInUse() {
        CustomerRegistrationRequest request = new CustomerRegistrationRequest("alex", "alex@gmail.com", 20,"Male","123");

        when(customerRepository.existsCustomerByEmail(request.email())).thenReturn(true);


        assertThatThrownBy(()->underTest.addCustomer(request)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("email already taken");

    }


}
