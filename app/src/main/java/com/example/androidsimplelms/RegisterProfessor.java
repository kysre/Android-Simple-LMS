package com.example.androidsimplelms;

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

import com.gilecode.yagson.YaGson;

import java.util.ArrayList;

import com.example.androidsimplelms.Controller.Controller;
import com.example.androidsimplelms.Model.User;


public class RegisterProfessor extends Fragment {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register_professor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button registerButton = view.findViewById(R.id.button);
        EditText firstName = view.findViewById(R.id.firstname);
        EditText lastName = view.findViewById(R.id.lastname);
        EditText university = view.findViewById(R.id.universityname);
        EditText username = view.findViewById(R.id.username);
        EditText password = view.findViewById(R.id.password);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Controller.checkUsernameForRegister(username.getText().toString())) {
                    ArrayList<User> professors = Controller.addProfessor(firstName.getText().toString(), lastName.getText().toString(),
                            university.getText().toString(), username.getText().toString()
                            , password.getText().toString());
                    YaGson yaGson = new YaGson();
                    String data = yaGson.toJson(professors);
                    Toast toast=Toast.makeText(getContext(),"register was successfully",
                            Toast.LENGTH_SHORT);
                    sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                    editor = sp.edit();
                    editor.putString("users", data);
                    editor.commit();

                    toast.show();
                    
                    NavHostFragment.findNavController(RegisterProfessor.this)
                            .navigate(R.id.action_registerProfessor_to_login);
                }else{
                    Toast toast=Toast.makeText(getContext(),"this username is already exist",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}
