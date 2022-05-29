package com.example.androidsimplelms;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.example.androidsimplelms.Controller.Controller;
import com.example.androidsimplelms.Model.User;
import com.example.androidsimplelms.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button loginButton = view.findViewById(R.id.loginButton);
        Switch isProfessor = view.findViewById(R.id.isProfessor);
        sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sp.edit();
        editor.commit();
        YaGson yaGson = new YaGson();
        String data = sp.getString("users", "");
        Type type = new TypeToken<ArrayList<User>>() {
        }.getType();
        ArrayList<User> users = yaGson.fromJson(data, type);
        Controller.initializer(users);

        binding.RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isProfessor.isChecked()) {
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_registerProfessor);
                } else {
                    NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.
                            action_FirstFragment_to_registerStudent);
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this).
                        navigate(R.id.action_FirstFragment_to_login);

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}