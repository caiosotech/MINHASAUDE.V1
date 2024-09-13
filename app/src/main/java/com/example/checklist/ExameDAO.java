package com.example.checklist;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class ExameDAO extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "checklist.db";
    private static final int DATABASE_VERSION = 12; // Incrementa a versão para atualização

    // Nomes das tabelas e colunas
    private static final String TABLE_EXAMES = "exames";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOME_EXAME = "nome_exame";
    private static final String COLUMN_DATA = "data";
    private static final String COLUMN_NOME_HOSPITAL = "nome_hospital";
    private static final String COLUMN_MEDICO_EMAIL = "medico_email";
    private static final String COLUMN_PACIENTE_CPF = "paciente_cpf"; // Novo campo

    public ExameDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criação das tabelas
        String CREATE_EXAMES_TABLE = "CREATE TABLE " + TABLE_EXAMES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOME_EXAME + " TEXT, " +
                COLUMN_DATA + " TEXT, " +
                COLUMN_NOME_HOSPITAL + " TEXT, " +
                COLUMN_MEDICO_EMAIL + " TEXT, " +
                COLUMN_PACIENTE_CPF + " TEXT)";
        db.execSQL(CREATE_EXAMES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 6) {
            // Atualiza a tabela de exames adicionando o campo nome_hospital
            db.execSQL("ALTER TABLE " + TABLE_EXAMES + " ADD COLUMN " + COLUMN_NOME_HOSPITAL + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_EXAMES + " ADD COLUMN " + COLUMN_PACIENTE_CPF + " TEXT");
        }
        // Aqui você pode adicionar mais condições de upgrade para versões futuras
    }

    /**
     * Adiciona um novo exame ao banco de dados.
     */
    public void addExame(Exame exame) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME_EXAME, exame.getNomeExame());
        values.put(COLUMN_DATA, exame.getData());
        values.put(COLUMN_NOME_HOSPITAL, exame.getNomeHospital());
        values.put(COLUMN_MEDICO_EMAIL, exame.getMedicoEmail());
        values.put(COLUMN_PACIENTE_CPF, exame.getPacienteCPF()); // Adiciona o CPF do paciente

        db.insert(TABLE_EXAMES, null, values);
        db.close();
    }

    /**
     * Obtém a lista de exames associados ao e-mail do médico.
     */
    public List<Exame> obterExamesDoMedico(String medicoEmail) {
        List<Exame> exames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String SELECT_EXAMES_BY_MEDICO = "SELECT * FROM " + TABLE_EXAMES + " WHERE " + COLUMN_MEDICO_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(SELECT_EXAMES_BY_MEDICO, new String[]{medicoEmail});

        int dataIndex = cursor.getColumnIndex(COLUMN_DATA);
        int nomeHospitalIndex = cursor.getColumnIndex(COLUMN_NOME_HOSPITAL);
        int nomeExameIndex = cursor.getColumnIndex(COLUMN_NOME_EXAME);
        int medicoEmailIndex = cursor.getColumnIndex(COLUMN_MEDICO_EMAIL);
        int pacienteCpfIndex = cursor.getColumnIndex(COLUMN_PACIENTE_CPF);

        if (cursor.moveToFirst()) {
            do {
                String data = cursor.getString(dataIndex);
                String nomeHospital = cursor.getString(nomeHospitalIndex);
                String nomeExame = cursor.getString(nomeExameIndex);
                String emailMedico = cursor.getString(medicoEmailIndex);
                String pacienteCpf = cursor.getString(pacienteCpfIndex);

                Exame exame = new Exame(nomeExame, data, nomeHospital, emailMedico, pacienteCpf);
                exames.add(exame);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return exames;
    }

    /**
     * Obtém a lista de exames associados ao CPF do paciente.
     */
    public List<Exame> obterExamesDoPaciente(String pacienteCPF) {
        List<Exame> exames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String SELECT_EXAMES_BY_PACIENTE = "SELECT * FROM " + TABLE_EXAMES + " WHERE " + COLUMN_PACIENTE_CPF + " = ?";
        Cursor cursor = db.rawQuery(SELECT_EXAMES_BY_PACIENTE, new String[]{pacienteCPF});

        int dataIndex = cursor.getColumnIndex(COLUMN_DATA);
        int nomeHospitalIndex = cursor.getColumnIndex(COLUMN_NOME_HOSPITAL);
        int nomeExameIndex = cursor.getColumnIndex(COLUMN_NOME_EXAME);
        int medicoEmailIndex = cursor.getColumnIndex(COLUMN_MEDICO_EMAIL);
        int pacienteCpfIndex = cursor.getColumnIndex(COLUMN_PACIENTE_CPF);

        if (cursor.moveToFirst()) {
            do {
                String data = cursor.getString(dataIndex);
                String nomeHospital = cursor.getString(nomeHospitalIndex);
                String nomeExame = cursor.getString(nomeExameIndex);
                String emailMedico = cursor.getString(medicoEmailIndex);
                String cpfPaciente = cursor.getString(pacienteCpfIndex);

                Exame exame = new Exame(nomeExame, data, nomeHospital, emailMedico, cpfPaciente);
                exames.add(exame);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return exames;
    }

    /**
     * Busca exames com base na consulta fornecida.
     */
    public List<Exame> buscarExamesPorConsulta(String query) {
        List<Exame> exames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String SELECT_EXAMES_BY_QUERY = "SELECT * FROM " + TABLE_EXAMES +
                " WHERE " + COLUMN_NOME_EXAME + " LIKE ? OR " + COLUMN_NOME_HOSPITAL + " LIKE ?";
        Cursor cursor = db.rawQuery(SELECT_EXAMES_BY_QUERY, new String[]{"%" + query + "%", "%" + query + "%"});

        int dataIndex = cursor.getColumnIndex(COLUMN_DATA);
        int nomeHospitalIndex = cursor.getColumnIndex(COLUMN_NOME_HOSPITAL);
        int nomeExameIndex = cursor.getColumnIndex(COLUMN_NOME_EXAME);
        int medicoEmailIndex = cursor.getColumnIndex(COLUMN_MEDICO_EMAIL);
        int pacienteCpfIndex = cursor.getColumnIndex(COLUMN_PACIENTE_CPF);

        if (cursor.moveToFirst()) {
            do {
                String data = cursor.getString(dataIndex);
                String nomeHospital = cursor.getString(nomeHospitalIndex);
                String nomeExame = cursor.getString(nomeExameIndex);
                String emailMedico = cursor.getString(medicoEmailIndex);
                String cpfPaciente = cursor.getString(pacienteCpfIndex);

                Exame exame = new Exame(nomeExame, data, nomeHospital, emailMedico, cpfPaciente);
                exames.add(exame);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return exames;
    }
}