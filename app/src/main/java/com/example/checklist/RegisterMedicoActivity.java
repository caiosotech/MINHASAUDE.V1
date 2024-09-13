package com.example.checklist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class RegisterMedicoActivity extends AppCompatActivity {

    private static final String TAG = "RegisterMedicoActivity";
    private EditText nomeEditText, cpfEditText, rgEditText, crmEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private CheckBox termsCheckBox;
    private Button registerButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_medico);

        nomeEditText = findViewById(R.id.nomeMedicoEditText);
        cpfEditText = findViewById(R.id.cpfMedicoEditText);
        rgEditText = findViewById(R.id.rgMedicoEditText);
        crmEditText = findViewById(R.id.crmMedicoEditText);
        emailEditText = findViewById(R.id.emailMedicoEditText);
        passwordEditText = findViewById(R.id.passwordMedicoEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordMedicoEditText);
        termsCheckBox = findViewById(R.id.termsOfUseCheckBox);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarMedico();
            }
        });
    }

    private void registrarMedico() {
        String nome = nomeEditText.getText().toString().trim();
        String cpf = cpfEditText.getText().toString().trim();
        String rg = rgEditText.getText().toString().trim();
        String crm = crmEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        Log.d(TAG, "Attempting to register medico with email: " + email);

        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(cpf) || TextUtils.isEmpty(rg) || TextUtils.isEmpty(crm)
                || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Log.w(TAG, "Validation failed: One or more fields are empty");
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Log.w(TAG, "Password mismatch: Password and confirm password do not match");
            Toast.makeText(this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!termsCheckBox.isChecked()) {
            Log.w(TAG, "Terms not accepted: User did not agree to terms of use");
            Toast.makeText(this, "Você deve aceitar os Termos de Uso.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Creating Medico object with details: Nome: " + nome + ", CPF: " + cpf + ", RG: " + rg + ", CRM: " + crm + ", Email: " + email);

        Medico medico = new Medico(nome, cpf, rg, crm, email, password, "Médico");
        MedicoDAO medicoDAO = new MedicoDAO(RegisterMedicoActivity.this);

        long resultado = -1;
        try {
            resultado = medicoDAO.inserirMedico(medico);
        } catch (Exception e) {
            Log.e(TAG, "Error inserting medico into database: " + e.getMessage());
        }

        if (resultado != -1) {
            Log.d(TAG, "Medico registered successfully with email: " + email);
            Toast.makeText(this, "Médico registrado com sucesso!", Toast.LENGTH_SHORT).show();

            // Save email in SharedPreferences
            SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("medico_email", email);
            editor.apply();
            Log.d(TAG, "Email saved to SharedPreferences: " + email);

            // Pass email to MedicoMainActivity
            Intent intent = new Intent(RegisterMedicoActivity.this, MedicoMainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Log.d(TAG, "Failed to register medico with email: " + email);
            Toast.makeText(this, "Falha no registro. Tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }
}
