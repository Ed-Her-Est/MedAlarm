package com.example.medalarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContactsFragment extends Fragment {

    private ListView listViewMedicaments;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> medicamentList;
    private ArrayList<String> medicamentDescriptions; // Nueva lista para las descripciones

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        // Inicializar el ListView y las listas
        listViewMedicaments = view.findViewById(R.id.listViewMedicaments);
        medicamentList = new ArrayList<>();
        medicamentDescriptions = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, medicamentList);
        listViewMedicaments.setAdapter(adapter);

        // Llamar al método para obtener los medicamentos
        fetchMedicamentNames(new MedicamentCallback() {
            @Override
            public void onSuccess(ArrayList<String> medicaments, ArrayList<String> descriptions) {
                medicamentList.clear();
                medicamentDescriptions.clear();
                medicamentList.addAll(medicaments);
                medicamentDescriptions.addAll(descriptions);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Manejar el clic en un item de la lista
        listViewMedicaments.setOnItemClickListener((parent, view1, position, id) -> {
            // Obtener el nombre y la descripción del medicamento seleccionado
            String selectedMedicament = medicamentList.get(position);
            String description = medicamentDescriptions.get(position);

            // Crear un bundle con los datos
            Bundle bundle = new Bundle();
            bundle.putString("medicamentName", selectedMedicament);
            bundle.putString("medicamentDescription", description);

            // Navegar al fragmento de detalles y pasar los datos
            NavController navController = NavHostFragment.findNavController(ContactsFragment.this);
            navController.navigate(R.id.action_contactsFragment_to_medicamentDetailFragment, bundle);
        });

        return view;
    }

    private void fetchMedicamentNames(final MedicamentCallback callback) {
        String url = "https://f53e-187-153-167-255.ngrok-free.app/";

        // Realizar la solicitud a la API
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Parsear los nombres y las descripciones de los medicamentos y enviarlos al callback
                        ArrayList<String> medicamentNames = parseMedicamentNames(response);
                        ArrayList<String> medicamentDescriptions = parseMedicamentDescriptions(response);
                        if (medicamentNames.isEmpty()) {
                            callback.onError("No se encontraron medicamentos.");
                        } else {
                            callback.onSuccess(medicamentNames, medicamentDescriptions);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError("Error al cargar los medicamentos: " + error.getMessage());
                    }
                });

        // Agregar la solicitud a la cola de Volley
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
            e.printStackTrace();
        }
        return names;
    }

    private ArrayList<String> parseMedicamentDescriptions(String response) {
        ArrayList<String> descriptions = new ArrayList<>();
        try {
            JSONArray resultadosArray = new JSONArray(response);
            int limit = Math.min(resultadosArray.length(), 50);
            for (int i = 0; i < limit; i++) {
                JSONObject medicamento = resultadosArray.getJSONObject(i);
                if (medicamento.has("descripcion") && !medicamento.isNull("descripcion")) {
                    descriptions.add(medicamento.getString("descripcion"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return descriptions;
    }

    // Interfaz de callback para manejar la respuesta de la API
    public interface MedicamentCallback {
        void onSuccess(ArrayList<String> medicamentNames, ArrayList<String> medicamentDescriptions);
        void onError(String errorMessage);
    }
}
