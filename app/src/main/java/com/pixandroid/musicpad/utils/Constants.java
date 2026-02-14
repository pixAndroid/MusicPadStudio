package com.pixandroid.musicpad.utils;

/**
 * Application-wide constants
 */
public class Constants {
    
    // Audio Constants
    public static final int MIN_BPM = 60;
    public static final int MAX_BPM = 300;
    public static final int DEFAULT_BPM = 120;
    public static final int MAX_STREAMS = 32;
    public static final int AUDIO_LATENCY_MS = 100;
    
    // Pad Grid Constants
    public static final int PAD_ROWS = 4;
    public static final int PAD_COLUMNS = 8;
    public static final int TOTAL_PADS = PAD_ROWS * PAD_COLUMNS; // 32 pads
    
    // Recording Constants
    public static final int MAX_RECORDING_DURATION_MS = 300000; // 5 minutes
    public static final int MAX_RECORDINGS = 100;
    
    // Volume Constants
    public static final float MIN_VOLUME = 0.0f;
    public static final float MAX_VOLUME = 1.0f;
    public static final float DEFAULT_VOLUME = 1.0f;
    
    // Animation Constants
    public static final int PAD_PRESS_DURATION = 100;
    public static final int PAD_GLOW_DURATION = 300;
    public static final int FADE_IN_DURATION = 300;
    
    // Sound Pack Names
    public static final String PACK_EDM = "EDM";
    public static final String PACK_HIPHOP = "Hip-Hop";
    public static final String PACK_TRAP = "Trap";
    public static final String PACK_DUBSTEP = "Dubstep";
    public static final String PACK_DRUMKIT = "Drum Kit";
    public static final String PACK_CUSTOM = "Custom";
    
    // Premium Features
    public static final String PREMIUM_SKU = "premium_subscription";
    public static final String PREMIUM_PRICE = "$2.99/month";
    
    // SharedPreferences Keys
    public static final String PREFS_AUDIO = "audio_prefs";
    public static final String PREFS_UI = "ui_prefs";
    public static final String PREFS_USER = "user_prefs";
    
    // Intent Extra Keys
    public static final String EXTRA_SESSION_ID = "session_id";
    public static final String EXTRA_SOUND_PACK = "sound_pack";
    public static final String EXTRA_PAD_INDEX = "pad_index";
    
    // Database
    public static final String DATABASE_NAME = "musicpad_database";
    public static final int DATABASE_VERSION = 1;
    
    // Ad Unit IDs (Test IDs - replace with real ones for production)
    public static final String AD_BANNER_ID = "ca-app-pub-3940256099942544/6300978111";
    public static final String AD_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712";
    public static final String AD_REWARDED_ID = "ca-app-pub-3940256099942544/5224354917";
    
    // Export Formats
    public static final String EXPORT_FORMAT_MP3 = "mp3";
    public static final String EXPORT_FORMAT_WAV = "wav";
    
    // File Paths
    public static final String RECORDINGS_DIR = "MusicPadStudio/Recordings";
    public static final String EXPORTS_DIR = "MusicPadStudio/Exports";
    
    // Error Messages
    public static final String ERROR_AUDIO_INIT = "Failed to initialize audio engine";
    public static final String ERROR_LOAD_SOUND = "Failed to load sound";
    public static final String ERROR_PERMISSION = "Permission denied";
    
    // Request Codes
    public static final int REQUEST_AUDIO_PERMISSION = 1001;
    public static final int REQUEST_STORAGE_PERMISSION = 1002;
    
    private Constants() {
        // Private constructor to prevent instantiation
    }
}
