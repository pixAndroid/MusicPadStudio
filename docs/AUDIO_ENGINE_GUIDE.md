# Audio Engine Guide - Music Pad Studio

## Overview

The Audio Engine is the heart of Music Pad Studio, providing low-latency audio playback, recording, and effects processing. This guide covers the implementation details and best practices.

---

## Core Components

### 1. AudioEngine

**Purpose:** Low-latency sound playback using Android's SoundPool

**Key Features:**
- < 100ms latency
- Up to 32 simultaneous audio streams
- Per-pad volume control
- Master volume control
- Efficient memory management

**Implementation Details:**

```java
AudioAttributes audioAttributes = new AudioAttributes.Builder()
    .setUsage(AudioAttributes.USAGE_GAME)
    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
    .build();

SoundPool soundPool = new SoundPool.Builder()
    .setMaxStreams(MAX_STREAMS)
    .setAudioAttributes(audioAttributes)
    .build();
```

**Why SoundPool?**
- Optimized for short sound clips
- Hardware-accelerated on most devices
- Minimal latency
- Easy to use API
- Built-in volume control

**Load Sound:**
```java
int soundId = soundPool.load(context, resourceId, priority);
soundMap.put(padIndex, soundId);
```

**Play Sound:**
```java
int streamId = soundPool.play(
    soundId,           // Sound ID
    leftVolume,        // Left channel volume
    rightVolume,       // Right channel volume
    priority,          // Priority (0-highest)
    loop,              // Loop (-1 = infinite, 0 = no loop)
    rate              // Playback rate (0.5-2.0)
);
```

### 2. RecordingEngine

**Purpose:** Capture and replay pad sequences with precise timing

**Architecture:**
- Event-based recording
- Timestamp-based playback
- Non-blocking asynchronous operation
- Handler-based scheduling

**Recording Flow:**
1. Start recording (capture start time)
2. User taps pad
3. Calculate timestamp = current time - start time
4. Store PadHit(padIndex, timestamp, velocity)
5. Stop recording
6. Save to database

**Playback Flow:**
1. Load PadHits from database
2. Sort by timestamp
3. Schedule each hit using Handler.postDelayed()
4. Play sounds at exact timestamps
5. Handle playback completion

**Timestamp Precision:**
- Millisecond accuracy
- System.currentTimeMillis() for consistency
- Handler scheduling for playback timing

### 3. EffectsProcessor

**Purpose:** Real-time audio effects processing

**Supported Effects:**
1. **Reverb** - PresetReverb
2. **Bass Boost** - BassBoost
3. **Delay** - (Simulated)
4. **Distortion** - (Simulated)
5. **Echo** - (Simulated)

**Effect Levels:**
- OFF (0%)
- LOW (33%)
- MEDIUM (66%)
- HIGH (100%)

**Android Audio Effects Framework:**
```java
// Initialize reverb
reverb = new PresetReverb(priority, audioSessionId);
reverb.setPreset(PresetReverb.PRESET_LARGEROOM);
reverb.setEnabled(true);

// Initialize bass boost
bassBoost = new BassBoost(priority, audioSessionId);
bassBoost.setStrength(strength); // 0-1000
bassBoost.setEnabled(true);
```

**Audio Session ID:**
- Required to attach effects to audio output
- Obtained from AudioTrack or MediaPlayer
- All sounds in same session share effects

### 4. SoundPack

**Purpose:** Manage collections of sounds

**Structure:**
```java
class SoundPack {
    String name;
    String description;
    boolean isPremium;
    boolean isDownloaded;
    List<SoundInfo> sounds;
}

class SoundInfo {
    int padIndex;
    String soundName;
    int resourceId;
}
```

**Default Packs:**
1. **EDM** - Electronic dance music sounds (Free)
2. **Hip-Hop** - Classic hip-hop beats (Free)
3. **Trap** - Modern trap sounds (Premium)
4. **Dubstep** - Heavy dubstep drops (Premium)
5. **Drum Kit** - Acoustic drums (Free)
6. **Custom** - User-imported sounds (Free)

**Loading Sounds:**
```java
for (SoundInfo sound : pack.getSounds()) {
    audioEngine.loadSound(context, sound.padIndex, sound.resourceId);
}
```

---

## Performance Optimization

### 1. Audio Latency Reduction

**Best Practices:**
- Use SoundPool for short clips (< 5 seconds)
- Pre-load all sounds on startup
- Use USAGE_GAME audio attribute
- Enable low-latency mode when available
- Minimize audio processing in playback path

**Low-Latency Detection:**
```java
PackageManager pm = context.getPackageManager();
boolean hasLowLatency = pm.hasSystemFeature(
    PackageManager.FEATURE_AUDIO_LOW_LATENCY
);
```

**Optimal Settings:**
- Sample rate: 44100 Hz (CD quality)
- Buffer size: Minimum supported by device
- Format: 16-bit PCM
- Channels: Stereo (2 channels)

### 2. Memory Management

**Sound Loading:**
- Load sounds asynchronously
- Unload unused sounds
- Monitor memory usage
- Use compressed audio formats (OGG, MP3)

