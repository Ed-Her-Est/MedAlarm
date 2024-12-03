package com.example.medalarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class WelcomeFragment extends Fragment {

    private ImageView imageViewProfile;
    private TextView tvWelcomeMessage;
    private TextView tvRegisteredName;
    private TextView tvNoTreatment;
    private Button btnAddTreatment;

    public WelcomeFragment() {
        // Constructor vacío necesario
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        // Referencias a los elementos del diseño
        imageViewProfile = view.findViewById(R.id.imageView4);
        tvWelcomeMessage = view.findViewById(R.id.tv_welcome_message);
        tvRegisteredName = view.findViewById(R.id.tv_registered_name); // Referencia al TextView del nombre
        tvNoTreatment = view.findViewById(R.id.tv_no_treatment);
        btnAddTreatment = view.findViewById(R.id.btn_add_treatment);

        // Recuperar el nombre del usuario desde SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MedAlarmPrefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "Usuario");

        // Configurar el texto en el TextView de bienvenida y nombre registrado
        tvWelcomeMessage.setText("Cuida tu Salud ");
        tvRegisteredName.setText(userName);

        // Configurar el botón para añadir tratamiento
        btnAddTreatment.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_welcomeFragment_to_tratamientoFragment)
        );

        return view;
    }
}
