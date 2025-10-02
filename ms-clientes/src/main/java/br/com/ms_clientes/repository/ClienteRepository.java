package br.com.ms_clientes.repository;

import br.com.ms_clientes.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
     Cliente findById(int id);
     Page<Cliente> findAll(Pageable pageable);
}
