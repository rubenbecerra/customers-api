package customer;

import com.example.customers.Main;
import com.example.customers.auth.AuthenticationRequest;
import com.example.customers.dto.CustomerDTO;
import com.example.customers.dto.CustomerRegistrationRequest;
import com.example.customers.entity.Customer;
import com.example.customers.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.stream;

@SpringBootTest(
        classes = Main.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class CustomerIntegrationTest extends AbstractTestcontainersTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CustomerRepository customerRepository;

    private RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();

        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("Must register a new client successfully")
    void shouldRegisterCustomer() {
        String email = "alex-" + UUID.randomUUID() + "@example.com";
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Alex",
                email,
                25,
                "Male",
                "Password123!"
        );

        ResponseEntity<Void> response = restClient.post()
                .uri("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toBodilessEntity();

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(customerRepository.existsCustomerByEmail(email)).isTrue();
    }
    @Test
    @DisplayName("Must retrieve all the customers")
    void shouldGetAllCustomers() {
        String email1 = "user1-" + UUID.randomUUID() + "@gmail.com";
        String email2 = "user2-" + UUID.randomUUID() + "@gmail.com";

        CustomerRegistrationRequest request1 =  new CustomerRegistrationRequest(
                "user1",
                email1,
                25,
                "MALE",
                "123"
        );

        CustomerRegistrationRequest request2 = new CustomerRegistrationRequest(
                "user2",
                email2,
                29,
                "MALE",
                "1234"
        );
        restClient.post()
                .uri("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request1)
                .retrieve()
                .toBodilessEntity();

        AuthenticationRequest authReq = new AuthenticationRequest(
                email1,
                "123"
        );

        ResponseEntity<Void> loginResponse = restClient.post()
                .uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(authReq)
                .retrieve()
                .toBodilessEntity();

        String rawToken = loginResponse.getHeaders().getFirst("authorization");
        if (rawToken == null) {
            rawToken = loginResponse.getHeaders().getFirst("Authorization");
        }
        restClient.post()
                .uri("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request2)
                .retrieve()
                .toBodilessEntity();


        List<CustomerDTO> customers = restClient.get()
                .uri("api/v1/customers")
                .header("Authorization", "Bearer " + rawToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<List<CustomerDTO>>() {
                });
        assertThat(customers).isNotNull();
        assertThat(customers).hasSize(2);
        assertThat(customers).extracting(CustomerDTO::email)
                .containsExactlyInAnyOrder(email1,email2);
    }
    @Test
    @DisplayName("Should retrieve a customer with the specified id")
    void shouldGetCustomerById() {
        String email1 = "user1-" + UUID.randomUUID() + "@gmail.com";
        String email2 = "user2-" + UUID.randomUUID() + "@gmail.com";

        CustomerRegistrationRequest request1 =  new CustomerRegistrationRequest(
                "user1",
                email1,
                25,
                "MALE",
                "123"
        );

        CustomerRegistrationRequest request2 = new CustomerRegistrationRequest(
                "user2",
                email2,
                29,
                "MALE",
                "1234"
        );

        restClient.post()
                .uri("api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request1)
                .retrieve()
                .toBodilessEntity();

        AuthenticationRequest authReq = new AuthenticationRequest(
                email1,
                "123"
        );
        ResponseEntity<Void> loginResponse = restClient.post()
                .uri("api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(authReq)
                .retrieve()
                .toBodilessEntity();
        String rawToken = loginResponse.getHeaders().getFirst("authorization");
        if (rawToken == null) {
            rawToken = loginResponse.getHeaders().getFirst("Authorization");
        }

        restClient.post()
                .uri("api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request2)
                .retrieve().toBodilessEntity();

        List<CustomerDTO> allCustomers = restClient.get()
                .uri("api/v1/customers")
                .header("Authorization", "Bearer " + rawToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().body(new ParameterizedTypeReference<List<CustomerDTO>>() {
                });

        assert allCustomers != null;

        int id = allCustomers.stream().filter(c -> c.email().equals(email1))
                .map(CustomerDTO::id)
                .findFirst().orElseThrow();
        CustomerDTO customer = restClient.get()
                .uri("api/v1/customers/{id}",id)
                .header("Authorization", "Bearer " + rawToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().body(CustomerDTO.class);
        assertThat(customer).isNotNull();
        assertThat(customer.id()).isEqualTo(id);
        assertThat(customer.name()).isEqualTo("user1");
        assertThat(customer.email()).isEqualTo(email1);
        assertThat(customer.age()).isEqualTo(25);
        assertThat(customer.gender()).isEqualTo("MALE");

    }
    @Test
    @DisplayName("Should delete an user with the specified id")
    void shouldDeleteUserById() {
        String email1 = "user1-" + UUID.randomUUID() + "@gmail.com";
        String email2 = "user2-" + UUID.randomUUID() + "@gmail.com";

        CustomerRegistrationRequest request1 = new CustomerRegistrationRequest(
                "user1",
                email1,
                26,
                "FEMALE",
                "123"
        );
        CustomerRegistrationRequest request2 = new CustomerRegistrationRequest(
                "user2",
                email2,
                28,
                "MALE",
                "1234"
        );

        restClient.post()
                .uri("api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request1)
                .retrieve()
                .toBodilessEntity();

        AuthenticationRequest authReq = new AuthenticationRequest(
                email1,
                "123"
        );
        ResponseEntity<Void> loginResponse = restClient.post()
                .uri("api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(authReq)
                .retrieve().toBodilessEntity();

        String rawToken = loginResponse.getHeaders().getFirst("authorization");
        if (rawToken == null) {
            rawToken = loginResponse.getHeaders().getFirst("Authorization");
        }

        restClient.post()
                .uri("api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request2)
                .retrieve().toBodilessEntity();

        List<CustomerDTO> allCustomers = restClient.get()
                .uri("api/v1/customers")
                .header("Authorization", "Bearer " + rawToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().body(new ParameterizedTypeReference<List<CustomerDTO>>() {
                });

        assert allCustomers != null;
        int id = allCustomers.stream().filter(c -> c.email().equals(email1))
                .map(CustomerDTO::id).findFirst().orElseThrow();

        ResponseEntity<Void> deleteResponse = restClient.delete()
                .uri("api/v1/customers/{id}",id)
                .header("Authorization", "Bearer " + rawToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().toBodilessEntity();

        final String jwtToken = rawToken;
        assertThat(deleteResponse.getStatusCode().value()).isEqualTo(200);
        assertThat(customerRepository.existsById(id)).isFalse();
        assertThatThrownBy(() -> restClient.get()
                .uri("/api/v1/customers/{id}",id)
                .header("Authorization", "Bearer " + jwtToken)
                .retrieve().body(CustomerDTO.class)
        ).isInstanceOf(HttpClientErrorException.NotFound.class);

    }
    @Test
    @DisplayName("Should update the user's mail")
    void shouldUpdateEmail() {
        String originalEmail = "user1-" + UUID.randomUUID() + "@gmail.com";

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "user1",
                originalEmail,
                24,
                "MALE",
                "123"
        );
        restClient.post()
                .uri("api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toBodilessEntity();

        AuthenticationRequest authReq = new AuthenticationRequest(
                originalEmail,
                "123"
        );
        ResponseEntity<Void> loginResponse = restClient.post()
                .uri("api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(authReq)
                .retrieve().toBodilessEntity();

        final String rawToken = loginResponse.getHeaders().getFirst("Authorization");

        List<CustomerDTO> allCustomers = restClient.get()
                .uri("api/v1/customers")
                .header("Authorization", "Bearer " + rawToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<List<CustomerDTO>>() {
                });
        assert allCustomers != null;
        int id = allCustomers.stream().filter(c->c.email().equals(originalEmail))
                .map(CustomerDTO::id).findFirst().orElseThrow();
        String updatedEmail = "user1-" + UUID.randomUUID() + "@gmail.com";
        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(
                "Alex",
                updatedEmail,
                32,
                "MALE",
                "1234"
        );
        ResponseEntity<Void> updateResponse = restClient.put()
                .uri("api/v1/customers/{id}",id)
                .header("Authorization", "Bearer "+ rawToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(updateRequest)
                .retrieve().toBodilessEntity();

        assertThat(updateResponse.getStatusCode().value()).isEqualTo(200);

        Customer updatedInDb = customerRepository.findById(id).orElseThrow();
        assertThat(updatedInDb.getName()).isEqualTo("Alex");
        assertThat(updatedInDb.getEmail()).isEqualTo(updatedEmail);
        assertThat(updatedInDb.getAge()).isEqualTo(32);
    }
}