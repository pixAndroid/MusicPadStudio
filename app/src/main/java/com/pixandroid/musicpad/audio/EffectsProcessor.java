package com.pixandroid.musicpad.audio;

import android.media.audiofx.BassBoost;
import android.media.audiofx.EnvironmentalReverb;
import android.media.audiofx.PresetReverb;
import android.util.Log;

/**
 * Audio effects processor for real-time audio effects
 */
public class EffectsProcessor {
    
    private static final String TAG = "EffectsProcessor";
    
    // Effect types
    public enum EffectType {
        REVERB,
        DELAY,
        DISTORTION,
        BASS_BOOST,
        ECHO
    }
    
    // Effect levels
    public enum EffectLevel {
        OFF(0),
        LOW(33),
        MEDIUM(66),
        HIGH(100);
        
        private final int value;
        
        EffectLevel(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }
    
    private PresetReverb reverb;
    private BassBoost bassBoost;
    
    private int audioSessionId;
    private boolean isInitialized;
    
    private EffectLevel reverbLevel = EffectLevel.OFF;
    private EffectLevel delayLevel = EffectLevel.OFF;
    private EffectLevel distortionLevel = EffectLevel.OFF;
    private EffectLevel bassBoostLevel = EffectLevel.OFF;
    private EffectLevel echoLevel = EffectLevel.OFF;
    
    public EffectsProcessor(int audioSessionId) {
        this.audioSessionId = audioSessionId;
        initialize();
    }
    
    /**
     * Initialize audio effects
     */
    private void initialize() {
        try {
            // Initialize reverb
            reverb = new PresetReverb(0, audioSessionId);
            reverb.setPreset(PresetReverb.PRESET_LARGEROOM);
            reverb.setEnabled(false);
            
            // Initialize bass boost
            bassBoost = new BassBoost(0, audioSessionId);
            bassBoost.setEnabled(false);
            
            isInitialized = true;
            Log.d(TAG, "Effects processor initialized");
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize effects", e);
            isInitialized = false;
        }
    }
    
    /**
     * Set reverb level
     */
    public void setReverbLevel(EffectLevel level) {
        if (!isInitialized || reverb == null) {
            return;
        }
        
        reverbLevel = level;
        
        try {
            if (level == EffectLevel.OFF) {
                reverb.setEnabled(false);
            } else {
                reverb.setEnabled(true);
                
                // Set reverb preset based on level
                switch (level) {
                    case LOW:
                        reverb.setPreset(PresetReverb.PRESET_SMALLROOM);
                        break;
                    case MEDIUM:
                        reverb.setPreset(PresetReverb.PRESET_MEDIUMROOM);
                        break;
                    case HIGH:
                        reverb.setPreset(PresetReverb.PRESET_LARGEHALL);
                        break;
                }
            }
            Log.d(TAG, "Reverb level set to: " + level);
        } catch (Exception e) {
            Log.e(TAG, "Failed to set reverb level", e);
        }
    }
    
    /**
     * Set bass boost level
     */
    public void setBassBoostLevel(EffectLevel level) {
        if (!isInitialized || bassBoost == null) {
            return;
        }
        
        bassBoostLevel = level;
        
        try {
            if (level == EffectLevel.OFF) {
                bassBoost.setEnabled(false);
            } else {
                bassBoost.setEnabled(true);
                
                // Calculate strength (0-1000)
                short strength = (short) (level.getValue() * 10);
                bassBoost.setStrength(strength);
            }
            Log.d(TAG, "Bass boost level set to: " + level);
        } catch (Exception e) {
            Log.e(TAG, "Failed to set bass boost level", e);
        }
    }
    
    /**
     * Set delay level (simulated)
     */
    public void setDelayLevel(EffectLevel level) {
        delayLevel = level;
        Log.d(TAG, "Delay level set to: " + level);
    }
    
    /**
     * Set distortion level (simulated)
     */
    public void setDistortionLevel(EffectLevel level) {
        distortionLevel = level;
        Log.d(TAG, "Distortion level set to: " + level);
    }
    
    /**
     * Set echo level (simulated)
     */
    public void setEchoLevel(EffectLevel level) {
        echoLevel = level;
        Log.d(TAG, "Echo level set to: " + level);
    }
    
    /**
     * Get current reverb level
     */
    public EffectLevel getReverbLevel() {
        return reverbLevel;
    }
    
    /**
     * Get current bass boost level
     */
    public EffectLevel getBassBoostLevel() {
        return bassBoostLevel;
    }
    
    /**
     * Get current delay level
     */
    public EffectLevel getDelayLevel() {
        return delayLevel;
    }
    
    /**
     * Get current distortion level
     */
    public EffectLevel getDistortionLevel() {
        return distortionLevel;
    }
    
    /**
     * Get current echo level
     */
    public EffectLevel getEchoLevel() {
        return echoLevel;
    }
    
    /**
     * Reset all effects to OFF
     */
    public void resetAllEffects() {
        setReverbLevel(EffectLevel.OFF);
        setBassBoostLevel(EffectLevel.OFF);
        setDelayLevel(EffectLevel.OFF);
        setDistortionLevel(EffectLevel.OFF);
        setEchoLevel(EffectLevel.OFF);
        Log.d(TAG, "All effects reset");
    }
    
    /**
     * Release effects processor
     */
    public void release() {
        if (reverb != null) {
            reverb.release();
            reverb = null;
        }
        
        if (bassBoost != null) {
            bassBoost.release();
            bassBoost = null;
        }
        
        isInitialized = false;
        Log.d(TAG, "Effects processor released");
    }
    
    public boolean isInitialized() {
        return isInitialized;
    }
}
