package com.example.checklist;

public class Exame {
    private String nomeExame;
    private String data;
    private String nomeHospital;
    private String medicoEmail;
    private String pacienteCPF;

    // Construtor com cinco parâmetros
    public Exame(String nomeExame, String data, String nomeHospital, String medicoEmail, String pacienteCPF) {
        this.nomeExame = nomeExame;
        this.data = data;
        this.nomeHospital = nomeHospital;
        this.medicoEmail = medicoEmail;
        this.pacienteCPF = pacienteCPF;
    }

    // Getters e setters
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

    // Método fictício para obter o nome do médico, ajustado conforme necessário
    public String getNomeMedico() {
        return "Nome do Médico"; // Retorne o nome real do médico, se possível
    }

    // Método fictício para obter o nome do paciente, ajustado conforme necessário
    public String getNomePaciente() {
        return "Nome do Paciente"; // Retorne o nome real do paciente, se possível
    }
}
