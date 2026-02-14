package com.pixandroid.musicpad.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.pixandroid.musicpad.models.PadHit;
import com.pixandroid.musicpad.models.Session;

/**
 * Room database for Music Pad Studio
 */
@Database(entities = {Session.class, PadHit.class}, version = 1, exportSchema = true)
@TypeConverters({Converters.class})
public abstract class SessionDatabase extends RoomDatabase {
    
    private static final String DATABASE_NAME = "musicpad_database";
    private static SessionDatabase instance;
    
    public abstract SessionDao sessionDao();
    public abstract PadHitDao padHitDao();
    
    public static synchronized SessionDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.getApplicationContext(),
                SessionDatabase.class,
                DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build();
        }
        return instance;
    }
    
    public static void destroyInstance() {
        instance = null;
    }
}
