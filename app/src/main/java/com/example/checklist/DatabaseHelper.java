package com.example.checklist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "checklist.db";
    private static final int DATABASE_VERSION = 17; // Versão atual do banco de dados

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
    public static final String COLUMN_EXAME_NOME = "nome_exame";
    public static final String COLUMN_EXAME_DATA = "data";
    public static final String COLUMN_EXAME_NOME_HOSPITAL = "nome_hospital";
    public static final String COLUMN_EXAME_MEDICO_EMAIL = "medico_email";
    public static final String COLUMN_EXAME_PACIENTE_CPF = "paciente_cpf"; // Novo campo para o CPF

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criar tabelas no banco de dados
        String CREATE_MEDICO_TABLE = "CREATE TABLE " + TABLE_MEDICO + " (" +
                COLUMN_MEDICO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MEDICO_NOME + " TEXT, " +
                COLUMN_MEDICO_CPF + " TEXT, " +
                COLUMN_MEDICO_RG + " TEXT, " +
                COLUMN_MEDICO_CRM + " TEXT, " +
                COLUMN_MEDICO_EMAIL + " TEXT, " +
                COLUMN_MEDICO_SENHA + " TEXT, " +
                COLUMN_MEDICO_TIPO_USUARIO + " TEXT)";

        String CREATE_PACIENTE_TABLE = "CREATE TABLE " + TABLE_PACIENTE + " (" +
                COLUMN_PACIENTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PACIENTE_NOME + " TEXT, " +
                COLUMN_PACIENTE_CPF + " TEXT, " +
                COLUMN_PACIENTE_RG + " TEXT, " +
                COLUMN_PACIENTE_EMAIL + " TEXT, " +
                COLUMN_PACIENTE_SENHA + " TEXT, " +
                COLUMN_PACIENTE_TIPO_USUARIO + " TEXT)";

        String CREATE_EXAMES_TABLE = "CREATE TABLE " + TABLE_EXAMES + " (" +
                COLUMN_EXAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EXAME_NOME + " TEXT, " +
                COLUMN_EXAME_DATA + " TEXT, " +
                COLUMN_EXAME_NOME_HOSPITAL + " TEXT, " +
                COLUMN_EXAME_MEDICO_EMAIL + " TEXT, " +
                COLUMN_EXAME_PACIENTE_CPF + " TEXT)"; // Campo CPF do paciente adicionado

        // Executar as instruções de criação de tabelas
        db.execSQL(CREATE_MEDICO_TABLE);
        db.execSQL(CREATE_PACIENTE_TABLE);
        db.execSQL(CREATE_EXAMES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Verificar se alguma atualização é necessária
        if (oldVersion < 13) {
            // Atualizar tabela exames para incluir o campo CPF do paciente
            db.execSQL("ALTER TABLE " + TABLE_EXAMES + " ADD COLUMN " + COLUMN_EXAME_PACIENTE_CPF + " TEXT");
        }
    }
}