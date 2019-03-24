package com.mmh19.spring.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mmh19.spring.R;
import com.mmh19.spring.models.Break;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class BreaksAdapter extends ListAdapter<Break, BreaksAdapter.BreakHolder> {

    private JoinBreak listener;

    public BreaksAdapter(JoinBreak listener) {
        super(new DiffUtil.ItemCallback<Break>() {
            @Override
            public boolean areItemsTheSame(@NonNull Break oldItem, @NonNull Break newItem) {
                return oldItem.getIdString().equals(newItem.getIdString());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Break oldItem, @NonNull Break newItem) {
                return oldItem.getJoined() == newItem.getJoined();
            }
        });
        this.listener = listener;
    }

    public class BreakHolder extends RecyclerView.ViewHolder {
        private TextView textStart;
        private TextView textJoined;
        private TextView textMax;
        private TextView textSlash;
        private TextView textTipo;
        private ImageView imageBackground;
        private ImageButton buttonPartecipa;

        public BreakHolder(View view) {
            super(view);
            textJoined = view.findViewById(R.id.text_joined);
            textMax = view.findViewById(R.id.text_max_partecipanti);
            textStart = view.findViewById(R.id.text_inizio);
            textSlash = view.findViewById(R.id.text_slash);
            textTipo = view.findViewById(R.id.text_tipo);
            imageBackground = view.findViewById(R.id.image_break_background);
            buttonPartecipa = view.findViewById(R.id.button_partecipa);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getItem(getAdapterPosition()).getJoined() != getItem(getAdapterPosition()).getMaxPartecipanti()) {
                        if (buttonPartecipa.getVisibility() == View.GONE) {
                            listener.join(getItem(getAdapterPosition()));
                            buttonPartecipa.setVisibility(View.VISIBLE);
                        } else {
                            listener.unjoin(getItem(getAdapterPosition()));
                            buttonPartecipa.setVisibility(View.GONE);
                        }
                    }
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.remove(getItem(getAdapterPosition()));
                    return true;
                }
            });

            buttonPartecipa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.unjoin(getItem(getAdapterPosition()));
                    buttonPartecipa.setVisibility(View.GONE);
                }
            });
        }

        public void bindData(Break nope) {
            textMax.setText(String.valueOf(nope.getMaxPartecipanti()));
            textJoined.setText(String.valueOf(nope.getJoined()));
            textStart.setText(nope.getOrario());
            textTipo.setText("#" + nope.getTipo());
            Picasso.get().load(nope.getUrlImmagine()).into(imageBackground);
        }

    }

    public interface JoinBreak {
        void join(Break br);
        void remove(Break br);
        void unjoin(Break br);
    }

    @NonNull
    @Override
    public BreakHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_break2, parent, false);
        return new BreakHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BreakHolder holder, int position) {
        holder.bindData(getItem(position));
    }
}
