package com.pixandroid.musicpad.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.pixandroid.musicpad.models.PadHit;
import java.util.List;

/**
 * Data Access Object for PadHit entity
 */
@Dao
public interface PadHitDao {
    
    @Insert
    long insert(PadHit padHit);
    
    @Insert
    void insertAll(List<PadHit> padHits);
    
    @Update
    void update(PadHit padHit);
    
    @Delete
    void delete(PadHit padHit);
    
    @Query("SELECT * FROM pad_hits WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    LiveData<List<PadHit>> getHitsForSession(long sessionId);
    
    @Query("SELECT * FROM pad_hits WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    List<PadHit> getHitsForSessionSync(long sessionId);
    
    @Query("DELETE FROM pad_hits WHERE sessionId = :sessionId")
    void deleteHitsForSession(long sessionId);
    
    @Query("SELECT COUNT(*) FROM pad_hits WHERE sessionId = :sessionId")
    int getHitCountForSession(long sessionId);
    
    @Query("DELETE FROM pad_hits")
    void deleteAllHits();
}
