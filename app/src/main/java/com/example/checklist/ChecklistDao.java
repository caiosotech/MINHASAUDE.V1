/*package com.example.checklist;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ChecklistDao {
    @Query("SELECT * FROM checklist_items")
    List<ChecklistItem> getAllItems();

    @Insert
    void insert(ChecklistItem item);

    @Insert
    long insertUser(User user);

    @Update
    void update(ChecklistItem item);

    @Query("DELETE FROM checklist_items WHERE id = :itemId")
    void delete(int itemId);

    @Query("SELECT id FROM users WHERE username = :username AND password = :password")
    int getUserIdByUsernameAndPassword(String username, String password);

    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    int getUserCountByUsername(String username);

    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    int getUserCountByEmail(String email);

    @Query("SELECT * FROM users WHERE id = :userId")
    LiveData<User> getUserById(int userId);

    @Update
    void updatePassword(User user);
}
*/