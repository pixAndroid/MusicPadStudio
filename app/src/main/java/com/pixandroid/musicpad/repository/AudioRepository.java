package com.pixandroid.musicpad.repository;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for managing audio-related data and preferences
 */
public class AudioRepository {
    
    private static final String PREFS_NAME = "audio_prefs";
    private static final String KEY_CURRENT_PACK = "current_pack";
    private static final String KEY_BPM = "bpm";
    private static final String KEY_MASTER_VOLUME = "master_volume";
    private static final String KEY_METRONOME_ENABLED = "metronome_enabled";
    private static final String KEY_EFFECTS_ENABLED = "effects_enabled";
    
    private final SharedPreferences prefs;
    
    public AudioRepository(Application application) {
        prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    // Sound Pack Management
    public String getCurrentSoundPack() {
        return prefs.getString(KEY_CURRENT_PACK, "EDM");
    }
    
    public void setCurrentSoundPack(String packName) {
        prefs.edit().putString(KEY_CURRENT_PACK, packName).apply();
    }
    
    public List<String> getAvailableSoundPacks() {
        List<String> packs = new ArrayList<>();
        packs.add("EDM");
        packs.add("Hip-Hop");
        packs.add("Trap");
        packs.add("Dubstep");
        packs.add("Drum Kit");
        packs.add("Custom");
        return packs;
    }
    
    // BPM Management
    public int getBpm() {
        return prefs.getInt(KEY_BPM, 120);
    }
    
    public void setBpm(int bpm) {
        prefs.edit().putInt(KEY_BPM, Math.max(60, Math.min(300, bpm))).apply();
    }
    
    // Volume Management
    public float getMasterVolume() {
        return prefs.getFloat(KEY_MASTER_VOLUME, 1.0f);
    }
    
    public void setMasterVolume(float volume) {
        prefs.edit().putFloat(KEY_MASTER_VOLUME, Math.max(0.0f, Math.min(1.0f, volume))).apply();
    }
    
    // Metronome
    public boolean isMetronomeEnabled() {
        return prefs.getBoolean(KEY_METRONOME_ENABLED, false);
    }
    
    public void setMetronomeEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_METRONOME_ENABLED, enabled).apply();
    }
    
    // Effects
    public boolean areEffectsEnabled() {
        return prefs.getBoolean(KEY_EFFECTS_ENABLED, false);
    }
    
    public void setEffectsEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_EFFECTS_ENABLED, enabled).apply();
    }
}
