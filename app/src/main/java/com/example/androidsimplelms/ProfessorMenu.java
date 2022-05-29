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


public class ProfessorMenu extends Fragment implements RecyclerViewAdapter.SelectListener {

    private ArrayList<RecyclerViewAdapter.ListItem> listItems;
    EditText createCourseEditText;
    EditText courseIdEditText;
    Button createCourseButton;
    Button enterCourseButton;
    RecyclerView createdCoursesRecyclerView;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ProfessorMenu() {
        // Required empty public constructor
    }

    public static ProfessorMenu newInstance(String param1, String param2) {
        ProfessorMenu fragment = new ProfessorMenu();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_professor_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createCourseEditText = view.findViewById(R.id.createCourseEditText);
        courseIdEditText = view.findViewById(R.id.courseIdEditText);
        createCourseButton = view.findViewById(R.id.createCourseButton);
        enterCourseButton = view.findViewById(R.id.professorEnterCourseButton);
        createdCoursesRecyclerView = view.findViewById(R.id.professorCourseRecyclerView);
        createdCoursesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listItems = new ArrayList<>();
        listItems.addAll(Controller.getCourseListItems());
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), listItems, this);
        createdCoursesRecyclerView.setAdapter(adapter);

        createCourseButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                String courseName = createCourseEditText.getText().toString();
                boolean result = Controller.createNewCourse(courseName);
                if (result) {
                    Toast toast = Toast.makeText(getContext(),
                            "Course created successfully!", Toast.LENGTH_LONG);
                    toast.show();
                    listItems.clear();
                    listItems.addAll(Controller.getCourseListItems());
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
                            "Course already exists!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        enterCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!courseIdEditText.getText().toString().equals("")) {
                    int courseId = Integer.parseInt(courseIdEditText.getText().toString());
                    String courseName = Controller.getCourseNameById(courseId);
                    if (courseName != null) {
                        if (Controller.isCourseJoinedByOnlineUser(courseName)) {
                            NavHostFragment.findNavController(ProfessorMenu.this)
                                    .navigate(ProfessorMenuDirections
                                            .actionProfessorMenuToProfessorCourseFragment((courseName)));
                        } else {
                            Toast toast = Toast.makeText(getContext(),
                                    "You don't have this course!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    } else {
                        Toast toast = Toast.makeText(getContext(),
                                "Course with this id doesn't exist!", Toast.LENGTH_LONG);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(getContext(),
                            "Enter valid Id!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    @Override
    public void onItemClicked(RecyclerViewAdapter.ListItem listItem) {
        String courseName = listItem.getLeftString();
        NavHostFragment.findNavController(ProfessorMenu.this)
                .navigate(ProfessorMenuDirections.
                        actionProfessorMenuToProfessorCourseFragment((courseName)));
    }
}