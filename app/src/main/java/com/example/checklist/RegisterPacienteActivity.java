package com.example.checklist;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterPacienteActivity extends AppCompatActivity {

    private EditText nomeEditText, cpfEditText, rgEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private CheckBox termsCheckBox;
    private Button registerButton;
    private PacienteDAO pacienteDAO;

    private static final String TAG = "RegisterPacienteActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_paciente);

        // Inicializando os elementos da UI
        nomeEditText = findViewById(R.id.nomePacienteEditText);
        cpfEditText = findViewById(R.id.cpfPacienteEditText);
        rgEditText = findViewById(R.id.rgPacienteEditText);
        emailEditText = findViewById(R.id.emailPacienteEditText);
        passwordEditText = findViewById(R.id.passwordPacienteEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordPacienteEditText);
        termsCheckBox = findViewById(R.id.termsOfUseCheckBox);
        registerButton = findViewById(R.id.registerButton);

        // Inicializando o DAO
        pacienteDAO = new PacienteDAO(RegisterPacienteActivity.this);

        // Configurando o botão de registro
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarPaciente();
            }
        });
    }

    private void registrarPaciente() {
        String nome = nomeEditText.getText().toString().trim();
        String cpf = cpfEditText.getText().toString().trim();
        String rg = rgEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        Log.d(TAG, "Iniciando o registro do paciente");
        Log.d(TAG, "Nome: " + nome);
        Log.d(TAG, "CPF: " + cpf);
        Log.d(TAG, "RG: " + rg);
        Log.d(TAG, "Email: " + email);

        // Verificações de campo
        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(cpf) || TextUtils.isEmpty(rg)
                || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Erro: Campos obrigatórios não preenchidos");
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Erro: Senhas não coincidem");
            return;
        }

        if (!termsCheckBox.isChecked()) {
            Toast.makeText(this, "Você deve aceitar os Termos de Uso.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Erro: Termos de uso não aceitos");
            return;
        }

        // Cria um objeto Paciente
        Paciente paciente = new Paciente(nome, cpf, rg, email, password, "Paciente");

        // Verifica duplicidade antes de inserir
        if (pacienteDAO.verificarPacienteExistente(paciente)) {
            Toast.makeText(this, "Já existe um paciente com os mesmos dados.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Erro: Paciente já existente");
            return;
        }

        Log.d(TAG, "Dados do paciente prontos para inserção");

        // Lógica para salvar os dados do paciente no banco de dados
        long resultado = pacienteDAO.inserirPaciente(paciente);

        if (resultado != -1) {
            Toast.makeText(this, "Paciente registrado com sucesso!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Paciente registrado com sucesso!");

            // Após o registro bem-sucedido, inicie a PacienteMainActivity com o CPF do paciente
            Intent intent = new Intent(RegisterPacienteActivity.this, PacienteMainActivity.class);
            intent.putExtra("paciente_cpf", cpf); // Adiciona o CPF como extra
            startActivity(intent);
            finish(); // Fecha a atividade atual

        } else {
            Toast.makeText(this, "Erro ao registrar paciente.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Erro ao registrar paciente: resultado = " + resultado);
        }

        pacienteDAO.close();
    }
}