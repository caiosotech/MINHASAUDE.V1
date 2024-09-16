package com.example.checklist;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class VisuExameMedico extends AppCompatActivity {

    private static final String TAG = "VisuExameMedico";
    private static final int REQUEST_CODE_PICK_FILE = 1;

    private TextView textViewNomePaciente, textViewCpfPaciente, textViewDescricaoExame, textViewNomeHospital;
    private TextView textViewNomeMedico, textViewData, textViewHora, textViewAnexo;
    private Button buttonBaixarAnexo, buttonVoltar;

    private String anexoUriString;
    private File downloadedFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visu_exame_medico);

        // Referências aos TextViews e Botões
        textViewNomePaciente = findViewById(R.id.textViewNomePaciente);
        textViewCpfPaciente = findViewById(R.id.textViewCpfPaciente);
        textViewDescricaoExame = findViewById(R.id.textViewDescricaoExame);
        textViewNomeHospital = findViewById(R.id.textViewNomeHospital);
        textViewNomeMedico = findViewById(R.id.textViewNomeMedico);
        textViewData = findViewById(R.id.textViewData);
        textViewHora = findViewById(R.id.textViewHora);
        textViewAnexo = findViewById(R.id.textViewAnexo);
        buttonVoltar = findViewById(R.id.buttonVoltar);
        buttonBaixarAnexo = findViewById(R.id.buttonBaixarAnexo);

        // Recebendo dados da Intent
        Intent intent = getIntent();
        String nomePaciente = intent.getStringExtra("nomePaciente");
        String cpfPaciente = intent.getStringExtra("cpfPaciente");
        String descricaoExame = intent.getStringExtra("descricaoExame");
        String nomeHospital = intent.getStringExtra("nomeHospital");
        String nomeMedico = intent.getStringExtra("nomeMedico");
        String data = intent.getStringExtra("data");
        String hora = intent.getStringExtra("hora");
        String anexo = intent.getStringExtra("anexo");

        // Definindo valores nos TextViews
        textViewNomePaciente.setText(nomePaciente);
        textViewCpfPaciente.setText(cpfPaciente);
        textViewDescricaoExame.setText(descricaoExame);
        textViewNomeHospital.setText(nomeHospital);
        textViewNomeMedico.setText(nomeMedico);
        textViewData.setText(data);
        textViewHora.setText(hora);
        textViewAnexo.setText(anexo);

        // Lógica do botão Voltar
        buttonVoltar.setOnClickListener(v -> finish());

        // Lógica do botão Baixar Anexo
        buttonBaixarAnexo.setOnClickListener(v -> {
            if (anexo != null && !anexo.isEmpty()) {
                Uri uri = Uri.parse(anexo);
                try {
                    downloadFile(uri);
                } catch (Exception e) {
                    Log.e(TAG, "Arquivo Baixado", e);
                    Toast.makeText(VisuExameMedico.this, "Arquivo Baixado", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(VisuExameMedico.this, "Anexo não disponível", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == RESULT_OK && resultData != null) {
            Uri uri = resultData.getData();
            if (uri != null) {
                try {
                    downloadFile(uri);
                } catch (Exception e) {
                    Log.e(TAG, "Arquivo Baixado", e);
                    Toast.makeText(VisuExameMedico.this, "Arquivo Baixado", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void downloadFile(Uri uri) {
        if (uri != null) {
            try {
                // Obter o ContentResolver
                ContentResolver contentResolver = getContentResolver();

                // Obter o nome do arquivo a partir do URI
                String fileName = "downloaded_file"; // Pode ajustar conforme necessário

                // Criar um arquivo temporário para armazenar o anexo
                File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName); // Usando Pictures para armazenar a imagem
                downloadedFile = file;

                // Abrir o InputStream do URI
                InputStream inputStream = contentResolver.openInputStream(uri);
                FileOutputStream outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                inputStream.close();
                outputStream.close();

                // Atualizar a UI para mostrar o caminho do arquivo
                textViewAnexo.setText(file.getAbsolutePath());

                // Abrir o arquivo salvo
                Uri fileUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
                Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                String mimeType = getContentResolver().getType(fileUri);
                viewIntent.setDataAndType(fileUri, mimeType != null ? mimeType : "*/*");
                viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(viewIntent);

                Log.d(TAG, "Anexo baixado e visualizado com sucesso.");
            } catch (Exception e) {
                Log.e(TAG, "Arquivo Baixado ", e);
                Toast.makeText(VisuExameMedico.this, "Arquivo Baixado", Toast.LENGTH_SHORT).show();
            }
        } else {
            // URI nulo
            Toast.makeText(VisuExameMedico.this, "Anexo não disponível", Toast.LENGTH_SHORT).show();
        }
    }

}