package com.mmh19.spring;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.mmh19.spring.activities.MainActivity;
import com.mmh19.spring.models.Break;
import com.mmh19.spring.viewmodels.BreakViewModel;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

public class BreakDialogFragment extends DialogFragment {

    private Spinner spinner;
    private NumberPicker numberPicker;
    private Button buttonBreak;
    private Button buttonAnnulla;
    private BreakViewModel breakViewModel;

    public BreakDialogFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        breakViewModel = ViewModelProviders.of((MainActivity) context).get(BreakViewModel.class);
    }

    public static BreakDialogFragment newInstance(String title) {
        BreakDialogFragment frag = new BreakDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_new_break, container, false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinner = view.findViewById(R.id.spinner_tipo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.break_tipo, R.layout.item_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        numberPicker = view.findViewById(R.id.picker_orario);
        numberPicker.setDisplayedValues(getResources().getStringArray(R.array.minuti));
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(6);
        numberPicker.setEnabled(true);
        numberPicker.setWrapSelectorWheel(true);

        buttonBreak = view.findViewById(R.id.button_break);
        buttonBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                breakViewModel.addBreak(buildBreak(spinner.getSelectedItem().toString(), numberPicker.getValue()));
                dismiss();
            }
        });

        buttonAnnulla = view.findViewById(R.id.button_annulla);
        buttonAnnulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private Break buildBreak(String tipo, int orario) {
        Break br = new Break();
        br.setTipo(spinner.getSelectedItem().toString());
        //TODO set autore
        switch (spinner.getSelectedItem().toString()) {
            case "Ping Pong":
                br.setMaxPartecipanti(4);
                br.setUrlImmagine("https://i.imgur.com/r6FP7Hb.jpg");
                break;
            case "Biliardino":
                br.setMaxPartecipanti(4);
                br.setUrlImmagine("https://i.imgur.com/wl8185i.png");
                break;
            case "Cochina bella fresca":
                br.setUrlImmagine("https://i.imgur.com/djxoEAa.jpg");
                br.setMaxPartecipanti(3);
                break;
            case "Caffè":
                br.setUrlImmagine("https://i.imgur.com/w23O1Us.jpg");
                br.setMaxPartecipanti(5);
                break;
            case "Thè caldo":
                br.setUrlImmagine("https://i.imgur.com/HbdRBjy.jpg");
                br.setMaxPartecipanti(2);
                break;
            case "Boccata d'aria":
                br.setUrlImmagine("https://i.imgur.com/CmMEl9e.jpg");
                br.setMaxPartecipanti(6);
                break;
            case "Sala break":
                br.setUrlImmagine("https://i.imgur.com/9HAfgZr.jpg");
                br.setMaxPartecipanti(6);
                break;
            case "Videogame":
                br.setUrlImmagine("https://i.imgur.com/R03dlK1.jpg");
                br.setMaxPartecipanti(2);
                break;
        }
        br.setJoined(0);
        br.setOrario(orario*5 + " minuti");
        br.setIdString(UUID.randomUUID().toString());
        return br;
    }
}
