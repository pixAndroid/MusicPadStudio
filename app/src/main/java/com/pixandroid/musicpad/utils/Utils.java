package com.pixandroid.musicpad.utils;

import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Utility helper methods
 */
public class Utils {
    
    private static final String TAG = "Utils";
    
    /**
     * Format duration in milliseconds to MM:SS format
     */
    public static String formatDuration(long durationMs) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs) - 
                       TimeUnit.MINUTES.toSeconds(minutes);
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }
    
    /**
     * Format date to readable string
     */
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
        return sdf.format(date);
    }
    
    /**
     * Format date to short string
     */
    public static String formatDateShort(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return sdf.format(date);
    }
    
    /**
     * Convert BPM to milliseconds per beat
     */
    public static long bpmToMs(int bpm) {
        return 60000L / bpm;
    }
    
    /**
     * Calculate pad index from row and column
     */
    public static int getPadIndex(int row, int col) {
        return row * Constants.PAD_COLUMNS + col;
    }
    
    /**
     * Get row from pad index
     */
    public static int getRowFromIndex(int index) {
        return index / Constants.PAD_COLUMNS;
    }
    
    /**
     * Get column from pad index
     */
    public static int getColFromIndex(int index) {
        return index % Constants.PAD_COLUMNS;
    }
    
    /**
     * Clamp a value between min and max
     */
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * Clamp a float value between min and max
     */
    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * Perform haptic feedback
     */
    public static void vibrate(Context context, long durationMs) {
        try {
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null && vibrator.hasVibrator()) {
                vibrator.vibrate(durationMs);
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to vibrate", e);
        }
    }
    
    /**
     * Check if a string is null or empty
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Get default pad name
     */
    public static String getDefaultPadName(int padIndex) {
        String[] names = {
            "Kick", "Snare", "Hi-Hat", "Clap",
            "Tom 1", "Tom 2", "Crash", "Ride",
            "Perc 1", "Perc 2", "Perc 3", "Perc 4",
            "FX 1", "FX 2", "FX 3", "FX 4",
            "Bass 1", "Bass 2", "Lead 1", "Lead 2",
            "Synth 1", "Synth 2", "Synth 3", "Synth 4",
            "Pad 25", "Pad 26", "Pad 27", "Pad 28",
            "Pad 29", "Pad 30", "Pad 31", "Pad 32"
        };
        
        if (padIndex >= 0 && padIndex < names.length) {
            return names[padIndex];
        }
        return "Pad " + (padIndex + 1);
    }
    
    /**
     * Log debug message
     */
    public static void logDebug(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message);
        }
    }
    
    /**
     * Log error message
     */
    public static void logError(String tag, String message, Throwable throwable) {
        Log.e(tag, message, throwable);
    }
    
    /**
     * Generate recording name
     */
    public static String generateRecordingName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault());
        return "Recording_" + sdf.format(new Date());
    }
    
    /**
     * Convert decibels to linear volume
     */
    public static float dbToLinear(float db) {
        return (float) Math.pow(10, db / 20);
    }
    
    /**
     * Convert linear volume to decibels
     */
    public static float linearToDb(float linear) {
        return 20 * (float) Math.log10(linear);
    }
    
    private Utils() {
        // Private constructor to prevent instantiation
    }
}
