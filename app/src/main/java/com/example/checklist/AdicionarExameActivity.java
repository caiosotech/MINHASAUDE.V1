package com.example.checklist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class AdicionarExameActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST_CODE = 1;
    private static final String TAG = "AdicionarExameActivity";

    private EditText nomePacienteEditText;
    private EditText cpfPacienteEditText;
    private EditText descricaoExameEditText;
    private EditText nomeHospitalEditText;
    private EditText nomeMedicoEditText;
    private EditText dataEditText;
    private EditText horaEditText;
    private Button anexarArquivoButton;
    private Button cancelarButton;
    private Button salvarButton;

    private String medicoEmail; // Armazena o e-mail do médico
    private ExameDAO exameDAO; // Variável para o ExameDAO
    private Uri uriAnexo; // Variável para armazenar o Uri do anexo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_exame);

        // Inicializa os campos
        nomePacienteEditText = findViewById(R.id.nomePacienteEditText);
        cpfPacienteEditText = findViewById(R.id.cpfPacienteEditText);
        descricaoExameEditText = findViewById(R.id.descricaoExameEditText);
        nomeHospitalEditText = findViewById(R.id.nomeHospitalEditText);
        nomeMedicoEditText = findViewById(R.id.nomeMedicoEditText);
        dataEditText = findViewById(R.id.dataEditText);
        horaEditText = findViewById(R.id.horaEditText);
        anexarArquivoButton = findViewById(R.id.anexarArquivoButton);
        cancelarButton = findViewById(R.id.cancelarButton);
        salvarButton = findViewById(R.id.salvarButton);

        // Inicializa o ExameDAO
        exameDAO = new ExameDAO(this);

        // Recebe o e-mail do médico logado
        medicoEmail = getIntent().getStringExtra("MEDICO_EMAIL");

        // Adicione um log para verificar o e-mail do médico recebido
        Log.d(TAG, "E-mail do médico recebido: " + medicoEmail);

        // Configura os listeners
        dataEditText.setOnClickListener(v -> showDatePicker());
        horaEditText.setOnClickListener(v -> showTimePicker());
        anexarArquivoButton.setOnClickListener(v -> selectFile());
        cancelarButton.setOnClickListener(v -> finish());  // Fecha a activity
        salvarButton.setOnClickListener(v -> saveExame());

    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String date = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            dataEditText.setText(date);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            String time = hourOfDay + ":" + String.format("%02d", minute1);
            horaEditText.setText(time);
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Selecionar Arquivo"), PICK_FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            uriAnexo = data.getData(); // Armazena o Uri selecionado
            if (uriAnexo != null) {
                Log.d(TAG, "Arquivo selecionado: " + uriAnexo.toString());
                Toast.makeText(this, "Arquivo selecionado: " + uriAnexo.getPath(), Toast.LENGTH_LONG).show();
            } else {
                Log.e(TAG, "Nenhum arquivo selecionado.");
                Toast.makeText(this, "Nenhum arquivo selecionado.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveExame() {
        String nomePaciente = nomePacienteEditText.getText().toString().trim();
        String cpfPaciente = cpfPacienteEditText.getText().toString().trim();
        String descricaoExame = descricaoExameEditText.getText().toString().trim();
        String nomeHospital = nomeHospitalEditText.getText().toString().trim();
        String nomeMedico = nomeMedicoEditText.getText().toString().trim();
        String data = dataEditText.getText().toString().trim();
        String hora = horaEditText.getText().toString().trim();

        Log.d(TAG, "Tentando salvar o exame com os seguintes dados:");
        Log.d(TAG, "Nome do Paciente: " + nomePaciente);
        Log.d(TAG, "CPF do Paciente: " + cpfPaciente);
        Log.d(TAG, "Descrição do Exame: " + descricaoExame);
        Log.d(TAG, "Nome do Hospital: " + nomeHospital);
        Log.d(TAG, "Nome do Médico: " + nomeMedico);
        Log.d(TAG, "Data: " + data);
        Log.d(TAG, "Hora: " + hora);
        Log.d(TAG, "E-mail do Médico: " + medicoEmail);

        if (nomePaciente.isEmpty() || cpfPaciente.isEmpty() || descricaoExame.isEmpty() ||
                nomeHospital.isEmpty() || nomeMedico.isEmpty() || data.isEmpty() || hora.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Erro: Campos obrigatórios não preenchidos.");
            return;
        }

        String anexo = (uriAnexo != null) ? uriAnexo.toString() : ""; // Converte o Uri para String, se não for nulo

        Exame exame = new Exame(data, nomeHospital, medicoEmail, nomeMedico, cpfPaciente, descricaoExame, hora, anexo, nomePaciente);
        Log.d(TAG, "Criado objeto Exame: " + exame.toString());

        boolean sucesso = exameDAO.adicionarExame(exame);

        if (sucesso) {
            Log.d(TAG, "Exame salvo com sucesso!");
            Toast.makeText(this, "Exame salvo com sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            Log.e(TAG, "Falha ao salvar o exame.");
            Toast.makeText(this, "Erro ao salvar exame.", Toast.LENGTH_SHORT).show();
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("EXAME_ADICIONADO", true);
        setResult(RESULT_OK, resultIntent);
        finish();  // Fecha a activity após salvar o exame
    }

    private void toggleFavorito() {
        // Adicione a lógica para favoritar ou desfavoritar o exame
        Toast.makeText(this, "Favorito alternado", Toast.LENGTH_SHORT).show();
    }
}
