package com.example.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.journal;
import com.example.studentplanner.Constant;
import com.example.studentplanner.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalHolder> {

    private Context context;
    private ArrayList<journal> list;

    public JournalAdapter(Context context, ArrayList<journal> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public JournalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_journal,parent,false);
        return new JournalHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalHolder holder, int position) {
        journal journal = list.get(position);
        Picasso.get().load(Constant.URL+"storage/profiles"+journal.getUser().getPhoto()).into(holder.imgProfile);
        Picasso.get().load(Constant.URL+"storage/journal"+journal.getPhoto()).into(holder.imgJournal);
        holder.txtName.setText(journal.getUser().getUserName());
        holder.txtComments.setText("View all"+journal.getComments());
        holder.txtLikes.setText(journal.getLikes()+"Likes");
        holder.txtDate.setText(journal.getDate());
        holder.txtDesc.setText(journal.getAbout());
        holder.txtTag.setText(journal.getTag());
        holder.txtFeelings.setText(journal.getFeelings());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class JournalHolder extends RecyclerView.ViewHolder{

        private TextView txtName,txtDate,txtDesc,txtTag,txtFeelings,txtLikes,txtComments;
        private CircleImageView imgProfile;
        private ImageView imgJournal;
        private ImageButton btnJournalOption,btnLike,btnComment;

        public JournalHolder(@NonNull View itemView) {
            super(itemView);

            txtName=itemView.findViewById(R.id.txtJournalname);
            txtDate=itemView.findViewById(R.id.txtJournalDate);
            txtDesc=itemView.findViewById(R.id.txtjournalAbout);
            txtTag=itemView.findViewById(R.id.txtjournalTag);
            txtFeelings=itemView.findViewById(R.id.txtjournalFeeling);
            txtLikes=itemView.findViewById(R.id.txtJournalLikes);
            txtComments=itemView.findViewById(R.id.txtJournalComments);
            imgProfile=itemView.findViewById(R.id.imgjournalProfile);
            imgJournal=itemView.findViewById(R.id.imgJournalPhoto);
            btnJournalOption=itemView.findViewById(R.id.btnJournalOption);
            btnLike=itemView.findViewById(R.id.btnJournalLike);
            btnComment=itemView.findViewById(R.id.btnJournalComment);



        }
    }
}
