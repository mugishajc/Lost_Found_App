package xyz.developerbab.lostandfoundapp.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import xyz.developerbab.lostandfoundapp.R;
import xyz.developerbab.lostandfoundapp.model.Document;
import xyz.developerbab.lostandfoundapp.model.Document2;
import xyz.developerbab.lostandfoundapp.model.Mydocument;

public class Viewdoc2Adapter extends RecyclerView.Adapter<Viewdoc2Adapter.Viewdoc2AdadpterViewHolder> {

    private Context mcontext;
    private List<Mydocument> muploads;

    private OnItemClickListener mListener;

        public Viewdoc2Adapter(Context context, List<Mydocument> uploads) {

            this.mcontext = context;
            this.muploads = uploads;
        }

    @NonNull
    @Override
    public Viewdoc2AdadpterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_mydoc, parent, false);

        return new Viewdoc2AdadpterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewdoc2AdadpterViewHolder holder, int position) {

        Mydocument uploadCurrent = muploads.get(position);

        if (uploadCurrent != null) {

            holder.nametv.setText(uploadCurrent.getNames());
            holder.categorytv.setText(uploadCurrent.getCategorynames());
            holder.desriptiontv.setText("Description: "+"\n"+uploadCurrent.getDescription());
            holder.statustv.setText("Status:  "+uploadCurrent.getStatus());
            holder.tvmyidmydoc.setText(uploadCurrent.getId());
            holder.referencetv.setText("Ref: "+uploadCurrent.getReference());
        }


    }


    @Override
    public int getItemCount() {
        return muploads.size();
    }

    public class Viewdoc2AdadpterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView nametv, categorytv, desriptiontv, statustv, tvmyidmydoc, referencetv;

        public Viewdoc2AdadpterViewHolder(@NonNull View itemView) {

            super(itemView);
            referencetv = itemView.findViewById(R.id.referencetv);
            tvmyidmydoc = itemView.findViewById(R.id.tvmyidmydoc);
            nametv = itemView.findViewById(R.id.nametv);
            categorytv = itemView.findViewById(R.id.categorytv);
            desriptiontv = itemView.findViewById(R.id.descriptiontv);
            statustv = itemView.findViewById(R.id.statustv);

                itemView.setOnClickListener(this);

            }

            @Override
            public void onClick(View v) {
                if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;

    }


}
