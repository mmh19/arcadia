package com.mmh19.spring.viewmodels;

import android.util.Log;
import android.widget.Switch;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mmh19.spring.models.Break;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BreakViewModel extends ViewModel {

    private MutableLiveData<List<Break>> breakList;

    public BreakViewModel() {
        breakList = new MutableLiveData<>();
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        observeBreaks();
    }

    public LiveData<List<Break>> getBreakList() {
        return breakList;
    }

    private void observeBreaks() {

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Break> brList = new ArrayList<>();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child("breaks").getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                while (iterator.hasNext()) {
                    DataSnapshot next = iterator.next();
                    Break br = new Break();
                    br.setIdString(next.getKey());
                    br.setAutore(String.valueOf(next.child("autore").getValue()));
                    br.setOrario(String.valueOf(next.child("orario").getValue()));
                    br.setJoined(Integer.parseInt(next.child("joined").getValue()+""));
                    br.setMaxPartecipanti(Integer.parseInt(next.child("maxPartecipanti").getValue()+""));
                    br.setUrlImmagine(String.valueOf(next.child("urlImmagine").getValue()));
                    br.setTipo(String.valueOf(next.child("tipo").getValue()));
                    brList.add(br);
                }
                breakList.setValue(brList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("realtime", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        FirebaseDatabase.getInstance().getReference().addValueEventListener(postListener);
    }

    public void addBreak(Break br) {
        FirebaseDatabase.getInstance().getReference().child("breaks").child(br.getIdString())
                .setValue(br);
    }

    public void removeBreak(Break br) {
        FirebaseDatabase.getInstance().getReference().child("breaks").child(br.getIdString())
                .setValue(null);
    }

    public void joinBreak(Break br) {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> brMap = br.toMap();
        //TODO crea ovviamente problemi ma mancano le API dal backend
        brMap.put("joined", (Integer) brMap.get("joined") + 1);
        childUpdates.put("/breaks/" + br.getIdString(), brMap);
        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);
    }

    public void unjoin(Break br) {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> brMap = br.toMap();
        //TODO come sopra
        brMap.put("joined", (Integer) brMap.get("joined") - 1);
        childUpdates.put("/breaks/" + br.getIdString(), brMap);
        FirebaseDatabase.getInstance().getReference().updateChildren(childUpdates);
    }

}
