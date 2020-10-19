package br.com.facef.rabbitmqdlq.business;

import br.com.facef.rabbitmqdlq.model.Mensagem;

public interface MensagemBusiness {
    Mensagem save(Mensagem mensagem);
}
