package com.pixandroid.musicpad;

import android.app.Application;
import android.util.Log;
import com.pixandroid.musicpad.database.SessionDatabase;

/**
 * Application class for Music Pad Studio
 */
public class App extends Application {
    
    private static final String TAG = "MusicPadApp";
    private static App instance;
    
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        
        Log.d(TAG, "Music Pad Studio initialized");
        
        // Initialize database
        SessionDatabase.getInstance(this);
        
        // Initialize other components as needed
        initializeComponents();
    }
    
    /**
     * Initialize application components
     */
    private void initializeComponents() {
        // Initialize crash reporting, analytics, etc.
        // This is where you would initialize Firebase, Crashlytics, etc.
        Log.d(TAG, "Components initialized");
    }
    
    /**
     * Get application instance
     */
    public static App getInstance() {
        return instance;
    }
    
    @Override
    public void onTerminate() {
        super.onTerminate();
        
        // Clean up resources
        SessionDatabase.destroyInstance();
        
        Log.d(TAG, "Music Pad Studio terminated");
    }
}
