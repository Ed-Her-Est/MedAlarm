package com.example.medalarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class MedicamentDetailFragment extends Fragment {

    private TextView textViewMedicamentName;
    private TextView textViewMedicamentDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicament_detail, container, false);

        textViewMedicamentName = view.findViewById(R.id.textViewMedicamentName);
        textViewMedicamentDescription = view.findViewById(R.id.textViewMedicamentDescription);

        // Obtener los datos pasados desde el fragmento anterior
        Bundle bundle = getArguments();
        if (bundle != null) {
            String medicamentName = bundle.getString("medicamentName");
            String medicamentDescription = bundle.getString("medicamentDescription");

            // Mostrar los datos en los TextViews
            textViewMedicamentName.setText(medicamentName);
            textViewMedicamentDescription.setText(medicamentDescription);
        }

        return view;
    }
}
