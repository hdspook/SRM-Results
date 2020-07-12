package com.integrationlabs.results;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.UserViewHolder> {
    private Context mContext;
    private List<User> mUploads;

    public ResultAdapter(Context context, List<User> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.result_template, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User uploadCurrent = mUploads.get(position);

        holder.course.setText(uploadCurrent.getCourseName());
        String temp = "Semester-"+uploadCurrent.getSemester()+" | "+"Credits-"+uploadCurrent.getCredits();
        holder.semCred.setText(temp);
        holder.marks.setText(uploadCurrent.getMarks());
        holder.result.setText(uploadCurrent.getResult());
        holder.grade.setText(uploadCurrent.getGrade());
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView course,semCred,marks,grade,result;

        public UserViewHolder(View itemView) {
            super(itemView);

            course = itemView.findViewById(R.id.course);
            semCred = itemView.findViewById(R.id.semCred);
            marks = itemView.findViewById(R.id.marks);
            grade = itemView.findViewById(R.id.grade);
            result = itemView.findViewById(R.id.result);

        }
    }
}