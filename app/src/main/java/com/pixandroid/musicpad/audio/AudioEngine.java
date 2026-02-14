package com.pixandroid.musicpad.audio;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;

/**
 * Audio engine for low-latency sound playback using SoundPool
 */
public class AudioEngine {
    
    private static final String TAG = "AudioEngine";
    private static final int MAX_STREAMS = 32;
    
    private SoundPool soundPool;
    private final Map<Integer, Integer> soundMap; // padIndex -> soundId
    private final Map<Integer, Float> volumeMap; // padIndex -> volume
    private boolean isInitialized;
    private float masterVolume = 1.0f;
    
    public AudioEngine() {
        soundMap = new HashMap<>();
        volumeMap = new HashMap<>();
        initializeSoundPool();
    }
    
    private void initializeSoundPool() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build();
        
        soundPool = new SoundPool.Builder()
            .setMaxStreams(MAX_STREAMS)
            .setAudioAttributes(audioAttributes)
            .build();
        
        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            if (status == 0) {
                Log.d(TAG, "Sound loaded successfully: " + sampleId);
            } else {
                Log.e(TAG, "Failed to load sound: " + sampleId);
            }
        });
        
        isInitialized = true;
    }
    
    /**
     * Load a sound for a specific pad
     */
    public int loadSound(Context context, int padIndex, int resourceId) {
        if (!isInitialized) {
            Log.e(TAG, "AudioEngine not initialized");
            return -1;
        }
        
        int soundId = soundPool.load(context, resourceId, 1);
        soundMap.put(padIndex, soundId);
        volumeMap.put(padIndex, 1.0f);
        
        Log.d(TAG, "Loaded sound for pad " + padIndex + ", soundId: " + soundId);
        return soundId;
    }
    
    /**
     * Play sound for a specific pad
     */
    public int playPad(int padIndex) {
        return playPad(padIndex, 1.0f);
    }
    
    /**
     * Play sound for a specific pad with velocity
     */
    public int playPad(int padIndex, float velocity) {
        if (!isInitialized) {
            Log.e(TAG, "AudioEngine not initialized");
            return -1;
        }
        
        Integer soundId = soundMap.get(padIndex);
        if (soundId == null) {
            Log.w(TAG, "No sound loaded for pad " + padIndex);
            return -1;
        }
        
        Float padVolume = volumeMap.get(padIndex);
        if (padVolume == null) padVolume = 1.0f;
        
        float finalVolume = masterVolume * padVolume * velocity;
        
        int streamId = soundPool.play(soundId, finalVolume, finalVolume, 1, 0, 1.0f);
        Log.d(TAG, "Playing pad " + padIndex + ", streamId: " + streamId + ", volume: " + finalVolume);
        
        return streamId;
    }
    
    /**
     * Set volume for a specific pad
     */
    public void setPadVolume(int padIndex, float volume) {
        volumeMap.put(padIndex, Math.max(0.0f, Math.min(1.0f, volume)));
    }
    
    /**
     * Get volume for a specific pad
     */
    public float getPadVolume(int padIndex) {
        Float volume = volumeMap.get(padIndex);
        return volume != null ? volume : 1.0f;
    }
    
    /**
     * Set master volume
     */
    public void setMasterVolume(float volume) {
        masterVolume = Math.max(0.0f, Math.min(1.0f, volume));
    }
    
    /**
     * Get master volume
     */
    public float getMasterVolume() {
        return masterVolume;
    }
    
    /**
     * Stop a specific stream
     */
    public void stopStream(int streamId) {
        if (soundPool != null) {
            soundPool.stop(streamId);
        }
    }
    
    /**
     * Unload a sound
     */
    public void unloadSound(int padIndex) {
        Integer soundId = soundMap.get(padIndex);
        if (soundId != null) {
            soundPool.unload(soundId);
            soundMap.remove(padIndex);
            volumeMap.remove(padIndex);
        }
    }
    
    /**
     * Unload all sounds
     */
    public void unloadAllSounds() {
        for (Integer soundId : soundMap.values()) {
            soundPool.unload(soundId);
        }
        soundMap.clear();
        volumeMap.clear();
    }
    
    /**
     * Release the audio engine
     */
    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
        soundMap.clear();
        volumeMap.clear();
        isInitialized = false;
    }
    
    public boolean isInitialized() {
        return isInitialized;
    }
}
