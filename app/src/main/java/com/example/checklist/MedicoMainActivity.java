package com.example.checklist;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MedicoMainActivity extends AppCompatActivity {

    private static final String TAG = "MedicoMainActivity";
    private static final int ADD_EXAM_REQUEST_CODE = 1; // Código de solicitação para adicionar exame

    private ImageView iconHome, imageBottom;
    private ListView listViewExames;
    private ExameAdapter adapter;
    private List<Exame> exames;
    private TextView emptyTextView;
    private ExameDAO exameDAO;
    private String medicoEmail;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico_main);

        // Referências aos ícones e componentes da interface
        iconHome = findViewById(R.id.iconHome);
        imageBottom = findViewById(R.id.imageBottom);
        listViewExames = findViewById(R.id.listViewExames);
        emptyTextView = findViewById(R.id.emptyTextView);
        searchEditText = findViewById(R.id.searchEditText); // Adicionado

        exameDAO = new ExameDAO(this);

        // Lógica para o ícone de Home
        iconHome.setOnClickListener(v -> reloadHomeContent());

        // Lógica para o ícone de adicionar exame
        imageBottom.setOnClickListener(v -> {
            Intent intent = new Intent(MedicoMainActivity.this, AdicionarExameActivity.class);
            intent.putExtra("MEDICO_EMAIL", medicoEmail);
            startActivityForResult(intent, ADD_EXAM_REQUEST_CODE);
        });

        // Obtendo o e-mail do médico
        Intent intent = getIntent();
        medicoEmail = intent.getStringExtra("medico_email");

        Log.d(TAG, "E-mail do médico: " + medicoEmail);

        if (medicoEmail != null && !medicoEmail.isEmpty()) {
            loadExams(medicoEmail);
        } else {
            Log.e(TAG, "No medico email found in Intent");
            listViewExames.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        }

        // Adicionando listener para o campo de pesquisa
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchExams(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void loadExams(String medicoEmail) {
        Log.d(TAG, "Loading exams for medico email: " + medicoEmail);

        exames = exameDAO.obterExamesDoMedico(medicoEmail);

        if (exames != null) {
            if (!exames.isEmpty()) {
                Log.d(TAG, "Number of exams retrieved: " + exames.size());
                adapter = new ExameAdapter(this, exames);
                listViewExames.setAdapter(adapter);
                listViewExames.setVisibility(View.VISIBLE);
                emptyTextView.setVisibility(View.GONE);
            } else {
                Log.d(TAG, "Nenhum exame encontrado para o médico: " + medicoEmail);
                listViewExames.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.VISIBLE);
            }
        } else {
            Log.d(TAG, "Falha ao obter exames para o médico: " + medicoEmail);
            listViewExames.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        }
    }

    private void searchExams(String query) {
        exames = exameDAO.buscarExamesPorConsulta(query);

        if (exames != null) {
            if (!exames.isEmpty()) {
                Log.d(TAG, "Exames encontrados com a consulta: " + query);
                adapter = new ExameAdapter(this, exames);
                listViewExames.setAdapter(adapter);
                listViewExames.setVisibility(View.VISIBLE);
                emptyTextView.setVisibility(View.GONE);
            } else {
                Log.d(TAG, "Nenhum exame encontrado para a consulta: " + query);
                listViewExames.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.VISIBLE);
            }
        } else {
            Log.e(TAG, "Falha ao buscar exames para a consulta: " + query);
            listViewExames.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        }
    }

    private void reloadHomeContent() {
        if (medicoEmail != null && !medicoEmail.isEmpty()) {
            loadExams(medicoEmail);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EXAM_REQUEST_CODE && resultCode == RESULT_OK) {
            // Atualiza a lista de exames ao voltar de AdicionarExameActivity
            if (medicoEmail != null && !medicoEmail.isEmpty()) {
                loadExams(medicoEmail);
            }
        }
    }
}