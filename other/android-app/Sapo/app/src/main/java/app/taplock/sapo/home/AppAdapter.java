package app.taplock.sapo.home;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.taplock.sapo.R;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {

    String[] name = {};
    String[] address = {};
    String[] status = {};

    private OnNoteListener mOnNoteListener;

    private LayoutInflater layoutInflater;

    AppAdapter(ArrayList<String> _name, ArrayList<String> _address, ArrayList<String> _status, OnNoteListener onNoteListener) {
        name = _name.toArray(new String[0]);
        address = _address.toArray(new String[0]);
        status = _status.toArray(new String[0]);

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

        if(status[position] == "Activo")
        {
            //holder.status.setBackgroundResource(R.drawable.active_status_bd);

        }
        else if(status[position] == "Pendiente")
        {
            //holder.status.setBackgroundResource(R.drawable.pending_status_bd);
            //holder.status.setdrawabM

        }

        holder.name.setText(name[position]);
        holder.address.setText(address[position]);
        //holder.status.setText(status[position]);
    }

    @Override
    public int getItemCount() {
        return name.length;
    }

    public class AppViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgIcon;
        TextView name;
        TextView address;
        TextView status;

        OnNoteListener onNoteListener;

        public AppViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            //imgIcon = itemView.findViewById(R.id.imgIcon);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            status = itemView.findViewById(R.id.status);
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
