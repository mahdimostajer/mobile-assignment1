package com.example.assignment1;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment1.Models.ResponseAssignment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AssignmentAnswerAdapter extends RecyclerView.Adapter<AssignmentAnswerAdapter.AssignmentAnswerViewHolder> {
    private final List<ResponseAssignment> responseAssignments;
    private LayoutInflater mInflater;
    private Context context;
    public final static String EXTRA_ASSIGNMENT = "extra assignment answer";
    private TextView gradeTextView;
    private SharedPreferences preferences;
    private String sharedPrefFile =
            "com.example.android.assignment1";


    public AssignmentAnswerAdapter(Context context, List<ResponseAssignment> responseAssignments) {
        mInflater = LayoutInflater.from(context);
        this.responseAssignments = responseAssignments;
        this.context = context;
        preferences = context.getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
    }

    class AssignmentAnswerViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final TextView subtitle;
        public final TextView grade;
        final AssignmentAnswerAdapter mAdapter;

        public AssignmentAnswerViewHolder(View itemView, AssignmentAnswerAdapter adapter) {
            super(itemView);
            mAdapter = adapter;
            title = itemView.findViewById(R.id.item_title);
            subtitle = itemView.findViewById(R.id.item_subtitle);
            grade = itemView.findViewById(R.id.item_grade);
        }
    }

    @NonNull
    @Override
    public AssignmentAnswerAdapter.AssignmentAnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.assignment_answer_item,
                parent, false);
        return new AssignmentAnswerViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentAnswerAdapter.AssignmentAnswerViewHolder holder, int position) {
        ResponseAssignment current = responseAssignments.get(position);
        holder.title.setText(current.studentId);
        holder.subtitle.setText(current.answer);
        holder.grade.setText(current.grade);
        holder.itemView.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            final View dialogView = LayoutInflater.from(context).inflate(R.layout.fragment_professor_assignment_answer_dialog, null);
            TextView answer = dialogView.findViewById(R.id.prof_answer_dialog_question_textview);
            answer.setText(current.answer);
            gradeTextView = dialogView.findViewById(R.id.grade_dialog_plain_text);
            if (current.grade != null) {
                gradeTextView.setText(current.grade);
            }
//            builder.setTitle("Grade");
            builder.setView(dialogView);
            builder.setPositiveButton("submit", ((dialogInterface, i) -> handleGradeSubmission(holder,position)));
            builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
            builder.show();
        });
    }

    private void handleGradeSubmission(@NonNull AssignmentAnswerAdapter.AssignmentAnswerViewHolder holder,int position) {
        ResponseAssignment current = responseAssignments.get(position);
        Gson gson = new Gson();
        Type type = new TypeToken<List<ResponseAssignment>>() {}.getType();
        List<ResponseAssignment> responseAssignments = gson.fromJson(preferences.getString(StudentAssignmentActivity.RESPONSE_ASSIGNMENTS, null), type);
        if(responseAssignments == null)
            responseAssignments = new ArrayList<>();
        for (ResponseAssignment responseAssignment : responseAssignments) {
            if (responseAssignment.assignmentId.equals(current.assignmentId) && current.studentId.equals(responseAssignment.studentId)) {
                responseAssignment.grade = gradeTextView.getText().toString();
                holder.grade.setText(gradeTextView.getText().toString());
                break;
            }
        }
        String responsesJson = gson.toJson(responseAssignments);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(StudentAssignmentActivity.RESPONSE_ASSIGNMENTS, responsesJson);
        editor.apply();
        Toast.makeText(context, "grade submitted successfully", Toast.LENGTH_LONG).show();
    }

    @Override
    public int getItemCount() {
        return responseAssignments.size();
    }
}
