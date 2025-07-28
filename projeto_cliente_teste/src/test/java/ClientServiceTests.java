package com.iftm.client.repositories;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.seudominio.dto.ClientDTO;
import com.seudominio.entities.Client;
import com.seudominio.repositories.ClientRepository;
import com.seudominio.services.exceptions.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTests {

    @Mock
    private ClientRepository repository;

    @InjectMocks
    private ClientService service; 

    private Client client;
    private ClientDTO clientDTO;

    @BeforeEach
    void setUp() {
        client = new Client(1L, "Fulano", 5000.0, Instant.parse("2023-01-01T00:00:00Z"));
        clientDTO = new ClientDTO(client);
    }

    
    @Test
    void deleteShouldDoNothingWhenIdExists() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);
        
        assertDoesNotThrow(() -> service.delete(1L));
    }

    @Test
    void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
        when(repository.existsById(anyLong())).thenReturn(false);
        
        assertThrows(EmptyResultDataAccessException.class, () -> service.delete(1L));
    }

    @Test
void deleteShouldDoNothingWhenIdExists() {
    when(repository.existsById(1L)).thenReturn(true);
    doNothing().when(repository).deleteById(1L);
    
    assertDoesNotThrow(() -> service.delete(1L));
}

@Test
void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
    when(repository.existsById(anyLong())).thenReturn(false);
    
    assertThrows(EmptyResultDataAccessException.class, () -> service.delete(1L));
}

@Test
void findAllPagedShouldReturnPageOfClients() {
    PageRequest pageRequest = PageRequest.of(0, 10);
    when(repository.findAll(pageRequest)).thenReturn(new PageImpl<>(List.of(client)));
    
    Page<ClientDTO> result = service.findAllPaged(pageRequest);
    
    assertFalse(result.isEmpty());
    verify(repository).findAll(pageRequest);
}

@Test
void findByIncomeShouldReturnPageOfClients() {
    PageRequest pageRequest = PageRequest.of(0, 10);
    when(repository.findByIncome(5000.0, pageRequest)).thenReturn(new PageImpl<>(List.of(client))));
    
    Page<ClientDTO> result = service.findByIncome(5000.0, pageRequest);
    
    assertFalse(result.isEmpty());
    verify(repository).findByIncome(5000.0, pageRequest);
}

@Test
void findByIdShouldReturnClientDTOWhenIdExists() {
    when(repository.findById(1L)).thenReturn(Optional.of(client));
    
    ClientDTO result = service.findById(1L);
    
    assertNotNull(result);
    assertEquals("Fulano", result.getName());
}

@Test
void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
    when(repository.findById(anyLong())).thenReturn(Optional.empty());
    
    assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
}

@Test
void updateShouldReturnClientDTOWhenIdExists() {
    when(repository.findById(1L)).thenReturn(Optional.of(client));
    when(repository.save(any())).thenReturn(client);
    
    ClientDTO result = service.update(1L, clientDTO);
    
    assertNotNull(result);
    assertEquals("Fulano", result.getName());
}

@Test
void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
    when(repository.findById(anyLong())).thenReturn(Optional.empty());
    
    assertThrows(ResourceNotFoundException.class, () -> service.update(1L, clientDTO));
}

@Test
void insertShouldReturnClientDTO() {
    when(repository.save(any())).thenReturn(client);
    
    ClientDTO result = service.insert(clientDTO);
    
    assertNotNull(result);
    assertEquals("Fulano", result.getName());
}


}