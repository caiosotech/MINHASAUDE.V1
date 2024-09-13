package com.example.checklist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "checklist.db";
    private static final int DATABASE_VERSION = 12; // Versão atual do banco de dados

    // Nome das tabelas
    public static final String TABLE_MEDICO = "medico";
    public static final String TABLE_PACIENTE = "paciente";
    public static final String TABLE_EXAMES = "exames";

    // Colunas da tabela Medico
    public static final String COLUMN_MEDICO_ID = "id";
    public static final String COLUMN_MEDICO_NOME = "nome";
    public static final String COLUMN_MEDICO_CPF = "cpf";
    public static final String COLUMN_MEDICO_RG = "rg";
    public static final String COLUMN_MEDICO_CRM = "crm";
    public static final String COLUMN_MEDICO_EMAIL = "email";
    public static final String COLUMN_MEDICO_SENHA = "senha";
    public static final String COLUMN_MEDICO_TIPO_USUARIO = "tipo_usuario";

    // Colunas da tabela Paciente
    public static final String COLUMN_PACIENTE_ID = "id";
    public static final String COLUMN_PACIENTE_NOME = "nome";
    public static final String COLUMN_PACIENTE_CPF = "cpf";
    public static final String COLUMN_PACIENTE_RG = "rg";
    public static final String COLUMN_PACIENTE_EMAIL = "email";
    public static final String COLUMN_PACIENTE_SENHA = "senha";
    public static final String COLUMN_PACIENTE_TIPO_USUARIO = "tipo_usuario";

    // Colunas da tabela Exames
    public static final String COLUMN_EXAME_ID = "id";
    public static final String COLUMN_EXAME_NOME = "nome";
    public static final String COLUMN_EXAME_DATA = "data";
    public static final String COLUMN_EXAME_MEDICO_EMAIL = "medico_email";
    public static final String COLUMN_EXAME_NOME_HOSPITAL = "nome_hospital"; // Adicionando a coluna nome_hospital

    private static final String TABLE_CREATE_MEDICO = "CREATE TABLE " + TABLE_MEDICO + " (" +
            COLUMN_MEDICO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_MEDICO_NOME + " TEXT NOT NULL, " +
            COLUMN_MEDICO_CPF + " TEXT NOT NULL UNIQUE, " +
            COLUMN_MEDICO_RG + " TEXT NOT NULL, " +
            COLUMN_MEDICO_CRM + " TEXT NOT NULL UNIQUE, " +
            COLUMN_MEDICO_EMAIL + " TEXT NOT NULL UNIQUE, " +
            COLUMN_MEDICO_SENHA + " TEXT NOT NULL, " +
            COLUMN_MEDICO_TIPO_USUARIO + " TEXT NOT NULL" +
            ");";

    private static final String TABLE_CREATE_PACIENTE = "CREATE TABLE " + TABLE_PACIENTE + " (" +
            COLUMN_PACIENTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PACIENTE_NOME + " TEXT NOT NULL, " +
            COLUMN_PACIENTE_CPF + " TEXT NOT NULL UNIQUE, " +
            COLUMN_PACIENTE_RG + " TEXT NOT NULL, " +
            COLUMN_PACIENTE_EMAIL + " TEXT NOT NULL UNIQUE, " +
            COLUMN_PACIENTE_SENHA + " TEXT NOT NULL, " +
            COLUMN_PACIENTE_TIPO_USUARIO + " TEXT NOT NULL" +
            ");";

    private static final String TABLE_CREATE_EXAMES = "CREATE TABLE " + TABLE_EXAMES + " (" +
            COLUMN_EXAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_EXAME_NOME + " TEXT NOT NULL, " +
            COLUMN_EXAME_DATA + " TEXT NOT NULL, " +
            COLUMN_EXAME_MEDICO_EMAIL + " TEXT NOT NULL, " +
            COLUMN_EXAME_NOME_HOSPITAL + " TEXT NOT NULL, " + // Adicionando a coluna nome_hospital
            "FOREIGN KEY(" + COLUMN_EXAME_MEDICO_EMAIL + ") REFERENCES " + TABLE_MEDICO + "(" + COLUMN_MEDICO_EMAIL + ")" +
            ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_MEDICO);
        db.execSQL(TABLE_CREATE_PACIENTE);
        db.execSQL(TABLE_CREATE_EXAMES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 10) { // Atualizar para a versão 10
            db.execSQL("ALTER TABLE " + TABLE_EXAMES + " ADD COLUMN " + COLUMN_EXAME_NOME_HOSPITAL + " TEXT NOT NULL DEFAULT '';"); // Adiciona a nova coluna
        }
        // Adicione outras instruções de atualização aqui se houver mais versões e alterações
    }
}
