package com.pixandroid.musicpad.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * PadHit entity for individual pad hits in a session
 */
@Entity(tableName = "pad_hits",
        foreignKeys = @ForeignKey(
            entity = Session.class,
            parentColumns = "id",
            childColumns = "sessionId",
            onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("sessionId")})
public class PadHit {
    
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private long sessionId;
    private int padIndex; // 0-31 for 4x8 grid
    private long timestamp; // time from session start in milliseconds
    private float velocity; // 0.0 - 1.0
    private int soundId; // SoundPool sound ID
    
    public PadHit() {
        this.velocity = 1.0f;
    }
    
    public PadHit(long sessionId, int padIndex, long timestamp) {
        this.sessionId = sessionId;
        this.padIndex = padIndex;
        this.timestamp = timestamp;
        this.velocity = 1.0f;
    }
    
    // Getters and Setters
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public long getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }
    
    public int getPadIndex() {
        return padIndex;
    }
    
    public void setPadIndex(int padIndex) {
        this.padIndex = padIndex;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public float getVelocity() {
        return velocity;
    }
    
    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }
    
    public int getSoundId() {
        return soundId;
    }
    
    public void setSoundId(int soundId) {
        this.soundId = soundId;
    }
}
