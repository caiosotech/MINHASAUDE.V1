        package com.example.checklist;

public class Medico {
    private long id;
    private String nome;
    private String cpf;
    private String rg;
    private String crm;
    private String email;
    private String senha; // Renomeado para senha
    private String tipoUsuario;

    // Construtor
    public Medico(long id, String nome, String cpf, String rg, String crm, String email, String senha, String tipoUsuario) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.rg = rg;
        this.crm = crm;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
    }

    // Construtor sem ID
    public Medico(String nome, String cpf, String rg, String crm, String email, String senha, String tipoUsuario) {
        this.nome = nome;
        this.cpf = cpf;
        this.rg = rg;
        this.crm = crm;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
    }

    // Getters e setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() { // Método getPassword renomeado para getSenha
        return senha;
    }

    public void setSenha(String senha) { // Método setPassword renomeado para setSenha
        this.senha = senha;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}