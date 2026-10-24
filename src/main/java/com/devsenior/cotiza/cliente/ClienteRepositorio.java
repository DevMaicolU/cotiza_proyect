package com.devsenior.cotiza.cliente;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {

    boolean existsByTelefono(String telefono);

    Optional<Cliente> findByTelefono(String telefono);
}
