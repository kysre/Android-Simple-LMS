package com.example.androidsimplelms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidsimplelms.Controller.Controller;

import java.util.ArrayList;

public class StudentCourseFragment extends Fragment implements RecyclerViewAdapter.SelectListener {
    private String courseName;
    private ArrayList<RecyclerViewAdapter.ListItem> homeworkItemList;
    TextView courseTextView;
    TextView profTextView;
    EditText homeworkNameEditText;
    Button enterHomeworkButton;
    RecyclerView homeworkRecyclerView;
    RecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        courseTextView = view.findViewById(R.id.courseNameTextView);
        profTextView = view.findViewById(R.id.profNameTextView);
        homeworkNameEditText = view.findViewById(R.id.homeworkNameEditText);
        enterHomeworkButton = view.findViewById(R.id.enterHomeworkButton);
        homeworkRecyclerView = view.findViewById(R.id.courseHomeworkRecyclerView);

        courseName = StudentCourseFragmentArgs.fromBundle(getArguments()).getCourseName();
        courseTextView.setText(courseName);
        profTextView.setText(Controller.getCourseProfessorName(courseName));

        homeworkRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        homeworkItemList = new ArrayList<>();
        homeworkItemList.addAll(Controller.getHomeworkListItems(courseName));
        adapter = new RecyclerViewAdapter(getActivity(), homeworkItemList, this);
        homeworkRecyclerView.setAdapter(adapter);

        enterHomeworkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String homeworkName = homeworkNameEditText.getText().toString();
                if (Controller.getHomeworkQuestion(courseName, homeworkName) != null) {
                    NavHostFragment.findNavController(StudentCourseFragment.this)
                            .navigate(StudentCourseFragmentDirections
                                    .actionStudentCourseToHomeworkFragment(courseName, homeworkName));
                } else {
                    Toast toast = Toast.makeText(getContext(),
                            "Homework name is invalid!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    @Override
    public void onItemClicked(RecyclerViewAdapter.ListItem listItem) {
        String homeworkName = listItem.getLeftString();
        NavHostFragment.findNavController(StudentCourseFragment.this)
                .navigate(StudentCourseFragmentDirections
                        .actionStudentCourseToHomeworkFragment(courseName, homeworkName));
    }
}