**SoundPool Limits:**
- Maximum 32 simultaneous streams
- Maximum 1MB per sound (recommended)
- Total memory limit: Device dependent

**Memory Optimization:**
```java
// Unload sound when not needed
soundPool.unload(soundId);

// Release SoundPool
soundPool.release();
soundPool = null;
```

### 3. CPU Optimization

**Minimize Processing:**
- Avoid complex calculations in audio callback
- Pre-calculate volume levels
- Use hardware acceleration when available
- Batch operations when possible

**Thread Management:**
- Audio playback on dedicated thread
- UI updates on main thread
- Database operations on background thread

---

## Audio File Specifications

### Recommended Format

**Format:** OGG Vorbis
- Good compression
- Android native support
- Low CPU overhead
- Open source

**Alternatives:**
- MP3: Good compatibility, patented
- WAV: Uncompressed, large files
- AAC: Good quality, moderate size

### Sound Specifications

**Pad Sounds:**
- Format: OGG Vorbis
- Sample rate: 44100 Hz
- Bit depth: 16-bit
- Channels: Mono (stereo if needed)
- Duration: 0.1 - 5 seconds
- File size: < 100KB (< 500KB for longer samples)

**Normalization:**
- Peak level: -3 dB
- RMS level: -18 dB
- No clipping
- Consistent volume across pack

### File Organization

```
res/raw/
├── edm/
│   ├── kick_01.ogg
│   ├── snare_01.ogg
│   ├── hihat_01.ogg
│   └── ...
├── hiphop/
│   ├── kick_01.ogg
│   └── ...
└── ...
```

---

## Audio Focus Management

### Handle Audio Focus

**Purpose:** Respect other audio apps

**Implementation:**
```java
AudioManager audioManager = (AudioManager) 
    context.getSystemService(Context.AUDIO_SERVICE);

AudioManager.OnAudioFocusChangeListener focusListener = 
    new AudioManager.OnAudioFocusChangeListener() {
    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
                // Stop playback
                pausePlayback();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Pause temporarily
                pausePlayback();
                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                // Resume playback
                resumePlayback();
                break;
        }
    }
};

// Request audio focus
int result = audioManager.requestAudioFocus(
    focusListener,
    AudioManager.STREAM_MUSIC,
    AudioManager.AUDIOFOCUS_GAIN
);
```

---

## Testing & Quality Assurance

### Latency Testing

**Measurement:**
1. Connect device to audio analyzer
2. Trigger pad tap
3. Measure time from touch to audio output
4. Target: < 100ms

**Devices to Test:**
- High-end flagship devices
- Mid-range devices
- Budget devices
- Tablets
- Various Android versions

### Sound Quality Testing

**Checklist:**
- No clipping or distortion
- Consistent volume levels
- Clean sound with no artifacts
- Proper stereo imaging
- No audio glitches

### Stress Testing

**Scenarios:**
- Play all 32 pads simultaneously
- Rapid pad triggering
- Long recording sessions
- Effect switching during playback
- Memory pressure situations

---

## Troubleshooting

### Common Issues

**Issue: High Latency**
- **Cause:** Device limitations, buffer size
- **Solution:** Use low-latency API, optimize buffer size

**Issue: Audio Dropouts**
- **Cause:** CPU overload, memory pressure
- **Solution:** Reduce simultaneous streams, optimize code

**Issue: Sound Not Playing**
- **Cause:** Sound not loaded, invalid resource
- **Solution:** Check load status, verify resource ID

**Issue: Volume Too Low**
- **Cause:** Incorrect volume calculation
- **Solution:** Verify volume range (0.0 - 1.0)

### Debug Logging

```java
Log.d(TAG, "Sound loaded: id=" + soundId);
Log.d(TAG, "Playing pad: index=" + padIndex + 
           ", volume=" + volume);
Log.d(TAG, "Stream started: streamId=" + streamId);
```

---

## Future Enhancements

### Planned Features
- [ ] Advanced effects chain
- [ ] Custom effect creation
- [ ] Audio routing options
- [ ] MIDI controller support
- [ ] Multi-output support
- [ ] Spatial audio
- [ ] Audio visualization

### Research Areas
- Neural audio processing
- Machine learning for mastering
- Real-time pitch correction
- Advanced synthesis engines

---

## Resources

### Android Documentation
- [SoundPool](https://developer.android.com/reference/android/media/SoundPool)
- [Audio Effects](https://developer.android.com/reference/android/media/audiofx/package-summary)
- [Low-Latency Audio](https://developer.android.com/ndk/guides/audio/audio-latency)

### Audio Engineering
- Sample rate and Nyquist theorem
- Digital audio fundamentals
- Effect processing algorithms
- Mixing and mastering basics

### Tools
- Audacity: Audio editing
- Adobe Audition: Professional editing
- Reaper: DAW for testing
- Audio analyzer apps

---

## Conclusion

The Audio Engine is designed for professional-grade, low-latency audio performance. Following this guide ensures optimal audio quality, minimal latency, and efficient resource usage.

**Key Takeaways:**
- Use SoundPool for low latency
- Pre-load sounds
- Optimize for target devices
- Test on real hardware
- Monitor performance metrics
