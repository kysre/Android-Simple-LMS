package com.example.androidsimplelms;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
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

public class StudentMenu extends Fragment implements RecyclerViewAdapter.SelectListener {
    private ArrayList<RecyclerViewAdapter.ListItem> listItems;
    Button enterButton;
    RecyclerView courseRecyclerView;
    RecyclerViewAdapter adapter;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_student_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText classIdTextView = view.findViewById(R.id.studentCourseEditText);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch joinSwitch =
                view.findViewById(R.id.studentJoinSwitch);
        enterButton = view.findViewById(R.id.studentEnterCourseButton);
        courseRecyclerView = view.findViewById(R.id.studentCourseRecyclerView);
        courseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listItems = new ArrayList<>();
        listItems.addAll(Controller.getCourseListItems());
        adapter = new edu.sharif.hw_1.RecyclerViewAdapter(getActivity(), listItems, this);
        courseRecyclerView.setAdapter(adapter);

        joinSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    enterButton.setText("Join");
                    listItems.clear();
                    listItems.addAll(Controller.getNotJoinedCourseListItems());
                } else {
                    enterButton.setText("Enter");
                    listItems.clear();
                    listItems.addAll(Controller.getCourseListItems());
                }
                adapter.notifyDataSetChanged();
            }
        });

        enterButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                boolean isEnter = enterButton.getText().toString().equals("Enter");
                String courseIdStr = classIdTextView.getText().toString();
                if (!courseIdStr.equals("")) {
                    int courseId = Integer.parseInt(courseIdStr);
                    if (isEnter) {
                        String courseName = Controller.getCourseNameById(courseId);
                        if (courseName != null) {
                            boolean isCourseJoined = Controller.isCourseJoinedByOnlineUser(courseName);
                            if (isCourseJoined) {
                                NavHostFragment.findNavController(StudentMenu.this)
                                        .navigate(StudentMenuDirections
                                                .actionStudentMenuToCourseFragment(courseName));
                            } else {
                                Toast toast = Toast.makeText(getContext(),
                                        "You haven't joined this course!", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        } else {
                            Toast toast = Toast.makeText(getContext(),
                                    "Course Id was wrong!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    } else {
                        if (Controller.joinCourse(courseId)) {
                            Toast toast = Toast.makeText(getContext(),
                                    "Course joined Successfully!", Toast.LENGTH_LONG);
                            toast.show();
                            listItems.clear();
                            listItems.addAll(Controller.getNotJoinedCourseListItems());
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast toast = Toast.makeText(getContext(),
                                    "Unable to join course!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                } else {
                    Toast toast = Toast.makeText(getContext(),
                            "Please enter a valid Id!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemClicked(RecyclerViewAdapter.ListItem listItem) {
        String courseName = listItem.getLeftString();
        boolean isEnter = enterButton.getText().toString().equals("Enter");
        if (isEnter) {
            NavHostFragment.findNavController(StudentMenu.this)
                    .navigate(StudentMenuDirections.actionStudentMenuToCourseFragment(courseName));
        } else {
            if (Controller.joinCourse(courseName)) {
                Toast toast = Toast.makeText(getContext(),
                        "Course joined Successfully!", Toast.LENGTH_LONG);
                toast.show();
                listItems.clear();
                listItems.addAll(Controller.getNotJoinedCourseListItems());
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
                        "Unable to join course!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
}