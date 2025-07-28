package com.iftm.client.repositories;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.iftm.client.entities.Client;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.time.Instant;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Modifying
	@Query("DELETE FROM Client obj WHERE "
			+ "obj.cpf = :cpf")
	void deleteClientByCPF(String cpf);

	@Query("SELECT DISTINCT obj FROM Client obj WHERE "
			+ "obj.cpf = :cpf")
	Optional<Client> findClientByCPf(String cpf);
	
	
	@Query("SELECT obj FROM Client obj WHERE "
			+ "LOWER(obj.name) LIKE %:nome%")
	List<Client> findClientsByNomeIgnoreCase(String nome);
	
	@Query("SELECT obj FROM Client obj WHERE "
			+ "LOWER(obj.name) = LOWER(:nome)")
	Optional<Client> findClientByNomeIgnoreCase(String nome);

	@Query("SELECT DISTINCT obj FROM Client obj WHERE "
			+ "obj.income >= :salario")
	List<Client> findClientBySalarioAcima(double salario);

	@Query("SELECT DISTINCT obj FROM Client obj WHERE "
			+ "obj.income <= :salario")

			 @Query("SELECT c FROM Client c WHERE LOWER(c.name) = LOWER(:name)")
    Client findByNameIgnoreCase(@Param("name") String name);

    

	List<Client> findClientBySalarioAbaixo(double salario);
	
	List<Client> findClientByIncomeBetween(double salarioMenor, double salarioMaior);
	
	List<Client> findClientBybirthDateBetween(Instant DataInicio, Instant DataTermino);

	List<Client> findClientByCpfLike(String cpfParcial);

	List<Client> findClientByChildrenGreaterThanEqualOrderByNameAsc(int numeroFilhos);
	
	//métodos desenvolvidos em sala
	
	void deleteByCpf(String cpfExistente);

	Optional<Client> findByCpf(String string);

	void deleteByIncomeGreaterThan(double salarioI);

	List<Client> findByIncomeGreaterThan(double salarioI);
	
	List<Client> findByCpfLike(String parteCpf);

	Page<Client> findByIncome(Double income, Pageable pageable);
	
	Page<Client> findByIncomeGreaterThan(double salarioI, Pageable pageable);
	Page<Client> findByCpfLike(String parteCpf, Pageable pageable);
	Page<Client> findByCpfStartingWith(String parteCpf, Pageable pageable);

	// Busca clientes cujo nome contém um texto (ignorando case)
    @Query("SELECT c FROM Client c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :text, '%'))")
    List<Client> findByNameContainingIgnoreCase(@Param("text") String text);

    // Busca clientes com salário maior que um valor
    List<Client> findBySalaryGreaterThan(Double salary);

    // Busca clientes com salário menor que um valor
    List<Client> findBySalaryLessThan(Double salary);

    // Busca clientes com salário entre dois valores
    List<Client> findBySalaryBetween(Double minSalary, Double maxSalary);

    // Busca clientes por intervalo de data de nascimento
    List<Client> findByBirthDateBetween(Instant startDate, Instant endDate);
}
