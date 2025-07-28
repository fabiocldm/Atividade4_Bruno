package com.iftm.client.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.seupacote.dto.ClientDTO;
import com.seupacote.entities.Client;
import com.seupacote.repositories.ClientRepository;
import com.seupacote.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@ActiveProfiles("test") 
@Transactional 
public class ClientServiceIT {

    @Autowired
    private ClientService service;

    @Autowired
    private ClientRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Client client;
    private PageRequest pageRequest;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 999L;
        pageRequest = PageRequest.of(0, 10);

        
        client = new Client(null, "Fulano", 5000.0, Instant.parse("2023-01-01T00:00:00Z"));
        repository.save(client);
    }

    

    @Test
    void deleteShouldDoNothingWhenIdExists() {
        assertDoesNotThrow(() -> service.delete(existingId));
    }

    @Test
    void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
        assertThrows(EmptyResultDataAccessException.class, () -> service.delete(nonExistingId));
    }

    @Test
    void findAllPagedShouldReturnPageOfClients() {
        Page<ClientDTO> result = service.findAllPaged(pageRequest);
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findByIncomeShouldReturnPageOfClients() {
        Page<ClientDTO> result = service.findByIncome(5000.0, pageRequest);
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findByIdShouldReturnClientDTOWhenIdExists() {
        ClientDTO result = service.findById(existingId);
        assertNotNull(result);
        assertEquals("Fulano", result.getName());
    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(ResourceNotFoundException.class, () -> service.findById(nonExistingId));
    }

    @Test
    void updateShouldReturnClientDTOWhenIdExists() {
        ClientDTO dto = new ClientDTO(client);
        dto.setName("Novo Nome");
        
        ClientDTO result = service.update(existingId, dto);
        assertNotNull(result);
        assertEquals("Novo Nome", result.getName());
    }

    @Test
    void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        ClientDTO dto = new ClientDTO(client);
        assertThrows(ResourceNotFoundException.class, () -> service.update(nonExistingId, dto));
    }

    @Test
    void insertShouldReturnClientDTO() {
        ClientDTO newDto = new ClientDTO(null, "Novo Cliente", 3000.0, Instant.now());
        ClientDTO result = service.insert(newDto);
        
        assertNotNull(result.getId());
        assertEquals("Novo Cliente", result.getName());
    }
}