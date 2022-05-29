package com.example.androidsimplelms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.androidsimplelms.Controller.Controller;


public class Login extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button loginButton = view.findViewById(R.id.loginforloginpage);
        TextView username = view.findViewById(R.id.usernameforloginpage);
        TextView password = view.findViewById(R.id.passwordforloginpage);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Controller.loginErrorHandler(username.getText().toString(), password.getText().toString())) {
                    if (!Controller.checkForLogin(username.getText().toString())) {
                        NavHostFragment.findNavController(Login.this).
                                navigate(R.id.action_login_to_studentMenu);
                    } else {
                        NavHostFragment.findNavController(Login.this).
                                navigate(R.id.action_login_to_professorMenu);
                    }
                } else {
                    Toast toast = Toast.makeText(getContext(), "username or password is not valid", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}