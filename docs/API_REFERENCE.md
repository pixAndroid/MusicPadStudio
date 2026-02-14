# API Reference - Music Pad Studio

## Overview

This document provides detailed API reference for all public classes, methods, and interfaces in Music Pad Studio.

---

## Audio Package (`com.pixandroid.musicpad.audio`)

### AudioEngine

Low-latency audio playback engine using SoundPool.

#### Constructor
```java
public AudioEngine()
```
Creates a new AudioEngine instance with SoundPool initialized.

#### Methods

##### loadSound
```java
public int loadSound(Context context, int padIndex, int resourceId)
```
Loads a sound resource for a specific pad.

**Parameters:**
- `context` - Application context
- `padIndex` - Pad index (0-31)
- `resourceId` - Resource ID of sound file

**Returns:** Sound ID assigned by SoundPool, or -1 on error

---

##### playPad
```java
public int playPad(int padIndex)
public int playPad(int padIndex, float velocity)
```
Plays sound for the specified pad.

**Parameters:**
- `padIndex` - Pad index (0-31)
- `velocity` - Velocity/volume (0.0 - 1.0), defaults to 1.0

**Returns:** Stream ID, or -1 on error

---

##### setPadVolume
```java
public void setPadVolume(int padIndex, float volume)
```
Sets volume for a specific pad.

**Parameters:**
- `padIndex` - Pad index (0-31)
- `volume` - Volume level (0.0 - 1.0)

---

##### setMasterVolume
```java
public void setMasterVolume(float volume)
```
Sets master volume for all pads.

**Parameters:**
- `volume` - Master volume (0.0 - 1.0)

---

##### release
```java
public void release()
```
Releases all audio resources. Must be called when AudioEngine is no longer needed.

---

### RecordingEngine

Timestamp-based recording and playback engine.

#### Constructor
```java
public RecordingEngine(AudioEngine audioEngine)
```
Creates a RecordingEngine linked to an AudioEngine.

**Parameters:**
- `audioEngine` - AudioEngine instance for playback

#### Methods

##### startRecording
```java
public void startRecording()
```
Starts recording pad hits with timestamps.

---

##### stopRecording
```java
public List<PadHit> stopRecording()
```
Stops recording and returns the recorded hits.

**Returns:** List of PadHit objects

---

##### recordPadHit
```java
public void recordPadHit(int padIndex, float velocity)
```
Records a pad hit during recording.

**Parameters:**
- `padIndex` - Pad index (0-31)
- `velocity` - Hit velocity (0.0 - 1.0)

---

##### startPlayback
```java
public void startPlayback(List<PadHit> hits)
```
Starts playback of recorded hits.

**Parameters:**
- `hits` - List of PadHit objects to play back

---

##### stopPlayback
```java
public void stopPlayback()
```
Stops current playback.

---

##### setListener
```java
public void setListener(RecordingListener listener)
```
Sets listener for recording events.

**Parameters:**
- `listener` - RecordingListener implementation

---

#### RecordingListener Interface

```java
public interface RecordingListener {
    void onRecordingStarted();
    void onRecordingStopped(List<PadHit> hits);
    void onPlaybackStarted();
    void onPlaybackStopped();
    void onPadPlayed(int padIndex);
}
```

---

### EffectsProcessor

Real-time audio effects processor.

#### Constructor
```java
public EffectsProcessor(int audioSessionId)
```
Creates an EffectsProcessor for the given audio session.

**Parameters:**
- `audioSessionId` - Audio session ID from AudioEngine

#### Methods

##### setReverbLevel
```java
public void setReverbLevel(EffectLevel level)
```
Sets reverb effect level.

**Parameters:**
- `level` - Effect level (OFF, LOW, MEDIUM, HIGH)

---

##### setBassBoostLevel
```java
public void setBassBoostLevel(EffectLevel level)
```
Sets bass boost effect level.

**Parameters:**
- `level` - Effect level (OFF, LOW, MEDIUM, HIGH)

---

##### resetAllEffects
```java
public void resetAllEffects()
```
Resets all effects to OFF.

---

##### release
```java
public void release()
```
Releases all effect resources.

---

#### EffectLevel Enum

