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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gilecode.yagson.YaGson;

import java.util.ArrayList;

import com.example.androidsimplelms.Controller.Controller;
import com.example.androidsimplelms.Model.User;

public class ProfessorHomeworkFragment extends Fragment implements RecyclerViewAdapter.SelectListener {
    private ArrayList<RecyclerViewAdapter.ListItem> listItems;
    private String courseName;
    private String homeworkName;
    TextView courseNameTextView;
    TextView homeworkNameTextView;
    TextView questionTextView;
    EditText renameHomeworkEditText;
    Button renameButton;
    RecyclerView studentMarksRecyclerView;
    RecyclerViewAdapter adapter;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_professor_homework, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        courseNameTextView = view.findViewById(R.id.profCourseNameInHomeworkTextView);
        homeworkNameTextView = view.findViewById(R.id.profHomeworkNameTextView);
        questionTextView = view.findViewById(R.id.profHomeworkQuestionTextView);
        renameHomeworkEditText = view.findViewById(R.id.renameHomeworkEditText);
        renameButton = view.findViewById(R.id.renameHomeworkButton);
        studentMarksRecyclerView = view.findViewById(R.id.profStudentMarkRecyclerView);

        courseName = ProfessorHomeworkFragmentArgs.fromBundle(getArguments()).getCourseName();
        homeworkName = ProfessorHomeworkFragmentArgs.fromBundle(getArguments()).getHomeworkName();

        courseNameTextView.setText(courseName);
        homeworkNameTextView.setText(homeworkName);
        questionTextView.setText(Controller.getHomeworkQuestion(courseName, homeworkName));

        studentMarksRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listItems = new ArrayList<>();
        listItems.addAll(Controller.getStudentMarksItemList(courseName, homeworkName));
        adapter = new RecyclerViewAdapter(getActivity(), listItems, this);
        studentMarksRecyclerView.setAdapter(adapter);

        renameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = renameHomeworkEditText.getText().toString();
                boolean result = Controller.renameHomework(courseName, homeworkName, newName);
                if (result) {
                    courseNameTextView.setText(newName);
                    Toast toast = Toast.makeText(getContext(),
                            "Course renamed!", Toast.LENGTH_LONG);
                    toast.show();

                    YaGson yaGson = new YaGson();
                    ArrayList<User> users = User.getUsers();
                    String data = yaGson.toJson(users);
                    sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                    editor = sp.edit();
                    editor.putString("users", data);
                    editor.commit();

                } else {
                    Toast toast = Toast.makeText(getContext(),
                            "Course name is unavailable!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    @Override
    public void onItemClicked(RecyclerViewAdapter.ListItem listItem) {
        String studentUsername = listItem.getLeftString();
        NavHostFragment.findNavController(ProfessorHomeworkFragment.this)
                .navigate(ProfessorHomeworkFragmentDirections
                        .actionProfessorHomeworkFragmentToProfessorMarkingFragment
                                (courseName, homeworkName, studentUsername));
    }
}
