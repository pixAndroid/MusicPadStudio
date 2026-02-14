package com.pixandroid.musicpad.ui.pads;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.card.MaterialCardView;
import com.pixandroid.musicpad.R;
import com.pixandroid.musicpad.databinding.ActivityPadsBinding;
import com.pixandroid.musicpad.ui.effects.EffectsActivity;
import com.pixandroid.musicpad.utils.Constants;
import com.pixandroid.musicpad.utils.Utils;
import com.pixandroid.musicpad.viewmodel.PadViewModel;
import com.pixandroid.musicpad.viewmodel.RecordingViewModel;

/**
 * Pads Activity - Main pad grid interface
 */
public class PadsActivity extends AppCompatActivity {
    
    private ActivityPadsBinding binding;
    private PadViewModel padViewModel;
    private RecordingViewModel recordingViewModel;
    
    private View[] padViews;
    private int[] padColors;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPadsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // Initialize ViewModels
        padViewModel = new ViewModelProvider(this).get(PadViewModel.class);
        recordingViewModel = new RecordingViewModel(getApplication(), padViewModel.getAudioEngine());
        
        padViews = new View[Constants.TOTAL_PADS];
        padColors = getResources().getIntArray(R.array.pad_colors);
        
        setupPadGrid();
        setupControls();
        observeViewModel();
    }
    
    /**
     * Setup the 4x8 pad grid
     */
    private void setupPadGrid() {
        GridLayout gridLayout = findViewById(R.id.padGrid);
        gridLayout.removeAllViews();
        
        // Configure grid
        gridLayout.setRowCount(Constants.PAD_ROWS);
        gridLayout.setColumnCount(Constants.PAD_COLUMNS);
        
        // Create pads
        for (int i = 0; i < Constants.TOTAL_PADS; i++) {
            final int padIndex = i;
            
            // Inflate pad item
            View padView = getLayoutInflater().inflate(R.layout.pad_item, gridLayout, false);
            MaterialCardView padCard = padView.findViewById(R.id.padCard);
            TextView padName = padView.findViewById(R.id.tvPadName);
            
            // Set pad name
            padName.setText(Utils.getDefaultPadName(padIndex));
            
            // Set pad color
            int colorIndex = padIndex % padColors.length;
            padCard.setCardBackgroundColor(padColors[colorIndex]);
            
            // Configure grid layout params
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = 0;
            params.rowSpec = GridLayout.spec(i / Constants.PAD_COLUMNS, 1, 1f);
            params.columnSpec = GridLayout.spec(i % Constants.PAD_COLUMNS, 1, 1f);
            params.setMargins(8, 8, 8, 8);
            padView.setLayoutParams(params);
            
            // Set touch listener
            padCard.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    onPadPressed(padIndex, v);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    onPadReleased(padIndex, v);
                    return true;
                }
                return false;
            });
            
            gridLayout.addView(padView);
            padViews[padIndex] = padCard;
        }
    }
    
    /**
     * Handle pad press
     */
    private void onPadPressed(int padIndex, View padView) {
        // Play sound
        padViewModel.playPad(padIndex, 1.0f);
        
        // Record if recording
        if (recordingViewModel.getRecordingEngine().isRecording()) {
            recordingViewModel.recordPadHit(padIndex, 1.0f);
        }
        
        // Visual feedback
        padView.animate()
            .scaleX(0.9f)
            .scaleY(0.9f)
            .alpha(0.7f)
            .setDuration(Constants.PAD_PRESS_DURATION)
            .start();
        
        // Haptic feedback
        Utils.vibrate(this, 50);
    }
    
    /**
     * Handle pad release
     */
    private void onPadReleased(int padIndex, View padView) {
        // Reset visual state
        padView.animate()
            .scaleX(1.0f)
            .scaleY(1.0f)
            .alpha(1.0f)
            .setDuration(Constants.PAD_PRESS_DURATION)
            .start();
    }
    
    /**
     * Setup control buttons
     */
    private void setupControls() {
        // BPM controls
        binding.btnBpmDecrease.setOnClickListener(v -> padViewModel.decreaseBpm());
        binding.btnBpmIncrease.setOnClickListener(v -> padViewModel.increaseBpm());
        
        // Record button
        binding.btnRecord.setOnClickListener(v -> toggleRecording());
        
        // Effects button
        binding.btnEffects.setOnClickListener(v -> {
            Intent intent = new Intent(this, EffectsActivity.class);
            startActivity(intent);
        });
        
        // Volume slider
        binding.sliderVolume.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                padViewModel.setMasterVolume(value / 100f);
            }
        });
        
        // Metronome button
        binding.btnMetronome.setOnClickListener(v -> padViewModel.toggleMetronome());
        
        // Loop button
        binding.btnLoop.setOnClickListener(v -> padViewModel.toggleLoopMode());
    }
    
    /**
     * Toggle recording state
     */
    private void toggleRecording() {
        if (recordingViewModel.getRecordingEngine().isRecording()) {
            recordingViewModel.stopRecording();
            binding.btnRecord.setText(R.string.btn_record);
            binding.btnRecord.setIcon(getDrawable(android.R.drawable.presence_video_online));
        } else {
            recordingViewModel.startRecording();
            binding.btnRecord.setText(R.string.btn_stop_record);
            binding.btnRecord.setIcon(getDrawable(android.R.drawable.presence_video_busy));
        }
    }
    
    /**
     * Observe ViewModel data
     */
    private void observeViewModel() {
        // Observe BPM changes
        padViewModel.getBpmLiveData().observe(this, bpm -> {
            if (bpm != null) {
                binding.tvBpm.setText(String.valueOf(bpm));
            }
        });
        
        // Observe master volume changes
        padViewModel.getMasterVolume().observe(this, volume -> {
            if (volume != null) {
                binding.sliderVolume.setValue(volume * 100);
            }
        });
        
        // Observe metronome state
        padViewModel.getIsMetronomeEnabled().observe(this, enabled -> {
            if (enabled != null && enabled) {
                binding.btnMetronome.setIconTint(getColorStateList(R.color.secondary));
            } else {
                binding.btnMetronome.setIconTint(getColorStateList(R.color.text_disabled));
            }
        });
        
        // Observe loop mode
        padViewModel.getIsLoopMode().observe(this, enabled -> {
            if (enabled != null && enabled) {
                binding.btnLoop.setIconTint(getColorStateList(R.color.secondary));
            } else {
                binding.btnLoop.setIconTint(getColorStateList(R.color.text_disabled));
            }
        });
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
