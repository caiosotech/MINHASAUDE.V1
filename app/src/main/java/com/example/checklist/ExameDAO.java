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
    private static final int DATABASE_VERSION = 18;

    private static final String TABLE_EXAMES = "exames";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOME_HOSPITAL = "nome_hospital";
    private static final String COLUMN_PACIENTE_CPF = "paciente_cpf";
    private static final String COLUMN_DATA = "data";
    private static final String COLUMN_NOME_EXAME = "nome_exame";
    private static final String COLUMN_MEDICO_EMAIL = "medico_email";

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
                + COLUMN_NOME_EXAME + " TEXT, "
                + COLUMN_MEDICO_EMAIL + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAMES);
        onCreate(db);
    }

    public void adicionarExame(Exame exame) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME_HOSPITAL, exame.getNomeHospital());
        values.put(COLUMN_PACIENTE_CPF, exame.getPacienteCPF());
        values.put(COLUMN_DATA, exame.getData());
        values.put(COLUMN_NOME_EXAME, exame.getNomeExame());
        values.put(COLUMN_MEDICO_EMAIL, exame.getMedicoEmail());
        Log.d("ExameDAO", "Adicionando exame: " +
                "NomeExame: " + exame.getNomeExame() + ", " +
                "Data: " + exame.getData() + ", " +
                "NomeHospital: " + exame.getNomeHospital() + ", " +
                "MedicoEmail: " + exame.getMedicoEmail() + ", " +
                "PacienteCPF: " + exame.getPacienteCPF());
        try {
            db.insert(TABLE_EXAMES, null, values);
        } catch (SQLException e) {
            Log.e("ExameDAO", "Error inserting exam: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    public Cursor getAllExames() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_EXAMES, null);
    }

    public Exame getExameById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EXAMES,
                new String[]{COLUMN_ID, COLUMN_NOME_HOSPITAL, COLUMN_PACIENTE_CPF, COLUMN_DATA, COLUMN_NOME_EXAME, COLUMN_MEDICO_EMAIL},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Exame exame = new Exame(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME_HOSPITAL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PACIENTE_CPF)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME_EXAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEDICO_EMAIL))
            );
            cursor.close();
            return exame;
        }
        return null;
    }

    public int updateExame(Exame exame) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME_HOSPITAL, exame.getNomeHospital());
        values.put(COLUMN_PACIENTE_CPF, exame.getPacienteCPF());
        values.put(COLUMN_DATA, exame.getData());
        values.put(COLUMN_NOME_EXAME, exame.getNomeExame());
        values.put(COLUMN_MEDICO_EMAIL, exame.getMedicoEmail());

        return db.update(TABLE_EXAMES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(exame.getId())});
    }

    public List<Exame> obterExamesDoMedico(String medicoEmail) {
        List<Exame> listaExames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Realiza a consulta com base no e-mail do médico
        Cursor cursor = db.query(TABLE_EXAMES,
                new String[]{COLUMN_ID, COLUMN_NOME_HOSPITAL, COLUMN_PACIENTE_CPF, COLUMN_DATA, COLUMN_NOME_EXAME, COLUMN_MEDICO_EMAIL},
                COLUMN_MEDICO_EMAIL + "=?",
                new String[]{medicoEmail},
                null, null, null);

        Log.d("ExameDAO", "Consultando exames para o e-mail do médico: " + medicoEmail);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Exame exame = new Exame(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME_HOSPITAL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PACIENTE_CPF)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME_EXAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEDICO_EMAIL))
                );
                listaExames.add(exame);
                Log.d("ExameDAO", "Exame encontrado: " + exame.getNomeExame());
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
                new String[]{COLUMN_ID, COLUMN_NOME_HOSPITAL, COLUMN_PACIENTE_CPF, COLUMN_DATA, COLUMN_NOME_EXAME, COLUMN_MEDICO_EMAIL},
                COLUMN_PACIENTE_CPF + "=?",
                new String[]{pacienteCPF},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Exame exame = new Exame(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME_HOSPITAL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PACIENTE_CPF)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME_EXAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEDICO_EMAIL))
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
                new String[]{COLUMN_ID, COLUMN_NOME_HOSPITAL, COLUMN_PACIENTE_CPF, COLUMN_DATA, COLUMN_NOME_EXAME, COLUMN_MEDICO_EMAIL},
                COLUMN_NOME_EXAME + " LIKE ? OR " + COLUMN_DATA + " LIKE ?",
                new String[]{"%" + query + "%", "%" + query + "%"},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Exame exame = new Exame(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME_HOSPITAL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PACIENTE_CPF)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME_EXAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEDICO_EMAIL))
                );
                listaExames.add(exame);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return listaExames;
    }

    public void deleteExame(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXAMES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}