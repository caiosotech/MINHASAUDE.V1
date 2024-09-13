/*package com.example.checklist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TarefaDAO extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tarefas.db";
    private static final int DATABASE_VERSION = 10;
    private static final String TABLE_NAME = "tarefas";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITULO = "titulo";
    private static final String COLUMN_SUBTITULO = "subtitulo";
    private static final String COLUMN_DATA = "data";
    private static final String COLUMN_HORA = "hora";
    private static final String COLUMN_DESCRICAO = "descricao";
    private static final String COLUMN_TAG = "tag";
    private static final String COLUMN_FINALIZADA = "finalizada";
    private static final String COLUMN_FAVORITA = "favorita"; // Adicionando coluna de favorita
    private static final String COLUMN_USER_ID = "user_id";

    public TarefaDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITULO + " TEXT, " +
                COLUMN_SUBTITULO + " TEXT, " +
                COLUMN_DATA + " TEXT, " +
                COLUMN_HORA + " TEXT, " +
                COLUMN_DESCRICAO + " TEXT, " +
                COLUMN_TAG + " TEXT, " +
                COLUMN_FINALIZADA + " INTEGER DEFAULT 0, " +
                COLUMN_FAVORITA + " INTEGER DEFAULT 0, " + // Adicionando coluna de favorita
                COLUMN_USER_ID + " INTEGER)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 10) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_FAVORITA + " INTEGER DEFAULT 0");
        }
    }

    public long inserirTarefa(Tarefa tarefa, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITULO, tarefa.getTitulo());
        values.put(COLUMN_SUBTITULO, tarefa.getSubtitulo());
        values.put(COLUMN_DATA, tarefa.getData());
        values.put(COLUMN_HORA, tarefa.getHora());
        values.put(COLUMN_DESCRICAO, tarefa.getDescricao());
        values.put(COLUMN_TAG, tarefa.getTag());
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_FAVORITA, tarefa.isFavorited() ? 1 : 0); // Adicionando valor de favorita
        long resultado = db.insert(TABLE_NAME, null, values);
        db.close();
        return resultado;
    }

    public int atualizarTarefa(Tarefa tarefa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITULO, tarefa.getTitulo());
        values.put(COLUMN_SUBTITULO, tarefa.getSubtitulo());
        values.put(COLUMN_DATA, tarefa.getData());
        values.put(COLUMN_HORA, tarefa.getHora());
        values.put(COLUMN_DESCRICAO, tarefa.getDescricao());
        values.put(COLUMN_TAG, tarefa.getTag());
        values.put(COLUMN_FAVORITA, tarefa.isFavorited() ? 1 : 0); // Adicionando valor de favorita
        int linhasAfetadas = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(tarefa.getId())});
        db.close();
        return linhasAfetadas;
    }

    public int deletarTarefa(int tarefaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int linhasAfetadas = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(tarefaId)});
        db.close();
        return linhasAfetadas;
    }

    public int marcarComoFinalizada(Tarefa tarefa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FINALIZADA, 1);
        int linhasAfetadas = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(tarefa.getId())});
        db.close();
        return linhasAfetadas;
    }

    public List<Tarefa> obterTarefasFinalizadasPorUsuario(int userId) {
        List<Tarefa> listaTarefas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USER_ID + " = ? AND " + COLUMN_FINALIZADA + " = 1",
                new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                Tarefa tarefa = new Tarefa();
                tarefa.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                tarefa.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITULO)));
                tarefa.setSubtitulo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBTITULO)));
                tarefa.setData(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA)));
                tarefa.setHora(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORA)));
                tarefa.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRICAO)));
                tarefa.setTag(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAG)));
                tarefa.setFavorited(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FAVORITA)) == 1);
                listaTarefas.add(tarefa);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listaTarefas;
    }

    public List<Tarefa> obterTarefasNaoFinalizadasPorUsuario(int userId) {
        List<Tarefa> listaTarefas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USER_ID + " = ? AND " + COLUMN_FINALIZADA + " = 0",
                new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                Tarefa tarefa = new Tarefa();
                tarefa.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                tarefa.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITULO)));
                tarefa.setSubtitulo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBTITULO)));
                tarefa.setData(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA)));
                tarefa.setHora(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORA)));
                tarefa.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRICAO)));
                tarefa.setTag(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAG)));
                tarefa.setFavorited(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FAVORITA)) == 1);
                listaTarefas.add(tarefa);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listaTarefas;
    }

    public List<Tarefa> obterTarefasPorTag(int userId, String tag) {
        List<Tarefa> listaTarefas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USER_ID + " = ? AND " + COLUMN_TAG + " = ?",
                new String[]{String.valueOf(userId), tag});
        if (cursor.moveToFirst()) {
            do {
                Tarefa tarefa = new Tarefa();
                tarefa.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                tarefa.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITULO)));
                tarefa.setSubtitulo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBTITULO)));
                tarefa.setData(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA)));
                tarefa.setHora(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORA)));
                tarefa.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRICAO)));
                tarefa.setTag(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAG)));
                tarefa.setFavorited(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FAVORITA)) == 1);
                listaTarefas.add(tarefa);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listaTarefas;
    }


    public List<Tarefa> obterTarefasNaoFinalizadasFavoritasPorUsuario(int userId) {
        List<Tarefa> listaTarefas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USER_ID + " = ? AND " + COLUMN_FAVORITA + " = 1 AND " + COLUMN_FINALIZADA + " = 0",
                new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                Tarefa tarefa = new Tarefa();
                tarefa.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                tarefa.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITULO)));
                tarefa.setSubtitulo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBTITULO)));
                tarefa.setData(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA)));
                tarefa.setHora(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORA)));
                tarefa.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRICAO)));
                tarefa.setTag(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAG)));
                tarefa.setFavorited(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FAVORITA)) == 1);
                listaTarefas.add(tarefa);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listaTarefas;
    }

    public List<Tarefa> obterTarefasFinalizadasFavoritasPorUsuario(int userId) {
        List<Tarefa> listaTarefas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USER_ID + " = ? AND " + COLUMN_FAVORITA + " = 1 AND " + COLUMN_FINALIZADA + " = 1",
                new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                Tarefa tarefa = new Tarefa();
                tarefa.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                tarefa.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITULO)));
                tarefa.setSubtitulo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBTITULO)));
                tarefa.setData(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA)));
                tarefa.setHora(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORA)));
                tarefa.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRICAO)));
                tarefa.setTag(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAG)));
                tarefa.setFavorited(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FAVORITA)) == 1);
                listaTarefas.add(tarefa);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listaTarefas;
    }



    public List<String> obterTagsPorUsuario(int userId) {
        List<String> listaTags = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT " + COLUMN_TAG + " FROM " + TABLE_NAME + " WHERE " + COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                listaTags.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAG)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listaTags;
    }
}
*/