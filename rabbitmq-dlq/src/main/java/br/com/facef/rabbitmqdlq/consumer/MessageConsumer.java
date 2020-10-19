package br.com.facef.rabbitmqdlq.consumer;

import br.com.facef.rabbitmqdlq.configuration.DirectExchangeConfiguration;
import br.com.facef.rabbitmqdlq.model.Mensagem;
import br.com.facef.rabbitmqdlq.repository.MensagemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import br.com.facef.rabbitmqdlq.business.MensagemBusiness;


import java.util.ArrayList;
import java.util.Map;

@Configuration
@Slf4j
public class MessageConsumer {

    private MensagemBusiness mensagemBusiness;

    @Autowired
    public MessageConsumer (MensagemBusiness mensagemBusiness) {
        this.mensagemBusiness = mensagemBusiness;
    };

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = DirectExchangeConfiguration.ORDER_MESSAGES_QUEUE_NAME)
    public void processOrderMessage(Message message) {
        log.info("Processing message: {}", message.toString());
        // By default the messages will be requeued
        // throw new RuntimeException("Business Rule Exception");
        // To dont requeue message can throw AmqpRejectAndDontRequeueException
        throw new AmqpRejectAndDontRequeueException("Business Rule Exception");
    }

    @RabbitListener(queues = DirectExchangeConfiguration.ORDER_MESSAGES_QUEUE_DLQ_NAME)
    public void processOrderMessageDlq(Message message) {

        try {
            throw new RuntimeException("Business Rule Exception");
        } catch (Exception e) {
            log.info("Mensagem recebida na fila DLQ: {}", message.toString());

            Map<String, Object> headers = message.getMessageProperties().getHeaders();

            Integer tentativasEnvio = (Integer) headers.get(DirectExchangeConfiguration.X_RETRIES_HEADER);

            if (tentativasEnvio == null) {
                tentativasEnvio = Integer.valueOf(0);
            } else {
                log.info("Tentativas de envio: {}", tentativasEnvio);
            }

            if (tentativasEnvio > 3) {
                this.rabbitTemplate.send(DirectExchangeConfiguration.ORDER_MESSAGES_QUEUE_PARKING_LOT_NAME, message
                );
            } else {
                headers.put(DirectExchangeConfiguration.X_RETRIES_HEADER, tentativasEnvio + 1);
                this.rabbitTemplate.send(DirectExchangeConfiguration.ORDER_MESSAGES_QUEUE_DLQ_NAME, message);
            }
        }
    }

    @RabbitListener(queues = DirectExchangeConfiguration.ORDER_MESSAGES_QUEUE_PARKING_LOT_NAME)
    public void processOrderMessageParkingLot(Message message) {
        log.info("Mensagem recebida na fila Parking Lot: {}", new String(message.getBody()));
        Mensagem mensagem = new Mensagem(new String(message.getBody()));
        mensagemBusiness.save(mensagem);
    }

}




