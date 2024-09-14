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
    private ImageView estrelaImageView;

    private String medicoEmail; // Armazena o e-mail do médico
    private ExameDAO exameDAO; // Variável para o ExameDAO

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
        estrelaImageView = findViewById(R.id.estrelaImageView);

        // Inicializa o ExameDAO
        exameDAO = new ExameDAO(this);

        // Recebe o e-mail do médico logado
        medicoEmail = getIntent().getStringExtra("MEDICO_EMAIL");

        // Adicione um log para verificar o e-mail do médico recebido
        Log.d("AdicionarExameActivity", "E-mail do médico recebido: " + medicoEmail);

        // Configura os listeners
        dataEditText.setOnClickListener(v -> showDatePicker());
        horaEditText.setOnClickListener(v -> showTimePicker());
        anexarArquivoButton.setOnClickListener(v -> selectFile());
        cancelarButton.setOnClickListener(v -> finish());  // Fecha a activity
        salvarButton.setOnClickListener(v -> saveExame());

        estrelaImageView.setOnClickListener(v -> toggleFavorito());
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
            Uri uri = data.getData();
            if (uri != null) {
                // Aqui você pode lidar com o arquivo selecionado
                Toast.makeText(this, "Arquivo selecionado: " + uri.getPath(), Toast.LENGTH_LONG).show();
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

        if (nomePaciente.isEmpty() || cpfPaciente.isEmpty() || descricaoExame.isEmpty() ||
                nomeHospital.isEmpty() || nomeMedico.isEmpty() || data.isEmpty() || hora.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        Exame exame = new Exame(descricaoExame, data, nomeHospital, medicoEmail, cpfPaciente);
        exameDAO.adicionarExame(exame);

        // Define o resultado para indicar que a lista precisa ser atualizada
        Intent resultIntent = new Intent();
        resultIntent.putExtra("EXAME_ADICIONADO", true);
        setResult(RESULT_OK, resultIntent);
        Toast.makeText(this, "Exame salvo com sucesso!", Toast.LENGTH_SHORT).show();
        finish();  // Fecha a activity após salvar o exame
    }


    private void toggleFavorito() {
        // Adicione a lógica para favoritar ou desfavoritar o exame
        Toast.makeText(this, "Favorito alternado", Toast.LENGTH_SHORT).show();
    }
}