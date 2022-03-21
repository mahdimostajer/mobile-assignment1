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

import com.example.assignment1.Models.Assignment;
import com.example.assignment1.Models.ResponseAssignment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder> {
    private final List<Assignment> assignments;
    private final String userId;
    private LayoutInflater mInflater;
    private Context context;
    private PanelActivity.UserType userType;
    private SharedPreferences preferences;
    private String sharedPrefFile =
            "com.example.android.assignment1";

    public AssignmentAdapter(Context context, List<Assignment> assignments, PanelActivity.UserType userType, String userId) {
        mInflater = LayoutInflater.from(context);
        this.assignments = assignments;
        this.context = context;
        this.userType = userType;
        this.userId = userId;
        preferences = context.getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
    }

    class AssignmentViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final TextView professor;
        public final TextView grade;
        final AssignmentAdapter mAdapter;

        public AssignmentViewHolder(View itemView, AssignmentAdapter adapter) {
            super(itemView);
            mAdapter = adapter;
            title = itemView.findViewById(R.id.item_title);
            professor = itemView.findViewById(R.id.item_subtitle);
            grade = itemView.findViewById(R.id.item_grade);
        }
    }

    @NonNull
    @Override
    public AssignmentAdapter.AssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.assignment_answer_item,
                parent, false);
        return new AssignmentViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentAdapter.AssignmentViewHolder holder, int position) {
        Assignment current = assignments.get(position);
        holder.title.setText(current.title);
        holder.professor.setVisibility(View.GONE);
        if (userType.label.equals("PROFESSOR")) {
            holder.grade.setVisibility(View.GONE);
        } else {
            ResponseAssignment responseAssignment = getAssignment(current.id);
            String grade = "-";
            if (responseAssignment != null) {
                grade = responseAssignment.grade;
            }
            holder.grade.setText(grade);
        }
        holder.itemView.setOnClickListener(view -> {
            Intent intent;
            if (userType.label.equals("PROFESSOR")) {
                intent = new Intent(context.getApplicationContext(), ProfessorAssignmentActivity.class);
            } else {
                intent = new Intent(context.getApplicationContext(), StudentAssignmentActivity.class);
                intent.putExtra(StudentLoginActivity.USERID,userId);
            }
            intent.putExtra(CourseActivity.EXTRA_ASSIGNMENT, current);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }

    private ResponseAssignment getAssignment(String assignmentId) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<ResponseAssignment>>() {}.getType();
        List<ResponseAssignment> responseAssignments = gson.fromJson(preferences.getString(StudentAssignmentActivity.RESPONSE_ASSIGNMENTS, null), type);
        if(responseAssignments == null)
            responseAssignments = new ArrayList<>();
        for (ResponseAssignment responseAssignment : responseAssignments) {
            if (responseAssignment.assignmentId.equals(assignmentId) && userId.equals(responseAssignment.studentId)) {
                return responseAssignment;
            }
        }
        return null;
    }
}
