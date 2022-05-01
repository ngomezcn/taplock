package app.taplock.sapo.users_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.taplock.sapo.R;

public class AppAdapter extends RecyclerView.Adapter<app.taplock.sapo.users_list.AppAdapter.AppViewHolder> {

    String[] name = {};
    String[] phone = {};
    String[] status = {};
    String[] seed_id = {};

    private OnNoteListener mOnNoteListener;

    private LayoutInflater layoutInflater;

    AppAdapter(ArrayList<String> _name, ArrayList<String> _phone, ArrayList<String> _status, ArrayList<String> _seed_id, OnNoteListener onNoteListener) {
        name = _name.toArray(new String[0]);
        phone = _phone.toArray(new String[0]);
        status = _status.toArray(new String[0]);
        seed_id = _seed_id.toArray(new String[0]);
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public app.taplock.sapo.users_list.AppAdapter.AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.user_list,parent,false);

        return new AppViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull app.taplock.sapo.users_list.AppAdapter.AppViewHolder holder, int position) {

        if(status[position] == "Activo")
        {
            //holder.status.setBackgroundResource(R.drawable.active_status_bd);
        }
        else if(status[position] == "Pendiente")
        {
            //holder.status.setBackgroundResource(R.drawable.pending_status_bd);
        }

        holder.name.setText(name[position] + " ");
        holder.phone.setText(phone[position]);
        holder.status.setText( " " + status[position]);
    }

    @Override
    public int getItemCount() {
        return name.length;
    }

    public class AppViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgIcon;
        TextView name;
        TextView phone;
        TextView status;

        app.taplock.sapo.users_list.AppAdapter.OnNoteListener onNoteListener;

        public AppViewHolder(@NonNull View itemView, app.taplock.sapo.users_list.AppAdapter.OnNoteListener onNoteListener) {
            super(itemView);
            //imgIcon = itemView.findViewById(R.id.imgIcon);
            name = itemView.findViewById(R.id.name_user);
            phone = itemView.findViewById(R.id.phone_user);
            status = itemView.findViewById(R.id.status_user);
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
