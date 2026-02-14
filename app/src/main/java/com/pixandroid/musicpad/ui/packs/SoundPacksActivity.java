package com.pixandroid.musicpad.ui.packs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.pixandroid.musicpad.R;
import com.pixandroid.musicpad.audio.SoundPack;
import com.pixandroid.musicpad.databinding.ActivitySoundPacksBinding;
import java.util.ArrayList;
import java.util.List;

/**
 * Sound Packs Activity - Sound pack selection and management
 */
public class SoundPacksActivity extends AppCompatActivity {
    
    private ActivitySoundPacksBinding binding;
    private SoundPackAdapter adapter;
    private List<SoundPack> soundPacks;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySoundPacksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        setupToolbar();
        setupRecyclerView();
        loadSoundPacks();
    }
    
    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }
    
    private void setupRecyclerView() {
        adapter = new SoundPackAdapter();
        binding.rvSoundPacks.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvSoundPacks.setAdapter(adapter);
    }
    
    private void loadSoundPacks() {
        soundPacks = SoundPack.getDefaultPacks();
        adapter.setSoundPacks(soundPacks);
        
        if (soundPacks.isEmpty()) {
            binding.tvEmptyState.setVisibility(View.VISIBLE);
            binding.rvSoundPacks.setVisibility(View.GONE);
        } else {
            binding.tvEmptyState.setVisibility(View.GONE);
            binding.rvSoundPacks.setVisibility(View.VISIBLE);
        }
    }
    
    /**
     * Sound Pack Adapter
     */
    private class SoundPackAdapter extends RecyclerView.Adapter<SoundPackAdapter.ViewHolder> {
        
        private List<SoundPack> packs = new ArrayList<>();
        
        public void setSoundPacks(List<SoundPack> packs) {
            this.packs = packs;
            notifyDataSetChanged();
        }
        
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sound_pack, parent, false);
            return new ViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            SoundPack pack = packs.get(position);
            holder.bind(pack);
        }
        
        @Override
        public int getItemCount() {
            return packs.size();
        }
        
        class ViewHolder extends RecyclerView.ViewHolder {
            
            private final TextView tvPackName;
            private final TextView tvPackDescription;
            private final TextView tvPremiumBadge;
            private final MaterialButton btnSelectPack;
            
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvPackName = itemView.findViewById(R.id.tvPackName);
                tvPackDescription = itemView.findViewById(R.id.tvPackDescription);
                tvPremiumBadge = itemView.findViewById(R.id.tvPremiumBadge);
                btnSelectPack = itemView.findViewById(R.id.btnSelectPack);
            }
            
            public void bind(SoundPack pack) {
                tvPackName.setText(pack.getName());
                tvPackDescription.setText(pack.getDescription());
                
                // Show premium badge if applicable
                if (pack.isPremium()) {
                    tvPremiumBadge.setVisibility(View.VISIBLE);
                } else {
                    tvPremiumBadge.setVisibility(View.GONE);
                }
                
                // Set button text based on download status
                if (pack.isDownloaded()) {
                    btnSelectPack.setText(R.string.btn_select_pack);
                } else {
                    btnSelectPack.setText(R.string.btn_download_pack);
                }
                
                // Handle click
                btnSelectPack.setOnClickListener(v -> {
                    if (pack.isPremium() && !pack.isDownloaded()) {
                        Toast.makeText(SoundPacksActivity.this, 
                            "Premium pack - Requires subscription", 
                            Toast.LENGTH_SHORT).show();
                    } else {
                        selectSoundPack(pack);
                    }
                });
            }
        }
    }
    
    private void selectSoundPack(SoundPack pack) {
        Toast.makeText(this, "Selected: " + pack.getName(), Toast.LENGTH_SHORT).show();
        // In production, load the sound pack into the audio engine
        finish();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
