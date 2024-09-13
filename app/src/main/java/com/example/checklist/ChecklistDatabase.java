package com.example.checklist;

import android.content.Context;
import android.os.AsyncTask;

/*import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ChecklistItem.class, User.class}, version = 1, exportSchema = false)
public abstract class ChecklistDatabase extends RoomDatabase {

    private static ChecklistDatabase instance;

    public abstract ChecklistDao checklistDao();

    // Método para obter a instância do banco de dados
    public static synchronized ChecklistDatabase getInstance(Context context) {
        if (instance == null) {
            // Criar uma instância do banco de dados se ainda não existir
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ChecklistDatabase.class, "checklist_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
    public void updatePassword(User user) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // Atualizar a senha na base de dados usando o DAO
                checklistDao().updatePassword(user);
            }
        });
    }

    // Método para destruir e limpar a instância do banco de dados quando necessário
    public static void destroyInstance() {
        instance = null;
    }
}
*/