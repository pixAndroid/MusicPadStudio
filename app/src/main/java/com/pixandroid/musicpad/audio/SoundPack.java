package com.pixandroid.musicpad.audio;

import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sound pack management system
 */
public class SoundPack {
    
    private String name;
    private String description;
    private boolean isPremium;
    private boolean isDownloaded;
    private List<SoundInfo> sounds;
    
    public SoundPack(String name, String description, boolean isPremium) {
        this.name = name;
        this.description = description;
        this.isPremium = isPremium;
        this.isDownloaded = !isPremium; // Free packs are pre-downloaded
        this.sounds = new ArrayList<>();
    }
    
    /**
     * Add a sound to the pack
     */
    public void addSound(int padIndex, String soundName, int resourceId) {
        sounds.add(new SoundInfo(padIndex, soundName, resourceId));
    }
    
    /**
     * Load all sounds from this pack into the audio engine
     */
    public void loadIntoEngine(Context context, AudioEngine audioEngine) {
        for (SoundInfo sound : sounds) {
            audioEngine.loadSound(context, sound.padIndex, sound.resourceId);
        }
    }
    
    /**
     * Get sound info for a specific pad
     */
    public SoundInfo getSoundForPad(int padIndex) {
        for (SoundInfo sound : sounds) {
            if (sound.padIndex == padIndex) {
                return sound;
            }
        }
        return null;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isPremium() {
        return isPremium;
    }
    
    public void setPremium(boolean premium) {
        isPremium = premium;
    }
    
    public boolean isDownloaded() {
        return isDownloaded;
    }
    
    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }
    
    public List<SoundInfo> getSounds() {
        return sounds;
    }
    
    public int getSoundCount() {
        return sounds.size();
    }
    
    /**
     * Sound information class
     */
    public static class SoundInfo {
        public int padIndex;
        public String soundName;
        public int resourceId;
        
        public SoundInfo(int padIndex, String soundName, int resourceId) {
            this.padIndex = padIndex;
            this.soundName = soundName;
            this.resourceId = resourceId;
        }
    }
    
    /**
     * Create default sound packs
     */
    public static List<SoundPack> getDefaultPacks() {
        List<SoundPack> packs = new ArrayList<>();
        
        // EDM Pack (Free)
        SoundPack edm = new SoundPack("EDM", "Electronic Dance Music sounds", false);
        edm.setDownloaded(true);
        packs.add(edm);
        
        // Hip-Hop Pack (Free)
        SoundPack hiphop = new SoundPack("Hip-Hop", "Classic hip-hop beats", false);
        hiphop.setDownloaded(true);
        packs.add(hiphop);
        
        // Trap Pack (Premium)
        SoundPack trap = new SoundPack("Trap", "Modern trap sounds", true);
        packs.add(trap);
        
        // Dubstep Pack (Premium)
        SoundPack dubstep = new SoundPack("Dubstep", "Heavy dubstep drops", true);
        packs.add(dubstep);
        
        // Drum Kit Pack (Free)
        SoundPack drumkit = new SoundPack("Drum Kit", "Acoustic drum kit", false);
        drumkit.setDownloaded(true);
        packs.add(drumkit);
        
        // Custom Pack (Free)
        SoundPack custom = new SoundPack("Custom", "Import your own sounds", false);
        custom.setDownloaded(true);
        packs.add(custom);
        
        return packs;
    }
}
