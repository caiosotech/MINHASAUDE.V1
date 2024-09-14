package com.example.checklist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MedicoDAO {

    private static final String TABLE_MEDICO = "medico"; // Nome da tabela ajustado
    private static final String TAG = "MedicoDAO";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOME = "nome";
    private static final String COLUMN_CPF = "cpf";
    private static final String COLUMN_RG = "rg";
    private static final String COLUMN_CRM = "crm";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_SENHA = "senha";
    private static final String COLUMN_TIPO_USUARIO = "tipo_usuario";

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context context; // Adicionei o campo Context

    public MedicoDAO(Context context) {
        this.context = context; // Inicialize o campo Context
        dbHelper = new DatabaseHelper(context);
        open();  // Abrir o banco de dados ao criar o DAO
    }

    // Abrir o banco de dados
    public void open() throws SQLException {
        if (dbHelper == null) {
            dbHelper = new DatabaseHelper(context);
        }
        if (database == null || !database.isOpen()) {
            database = dbHelper.getWritableDatabase();
        }
    }

    // Fechar o banco de dados
    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    // Inserir médico com verificação de duplicidade
    public long inserirMedico(Medico medico) {
        long id = -1;

        try {
            // Garantir que o banco de dados esteja aberto
            open();

            if (verificarMedicoExistente(medico)) {
                return -1; // Indica que o médico já existe
            }

            ContentValues values = new ContentValues();
            values.put(COLUMN_NOME, medico.getNome());
            values.put(COLUMN_CPF, medico.getCpf());
            values.put(COLUMN_RG, medico.getRg());
            values.put(COLUMN_CRM, medico.getCrm());

            // Certifique-se de que `medico.getEmail()` não é null
            String email = medico.getEmail();
            if (email == null) {
                Log.w(TAG, "E-mail do médico é null, inserindo como valor null no banco de dados.");
            }
            values.put(COLUMN_EMAIL, email);

            values.put(COLUMN_SENHA, medico.getSenha());
            values.put(COLUMN_TIPO_USUARIO, medico.getTipoUsuario());

            id = database.insert(TABLE_MEDICO, null, values); // Inserção no banco de dados
        } catch (SQLException e) {
            Log.e(TAG, "Erro ao inserir médico: " + e.getMessage());
        }

        return id;
    }

    // Verificar se o médico já existe com base no e-mail, CPF ou CRM
    public boolean verificarMedicoExistente(Medico medico) {
        Cursor cursor = null;
        boolean existe = false;

        try {
            String selection = COLUMN_EMAIL + " = ? OR " + COLUMN_CPF + " = ? OR " + COLUMN_CRM + " = ?";
            String[] selectionArgs = {medico.getEmail(), medico.getCpf(), medico.getCrm()};

            cursor = database.query(TABLE_MEDICO, null, selection, selectionArgs, null, null, null); // Corrija o nome da tabela se necessário
            existe = cursor != null && cursor.getCount() > 0;
        } catch (SQLException e) {
            Log.e(TAG, "Erro ao verificar duplicidade de médico: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return existe;
    }

    // Buscar médico pelo e-mail
    public Medico buscarMedicoPorEmail(String email) {
        Medico medico = null;
        Cursor cursor = null;

        try {
            cursor = database.query(
                    TABLE_MEDICO, // Corrija o nome da tabela se necessário
                    new String[]{COLUMN_ID, COLUMN_NOME, COLUMN_CPF, COLUMN_RG, COLUMN_CRM, COLUMN_EMAIL, COLUMN_SENHA, COLUMN_TIPO_USUARIO},
                    COLUMN_EMAIL + " = ?",
                    new String[]{email},
                    null, null, null
            );

            if (cursor != null && cursor.moveToFirst()) {
                medico = getMedicoFromCursor(cursor);
            } else {
                Log.e(TAG, "Nenhum médico encontrado com o e-mail: " + email);
            }
        } catch (SQLException e) {
            Log.e(TAG, "Erro ao buscar médico: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return medico;
    }

    // Atualizar informações do médico
    public int atualizarMedico(Medico medico) {
        int rowsAffected = 0;

        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NOME, medico.getNome());
            values.put(COLUMN_CPF, medico.getCpf());
            values.put(COLUMN_RG, medico.getRg());
            values.put(COLUMN_CRM, medico.getCrm());
            values.put(COLUMN_EMAIL, medico.getEmail());
            values.put(COLUMN_SENHA, medico.getSenha());
            values.put(COLUMN_TIPO_USUARIO, medico.getTipoUsuario());

            String whereClause = COLUMN_ID + " = ?";
            String[] whereArgs = {String.valueOf(medico.getId())};

            rowsAffected = database.update(TABLE_MEDICO, values, whereClause, whereArgs); // Corrija o nome da tabela se necessário
        } catch (SQLException e) {
            Log.e(TAG, "Erro ao atualizar médico: " + e.getMessage());
        }

        return rowsAffected;
    }

    // Método auxiliar para extrair informações de um cursor
    private Medico getMedicoFromCursor(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(COLUMN_ID);
        int nomeIndex = cursor.getColumnIndex(COLUMN_NOME);
        int cpfIndex = cursor.getColumnIndex(COLUMN_CPF);
        int rgIndex = cursor.getColumnIndex(COLUMN_RG);
        int crmIndex = cursor.getColumnIndex(COLUMN_CRM);
        int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
        int senhaIndex = cursor.getColumnIndex(COLUMN_SENHA);
        int tipoUsuarioIndex = cursor.getColumnIndex(COLUMN_TIPO_USUARIO);

        return new Medico(
                cursor.getLong(idIndex),
                cursor.getString(nomeIndex),
                cursor.getString(cpfIndex),
                cursor.getString(rgIndex),
                cursor.getString(crmIndex),
                cursor.getString(emailIndex),
                cursor.getString(senhaIndex),
                cursor.getString(tipoUsuarioIndex)
        );
    }
}