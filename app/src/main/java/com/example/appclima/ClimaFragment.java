package com.example.appclima;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appclima.adapter.PrevisaoDiaAdapter;
import com.example.appclima.modelo.PrevisaoDia;
import com.example.appclima.modelo.PrevisaoResposta;
import com.example.appclima.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ClimaFragment extends Fragment {

    private TextView txtViewCidade, txtViewTemperaturaAtual, txtViewUmidade;
    private EditText edtTextCodigoCidade;
    private FloatingActionButton fabBuscaCidade;
    private RecyclerView recyclerPrevisao;
    private ProgressDialog progressDialog;
    private PrevisaoDiaAdapter adapter;
    private OkHttpClient client = new OkHttpClient();


    public ClimaFragment() {
        // Required empty public constructor
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
        edtTextCodigoCidade = view.findViewById(R.id.editText_codigo_cidade);
        fabBuscaCidade = view.findViewById(R.id.fab_buscar_previsao);

        recyclerPrevisao = view.findViewById(R.id.recycler_clima);
        recyclerPrevisao.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new PrevisaoDiaAdapter(new ArrayList<PrevisaoDia>());
        recyclerPrevisao.setAdapter(adapter);

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtTextCodigoCidade.getText().toString().isEmpty())
                    new CarregarPrevisao().execute(edtTextCodigoCidade.getText().toString());
            }
        });

        fabBuscaCidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirLeitorQrCode();
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
                String codigoCidade = params[0];

                String urlString = "https://api.hgbrasil.com/weather?woeid=" + codigoCidade;

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

    private void abrirLeitorQrCode() {

        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Aponte a câmera para o QRCode");
        integrator.setCameraId(0);
        integrator.setBarcodeImageEnabled(false);
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String cidadeLida = result.getContents();
                new CarregarPrevisao().execute(cidadeLida);
            } else {
                Toast.makeText(getActivity(), "Leitura cancelada", Toast.LENGTH_SHORT).show();
            }
        }
    }
}