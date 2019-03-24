package com.mmh19.spring.activities;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mmh19.spring.BreakDialogFragment;
import com.mmh19.spring.R;
import com.mmh19.spring.adapters.BreaksAdapter;
import com.mmh19.spring.models.Break;
import com.mmh19.spring.viewmodels.BreakViewModel;
import com.mmh19.spring.views.RecyclerViewEmpty;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SimpleItemAnimator;

public class MainActivity extends AppCompatActivity implements BreaksAdapter.JoinBreak {

    private RecyclerViewEmpty recyclerBreaks;
    private BreaksAdapter breaksAdapter;
    private LinearLayoutManager linearLayoutManager;

    private Button buttonBreak;

    private BreakViewModel breakViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        breakViewModel = ViewModelProviders.of(this).get(BreakViewModel.class);

        recyclerBreaks = findViewById(R.id.recycler_breaks);
        recyclerBreaks.setEmptyView(findViewById(R.id.view_no_pause));

        breaksAdapter = new BreaksAdapter(this);
        recyclerBreaks.setAdapter(breaksAdapter);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerBreaks.setLayoutManager(linearLayoutManager);
        recyclerBreaks.setHasFixedSize(true);
        ((SimpleItemAnimator) recyclerBreaks.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerBreaks.setItemViewCacheSize(20);
        recyclerBreaks.setDrawingCacheEnabled(true);
        recyclerBreaks.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        breakViewModel.getBreakList().observe(this, new Observer<List<Break>>() {
            @Override
            public void onChanged(List<Break> breaks) {
                breaksAdapter.submitList(breaks);
            }
        });

        buttonBreak = findViewById(R.id.button_break);
        buttonBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                BreakDialogFragment editNameDialogFragment = BreakDialogFragment.newInstance("Chiedi una pausa");
                editNameDialogFragment.show(fm, "fragment_new_break");
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.d("token", token);

                        // Log and toast
                        /*String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("fcm", msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        */
                    }
                });
    }

    @Override
    public void join(Break br) {
        breakViewModel.joinBreak(br);
    }

    @Override
    public void remove(Break br) {
        breakViewModel.removeBreak(br);
    }

    @Override
    public void unjoin(Break br) {
        breakViewModel.unjoin(br);
    }
}
