package com.example.assignment1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment1.Models.Course;

import java.util.List;

public class CourseAdapter extends
        RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private final List<Course> courseList;
    private LayoutInflater mInflater;
    private Context context;
    private PanelActivity.UserType userType;
    private String userId;

    public CourseAdapter(Context context, List<Course> courseList, PanelActivity.UserType userType,String userId) {
        mInflater = LayoutInflater.from(context);
        this.courseList = courseList;
        this.context = context;
        this.userType = userType;
        this.userId = userId;
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final TextView professor;
        final CourseAdapter mAdapter;
        public final View view;

        public CourseViewHolder(View itemView, CourseAdapter adapter) {
            super(itemView);
            mAdapter = adapter;
            view = itemView;
            title = itemView.findViewById(R.id.course_title);
            professor = itemView.findViewById(R.id.prof_id);
        }
    }

    @NonNull
    @Override
    public CourseAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.course_item,
                parent, false);
        return new CourseViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseViewHolder holder, int position) {
        Course current = courseList.get(position);
        holder.title.setText(current.name);
        holder.professor.setText(current.ProfessorUsername);
        holder.view.setOnClickListener(view -> {
            Intent intent = new Intent(context, CourseActivity.class);
            intent.putExtra(PanelActivity.EXTRA_COURSE, current);
            intent.putExtra(PanelActivity.EXTRA_USER_TYPE, userType);
            if (userType.label.equals("STUDENT")) {
                intent.putExtra(StudentLoginActivity.USERID, userId);
            }
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }
}
