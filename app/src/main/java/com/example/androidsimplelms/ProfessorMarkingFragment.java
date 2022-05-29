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

import com.gilecode.yagson.YaGson;

import java.util.ArrayList;

import com.example.androidsimplelms.Controller.Controller;
import com.example.androidsimplelms.Model.User;

public class ProfessorMarkingFragment extends Fragment {

    private String courseName;
    private String homeworkName;
    private String studentUsername;
    TextView homeworkNameTextView;
    TextView studentNameTextView;
    TextView questionTextView;
    TextView answerTextView;
    TextView markTextView;
    EditText markEditText;
    Button markButton;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_professor_marking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeworkNameTextView = view.findViewById(R.id.profHomeworkNameInMarkingTextView);
        studentNameTextView = view.findViewById(R.id.profStudentNameTextView);
        questionTextView = view.findViewById(R.id.profHomeworkMarkingQuestionTextView);
        answerTextView = view.findViewById(R.id.profAnswerMarkingTextView);
        markTextView = view.findViewById(R.id.profStudentMarkTextView);
        markEditText = view.findViewById(R.id.newMarkEditText);
        markButton = view.findViewById(R.id.markButton);

        courseName = ProfessorMarkingFragmentArgs.fromBundle(getArguments()).getCourseName();
        homeworkName = ProfessorMarkingFragmentArgs.fromBundle(getArguments()).getHomeworkName();
        studentUsername = ProfessorMarkingFragmentArgs.fromBundle(getArguments()).getStudentName();

        homeworkNameTextView.setText(homeworkName);
        studentNameTextView.setText(studentUsername);
        questionTextView.setText(Controller.getHomeworkQuestion(courseName, homeworkName));
        answerTextView.setText(Controller.getStudentAnswer(courseName, homeworkName, studentUsername));
        markTextView.setText(Controller.getStudentMark(courseName, homeworkName, studentUsername));

        markButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newMarkStr = markEditText.getText().toString();
                if (!newMarkStr.equals("")) {
                    float newMark = Float.parseFloat(newMarkStr);
                    Controller.setMark(courseName, homeworkName, studentUsername, newMark);
                    Toast toast = Toast.makeText(getContext(),
                            "Marked successfully!", Toast.LENGTH_LONG);
                    toast.show();
                    markTextView.setText(String.valueOf(newMark));
                    markEditText.setText("");

                    YaGson yaGson = new YaGson();
                    ArrayList<User> users = User.getUsers();
                    String data = yaGson.toJson(users);
                    sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                    editor = sp.edit();
                    editor.putString("users", data);
                    editor.commit();

                } else {
                    Toast toast = Toast.makeText(getContext(),
                            "Please enter a valid mark!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
}
