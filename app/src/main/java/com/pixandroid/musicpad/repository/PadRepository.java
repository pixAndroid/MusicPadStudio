package com.pixandroid.musicpad.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.pixandroid.musicpad.database.PadHitDao;
import com.pixandroid.musicpad.database.SessionDao;
import com.pixandroid.musicpad.database.SessionDatabase;
import com.pixandroid.musicpad.models.PadHit;
import com.pixandroid.musicpad.models.Session;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repository for managing pad-related data operations
 */
public class PadRepository {
    
    private final SessionDao sessionDao;
    private final PadHitDao padHitDao;
    private final ExecutorService executorService;
    
    public PadRepository(Application application) {
        SessionDatabase database = SessionDatabase.getInstance(application);
        sessionDao = database.sessionDao();
        padHitDao = database.padHitDao();
        executorService = Executors.newFixedThreadPool(2);
    }
    
    // Session operations
    public void insertSession(Session session, OnSessionInsertedListener listener) {
        executorService.execute(() -> {
            long id = sessionDao.insert(session);
            session.setId(id);
            if (listener != null) {
                listener.onSessionInserted(id);
            }
        });
    }
    
    public void updateSession(Session session) {
        executorService.execute(() -> sessionDao.update(session));
    }
    
    public void deleteSession(Session session) {
        executorService.execute(() -> sessionDao.delete(session));
    }
    
    public void deleteSessionById(long id) {
        executorService.execute(() -> sessionDao.deleteById(id));
    }
    
    public LiveData<List<Session>> getAllSessions() {
        return sessionDao.getAllSessions();
    }
    
    public LiveData<Session> getSessionById(long id) {
        return sessionDao.getSessionById(id);
    }
    
    // PadHit operations
    public void insertPadHit(PadHit padHit) {
        executorService.execute(() -> padHitDao.insert(padHit));
    }
    
    public void insertAllPadHits(List<PadHit> padHits) {
        executorService.execute(() -> padHitDao.insertAll(padHits));
    }
    
    public LiveData<List<PadHit>> getHitsForSession(long sessionId) {
        return padHitDao.getHitsForSession(sessionId);
    }
    
    public void getHitsForSessionSync(long sessionId, OnHitsLoadedListener listener) {
        executorService.execute(() -> {
            List<PadHit> hits = padHitDao.getHitsForSessionSync(sessionId);
            if (listener != null) {
                listener.onHitsLoaded(hits);
            }
        });
    }
    
    public void deleteHitsForSession(long sessionId) {
        executorService.execute(() -> padHitDao.deleteHitsForSession(sessionId));
    }
    
    // Callbacks
    public interface OnSessionInsertedListener {
        void onSessionInserted(long sessionId);
    }
    
    public interface OnHitsLoadedListener {
        void onHitsLoaded(List<PadHit> hits);
    }
    
    public void shutdown() {
        executorService.shutdown();
    }
}
