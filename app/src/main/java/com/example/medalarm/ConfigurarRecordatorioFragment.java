package com.example.medalarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class ConfigurarRecordatorioFragment extends Fragment {

    private RadioGroup rgFrecuencia;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflamos el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_configurar_recordatorio, container, false);

        // Asociamos el RadioGroup y el Button con los elementos del layout
        rgFrecuencia = view.findViewById(R.id.rgFrecuencia);
        Button btnContinuar = view.findViewById(R.id.btnContinuar);

        // Configuramos el listener para el botón "Seguir"
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenemos la opción seleccionada en el RadioGroup
                int selectedId = rgFrecuencia.getCheckedRadioButtonId();

                // Verificamos si se ha seleccionado alguna opción
                if (selectedId == -1) {
                    Toast.makeText(getContext(), "Por favor, selecciona una opción", Toast.LENGTH_SHORT).show();
                } else {
                    // Procesamos la opción seleccionada
                    String frecuencia = "";

                    // Usamos if-else en lugar de switch
                    if (selectedId == R.id.rbDiariamente) {
                        frecuencia = "Diariamente";
                    } else if (selectedId == R.id.rbDeterminadosDias) {
                        frecuencia = "En determinados días de la semana";
                    } else if (selectedId == R.id.rbCadaXDias) {
                        frecuencia = "Cada X días";
                    } else if (selectedId == R.id.rbCadaXHoras) {
                        frecuencia = "Cada X horas";
                    } else if (selectedId == R.id.rbUnaVez) {
                        frecuencia = "Una vez";
                    }

                    // Mostramos un mensaje con la opción seleccionada
                    Toast.makeText(getContext(), "Opción seleccionada: " + frecuencia, Toast.LENGTH_SHORT).show();

                    // Usamos NavController para navegar al siguiente fragmento
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_configurarRecordatorioFragment_to_configurarAlarmaFragment);
                }
            }
        });

        return view;
    }
}
