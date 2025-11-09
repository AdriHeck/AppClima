package com.example.appclima.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appclima.R;
import com.example.appclima.modelo.PrevisaoDia;

import java.util.List;

public class PrevisaoDiaAdapter extends RecyclerView.Adapter<PrevisaoDiaAdapter.ViewHolder> {

    private List<PrevisaoDia> lista;

    public PrevisaoDiaAdapter(List<PrevisaoDia> lista) {
        this.lista = lista;
    }

    // Atualiza a lista quando vier nova previsão
    public void atualizarLista(List<PrevisaoDia> novaLista) {
        this.lista = novaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.previsao_dia_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PrevisaoDia previsaoDia = lista.get(position);

        holder.txtDiaSemana.setText(previsaoDia.getWeekday());
        holder.txtData.setText(previsaoDia.getDate());
        holder.txtDescricao.setText(previsaoDia.getDescription());
        holder.txtMax.setText("Máx: " + previsaoDia.getMax() + "°C");
        holder.txtMin.setText("Mín: " + previsaoDia.getMin() + "°C");
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDiaSemana, txtData, txtDescricao, txtMax, txtMin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDiaSemana = itemView.findViewById(R.id.txtDiaSemana);
            txtData = itemView.findViewById(R.id.txtData);
            txtDescricao = itemView.findViewById(R.id.txtDescricao);
            txtMax = itemView.findViewById(R.id.txtMax);
            txtMin = itemView.findViewById(R.id.txtMin);
        }
    }
}
