package com.example.checklist;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText usernameEditText, passwordEditText;
    private RelativeLayout loginButtonLayout, registerButtonLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButtonLayout = findViewById(R.id.loginButtonLayout);
        registerButtonLayout = findViewById(R.id.registerButtonLayout);

        loginButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        registerButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ChooseRegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login() {
        String email = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        Log.d(TAG, "Login attempt with email: " + email);

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar se é um médico
        MedicoDAO medicoDAO = new MedicoDAO(this);
        Medico medico = medicoDAO.buscarMedicoPorEmail(email);

        if (medico != null) {
            Log.d(TAG, "Médico encontrado: " + medico.getNome());
            if (medico.getSenha().equals(password)) {
                // Redirecionar para a página principal do médico
                Intent intent = new Intent(LoginActivity.this, MedicoMainActivity.class);
                intent.putExtra("medico_email", email); // Passa o e-mail do médico para a atividade principal
                startActivity(intent);
                finish();
                return; // Se médico for encontrado e autenticado, sair da função
            } else {
                Toast.makeText(this, "Credenciais inválidas. Tente novamente.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Verificar se é um paciente
        PacienteDAO pacienteDAO = new PacienteDAO(this);
        Paciente paciente = pacienteDAO.buscarPacientePorEmail(email);

        if (paciente != null) {
            Log.d(TAG, "Paciente encontrado: " + paciente.getNome());
            if (paciente.getSenha().equals(password)) {
                // Redirecionar para a página principal do paciente
                Intent intent = new Intent(LoginActivity.this, PacienteMainActivity.class);
                intent.putExtra("email_paciente", email); // Passa o e-mail do paciente para a atividade principal
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Credenciais inválidas. Tente novamente.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Caso nem médico nem paciente seja encontrado
            Toast.makeText(this, "Credenciais incorretas ou usuário não cadastrado.", Toast.LENGTH_SHORT).show();
        }
    }
}