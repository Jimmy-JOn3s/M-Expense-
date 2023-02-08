package com.uog.myapplication2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.uog.myapplication2.R;
import com.uog.myapplication2.database.Expenses;
import com.uog.myapplication2.database.Trip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ViewHolder>{
    public interface ClickListener {
        void onItemClick(int position, View v, long id);
    }
    private static ClickListener clickListener;
    private List<Expenses> expenseList;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView lblExpenseType;
        TextView lblExpenseAmount;
        TextView lblExpenseTime;
        TextView lblExpenseComment;
        Button btnExpenseRemove;

        public ViewHolder(View view) {
            super(view);
            lblExpenseType = (TextView) view.findViewById(R.id.lblExpenseType);
            lblExpenseAmount = (TextView) view.findViewById(R.id.lblExpenseAmount);
            lblExpenseTime = (TextView) view.findViewById(R.id.lblExpenseTime);
            lblExpenseComment = (TextView) view.findViewById(R.id.lblExpenseComment);
            btnExpenseRemove = (Button) view.findViewById(R.id.btnExpenseRemove);

            btnExpenseRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(getAdapterPosition(), view, R.id.btnExpenseRemove);
                }
            });
        }
    }

    public ExpenseListAdapter(List<Expenses> expenseList ){this.expenseList =expenseList;}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.expense_data_list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Expenses expenses =expenseList.get(position);
        viewHolder.lblExpenseType.setText( expenses.getExpenseType() );
        viewHolder.lblExpenseAmount.setText( String.valueOf(expenses.getAmount()) );
        viewHolder.lblExpenseComment.setText(expenses.getComment());
        String date =new SimpleDateFormat("dd-MM-yyyy").format(new Date(expenses.getExpenseTime()));
        viewHolder.lblExpenseTime.setText( date );
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        ExpenseListAdapter.clickListener = clickListener;
    }

    public void setTripList(List<Expenses> expenseList) {
        this.expenseList = expenseList;
    }
}
