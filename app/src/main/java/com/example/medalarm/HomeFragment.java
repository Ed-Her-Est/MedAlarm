package com.example.medalarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

public class HomeFragment extends Fragment {

    private TextView tvUserName;
    private EditText etBirthDate;
    private Button btnSave; // Agregado el botón de guardar

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Referencias a los elementos de la vista
        tvUserName = view.findViewById(R.id.tv_user_name);
        etBirthDate = view.findViewById(R.id.et_birth_date);
        btnSave = view.findViewById(R.id.btn_save);  // Referencia al botón de guardar

        // Recuperar el nombre del usuario desde SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MedAlarmPrefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "Usuario");

        // Configurar el texto en el TextView
        tvUserName.setText("Nombre: " + userName);

        // Agregar un TextWatcher para separar la fecha mientras el usuario escribe
        etBirthDate.addTextChangedListener(new TextWatcher() {

            boolean isUpdating = false; // Variable para evitar el bucle

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // No es necesario hacer nada antes de que el texto cambie
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                if (isUpdating) {
                    return; // Evitar actualizaciones infinitas
                }

                try {
                    // Eliminar cualquier carácter no numérico
                    String input = charSequence.toString().replaceAll("[^\\d]", "");

                    // Deshabilitar temporalmente el TextWatcher para evitar un bucle
                    isUpdating = true;

                    // Formato automático mientras el usuario escribe
                    if (input.length() <= 2) {
                        // Solo el día
                        etBirthDate.setText(input);
                    } else if (input.length() <= 4) {
                        // Día y mes
                        etBirthDate.setText(input.substring(0, 2) + "/" + input.substring(2));
                    } else {
                        // Día, mes y año
                        etBirthDate.setText(input.substring(0, 2) + "/" + input.substring(2, 4) + "/" + input.substring(4, 8));
                    }

                    // Asegurar que el cursor se posicione al final
                    etBirthDate.setSelection(etBirthDate.getText().length());

                } catch (Exception e) {
                    // Manejar la excepción y registrarla
                    Log.e("HomeFragment", "Error al actualizar el texto", e);
                } finally {
                    // Habilitar el TextWatcher nuevamente
                    isUpdating = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No es necesario hacer nada después de que el texto cambie
            }
        });

        // Lógica del botón de guardar
        btnSave.setOnClickListener(v -> {
            // Validar que el campo de fecha no esté vacío
            String birthDate = etBirthDate.getText().toString().trim();

            // Si el campo de fecha está vacío, mostrar un mensaje y no navegar
            if (birthDate.isEmpty()) {
                Toast.makeText(getActivity(), "Por favor, ingresa tus Datos", Toast.LENGTH_SHORT).show();
            } else {
                // Acciones adicionales antes de navegar, si es necesario

                // Navegar a WelcomeFragment
                NavController navController = NavHostFragment.findNavController(this);
                navController.navigate(R.id.action_homeFragment_to_welcomeFragment); // Asegúrate de tener esta acción en tu gráfico de navegación
            }
        });
    }
}
