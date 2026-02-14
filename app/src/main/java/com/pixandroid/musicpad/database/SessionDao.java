package com.pixandroid.musicpad.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.pixandroid.musicpad.models.Session;
import java.util.List;

/**
 * Data Access Object for Session entity
 */
@Dao
public interface SessionDao {
    
    @Insert
    long insert(Session session);
    
    @Update
    void update(Session session);
    
    @Delete
    void delete(Session session);
    
    @Query("SELECT * FROM sessions ORDER BY modifiedAt DESC")
    LiveData<List<Session>> getAllSessions();
    
    @Query("SELECT * FROM sessions WHERE id = :id")
    LiveData<Session> getSessionById(long id);
    
    @Query("SELECT * FROM sessions WHERE id = :id")
    Session getSessionByIdSync(long id);
    
    @Query("DELETE FROM sessions WHERE id = :id")
    void deleteById(long id);
    
    @Query("SELECT COUNT(*) FROM sessions")
    int getSessionCount();
    
    @Query("DELETE FROM sessions")
    void deleteAllSessions();
}
