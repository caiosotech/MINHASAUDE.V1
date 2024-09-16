package com.example.checklist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ExameDAO extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "exames.db";
    private static final int DATABASE_VERSION = 24; // Atualizado para nova versão

    private static final String TABLE_EXAMES = "exames";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOME_HOSPITAL = "nome_hospital";
    private static final String COLUMN_PACIENTE_CPF = "paciente_cpf";
    private static final String COLUMN_DATA = "data";
    private static final String COLUMN_MEDICO_EMAIL = "medico_email";
    private static final String COLUMN_NOME_MEDICO = "nome_medico";
    private static final String COLUMN_HORA = "hora";
    private static final String COLUMN_DESCRICAO_EXAME = "descricao_exame";
    private static final String COLUMN_ANEXO = "anexo";
    private static final String COLUMN_NOME_PACIENTE = "nome_paciente";

    public ExameDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_EXAMES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NOME_HOSPITAL + " TEXT, "
                + COLUMN_PACIENTE_CPF + " TEXT, "
                + COLUMN_DATA + " TEXT, "
                + COLUMN_MEDICO_EMAIL + " TEXT, "
                + COLUMN_NOME_MEDICO + " TEXT, "
                + COLUMN_HORA + " TEXT, "
                + COLUMN_DESCRICAO_EXAME + " TEXT, "
                + COLUMN_ANEXO + " TEXT, "
                + COLUMN_NOME_PACIENTE + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
        Log.d("ExameDAO", "Tabela criada: " + CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("ExameDAO", "Atualizando banco de dados da versão " + oldVersion + " para " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAMES);
        onCreate(db);
    }

    public boolean adicionarExame(Exame exame) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME_HOSPITAL, exame.getNomeHospital());
        values.put(COLUMN_PACIENTE_CPF, exame.getPacienteCPF());
        values.put(COLUMN_DATA, exame.getData());
        values.put(COLUMN_MEDICO_EMAIL, exame.getMedicoEmail());
        values.put(COLUMN_NOME_MEDICO, exame.getNomeMedico());
        values.put(COLUMN_HORA, exame.getHora());
        values.put(COLUMN_DESCRICAO_EXAME, exame.getDescricaoExame());
        values.put(COLUMN_ANEXO, exame.getAnexo());
        values.put(COLUMN_NOME_PACIENTE, exame.getNomePaciente());

        Log.d("ExameDAO", "Adicionando exame: " +
                "Data: " + exame.getData() + ", " +
                "NomeHospital: " + exame.getNomeHospital() + ", " +
                "MedicoEmail: " + exame.getMedicoEmail() + ", " +
                "NomeMedico: " + exame.getNomeMedico() + ", " +
                "PacienteCPF: " + exame.getPacienteCPF() + ", " +
                "Anexo: " + exame.getAnexo()); // Adicionado log para o campo anexo

        try {
            long result = db.insert(TABLE_EXAMES, null, values);
            if (result == -1) {
                Log.e("ExameDAO", "Falha ao adicionar exame");
                return false;  // Retorna false em caso de falha
            } else {
                Log.d("ExameDAO", "Exame adicionado com sucesso, ID: " + result);
                return true;  // Retorna true em caso de sucesso
            }
        } catch (SQLException e) {
            Log.e("ExameDAO", "Erro ao inserir exame: " + e.getMessage());
            return false;  // Retorna false em caso de exceção
        } finally {
            db.close();
        }
    }

    public List<Exame> obterExamesDoMedico(String medicoEmail) {
        List<Exame> listaExames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EXAMES,
                new String[]{COLUMN_ID, COLUMN_NOME_HOSPITAL, COLUMN_PACIENTE_CPF, COLUMN_DATA, COLUMN_DESCRICAO_EXAME, COLUMN_MEDICO_EMAIL, COLUMN_NOME_MEDICO, COLUMN_HORA, COLUMN_ANEXO, COLUMN_NOME_PACIENTE},
                COLUMN_MEDICO_EMAIL + "=?",
                new String[]{medicoEmail},
                null, null, null);

        Log.d("ExameDAO", "Consultando exames para o e-mail do médico: " + medicoEmail);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Exame exame = new Exame(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME_HOSPITAL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEDICO_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME_MEDICO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PACIENTE_CPF)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRICAO_EXAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANEXO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME_PACIENTE))
                );
                listaExames.add(exame);
                Log.d("ExameDAO", "Exame encontrado: " + exame.getDescricaoExame());
            } while (cursor.moveToNext());
        } else {
            Log.d("ExameDAO", "Nenhum exame encontrado para o e-mail do médico.");
        }

        if (cursor != null) {
            cursor.close();
        }

        return listaExames;
    }

    public List<Exame> obterExamesDoPaciente(String pacienteCPF) {
        List<Exame> listaExames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EXAMES,
                new String[]{COLUMN_ID, COLUMN_NOME_HOSPITAL, COLUMN_PACIENTE_CPF, COLUMN_DATA, COLUMN_DESCRICAO_EXAME, COLUMN_MEDICO_EMAIL, COLUMN_NOME_MEDICO, COLUMN_HORA, COLUMN_ANEXO, COLUMN_NOME_PACIENTE},
                COLUMN_PACIENTE_CPF + "=?",
                new String[]{pacienteCPF},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Exame exame = new Exame(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME_HOSPITAL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEDICO_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME_MEDICO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PACIENTE_CPF)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRICAO_EXAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANEXO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME_PACIENTE))
                );
                listaExames.add(exame);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return listaExames;
    }

    public List<Exame> buscarExamesPorConsulta(String query) {
        List<Exame> listaExames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EXAMES,
                new String[]{COLUMN_ID, COLUMN_NOME_HOSPITAL, COLUMN_PACIENTE_CPF, COLUMN_DATA, COLUMN_DESCRICAO_EXAME, COLUMN_MEDICO_EMAIL, COLUMN_NOME_MEDICO, COLUMN_HORA, COLUMN_ANEXO, COLUMN_NOME_PACIENTE},
                COLUMN_DESCRICAO_EXAME + " LIKE ? OR " + COLUMN_DATA + " LIKE ?",
                new String[]{"%" + query + "%", "%" + query + "%"},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Exame exame = new Exame(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME_HOSPITAL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEDICO_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME_MEDICO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PACIENTE_CPF)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRICAO_EXAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANEXO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME_PACIENTE))
                );
                listaExames.add(exame);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return listaExames;
    }

    public Cursor getAllExames() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXAMES, null);
        Log.d("ExameDAO", "Consultando todos os exames");
        return cursor;
    }

    public Exame getExameById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EXAMES,
                new String[]{COLUMN_ID, COLUMN_NOME_HOSPITAL, COLUMN_PACIENTE_CPF, COLUMN_DATA, COLUMN_MEDICO_EMAIL, COLUMN_NOME_MEDICO, COLUMN_HORA, COLUMN_DESCRICAO_EXAME, COLUMN_ANEXO, COLUMN_NOME_PACIENTE},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Exame exame = new Exame(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME_HOSPITAL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEDICO_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME_MEDICO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PACIENTE_CPF)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRICAO_EXAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANEXO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME_PACIENTE))
            );
            cursor.close();
            Log.d("ExameDAO", "Exame encontrado: " + exame.getDescricaoExame());
            return exame;
        }
        Log.d("ExameDAO", "Exame não encontrado, ID: " + id);
        return null;
    }

    public void atualizarExame(Exame exame) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME_HOSPITAL, exame.getNomeHospital());
        values.put(COLUMN_PACIENTE_CPF, exame.getPacienteCPF());
        values.put(COLUMN_DATA, exame.getData());
        values.put(COLUMN_MEDICO_EMAIL, exame.getMedicoEmail());
        values.put(COLUMN_NOME_MEDICO, exame.getNomeMedico());
        values.put(COLUMN_HORA, exame.getHora());
        values.put(COLUMN_DESCRICAO_EXAME, exame.getDescricaoExame());
        values.put(COLUMN_ANEXO, exame.getAnexo());
        values.put(COLUMN_NOME_PACIENTE, exame.getNomePaciente());

        try {
            int rowsAffected = db.update(TABLE_EXAMES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(exame.getId())});
            if (rowsAffected > 0) {
                Log.d("ExameDAO", "Exame atualizado com sucesso, ID: " + exame.getId());
            } else {
                Log.d("ExameDAO", "Nenhum exame atualizado, ID: " + exame.getId());
            }
        } catch (SQLException e) {
            Log.e("ExameDAO", "Erro ao atualizar exame: " + e.getMessage());
        } finally {
            db.close();
        }
    }



    public void removerExame(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int rowsDeleted = db.delete(TABLE_EXAMES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
            if (rowsDeleted > 0) {
                Log.d("ExameDAO", "Exame removido com sucesso, ID: " + id);
            } else {
                Log.d("ExameDAO", "Nenhum exame removido, ID: " + id);
            }
        } catch (SQLException e) {
            Log.e("ExameDAO", "Erro ao remover exame: " + e.getMessage());
        } finally {
            db.close();
        }
    }
}