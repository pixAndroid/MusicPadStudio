package com.pixandroid.musicpad.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.pixandroid.musicpad.database.Converters;
import java.util.Date;

/**
 * Session entity for recording sessions
 */
@Entity(tableName = "sessions")
@TypeConverters(Converters.class)
public class Session {
    
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private String name;
    private long duration; // in milliseconds
    private Date createdAt;
    private Date modifiedAt;
    private int bpm;
    private String soundPackName;
    private boolean isLooped;
    
    public Session() {
        this.createdAt = new Date();
        this.modifiedAt = new Date();
        this.bpm = 120;
        this.isLooped = false;
    }
    
    // Getters and Setters
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public long getDuration() {
        return duration;
    }
    
    public void setDuration(long duration) {
        this.duration = duration;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getModifiedAt() {
        return modifiedAt;
    }
    
    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
    
    public int getBpm() {
        return bpm;
    }
    
    public void setBpm(int bpm) {
        this.bpm = bpm;
    }
    
    public String getSoundPackName() {
        return soundPackName;
    }
    
    public void setSoundPackName(String soundPackName) {
        this.soundPackName = soundPackName;
    }
    
    public boolean isLooped() {
        return isLooped;
    }
    
    public void setLooped(boolean looped) {
        isLooped = looped;
    }
}
