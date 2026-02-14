package com.pixandroid.musicpad.ui.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.pixandroid.musicpad.databinding.ActivityMainBinding;
import com.pixandroid.musicpad.ui.pads.PadsActivity;
import com.pixandroid.musicpad.ui.packs.SoundPacksActivity;
import com.pixandroid.musicpad.ui.recording.RecordingActivity;
import com.pixandroid.musicpad.ui.settings.SettingsActivity;

/**
 * Main activity - Home dashboard
 */
public class MainActivity extends AppCompatActivity {
    
    private ActivityMainBinding binding;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        setupViews();
    }
    
    private void setupViews() {
        // Start Jamming - Navigate to Pads Activity
        binding.btnStartJamming.setOnClickListener(v -> {
            Intent intent = new Intent(this, PadsActivity.class);
            startActivity(intent);
        });
        
        // My Recordings - Navigate to Recording Activity
        binding.btnMyRecordings.setOnClickListener(v -> {
            Intent intent = new Intent(this, RecordingActivity.class);
            startActivity(intent);
        });
        
        // Sound Packs - Navigate to Sound Packs Activity
        binding.btnSoundPacks.setOnClickListener(v -> {
            Intent intent = new Intent(this, SoundPacksActivity.class);
            startActivity(intent);
        });
        
        // Settings - Navigate to Settings Activity
        binding.btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
