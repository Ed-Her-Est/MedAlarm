package com.example.medalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import java.util.Calendar;

public class ConfigurarAlarmaFragment extends Fragment {

    private TimePicker timePicker;
    private DatePicker datePickerInicio, datePickerFin;
    private Button btnGuardar, btnSonarAhora;
    private AlarmManager alarmManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflamos el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_configurar_alarma, container, false);

        // Asociamos el TimePicker, DatePickers y los Buttons
        timePicker = view.findViewById(R.id.timePicker);

        btnGuardar = view.findViewById(R.id.btnGuardar);
        btnSonarAhora = view.findViewById(R.id.btnSonarAhora);

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        // Configuramos el listener para el botón "Guardar"
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenemos la fecha y hora seleccionada
                int year = datePickerInicio.getYear();
                int month = datePickerInicio.getMonth();
                int day = datePickerInicio.getDayOfMonth();
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();

                // Mostramos un mensaje con la fecha y hora seleccionada
                Toast.makeText(getActivity(), "Alarma configurada para: " + day + "/" + (month + 1) + "/" + year + " a las " + hour + ":" + minute, Toast.LENGTH_SHORT).show();

                // Configuramos la alarma para la fecha y hora seleccionada
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day, hour, minute, 0); // Establecemos la fecha y hora
                setAlarm(calendar);
            }
        });

        // Configuramos el listener para el botón "Sonar Alarma Ahora"
        btnSonarAhora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sonamos la alarma inmediatamente
                Toast.makeText(getActivity(), "¡Alarma sonando ahora!", Toast.LENGTH_SHORT).show();
                soundAlarmNow();
            }
        });

        return view;
    }

    // Método para configurar la alarma
    private void setAlarm(Calendar calendar) {
        Intent intent = new Intent(getActivity(), AlarmaReceiver.class); // AlarmaReceiver es una clase que define el comportamiento de la alarma
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Configuramos la alarma en el AlarmManager
        if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            Toast.makeText(getActivity(), "La fecha y hora deben ser futuras", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para sonar la alarma ahora
    private void soundAlarmNow() {
        Intent intent = new Intent(getActivity(), AlarmaReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
    }
}
