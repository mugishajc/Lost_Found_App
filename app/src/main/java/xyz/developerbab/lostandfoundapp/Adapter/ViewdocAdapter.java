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

import de.hdodenhof.circleimageview.CircleImageView;
import xyz.developerbab.lostandfoundapp.R;
import xyz.developerbab.lostandfoundapp.model.Document;
import xyz.developerbab.lostandfoundapp.model.Document2;

public class ViewdocAdapter extends RecyclerView.Adapter<ViewdocAdapter.ViewdocAdadpterViewHolder> {

    private Context mcontext;
    private List<Document2> muploads;

    private OnItemClickListener mListener;

    public ViewdocAdapter(Context context, List<Document2> uploads) {

        this.mcontext = context;
        this.muploads = uploads;
    }

    @NonNull
    @Override
    public ViewdocAdadpterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_document, parent, false);

        return new ViewdocAdadpterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewdocAdadpterViewHolder holder, int position) {

        Document2 uploadCurrent = muploads.get(position);

        if (uploadCurrent != null) {

            holder.tvnamedoc.setText("Names: "+uploadCurrent.getDocname());
            holder.tvdatedoc.setText("Date: "+uploadCurrent.getDocdate());
            holder.btnstatusdoc.setText(uploadCurrent.getStatus());
            holder.tvdocdatefoundorlost.setText(uploadCurrent.getUserdate());
            holder.tvdocdesc.setText("Description: "+uploadCurrent.getDocdesdcription());
            holder.tvdoccategory.setText("Category: "+uploadCurrent.getDoccategory());
            holder.tvdocname.setText(uploadCurrent.getUsername());

            holder.tviddoc.setText(uploadCurrent.getId());

            Picasso.with(mcontext)
                    .load("http://lostandfound.developerbab.xyz/backend/profiles/"+uploadCurrent.getUserphoto_url())
                    .placeholder(R.drawable.ic_person_black_24dp)
                    .fit()
                    .centerCrop()
                    .into(holder.imgphotouserdoc);

        }


    }


    @Override
    public int getItemCount() {
        return muploads.size();
    }

    public class ViewdocAdadpterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CircleImageView imgphotouserdoc;

        public TextView tvdocname, tvdoccategory, tvdocdesc, tvdocdatefoundorlost, tvnamedoc, tvdatedoc,tviddoc;
        public Button btnstatusdoc;

        public ViewdocAdadpterViewHolder(@NonNull View itemView) {

            super(itemView);

            tviddoc=itemView.findViewById(R.id.tviddoc);
            imgphotouserdoc = itemView.findViewById(R.id.imgphotouserdoc);
            tvnamedoc = itemView.findViewById(R.id.tvnamedoc1);
            tvdatedoc = itemView.findViewById(R.id.tvdatedoc);
            btnstatusdoc = itemView.findViewById(R.id.btnstatusdoc);

            tvdocname = itemView.findViewById(R.id.tvdocnamenew);
            tvdoccategory = itemView.findViewById(R.id.tvdoccategory);
            tvdocdesc = itemView.findViewById(R.id.tvdocdesc);
            tvdocdatefoundorlost = itemView.findViewById(R.id.tvdocdatefoundorlost);


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
