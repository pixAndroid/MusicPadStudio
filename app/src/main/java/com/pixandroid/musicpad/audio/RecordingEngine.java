package com.pixandroid.musicpad.audio;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.pixandroid.musicpad.models.PadHit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Recording engine for capturing and playing back pad sequences
 */
public class RecordingEngine {
    
    private static final String TAG = "RecordingEngine";
    
    private final AudioEngine audioEngine;
    private final Handler handler;
    
    private boolean isRecording;
    private boolean isPlaying;
    private long recordingStartTime;
    private long playbackStartTime;
    
    private List<PadHit> currentRecording;
    private List<PadHit> playbackHits;
    private int playbackIndex;
    
    private RecordingListener listener;
    
    public RecordingEngine(AudioEngine audioEngine) {
        this.audioEngine = audioEngine;
        this.handler = new Handler(Looper.getMainLooper());
        this.currentRecording = new ArrayList<>();
    }
    
    /**
     * Start recording
     */
    public void startRecording() {
        if (isRecording) {
            Log.w(TAG, "Already recording");
            return;
        }
        
        currentRecording.clear();
        recordingStartTime = System.currentTimeMillis();
        isRecording = true;
        
        if (listener != null) {
            listener.onRecordingStarted();
        }
        
        Log.d(TAG, "Recording started");
    }
    
    /**
     * Stop recording
     */
    public List<PadHit> stopRecording() {
        if (!isRecording) {
            Log.w(TAG, "Not recording");
            return new ArrayList<>();
        }
        
        isRecording = false;
        List<PadHit> recording = new ArrayList<>(currentRecording);
        
        if (listener != null) {
            listener.onRecordingStopped(recording);
        }
        
        Log.d(TAG, "Recording stopped, captured " + recording.size() + " hits");
        return recording;
    }
    
    /**
     * Record a pad hit
     */
    public void recordPadHit(int padIndex, float velocity) {
        if (!isRecording) {
            return;
        }
        
        long timestamp = System.currentTimeMillis() - recordingStartTime;
        PadHit hit = new PadHit();
        hit.setPadIndex(padIndex);
        hit.setTimestamp(timestamp);
        hit.setVelocity(velocity);
        
        currentRecording.add(hit);
        Log.d(TAG, "Recorded hit: pad=" + padIndex + ", timestamp=" + timestamp);
    }
    
    /**
     * Start playback of recorded hits
     */
    public void startPlayback(List<PadHit> hits) {
        if (isPlaying) {
            stopPlayback();
        }
        
        if (hits == null || hits.isEmpty()) {
            Log.w(TAG, "No hits to play back");
            return;
        }
        
        playbackHits = new ArrayList<>(hits);
        Collections.sort(playbackHits, Comparator.comparingLong(PadHit::getTimestamp));
        
        playbackIndex = 0;
        playbackStartTime = System.currentTimeMillis();
        isPlaying = true;
        
        if (listener != null) {
            listener.onPlaybackStarted();
        }
        
        scheduleNextHit();
        Log.d(TAG, "Playback started with " + playbackHits.size() + " hits");
    }
    
    /**
     * Schedule the next hit in playback
     */
    private void scheduleNextHit() {
        if (!isPlaying || playbackIndex >= playbackHits.size()) {
            stopPlayback();
            return;
        }
        
        PadHit hit = playbackHits.get(playbackIndex);
        long currentTime = System.currentTimeMillis() - playbackStartTime;
        long delay = hit.getTimestamp() - currentTime;
        
        if (delay < 0) {
            delay = 0;
        }
        
        handler.postDelayed(() -> {
            if (isPlaying) {
                audioEngine.playPad(hit.getPadIndex(), hit.getVelocity());
                
                if (listener != null) {
                    listener.onPadPlayed(hit.getPadIndex());
                }
                
                playbackIndex++;
                scheduleNextHit();
            }
        }, delay);
    }
    
    /**
     * Stop playback
     */
    public void stopPlayback() {
        if (!isPlaying) {
            return;
        }
        
        isPlaying = false;
        handler.removeCallbacksAndMessages(null);
        
        if (listener != null) {
            listener.onPlaybackStopped();
        }
        
        Log.d(TAG, "Playback stopped");
    }
    
    /**
     * Check if currently recording
     */
    public boolean isRecording() {
        return isRecording;
    }
    
    /**
     * Check if currently playing back
     */
    public boolean isPlaying() {
        return isPlaying;
    }
    
    /**
     * Get current recording duration
     */
    public long getRecordingDuration() {
        if (!isRecording) {
            return 0;
        }
        return System.currentTimeMillis() - recordingStartTime;
    }
    
    /**
     * Set recording listener
     */
    public void setListener(RecordingListener listener) {
        this.listener = listener;
    }
    
    /**
     * Clear current recording
     */
    public void clearRecording() {
        currentRecording.clear();
    }
    
    /**
     * Listener interface for recording events
     */
    public interface RecordingListener {
        void onRecordingStarted();
        void onRecordingStopped(List<PadHit> hits);
        void onPlaybackStarted();
        void onPlaybackStopped();
        void onPadPlayed(int padIndex);
    }
}
