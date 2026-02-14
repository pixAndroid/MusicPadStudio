package com.pixandroid.musicpad.ui.effects;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.slider.Slider;
import com.pixandroid.musicpad.R;
import com.pixandroid.musicpad.audio.EffectsProcessor.EffectLevel;
import com.pixandroid.musicpad.databinding.ActivityEffectsBinding;
import com.pixandroid.musicpad.viewmodel.EffectsViewModel;

/**
 * Effects Activity - Audio effects editor
 */
public class EffectsActivity extends AppCompatActivity {
    
    private ActivityEffectsBinding binding;
    private EffectsViewModel viewModel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEffectsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(EffectsViewModel.class);
        
        setupToolbar();
        setupSliders();
        setupButtons();
        observeViewModel();
    }
    
    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }
    
    private void setupSliders() {
        // Reverb slider
        binding.sliderReverb.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                viewModel.setReverbLevel(getLevelFromValue((int) value));
            }
        });
        
        // Delay slider
        binding.sliderDelay.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                viewModel.setDelayLevel(getLevelFromValue((int) value));
            }
        });
        
        // Distortion slider
        binding.sliderDistortion.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                viewModel.setDistortionLevel(getLevelFromValue((int) value));
            }
        });
        
        // Bass Boost slider
        binding.sliderBassBoost.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                viewModel.setBassBoostLevel(getLevelFromValue((int) value));
            }
        });
        
        // Echo slider
        binding.sliderEcho.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                viewModel.setEchoLevel(getLevelFromValue((int) value));
            }
        });
    }
    
    private void setupButtons() {
        // Reset button
        binding.btnResetEffects.setOnClickListener(v -> {
            viewModel.resetAllEffects();
            resetSliders();
        });
    }
    
    private void observeViewModel() {
        // Observe effect levels and update sliders
        viewModel.getReverbLevel().observe(this, level -> {
            if (level != null) {
                binding.sliderReverb.setValue(getValueFromLevel(level));
            }
        });
        
        viewModel.getDelayLevel().observe(this, level -> {
            if (level != null) {
                binding.sliderDelay.setValue(getValueFromLevel(level));
            }
        });
        
        viewModel.getDistortionLevel().observe(this, level -> {
            if (level != null) {
                binding.sliderDistortion.setValue(getValueFromLevel(level));
            }
        });
        
        viewModel.getBassBoostLevel().observe(this, level -> {
            if (level != null) {
                binding.sliderBassBoost.setValue(getValueFromLevel(level));
            }
        });
        
        viewModel.getEchoLevel().observe(this, level -> {
            if (level != null) {
                binding.sliderEcho.setValue(getValueFromLevel(level));
            }
        });
    }
    
    private EffectLevel getLevelFromValue(int value) {
        switch (value) {
            case 0:
                return EffectLevel.OFF;
            case 1:
                return EffectLevel.LOW;
            case 2:
                return EffectLevel.MEDIUM;
            case 3:
                return EffectLevel.HIGH;
            default:
                return EffectLevel.OFF;
        }
    }
    
    private int getValueFromLevel(EffectLevel level) {
        switch (level) {
            case OFF:
                return 0;
            case LOW:
                return 1;
            case MEDIUM:
                return 2;
            case HIGH:
                return 3;
            default:
                return 0;
        }
    }
    
    private void resetSliders() {
        binding.sliderReverb.setValue(0);
        binding.sliderDelay.setValue(0);
        binding.sliderDistortion.setValue(0);
        binding.sliderBassBoost.setValue(0);
        binding.sliderEcho.setValue(0);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
