package com.pixandroid.musicpad.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.pixandroid.musicpad.audio.EffectsProcessor;
import com.pixandroid.musicpad.audio.EffectsProcessor.EffectLevel;
import com.pixandroid.musicpad.audio.EffectsProcessor.EffectType;

/**
 * ViewModel for audio effects management
 */
public class EffectsViewModel extends AndroidViewModel {
    
    private EffectsProcessor effectsProcessor;
    
    private final MutableLiveData<EffectLevel> reverbLevel;
    private final MutableLiveData<EffectLevel> delayLevel;
    private final MutableLiveData<EffectLevel> distortionLevel;
    private final MutableLiveData<EffectLevel> bassBoostLevel;
    private final MutableLiveData<EffectLevel> echoLevel;
    
    private final MutableLiveData<Boolean> effectsEnabled;
    
    public EffectsViewModel(@NonNull Application application) {
        super(application);
        
        // Note: EffectsProcessor needs an audio session ID, which comes from AudioEngine
        // This will be initialized when audio engine is available
        
        reverbLevel = new MutableLiveData<>(EffectLevel.OFF);
        delayLevel = new MutableLiveData<>(EffectLevel.OFF);
        distortionLevel = new MutableLiveData<>(EffectLevel.OFF);
        bassBoostLevel = new MutableLiveData<>(EffectLevel.OFF);
        echoLevel = new MutableLiveData<>(EffectLevel.OFF);
        effectsEnabled = new MutableLiveData<>(false);
    }
    
    /**
     * Initialize effects processor with audio session ID
     */
    public void initializeEffects(int audioSessionId) {
        if (effectsProcessor == null) {
            effectsProcessor = new EffectsProcessor(audioSessionId);
        }
    }
    
    /**
     * Set reverb level
     */
    public void setReverbLevel(EffectLevel level) {
        reverbLevel.setValue(level);
        if (effectsProcessor != null) {
            effectsProcessor.setReverbLevel(level);
        }
        updateEffectsEnabled();
    }
    
    /**
     * Set delay level
     */
    public void setDelayLevel(EffectLevel level) {
        delayLevel.setValue(level);
        if (effectsProcessor != null) {
            effectsProcessor.setDelayLevel(level);
        }
        updateEffectsEnabled();
    }
    
    /**
     * Set distortion level
     */
    public void setDistortionLevel(EffectLevel level) {
        distortionLevel.setValue(level);
        if (effectsProcessor != null) {
            effectsProcessor.setDistortionLevel(level);
        }
        updateEffectsEnabled();
    }
    
    /**
     * Set bass boost level
     */
    public void setBassBoostLevel(EffectLevel level) {
        bassBoostLevel.setValue(level);
        if (effectsProcessor != null) {
            effectsProcessor.setBassBoostLevel(level);
        }
        updateEffectsEnabled();
    }
    
    /**
     * Set echo level
     */
    public void setEchoLevel(EffectLevel level) {
        echoLevel.setValue(level);
        if (effectsProcessor != null) {
            effectsProcessor.setEchoLevel(level);
        }
        updateEffectsEnabled();
    }
    
    /**
     * Reset all effects
     */
    public void resetAllEffects() {
        setReverbLevel(EffectLevel.OFF);
        setDelayLevel(EffectLevel.OFF);
        setDistortionLevel(EffectLevel.OFF);
        setBassBoostLevel(EffectLevel.OFF);
        setEchoLevel(EffectLevel.OFF);
        effectsEnabled.setValue(false);
    }
    
    /**
     * Update effects enabled state
     */
    private void updateEffectsEnabled() {
        boolean anyEnabled = 
            (reverbLevel.getValue() != EffectLevel.OFF) ||
            (delayLevel.getValue() != EffectLevel.OFF) ||
            (distortionLevel.getValue() != EffectLevel.OFF) ||
            (bassBoostLevel.getValue() != EffectLevel.OFF) ||
            (echoLevel.getValue() != EffectLevel.OFF);
        
        effectsEnabled.setValue(anyEnabled);
    }
    
    /**
     * Cycle through effect levels
     */
    public EffectLevel cycleEffectLevel(EffectLevel current) {
        switch (current) {
            case OFF:
                return EffectLevel.LOW;
            case LOW:
                return EffectLevel.MEDIUM;
            case MEDIUM:
                return EffectLevel.HIGH;
            case HIGH:
            default:
                return EffectLevel.OFF;
        }
    }
    
    // LiveData getters
    public LiveData<EffectLevel> getReverbLevel() {
        return reverbLevel;
    }
    
    public LiveData<EffectLevel> getDelayLevel() {
        return delayLevel;
    }
    
    public LiveData<EffectLevel> getDistortionLevel() {
        return distortionLevel;
    }
    
    public LiveData<EffectLevel> getBassBoostLevel() {
        return bassBoostLevel;
    }
    
    public LiveData<EffectLevel> getEchoLevel() {
        return echoLevel;
    }
    
    public LiveData<Boolean> getEffectsEnabled() {
        return effectsEnabled;
    }
    
    public EffectsProcessor getEffectsProcessor() {
        return effectsProcessor;
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        if (effectsProcessor != null) {
            effectsProcessor.release();
        }
    }
}
