package com.example.checklist;

public class Exame {
    private int id;
    private String data;
    private String nomeHospital;
    private String medicoEmail; // E-mail do médico
    private String nomeMedico; // Nome do médico
    private String pacienteCPF;
    private String descricaoExame; // Novo campo
    private String hora; // Novo campo
    private String anexo; // Novo campo
    private String nomePaciente; // Novo campo

    // Construtor com todos os parâmetros (incluindo o 'id')
    public Exame(int id, String data, String nomeHospital, String medicoEmail, String nomeMedico, String pacienteCPF, String descricaoExame, String hora, String anexo, String nomePaciente) {
        this.id = id;
        this.data = data;
        this.nomeHospital = nomeHospital;
        this.medicoEmail = medicoEmail;
        this.nomeMedico = nomeMedico;
        this.pacienteCPF = pacienteCPF;
        this.descricaoExame = descricaoExame;
        this.hora = hora;
        this.anexo = anexo;
        this.nomePaciente = nomePaciente;
    }

    // Construtor sem o 'id' para novos registros
    public Exame(String data, String nomeHospital, String medicoEmail, String nomeMedico, String pacienteCPF, String descricaoExame, String hora, String anexo, String nomePaciente) {
        this(-1, data, nomeHospital, medicoEmail, nomeMedico, pacienteCPF, descricaoExame, hora, anexo, nomePaciente);
    }

    // Getters e setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getNomeMedico() {
        return nomeMedico;
    }

    public void setNomeMedico(String nomeMedico) {
        this.nomeMedico = nomeMedico;
    }

    public String getPacienteCPF() {
        return pacienteCPF;
    }

    public void setPacienteCPF(String pacienteCPF) {
        this.pacienteCPF = pacienteCPF;
    }

    public String getDescricaoExame() {
        return descricaoExame;
    }

    public void setDescricaoExame(String descricaoExame) {
        this.descricaoExame = descricaoExame;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getAnexo() {
        return anexo;
    }

    public void setAnexo(String anexo) {
        this.anexo = anexo;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    @Override
    public String toString() {
        return "Exame{" +
                "id=" + id +
                ", data='" + data + '\'' +
                ", nomeHospital='" + nomeHospital + '\'' +
                ", medicoEmail='" + medicoEmail + '\'' +
                ", nomeMedico='" + nomeMedico + '\'' +
                ", pacienteCPF='" + pacienteCPF + '\'' +
                ", descricaoExame='" + descricaoExame + '\'' +
                ", hora='" + hora + '\'' +
                ", anexo='" + anexo + '\'' +
                ", nomePaciente='" + nomePaciente + '\'' +
                '}';
    }
}