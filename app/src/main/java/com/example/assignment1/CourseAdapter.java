package com.example.assignment1;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment1.Models.Course;
import com.example.assignment1.Models.Professor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CourseAdapter extends
        RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private final List<Course> courseList;
    private LayoutInflater mInflater;
    private Context context;
    private PanelActivity.UserType userType;
    private String userId;
    private SharedPreferences preferences;
    private String sharedPrefFile =
            "com.example.android.assignment1";

    public CourseAdapter(Context context, List<Course> courseList, PanelActivity.UserType userType,String userId) {
        mInflater = LayoutInflater.from(context);
        this.courseList = courseList;
        this.context = context;
        this.userType = userType;
        this.userId = userId;
        preferences = context.getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
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
        holder.professor.setText("prof. " + getProfessorName(current.ProfessorUsername));
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

    private String getProfessorName(String username) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Professor>>() {}.getType();
        List<Professor> professors = gson.fromJson(preferences.getString(ProfessorRegisterActivity.PROFESSORS, null), type);
        if(professors != null){
            for (Professor professor : professors){
                if(professor.username.equals(username)){
                        return professor.lastname;
                }
            }
        }
        return null;
    }

}
