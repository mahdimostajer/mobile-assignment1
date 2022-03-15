package com.example.assignment1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment1.Models.Assignment;

import java.util.List;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder> {
    private final List<Assignment> assignments;
    private LayoutInflater mInflater;
    private Context context;

    public AssignmentAdapter(Context context, List<Assignment> assignments) {
        mInflater = LayoutInflater.from(context);
        this.assignments = assignments;
        this.context = context;
    }

    class AssignmentViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final TextView professor;
        final AssignmentAdapter mAdapter;

        public AssignmentViewHolder(View itemView, AssignmentAdapter adapter) {
            super(itemView);
            mAdapter = adapter;
            title = itemView.findViewById(R.id.course_title);
            professor = itemView.findViewById(R.id.prof_id);
        }
    }

    @NonNull
    @Override
    public AssignmentAdapter.AssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.course_item,
                parent, false);
        return new AssignmentViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentAdapter.AssignmentViewHolder holder, int position) {
        Assignment current = assignments.get(position);
        holder.title.setText(current.title);
        holder.professor.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), ResponseAssignmentActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }
}
