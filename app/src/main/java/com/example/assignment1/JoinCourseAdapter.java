package com.example.assignment1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment1.Models.Course;

import java.util.List;

public class JoinCourseAdapter extends RecyclerView.Adapter<JoinCourseAdapter.JoinCourseViewHolder>{
    private final List<Course> courseList;
    private LayoutInflater mInflater;
    private Context context;
    View.OnClickListener clickListener;

    public void setOnClickListener(View.OnClickListener listener){
        clickListener = listener;
    }

    public JoinCourseAdapter(Context context, List<Course> courseList) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.courseList = courseList;
    }
    class JoinCourseViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final TextView professor;
        public final Button joinButton;
        final JoinCourseAdapter mAdapter;

        public JoinCourseViewHolder(View itemView, JoinCourseAdapter adapter) {
            super(itemView);
            mAdapter = adapter;
            title = itemView.findViewById(R.id.course_title);
            professor = itemView.findViewById(R.id.prof_id);
            joinButton = itemView.findViewById(R.id.join_button);
        }
    }
    @NonNull
    @Override
    public JoinCourseAdapter.JoinCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.course_item,
                parent, false);
        return new JoinCourseViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull JoinCourseAdapter.JoinCourseViewHolder holder, int position) {
        Course current = courseList.get(position);
        holder.title.setText(current.name);
        holder.professor.setText(current.ProfessorUsername);
        holder.joinButton.setVisibility(View.VISIBLE);
        holder.joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(JoinCourseActivity.JOINED_COURSE,current);
                if (context instanceof Activity)
                    ((Activity) context).setResult(Activity.RESULT_OK, intent);
                ((Activity) context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }
}
