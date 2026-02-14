package com.pixandroid.musicpad.ui.recording;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.pixandroid.musicpad.R;
import com.pixandroid.musicpad.databinding.ActivityRecordingManagerBinding;
import com.pixandroid.musicpad.models.Session;
import com.pixandroid.musicpad.utils.Utils;
import com.pixandroid.musicpad.viewmodel.RecordingViewModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Recording Activity - Recording session manager
 */
public class RecordingActivity extends AppCompatActivity {
    
    private ActivityRecordingManagerBinding binding;
    private RecordingViewModel viewModel;
    private RecordingAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecordingManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(RecordingViewModel.class);
        
        setupToolbar();
        setupRecyclerView();
        observeViewModel();
    }
    
    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }
    
    private void setupRecyclerView() {
        adapter = new RecordingAdapter();
        binding.rvRecordings.setLayoutManager(new LinearLayoutManager(this));
        binding.rvRecordings.setAdapter(adapter);
    }
    
    private void observeViewModel() {
        viewModel.getAllSessions().observe(this, sessions -> {
            if (sessions != null && !sessions.isEmpty()) {
                adapter.setSessions(sessions);
                binding.rvRecordings.setVisibility(View.VISIBLE);
                binding.emptyState.setVisibility(View.GONE);
            } else {
                binding.rvRecordings.setVisibility(View.GONE);
                binding.emptyState.setVisibility(View.VISIBLE);
            }
        });
    }
    
    /**
     * Recording Adapter
     */
    private class RecordingAdapter extends RecyclerView.Adapter<RecordingAdapter.ViewHolder> {
        
        private List<Session> sessions = new ArrayList<>();
        
        public void setSessions(List<Session> sessions) {
            this.sessions = sessions;
            notifyDataSetChanged();
        }
        
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recording, parent, false);
            return new ViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Session session = sessions.get(position);
            holder.bind(session);
        }
        
        @Override
        public int getItemCount() {
            return sessions.size();
        }
        
        class ViewHolder extends RecyclerView.ViewHolder {
            
            private final TextView tvRecordingName;
            private final TextView tvRecordingDuration;
            private final TextView tvRecordingDate;
            private final ImageButton btnPlay;
            private final ImageButton btnExport;
            private final ImageButton btnDelete;
            
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvRecordingName = itemView.findViewById(R.id.tvRecordingName);
                tvRecordingDuration = itemView.findViewById(R.id.tvRecordingDuration);
                tvRecordingDate = itemView.findViewById(R.id.tvRecordingDate);
                btnPlay = itemView.findViewById(R.id.btnPlayRecording);
                btnExport = itemView.findViewById(R.id.btnExportRecording);
                btnDelete = itemView.findViewById(R.id.btnDeleteRecording);
            }
            
            public void bind(Session session) {
                tvRecordingName.setText(session.getName());
                tvRecordingDuration.setText(getString(R.string.recording_duration, 
                    Utils.formatDuration(session.getDuration())));
                tvRecordingDate.setText(Utils.formatDateShort(session.getCreatedAt()));
                
                btnPlay.setOnClickListener(v -> playRecording(session));
                btnExport.setOnClickListener(v -> exportRecording(session));
                btnDelete.setOnClickListener(v -> showDeleteDialog(session));
            }
        }
    }
    
    private void playRecording(Session session) {
        viewModel.playSession(session.getId());
        Toast.makeText(this, "Playing: " + session.getName(), Toast.LENGTH_SHORT).show();
    }
    
    private void exportRecording(Session session) {
        Toast.makeText(this, "Export functionality - Premium feature", Toast.LENGTH_SHORT).show();
    }
    
    private void showDeleteDialog(Session session) {
        new AlertDialog.Builder(this)
            .setTitle(R.string.delete_confirm_title)
            .setMessage(R.string.delete_confirm_message)
            .setPositiveButton(R.string.btn_yes, (dialog, which) -> {
                viewModel.deleteSession(session.getId());
                Toast.makeText(this, "Recording deleted", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton(R.string.btn_no, null)
            .show();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
