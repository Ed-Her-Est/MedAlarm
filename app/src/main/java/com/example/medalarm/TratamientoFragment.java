package com.example.medalarm;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter; // Importar ArrayAdapter
import android.widget.AutoCompleteTextView; // Importar AutoCompleteTextView
import android.widget.ProgressBar; // Importar ProgressBar
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
public class TratamientoFragment extends Fragment {

    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> medicamentList;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View rootView = inflater.inflate(R.layout.fragment_tratamiento, container, false);

        // Inicializar las vistas
        autoCompleteTextView = rootView.findViewById(R.id.medicamentAutoCompleteTextView);
        progressBar = rootView.findViewById(R.id.progressBar);

        // Inicializar la lista
        medicamentList = new ArrayList<>();

        // Crear un adaptador simple
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, medicamentList);
        autoCompleteTextView.setAdapter(adapter);

        // Llamar a la función para obtener los medicamentos
        fetchMedicamentNames(new MedicamentCallback() {
            @Override
            public void onSuccess(ArrayList<String> medicamentNames) {
                progressBar.setVisibility(View.GONE);
                medicamentList.clear();
                medicamentList.addAll(medicamentNames);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        progressBar.setVisibility(View.VISIBLE);

        // Configurar el OnClickListener para los íconos
        rootView.findViewById(R.id.pillIcon).setOnClickListener(v -> v.setBackgroundResource(R.drawable.selector_icon_background));
        rootView.findViewById(R.id.injectionIcon).setOnClickListener(v -> v.setBackgroundResource(R.drawable.selector_icon_background));
        rootView.findViewById(R.id.capsuleIcon).setOnClickListener(v -> v.setBackgroundResource(R.drawable.selector_icon_background));
        rootView.findViewById(R.id.dropsIcon).setOnClickListener(v -> v.setBackgroundResource(R.drawable.selector_icon_background));
        rootView.findViewById(R.id.spoonIcon).setOnClickListener(v -> v.setBackgroundResource(R.drawable.selector_icon_background));
        rootView.findViewById(R.id.gelIcon).setOnClickListener(v -> v.setBackgroundResource(R.drawable.selector_icon_background));

        // Configurar el OnClickListener para el botón "Crear Recordatorio"
        rootView.findViewById(R.id.createReminderButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Usar NavController para navegar al fragmento de configurar recordatorio
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_tratamientoFragment_to_configurarRecordatorioFragment);
            }
        });

        return rootView;
    }

    private void fetchMedicamentNames(final MedicamentCallback callback) {
        String url = "https://f53e-187-153-167-255.ngrok-free.app/";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<String> medicamentNames = parseMedicamentNames(response);
                        if (medicamentNames.isEmpty()) {
                            callback.onError("No se encontraron medicamentos.");
                        } else {
                            callback.onSuccess(medicamentNames);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError("Error al cargar los medicamentos: " + error.getMessage());
                    }
                });

        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    private ArrayList<String> parseMedicamentNames(String response) {
        ArrayList<String> names = new ArrayList<>();
        try {
            JSONArray resultadosArray = new JSONArray(response);
            int limit = Math.min(resultadosArray.length(), 50);
            for (int i = 0; i < limit; i++) {
                JSONObject medicamento = resultadosArray.getJSONObject(i);
                if (medicamento.has("nombre") && !medicamento.isNull("nombre")) {
                    names.add(medicamento.getString("nombre"));
                }
            }
        } catch (JSONException e) {
            Log.e("TratamientoFragment", "Error al analizar la respuesta JSON", e);
        }
        return names;
    }

    // Interfaz de callback para manejar la respuesta de la API
    public interface MedicamentCallback {
        void onSuccess(ArrayList<String> medicamentNames);
        void onError(String errorMessage);
    }
}
