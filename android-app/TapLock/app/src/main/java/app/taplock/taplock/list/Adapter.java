package app.taplock.taplock.list;


import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.taplock.taplock.R;

public class Adapter extends RecyclerView.Adapter<Adapter.AppViewHolder> {

    String[] name = {};
    String[] address = {};

    private OnNoteListener mOnNoteListener;

    private LayoutInflater layoutInflater;

    Adapter(ArrayList<String> _name, ArrayList<String> _address, OnNoteListener onNoteListener) {
        name = _name.toArray(new String[0]);
        address = _address.toArray(new String[0]);

        this.mOnNoteListener = onNoteListener;

    }
    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());

        //
        View view = layoutInflater.inflate(R.layout.item_list,parent,false);

        return new AppViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {

        holder.name.setText(name[position]);
        holder.address.setText(address[position]);
    }

    @Override
    public int getItemCount() {
        return name.length;
    }

    public class AppViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView address;

        OnNoteListener onNoteListener;

        public AppViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            //imgIcon = itemView.findViewById(R.id.imgIcon);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
