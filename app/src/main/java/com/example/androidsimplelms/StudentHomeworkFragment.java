package com.example.androidsimplelms;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.gilecode.yagson.YaGson;

import java.util.ArrayList;

import com.example.androidsimplelms.Controller.Controller;
import com.example.androidsimplelms.Model.User;

public class StudentHomeworkFragment extends Fragment {
    private String courseName;
    private String homeworkName;
    TextView courseNameTextView;
    TextView homeworkNameTextView;
    TextView questionTextView;
    TextView answerTextView;
    TextView markTextView;
    EditText answerEditText;
    Button submitButton;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_homework, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        courseNameTextView = view.findViewById(R.id.profCourseNameInHomeworkTextView);
        homeworkNameTextView = view.findViewById(R.id.profHomeworkNameTextView);
        questionTextView = view.findViewById(R.id.profHomeworkQuestionTextView);
        answerTextView = view.findViewById(R.id.homeworkAnswerTextView);
        markTextView = view.findViewById(R.id.studentHomeworkMarkTextView);
        answerEditText = view.findViewById(R.id.homeworkAnswerEditText);
        submitButton = view.findViewById(R.id.answerSubmitButton);

        courseName = StudentHomeworkFragmentArgs.fromBundle(getArguments()).getCourseName();
        homeworkName = StudentHomeworkFragmentArgs.fromBundle(getArguments()).getHomeworkName();
        courseNameTextView.setText(courseName);
        homeworkNameTextView.setText(homeworkName);
        questionTextView.setText(Controller.getHomeworkQuestion(courseName, homeworkName));
        answerTextView.setText(Controller.getPreviousAnswer(courseName, homeworkName));

        String mark = Controller.getOnlineStudentMark(courseName, homeworkName);
        if (mark != null) {
            markTextView.setText(mark);
            answerEditText.setFocusable(false);
            submitButton.setEnabled(false);
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer = answerEditText.getText().toString();
                boolean result = Controller.setAnswerForOnlineUser(courseName, homeworkName, answer);
                if (result) {
                    Toast toast = Toast.makeText(getContext(),
                            "Answer submitted successfully!", Toast.LENGTH_LONG);
                    toast.show();

                    YaGson yaGson = new YaGson();
                    ArrayList<User> users = User.getUsers();
                    String data = yaGson.toJson(users);
                    sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                    editor = sp.edit();
                    editor.putString("users", data);
                    editor.commit();

                    NavHostFragment.findNavController(StudentHomeworkFragment.this)
                            .navigate(StudentHomeworkFragmentDirections
                                    .actionStudentHomeworkFragmentToStudentCourseFragment(courseName));
                } else {
                    Toast toast = Toast.makeText(getContext(),
                            "An error occurred!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
}
