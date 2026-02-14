package com.pixandroid.musicpad.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.pixandroid.musicpad.databinding.ActivitySettingsBinding;
import com.pixandroid.musicpad.utils.BuildConfig;

/**
 * Settings Activity - App settings and preferences
 */
public class SettingsActivity extends AppCompatActivity {
    
    private ActivitySettingsBinding binding;
    private SharedPreferences prefs;
    
    private static final String PREFS_NAME = "app_settings";
    private static final String KEY_ANIMATIONS = "animations_enabled";
    private static final String KEY_HAPTIC = "haptic_enabled";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        
        setupToolbar();
        setupSettings();
        loadSettings();
    }
    
    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }
    
    private void setupSettings() {
        // Animations switch
        binding.switchAnimations.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(KEY_ANIMATIONS, isChecked).apply();
        });
        
        // Haptic feedback switch
        binding.switchHaptic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(KEY_HAPTIC, isChecked).apply();
        });
        
        // Premium button
        binding.btnGoPremium.setOnClickListener(v -> {
            Toast.makeText(this, "Premium subscription feature", Toast.LENGTH_SHORT).show();
            // In production, launch billing flow
        });
        
        // Version
        binding.tvVersionNumber.setText("v" + BuildConfig.VERSION_NAME);
    }
    
    private void loadSettings() {
        // Load saved preferences
        boolean animationsEnabled = prefs.getBoolean(KEY_ANIMATIONS, true);
        boolean hapticEnabled = prefs.getBoolean(KEY_HAPTIC, true);
        
        binding.switchAnimations.setChecked(animationsEnabled);
        binding.switchHaptic.setChecked(hapticEnabled);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
