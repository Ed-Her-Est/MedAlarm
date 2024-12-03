package com.example.medalarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private EditText editTextName;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextName = findViewById(R.id.editTextname);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener el nombre ingresado
                String name = editTextName.getText().toString();

                // Verificar si el campo está vacío
                if (name.isEmpty()) {
                    // Mostrar un mensaje de advertencia (Toast)
                    Toast.makeText(Login.this, "Por favor ingresa tu nombre", Toast.LENGTH_SHORT).show();
                } else {
                    // Si el nombre no está vacío, guardarlo en SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MedAlarmPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userName", name);
                    editor.apply();

                    // Iniciar MainActivity
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