```java
public enum EffectLevel {
    OFF(0),
    LOW(33),
    MEDIUM(66),
    HIGH(100);
}
```

---

### SoundPack

Sound pack management.

#### Constructor
```java
public SoundPack(String name, String description, boolean isPremium)
```
Creates a new SoundPack.

**Parameters:**
- `name` - Pack name
- `description` - Pack description
- `isPremium` - Whether pack requires premium

#### Methods

##### addSound
```java
public void addSound(int padIndex, String soundName, int resourceId)
```
Adds a sound to the pack.

**Parameters:**
- `padIndex` - Pad index (0-31)
- `soundName` - Display name
- `resourceId` - Resource ID

---

##### loadIntoEngine
```java
public void loadIntoEngine(Context context, AudioEngine audioEngine)
```
Loads all sounds from pack into AudioEngine.

**Parameters:**
- `context` - Application context
- `audioEngine` - Target AudioEngine

---

##### getDefaultPacks
```java
public static List<SoundPack> getDefaultPacks()
```
Returns list of default sound packs.

**Returns:** List of SoundPack objects

---

## Models Package (`com.pixandroid.musicpad.models`)

### Session

Recording session entity.

#### Fields
```java
@PrimaryKey(autoGenerate = true)
private long id;
private String name;
private long duration;
private Date createdAt;
private Date modifiedAt;
private int bpm;
private String soundPackName;
private boolean isLooped;
```

#### Methods
Standard getters and setters for all fields.

---

### PadHit

Individual pad hit record.

#### Fields
```java
@PrimaryKey(autoGenerate = true)
private long id;
private long sessionId;
private int padIndex;
private long timestamp;
private float velocity;
private int soundId;
```

#### Constructor
```java
public PadHit()
public PadHit(long sessionId, int padIndex, long timestamp)
```

---

## Database Package (`com.pixandroid.musicpad.database`)

### SessionDao

Data access for sessions.

#### Methods

##### insert
```java
@Insert
long insert(Session session)
```
Inserts a session and returns the ID.

---

##### getAllSessions
```java
@Query("SELECT * FROM sessions ORDER BY modifiedAt DESC")
LiveData<List<Session>> getAllSessions()
```
Returns all sessions ordered by modification date.

---

##### getSessionById
```java
@Query("SELECT * FROM sessions WHERE id = :id")
LiveData<Session> getSessionById(long id)
```
Returns a specific session by ID.

---

##### delete
```java
@Delete
void delete(Session session)
```
Deletes a session.

---

### PadHitDao

Data access for pad hits.

#### Methods

##### insert
```java
@Insert
long insert(PadHit padHit)
```
Inserts a pad hit.

---

##### insertAll
```java
@Insert
void insertAll(List<PadHit> padHits)
```
Inserts multiple pad hits.

---

##### getHitsForSession
```java
@Query("SELECT * FROM pad_hits WHERE sessionId = :sessionId ORDER BY timestamp ASC")
LiveData<List<PadHit>> getHitsForSession(long sessionId)
```
Returns all hits for a session.

---

### SessionDatabase

Room database.

#### Methods

##### getInstance
```java
public static synchronized SessionDatabase getInstance(Context context)
```
Returns singleton database instance.

**Parameters:**
- `context` - Application context

**Returns:** SessionDatabase instance

---

## Repository Package (`com.pixandroid.musicpad.repository`)

### PadRepository

Manages pad-related data operations.

#### Constructor
```java
public PadRepository(Application application)
```

#### Methods

##### insertSession
```java
public void insertSession(Session session, OnSessionInsertedListener listener)
```
Inserts a session asynchronously.

**Parameters:**
- `session` - Session to insert
- `listener` - Callback for inserted ID

---

##### getAllSessions
```java
public LiveData<List<Session>> getAllSessions()
```
Returns LiveData of all sessions.

---

##### insertPadHit
```java
public void insertPadHit(PadHit padHit)
```
Inserts a pad hit asynchronously.

---

### AudioRepository

Manages audio preferences.

#### Constructor
```java
public AudioRepository(Application application)
```

#### Methods

##### getCurrentSoundPack
```java
public String getCurrentSoundPack()
```
Returns currently selected sound pack name.

---

##### setCurrentSoundPack
```java
public void setCurrentSoundPack(String packName)
```
Sets current sound pack.

