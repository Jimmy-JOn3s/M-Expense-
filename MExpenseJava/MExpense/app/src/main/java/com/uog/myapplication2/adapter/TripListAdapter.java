package com.uog.myapplication2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.uog.myapplication2.R;
import com.uog.myapplication2.database.Trip;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.ViewHolder>{
    public interface ClickListener {
        void onItemClick(int position, View v, long id);
    }
    private static ClickListener clickListener;
    private List<Trip> tripList;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView lblTripName;
        TextView lblTripDestination;
        TextView lblTripDate;
        Button btnTripEdit;
        Button btnTripRemove;
        Button btnTripExpense;
        ImageButton btnTripDetail;

        public ViewHolder(View view) {
            super(view);
            lblTripName = (TextView) view.findViewById(R.id.lblTripName);
            lblTripDestination = (TextView) view.findViewById(R.id.lblTripDestination);
            lblTripDate = (TextView) view.findViewById(R.id.lblTripDate);
            btnTripEdit = (Button) view.findViewById(R.id.btnTripEdit);
            btnTripRemove = (Button) view.findViewById(R.id.btnTripRemove);
            btnTripExpense = (Button) view.findViewById(R.id.btnTripExpense);
            btnTripDetail = (ImageButton) view.findViewById(R.id.btnTripDetail);

            btnTripEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition(), view, R.id.btnTripEdit);
                }
            });
            btnTripRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition(), view, R.id.btnTripRemove);
                }
            });
            btnTripExpense.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition(), view, R.id.btnTripExpense);
                }
            });
            btnTripDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition(), view, R.id.btnTripDetail);
                }
            });
        }
    }

    public TripListAdapter( List<Trip> tripList ){this.tripList =tripList;}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trip_data_list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Trip trip =tripList.get(position);
        viewHolder.lblTripName.setText(trip.getName());
        viewHolder.lblTripDestination.setText(trip.getDestination());
        String date =new SimpleDateFormat("dd-MM-yyyy").format(new Date(trip.getDate()));
        viewHolder.lblTripDate.setText( date );
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        TripListAdapter.clickListener = clickListener;
    }

    public void setTripList(List<Trip> tripList) {
        this.tripList = tripList;
    }
}
