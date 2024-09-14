package com.example.checklist;

public class Exame {
    private int id;
    private String nomeExame;
    private String data;
    private String nomeHospital;
    private String medicoEmail;
    private String pacienteCPF;

    // Construtor com todos os parâmetros (incluindo o 'id')
    public Exame(int id, String nomeExame, String data, String nomeHospital, String medicoEmail, String pacienteCPF) {
        this.id = id;
        this.nomeExame = nomeExame;
        this.data = data;
        this.nomeHospital = nomeHospital;
        this.medicoEmail = medicoEmail;
        this.pacienteCPF = pacienteCPF;
    }

    // Construtor sem o 'id' para novos registros
    public Exame(String nomeExame, String data, String nomeHospital, String medicoEmail, String pacienteCPF) {
        this.nomeExame = nomeExame;
        this.data = data;
        this.nomeHospital = nomeHospital;
        this.medicoEmail = medicoEmail;
        this.pacienteCPF = pacienteCPF;
    }

    // Getters e setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeExame() {
        return nomeExame;
    }

    public void setNomeExame(String nomeExame) {
        this.nomeExame = nomeExame;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNomeHospital() {
        return nomeHospital;
    }

    public void setNomeHospital(String nomeHospital) {
        this.nomeHospital = nomeHospital;
    }

    public String getMedicoEmail() {
        return medicoEmail;
    }

    public void setMedicoEmail(String medicoEmail) {
        this.medicoEmail = medicoEmail;
    }

    public String getPacienteCPF() {
        return pacienteCPF;
    }

    public void setPacienteCPF(String pacienteCPF) {
        this.pacienteCPF = pacienteCPF;
    }
}