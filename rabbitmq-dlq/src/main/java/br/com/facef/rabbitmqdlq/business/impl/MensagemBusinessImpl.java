package br.com.facef.rabbitmqdlq.business.impl;

import br.com.facef.rabbitmqdlq.business.MensagemBusiness;
import br.com.facef.rabbitmqdlq.model.Mensagem;
import br.com.facef.rabbitmqdlq.repository.MensagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MensagemBusinessImpl implements MensagemBusiness {

    private MensagemRepository mensagemRepository;

    @Autowired
    public MensagemBusinessImpl(MensagemRepository mensagemRepository) {
        this.mensagemRepository = mensagemRepository;
    }

    @Override
    public Mensagem save(Mensagem mensagem) {
        return mensagemRepository.save(mensagem);
    }
}
