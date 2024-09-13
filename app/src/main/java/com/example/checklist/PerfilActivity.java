/*package com.example.checklist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PerfilActivity extends AppCompatActivity {

    private TextView textViewNomeUsuario, textViewNome, textViewId;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Vincular as views do layout
        textViewNomeUsuario = findViewById(R.id.textViewNomeUsuario);
        textViewNome = findViewById(R.id.nomeususario);
        textViewId = findViewById(R.id.id);

        // Inicializar o DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Carregar informações do usuário logado a partir do banco de dados
        loadUserProfile();

        // Configurar o botão Voltar Home
        ImageView buttonVoltarHome = findViewById(R.id.buttonVoltarHome);
        buttonVoltarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Voltar para MedicoMainActivity
                Intent intent = new Intent(PerfilActivity.this, MedicoMainActivity.class);
                startActivity(intent);
                finish(); // Encerra a atividade atual para que ela não fique na pilha
            }
        });
    }

    private void loadUserProfile() {
        // Obter o e-mail do médico logado (aqui você precisará passar o e-mail de alguma forma, por exemplo, via Intent ou SharedPreferences)
        String medicoEmail = getIntent().getStringExtra("email_medico");

        if (medicoEmail == null) {
            // Caso não tenha o e-mail, mostrar mensagem de erro e sair
            textViewNomeUsuario.setText("E-mail não fornecido");
            textViewNome.setText("Nome não encontrado");
            textViewId.setText("ID: N/A");
            return;
        }

        // Recuperar os dados do banco de dados
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_MEDICO + " WHERE " + DatabaseHelper.COLUMN_MEDICO_EMAIL + " = ?", new String[]{medicoEmail});

        if (cursor.moveToFirst()) {
            // Use getColumnIndexOrThrow para evitar o retorno de -1
            int nomeIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MEDICO_NOME);
            int idIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MEDICO_ID);

            String nomeUsuario = cursor.getString(nomeIndex);
            int id = cursor.getInt(idIndex);

            // Preencher os TextViews com as informações do perfil
            textViewNomeUsuario.setText("Usuário: " + medicoEmail);
            textViewNome.setText("Nome: " + nomeUsuario);
            textViewId.setText("ID: " + id);
        } else {
            // Caso o médico não seja encontrado
            textViewNomeUsuario.setText("Usuário não encontrado");
            textViewNome.setText("Nome não encontrado");
            textViewId.setText("ID: N/A");
        }

        cursor.close();
        db.close();
    }
}
*/