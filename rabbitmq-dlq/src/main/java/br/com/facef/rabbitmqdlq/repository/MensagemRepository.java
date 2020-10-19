package br.com.facef.rabbitmqdlq.repository;

import br.com.facef.rabbitmqdlq.model.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Integer> {
}
