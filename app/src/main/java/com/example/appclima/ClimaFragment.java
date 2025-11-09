package com.example.appclima;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.appclima.adapter.PrevisaoDiaAdapter;
import com.example.appclima.modelo.PrevisaoDia;
import com.example.appclima.modelo.PrevisaoResposta;
import com.example.appclima.utils.Utils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ClimaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView txtViewCidade, txtViewTemperaturaAtual, txtViewUmidade;
    private RecyclerView recyclerPrevisao;
    private ProgressDialog progressDialog;
    private PrevisaoDiaAdapter adapter;
    private OkHttpClient client = new OkHttpClient();


    public ClimaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClimaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClimaFragment newInstance(String param1, String param2) {
        ClimaFragment fragment = new ClimaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_clima, container, false);

        Button buscar = view.findViewById(R.id.btnBuscar);

        txtViewCidade = view.findViewById(R.id.fragment_clima_textview_cidade);
        txtViewTemperaturaAtual = view.findViewById(R.id.fragment_clima_textview_temp_atual);
        txtViewUmidade = view.findViewById(R.id.fragment_clima_textview_umidade);

        recyclerPrevisao = view.findViewById(R.id.recycler_clima);
        recyclerPrevisao.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new PrevisaoDiaAdapter(new ArrayList<PrevisaoDia>());
        recyclerPrevisao.setAdapter(adapter);

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CarregarPrevisao().execute();
            }
        });


        return view;
    }

    private class CarregarPrevisao extends AsyncTask<String, Void, PrevisaoResposta> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Carregando previsão do tempo...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected PrevisaoResposta doInBackground(String... params) {
            try {
                String urlString = "https://api.hgbrasil.com/weather?woeid=458191";

                Request request = new Request.Builder().url(urlString).get().build();

                Response response = client.newCall(request).execute();

                if (!response.isSuccessful()) {
                    return null;
                }

                String jsonString = response.body().string();

                PrevisaoResposta previsaoResposta = new Gson().fromJson(jsonString, PrevisaoResposta.class);

                return previsaoResposta;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(PrevisaoResposta previsaoResposta) {
            super.onPostExecute(previsaoResposta);

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss(); // Fecha o diálogo
            }

            if (previsaoResposta == null) {
                Utils.mostrarDialogErro(getActivity(), "Erro", "Não foi possível carregar a previsão do tempo");
                return;
            }

            mostraResultado(previsaoResposta);

        }

        private void mostraResultado(PrevisaoResposta previsaoResposta) {
            if (previsaoResposta.results.getCity() != null) {
                txtViewCidade.setVisibility(View.VISIBLE);
                txtViewCidade.setText("Cidade: " + previsaoResposta.results.getCity());
            }

            if (previsaoResposta.results.getTemp() != null) {
                txtViewTemperaturaAtual.setVisibility(View.VISIBLE);
                txtViewTemperaturaAtual.setText("Temperatura Atual: " + previsaoResposta.results.getTemp() + "ºC");
            }

            if (previsaoResposta.results.getTemp() != null) {
                txtViewUmidade.setVisibility(View.VISIBLE);
                txtViewUmidade.setText("Umidade Atual: " + previsaoResposta.results.getHumidity() + "%");
            }

            adapter.atualizarLista(previsaoResposta.results.getForecast());

        }
    }
}