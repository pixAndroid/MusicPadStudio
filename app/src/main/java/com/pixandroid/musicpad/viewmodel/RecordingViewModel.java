package com.pixandroid.musicpad.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.pixandroid.musicpad.audio.AudioEngine;
import com.pixandroid.musicpad.audio.RecordingEngine;
import com.pixandroid.musicpad.models.PadHit;
import com.pixandroid.musicpad.models.Session;
import com.pixandroid.musicpad.repository.PadRepository;
import com.pixandroid.musicpad.utils.Utils;
import java.util.Date;
import java.util.List;

/**
 * ViewModel for recording management
 */
public class RecordingViewModel extends AndroidViewModel {
    
    private final PadRepository padRepository;
    private final RecordingEngine recordingEngine;
    
    private final MutableLiveData<Boolean> isRecording;
    private final MutableLiveData<Boolean> isPlaying;
    private final MutableLiveData<Long> recordingDuration;
    private final MutableLiveData<Session> currentSession;
    
    private long currentSessionId = -1;
    
    public RecordingViewModel(@NonNull Application application, AudioEngine audioEngine) {
        super(application);
        
        padRepository = new PadRepository(application);
        recordingEngine = new RecordingEngine(audioEngine);
        
        isRecording = new MutableLiveData<>(false);
        isPlaying = new MutableLiveData<>(false);
        recordingDuration = new MutableLiveData<>(0L);
        currentSession = new MutableLiveData<>();
        
        setupRecordingListener();
    }
    
    /**
     * Setup recording listener
     */
    private void setupRecordingListener() {
        recordingEngine.setListener(new RecordingEngine.RecordingListener() {
            @Override
            public void onRecordingStarted() {
                isRecording.postValue(true);
            }
            
            @Override
            public void onRecordingStopped(List<PadHit> hits) {
                isRecording.postValue(false);
                saveRecording(hits);
            }
            
            @Override
            public void onPlaybackStarted() {
                isPlaying.postValue(true);
            }
            
            @Override
            public void onPlaybackStopped() {
                isPlaying.postValue(false);
            }
            
            @Override
            public void onPadPlayed(int padIndex) {
                // Handle pad playback visual feedback
            }
        });
    }
    
    /**
     * Start recording
     */
    public void startRecording() {
        recordingEngine.startRecording();
        
        // Update duration periodically
        updateRecordingDuration();
    }
    
    /**
     * Stop recording
     */
    public void stopRecording() {
        recordingEngine.stopRecording();
    }
    
    /**
     * Record pad hit
     */
    public void recordPadHit(int padIndex, float velocity) {
        recordingEngine.recordPadHit(padIndex, velocity);
    }
    
    /**
     * Save recording to database
     */
    private void saveRecording(List<PadHit> hits) {
        if (hits.isEmpty()) {
            return;
        }
        
        Session session = new Session();
        session.setName(Utils.generateRecordingName());
        session.setDuration(recordingEngine.getRecordingDuration());
        session.setCreatedAt(new Date());
        session.setModifiedAt(new Date());
        
        padRepository.insertSession(session, sessionId -> {
            currentSessionId = sessionId;
            
            // Update session ID for all hits
            for (PadHit hit : hits) {
                hit.setSessionId(sessionId);
            }
            
            // Save hits
            padRepository.insertAllPadHits(hits);
            
            // Update current session
            currentSession.postValue(session);
        });
    }
    
    /**
     * Load and play a session
     */
    public void playSession(long sessionId) {
        padRepository.getHitsForSessionSync(sessionId, hits -> {
            if (hits != null && !hits.isEmpty()) {
                recordingEngine.startPlayback(hits);
            }
        });
    }
    
    /**
     * Stop playback
     */
    public void stopPlayback() {
        recordingEngine.stopPlayback();
    }
    
    /**
     * Delete a session
     */
    public void deleteSession(long sessionId) {
        padRepository.deleteSessionById(sessionId);
        padRepository.deleteHitsForSession(sessionId);
    }
    
    /**
     * Get all sessions
     */
    public LiveData<List<Session>> getAllSessions() {
        return padRepository.getAllSessions();
    }
    
    /**
     * Update recording duration
     */
    private void updateRecordingDuration() {
        if (recordingEngine.isRecording()) {
            long duration = recordingEngine.getRecordingDuration();
            recordingDuration.postValue(duration);
            
            // Schedule next update
            getApplication().getMainExecutor().execute(() -> {
                try {
                    Thread.sleep(100);
                    updateRecordingDuration();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
    
    // LiveData getters
    public LiveData<Boolean> getIsRecording() {
        return isRecording;
    }
    
    public LiveData<Boolean> getIsPlaying() {
        return isPlaying;
    }
    
    public LiveData<Long> getRecordingDuration() {
        return recordingDuration;
    }
    
    public LiveData<Session> getCurrentSession() {
        return currentSession;
    }
    
    public RecordingEngine getRecordingEngine() {
        return recordingEngine;
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        recordingEngine.stopPlayback();
        padRepository.shutdown();
    }
}
