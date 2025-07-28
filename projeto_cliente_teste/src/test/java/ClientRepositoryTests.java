package com.iftm.client.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.Instant;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ClientRepositoryTests {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void testFindByNameIgnoreCase_ExistingName_ReturnsClient() {
        Client client = new Client(null, "Bruno", 5000.0, Instant.parse("1990-01-01T00:00:00Z"));
        clientRepository.save(client);

        Client foundClient = clientRepository.findByNameIgnoreCase("bruno");
        assertThat(foundClient).isNotNull();
        assertThat(foundClient.getName()).isEqualTo("Bruno");
    }

    @Test
    public void testFindByNameContainingIgnoreCase_ExistingText_ReturnsClients() {
        Client client1 = new Client(null, "Bruno Silva", 5000.0, Instant.now());
        Client client2 = new Client(null, "Ana Bruno", 3000.0, Instant.now());
        clientRepository.saveAll(List.of(client1, client2));

        List<Client> clients = clientRepository.findByNameContainingIgnoreCase("bruno");
        assertThat(clients).hasSize(2);
    }

    @Test
    public void testFindBySalaryBetween_ReturnsClientsInRange() {
        Client client1 = new Client(null, "Maria", 3000.0, Instant.now());
        Client client2 = new Client(null, "João", 5000.0, Instant.now());
        clientRepository.saveAll(List.of(client1, client2));

        List<Client> clients = clientRepository.findBySalaryBetween(4000.0, 6000.0);
        assertThat(clients).hasSize(1);
        assertThat(clients.get(0).getName()).isEqualTo("João");
    }

    @Test
    public void testFindByBirthDateBetween_ReturnsClientsInDateRange() {
        Instant startDate = Instant.parse("1990-01-01T00:00:00Z");
        Instant endDate = Instant.now();

        Client client1 = new Client(null, "Carlos", 4000.0, Instant.parse("1995-05-15T00:00:00Z"));
        Client client2 = new Client(null, "Paula", 6000.0, Instant.parse("2000-10-20T00:00:00Z"));
        clientRepository.saveAll(List.of(client1, client2));

        List<Client> clients = clientRepository.findByBirthDateBetween(startDate, endDate);
        assertThat(clients).hasSize(2);
    }

    @Test
public void testUpdateClient_ModifyNameSalaryAndBirthDate_ChangesPersisted() {
    Client client = new Client(null, "Original Name", 1000.0, Instant.parse("1980-01-01T00:00:00Z"));
    clientRepository.save(client);

    client.setName("Updated Name");
    client.setSalary(2000.0);
    client.setBirthDate(Instant.parse("1990-01-01T00:00:00Z"));
    clientRepository.save(client);

    Client updatedClient = clientRepository.findById(client.getId()).orElseThrow();
    assertThat(updatedClient.getName()).isEqualTo("Updated Name");
    assertThat(updatedClient.getSalary()).isEqualTo(2000.0);
    assertThat(updatedClient.getBirthDate()).isEqualTo(Instant.parse("1990-01-01T00:00:00Z"));
}
}