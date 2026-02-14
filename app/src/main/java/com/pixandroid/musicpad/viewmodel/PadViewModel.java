package com.pixandroid.musicpad.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.pixandroid.musicpad.audio.AudioEngine;
import com.pixandroid.musicpad.audio.SoundPack;
import com.pixandroid.musicpad.repository.AudioRepository;
import com.pixandroid.musicpad.repository.PadRepository;
import com.pixandroid.musicpad.utils.Constants;

/**
 * ViewModel for pad grid and sound management
 */
public class PadViewModel extends AndroidViewModel {
    
    private final AudioRepository audioRepository;
    private final PadRepository padRepository;
    private final AudioEngine audioEngine;
    
    private final MutableLiveData<String> currentSoundPack;
    private final MutableLiveData<Integer> bpm;
    private final MutableLiveData<Float> masterVolume;
    private final MutableLiveData<Boolean> isMetronomeEnabled;
    private final MutableLiveData<Boolean> isLoopMode;
    
    // Pad-specific volumes (padIndex -> volume)
    private final float[] padVolumes;
    
    public PadViewModel(@NonNull Application application) {
        super(application);
        
        audioRepository = new AudioRepository(application);
        padRepository = new PadRepository(application);
        audioEngine = new AudioEngine();
        
        currentSoundPack = new MutableLiveData<>(audioRepository.getCurrentSoundPack());
        bpm = new MutableLiveData<>(audioRepository.getBpm());
        masterVolume = new MutableLiveData<>(audioRepository.getMasterVolume());
        isMetronomeEnabled = new MutableLiveData<>(audioRepository.isMetronomeEnabled());
        isLoopMode = new MutableLiveData<>(false);
        
        padVolumes = new float[Constants.TOTAL_PADS];
        for (int i = 0; i < Constants.TOTAL_PADS; i++) {
            padVolumes[i] = 1.0f;
        }
        
        // Set master volume in audio engine
        audioEngine.setMasterVolume(audioRepository.getMasterVolume());
    }
    
    /**
     * Play a pad
     */
    public void playPad(int padIndex, float velocity) {
        audioEngine.playPad(padIndex, velocity);
    }
    
    /**
     * Load a sound pack
     */
    public void loadSoundPack(String packName) {
        currentSoundPack.setValue(packName);
        audioRepository.setCurrentSoundPack(packName);
        
        // In production, load actual sounds from resources
        // For now, this is a placeholder
    }
    
    /**
     * Set BPM
     */
    public void setBpm(int newBpm) {
        int clampedBpm = Math.max(Constants.MIN_BPM, Math.min(Constants.MAX_BPM, newBpm));
        bpm.setValue(clampedBpm);
        audioRepository.setBpm(clampedBpm);
    }
    
    /**
     * Increase BPM
     */
    public void increaseBpm() {
        Integer currentBpm = bpm.getValue();
        if (currentBpm != null && currentBpm < Constants.MAX_BPM) {
            setBpm(currentBpm + 1);
        }
    }
    
    /**
     * Decrease BPM
     */
    public void decreaseBpm() {
        Integer currentBpm = bpm.getValue();
        if (currentBpm != null && currentBpm > Constants.MIN_BPM) {
            setBpm(currentBpm - 1);
        }
    }
    
    /**
     * Set master volume
     */
    public void setMasterVolume(float volume) {
        float clampedVolume = Math.max(0.0f, Math.min(1.0f, volume));
        masterVolume.setValue(clampedVolume);
        audioRepository.setMasterVolume(clampedVolume);
        audioEngine.setMasterVolume(clampedVolume);
    }
    
    /**
     * Set pad volume
     */
    public void setPadVolume(int padIndex, float volume) {
        if (padIndex >= 0 && padIndex < Constants.TOTAL_PADS) {
            float clampedVolume = Math.max(0.0f, Math.min(1.0f, volume));
            padVolumes[padIndex] = clampedVolume;
            audioEngine.setPadVolume(padIndex, clampedVolume);
        }
    }
    
    /**
     * Get pad volume
     */
    public float getPadVolume(int padIndex) {
        if (padIndex >= 0 && padIndex < Constants.TOTAL_PADS) {
            return padVolumes[padIndex];
        }
        return 1.0f;
    }
    
    /**
     * Toggle metronome
     */
    public void toggleMetronome() {
        Boolean current = isMetronomeEnabled.getValue();
        boolean newValue = current == null || !current;
        isMetronomeEnabled.setValue(newValue);
        audioRepository.setMetronomeEnabled(newValue);
    }
    
    /**
     * Toggle loop mode
     */
    public void toggleLoopMode() {
        Boolean current = isLoopMode.getValue();
        isLoopMode.setValue(current == null || !current);
    }
    
    // LiveData getters
    public LiveData<String> getCurrentSoundPack() {
        return currentSoundPack;
    }
    
    public LiveData<Integer> getBpmLiveData() {
        return bpm;
    }
    
    public LiveData<Float> getMasterVolume() {
        return masterVolume;
    }
    
    public LiveData<Boolean> getIsMetronomeEnabled() {
        return isMetronomeEnabled;
    }
    
    public LiveData<Boolean> getIsLoopMode() {
        return isLoopMode;
    }
    
    public AudioEngine getAudioEngine() {
        return audioEngine;
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        audioEngine.release();
        padRepository.shutdown();
    }
}
