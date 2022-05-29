package com.example.androidsimplelms;

import android.annotation.SuppressLint;
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

public class ProfessorCourseFragment extends Fragment implements RecyclerViewAdapter.SelectListener {

    private ArrayList<RecyclerViewAdapter.ListItem> listItems;
    private String courseName;
    private RecyclerViewAdapter adapter;
    TextView courseNameTextView;
    TextView profNameTextView;
    EditText homeworkNameEditText;
    EditText homeworkQuestionEditText;
    Button createHomeworkButton;
    RecyclerView homeworkRecyclerView;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_professor_course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        courseNameTextView = view.findViewById(R.id.profCourseNameTextView);
        profNameTextView = view.findViewById(R.id.profProfNameTextView);
        homeworkNameEditText = view.findViewById(R.id.homeworkNameEditText);
        homeworkQuestionEditText = view.findViewById(R.id.homeworkQuestionEditText);
        createHomeworkButton = view.findViewById(R.id.createHomeworkButton);
        homeworkRecyclerView = view.findViewById(R.id.profCourseHomeworkRecyclerView);

        courseName = ProfessorCourseFragmentArgs.fromBundle(getArguments()).getCourseName();
        courseNameTextView.setText(courseName);
        profNameTextView.setText(Controller.getCourseProfessorName(courseName));

        homeworkRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listItems = new ArrayList<>();
        listItems.addAll(Controller.getHomeworkListItems(courseName));
        adapter = new RecyclerViewAdapter(getActivity(), listItems, this);
        homeworkRecyclerView.setAdapter(adapter);

        createHomeworkButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                String homeworkName = homeworkNameEditText.getText().toString();
                String homeworkQuestion = homeworkQuestionEditText.getText().toString();
                if (homeworkName.equals("")) {
                    Toast toast = Toast.makeText(getContext(),
                            "Homework name can't be empty!", Toast.LENGTH_LONG);
                    toast.show();
                } else if (homeworkQuestion.equals("")) {
                    Toast toast = Toast.makeText(getContext(),
                            "Homework question can't be empty!", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    boolean result = Controller
                            .createHomeworkForCourse(courseName, homeworkName, homeworkQuestion);
                    if (result) {
                        Toast toast = Toast.makeText(getContext(),
                                "Homework created successfully!", Toast.LENGTH_LONG);
                        toast.show();
                        homeworkNameEditText.setText("");
                        homeworkQuestionEditText.setText("");
                        listItems.clear();
                        listItems.addAll(Controller.getHomeworkListItems(courseName));
                        adapter.notifyDataSetChanged();

                        YaGson yaGson = new YaGson();
                        ArrayList<User> users = User.getUsers();
                        String data = yaGson.toJson(users);
                        sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                        editor = sp.edit();
                        editor.putString("users", data);
                        editor.commit();
                    } else {
                        Toast toast = Toast.makeText(getContext(),
                                "Homework already exists! Choose a different name.", Toast.LENGTH_LONG);
                        toast.show();
                        homeworkNameEditText.setText("");
                    }
                }
            }
        });
    }

    @Override
    public void onItemClicked(RecyclerViewAdapter.ListItem listItem) {
        String homeworkName = listItem.getLeftString();
        NavHostFragment.findNavController(ProfessorCourseFragment.this)
                .navigate(ProfessorCourseFragmentDirections
                        .actionProfessorCourseFragmentToProfessorHomeworkFragment
                                (courseName, homeworkName));
    }
}
