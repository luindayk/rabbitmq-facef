package br.com.facef.rabbitmqdlq.model;

import javax.persistence.*;

@Entity
@Table(name = "message")
public class Mensagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String mensagem;

    public Mensagem() {
    }

    public Mensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