---

##### getBpm
```java
public int getBpm()
```
Returns current BPM setting.

---

##### setBpm
```java
public void setBpm(int bpm)
```
Sets BPM (clamped to 60-300).

---

## ViewModel Package (`com.pixandroid.musicpad.viewmodel`)

### PadViewModel

Manages pad grid state.

#### Constructor
```java
public PadViewModel(@NonNull Application application)
```

#### Methods

##### playPad
```java
public void playPad(int padIndex, float velocity)
```
Plays a pad.

---

##### setBpm
```java
public void setBpm(int newBpm)
```
Sets BPM.

---

##### setMasterVolume
```java
public void setMasterVolume(float volume)
```
Sets master volume.

---

##### getBpmLiveData
```java
public LiveData<Integer> getBpmLiveData()
```
Returns BPM as LiveData.

---

### RecordingViewModel

Manages recording sessions.

#### Constructor
```java
public RecordingViewModel(@NonNull Application application, AudioEngine audioEngine)
```

#### Methods

##### startRecording
```java
public void startRecording()
```
Starts recording.

---

##### stopRecording
```java
public void stopRecording()
```
Stops recording and saves to database.

---

##### playSession
```java
public void playSession(long sessionId)
```
Plays back a saved session.

---

##### getAllSessions
```java
public LiveData<List<Session>> getAllSessions()
```
Returns all sessions.

---

### EffectsViewModel

Manages audio effects.

#### Constructor
```java
public EffectsViewModel(@NonNull Application application)
```

#### Methods

##### setReverbLevel
```java
public void setReverbLevel(EffectLevel level)
```
Sets reverb level.

---

##### resetAllEffects
```java
public void resetAllEffects()
```
Resets all effects.

---

## Utils Package (`com.pixandroid.musicpad.utils`)

### Constants

Application constants.

#### Fields
```java
public static final int MIN_BPM = 60;
public static final int MAX_BPM = 300;
public static final int DEFAULT_BPM = 120;
public static final int TOTAL_PADS = 32;
public static final int PAD_ROWS = 4;
public static final int PAD_COLUMNS = 8;
// ... and more
```

---

### Utils

Utility methods.

#### Methods

##### formatDuration
```java
public static String formatDuration(long durationMs)
```
Formats duration to MM:SS.

---

##### formatDate
```java
public static String formatDate(Date date)
```
Formats date to readable string.

---

##### vibrate
```java
public static void vibrate(Context context, long durationMs)
```
Triggers haptic feedback.

---

##### getDefaultPadName
```java
public static String getDefaultPadName(int padIndex)
```
Returns default name for pad.

---

## Activities Package (`com.pixandroid.musicpad.ui.*`)

### MainActivity

Home dashboard activity.

**Layout:** `activity_main.xml`

**Features:**
- Navigation to all screens
- App version display

---

### PadsActivity

Main pad grid interface.

**Layout:** `activity_pads.xml`

**Features:**
- 4×8 pad grid
- BPM control
- Volume control
- Recording toggle
- Effects access
- Metronome
- Loop mode

---

### SoundPacksActivity

Sound pack selection.

**Layout:** `activity_sound_packs.xml`

**Features:**
- Grid of available packs
- Download/select packs
- Premium badge display

---

### RecordingActivity

Recording session manager.

**Layout:** `activity_recording_manager.xml`

**Features:**
- List of saved sessions
- Play/export/delete sessions
- Empty state handling

---

### EffectsActivity

Audio effects editor.

**Layout:** `activity_effects.xml`

**Features:**
- 5 effect sliders
- Real-time effect adjustment
- Reset all effects

---

### SettingsActivity

App settings.

**Layout:** `activity_settings.xml`

**Features:**
- Animation toggle
- Haptic feedback toggle
- Premium upgrade
- Version display

---

## Version History

### v1.0.0 (Initial Release)
- Complete MVVM implementation
- 32-pad grid with multi-touch
- Recording and playback system
- 6 sound packs
- Real-time effects
- Settings management

---

## Support

For API questions or issues:
- Email: support@musicpadstudio.com
- GitHub: https://github.com/pixAndroid/MusicPadStudio
- Documentation: https://musicpadstudio.com/docs
