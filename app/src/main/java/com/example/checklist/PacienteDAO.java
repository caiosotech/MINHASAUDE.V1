package com.example.checklist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PacienteDAO {

    private static final String TABLE_PACIENTE = "paciente"; // Nome corrigido da tabela
    private static final String TAG = "PacienteDAO";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOME = "nome";
    private static final String COLUMN_CPF = "cpf";
    private static final String COLUMN_RG = "rg";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_SENHA = "senha";
    private static final String COLUMN_TIPO_USUARIO = "tipo_usuario";

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public PacienteDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    // Inserir paciente com verificação de duplicidade
    public long inserirPaciente(Paciente paciente) {
        long id = -1;

        try {
            if (verificarPacienteExistente(paciente)) {
                return -1; // Indica que o paciente já existe
            }

            ContentValues values = new ContentValues();
            values.put(COLUMN_NOME, paciente.getNome());
            values.put(COLUMN_CPF, paciente.getCpf());
            values.put(COLUMN_RG, paciente.getRg());
            values.put(COLUMN_EMAIL, paciente.getEmail());
            values.put(COLUMN_SENHA, paciente.getSenha());
            values.put(COLUMN_TIPO_USUARIO, paciente.getTipoUsuario());

            id = database.insert(TABLE_PACIENTE, null, values);
        } catch (SQLException e) {
            Log.e(TAG, "Erro ao inserir paciente: " + e.getMessage());
        }

        return id;
    }

    // Verificar se o paciente já existe com base no e-mail, CPF ou RG
    public boolean verificarPacienteExistente(Paciente paciente) {
        Cursor cursor = null;
        boolean existe = false;

        try {
            String selection = COLUMN_EMAIL + " = ? OR " + COLUMN_CPF + " = ? OR " + COLUMN_RG + " = ?";
            String[] selectionArgs = {paciente.getEmail(), paciente.getCpf(), paciente.getRg()};

            cursor = database.query(TABLE_PACIENTE, null, selection, selectionArgs, null, null, null);
            existe = cursor != null && cursor.getCount() > 0;
        } catch (SQLException e) {
            Log.e(TAG, "Erro ao verificar duplicidade de paciente: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return existe;
    }

    // Verificar se o paciente já está cadastrado com os mesmos e-mail ou CPF
    public boolean isPacienteDuplicado(String email, String cpf) {
        Cursor cursor = null;
        boolean duplicado = false;

        try {
            String selection = COLUMN_EMAIL + " = ? OR " + COLUMN_CPF + " = ?";
            String[] selectionArgs = {email, cpf};

            cursor = database.query(TABLE_PACIENTE, null, selection, selectionArgs, null, null, null);
            duplicado = cursor != null && cursor.getCount() > 0;
        } catch (SQLException e) {
            Log.e(TAG, "Erro ao verificar duplicidade de paciente: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return duplicado;
    }

    // Buscar paciente pelo e-mail
    public Paciente buscarPacientePorEmail(String email) {
        Paciente paciente = null;
        Cursor cursor = null;

        try {
            cursor = database.query(
                    TABLE_PACIENTE,
                    new String[]{COLUMN_ID, COLUMN_NOME, COLUMN_CPF, COLUMN_RG, COLUMN_EMAIL, COLUMN_SENHA, COLUMN_TIPO_USUARIO},
                    COLUMN_EMAIL + " = ?",
                    new String[]{email},
                    null, null, null
            );

            if (cursor != null && cursor.moveToFirst()) {
                paciente = getPacienteFromCursor(cursor);
            } else {
                Log.e(TAG, "Nenhum paciente encontrado com o e-mail: " + email);
            }
        } catch (SQLException e) {
            Log.e(TAG, "Erro ao buscar paciente: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return paciente;
    }

    // Atualizar informações do paciente
    public int atualizarPaciente(Paciente paciente) {
        int rowsAffected = 0;

        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NOME, paciente.getNome());
            values.put(COLUMN_CPF, paciente.getCpf());
            values.put(COLUMN_RG, paciente.getRg());
            values.put(COLUMN_EMAIL, paciente.getEmail());
            values.put(COLUMN_SENHA, paciente.getSenha());
            values.put(COLUMN_TIPO_USUARIO, paciente.getTipoUsuario());

            String whereClause = COLUMN_ID + " = ?";
            String[] whereArgs = {String.valueOf(paciente.getId())};

            rowsAffected = database.update(TABLE_PACIENTE, values, whereClause, whereArgs);
        } catch (SQLException e) {
            Log.e(TAG, "Erro ao atualizar paciente: " + e.getMessage());
        }

        return rowsAffected;
    }

    // Método auxiliar para extrair informações de um cursor
    private Paciente getPacienteFromCursor(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(COLUMN_ID);
        int nomeIndex = cursor.getColumnIndex(COLUMN_NOME);
        int cpfIndex = cursor.getColumnIndex(COLUMN_CPF);
        int rgIndex = cursor.getColumnIndex(COLUMN_RG);
        int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
        int senhaIndex = cursor.getColumnIndex(COLUMN_SENHA);
        int tipoUsuarioIndex = cursor.getColumnIndex(COLUMN_TIPO_USUARIO);

        return new Paciente(
                cursor.getLong(idIndex),
                cursor.getString(nomeIndex),
                cursor.getString(cpfIndex),
                cursor.getString(rgIndex),
                cursor.getString(emailIndex),
                cursor.getString(senhaIndex),
                cursor.getString(tipoUsuarioIndex)
        );
    }

    // Fechar a conexão com o banco de dados
    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}
