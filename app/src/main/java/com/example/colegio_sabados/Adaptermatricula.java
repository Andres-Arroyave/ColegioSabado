package com.example.colegio_sabados;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adaptermatricula extends RecyclerView.Adapter<Adaptermatricula.matriculaViewHolder> {

    ArrayList<Clsmatriculas> listarmatriculas;

    public Adaptermatricula(ArrayList<Clsmatriculas> listarmatriculas) {
        this.listarmatriculas = listarmatriculas;
    }


    @NonNull
    @Override
    public Adaptermatricula.matriculaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.matriculasresource, null, false);
        return new Adaptermatricula.matriculaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptermatricula.matriculaViewHolder holder, int position) {
        holder.tvmatricula.setText(listarmatriculas.get(position).getMatricula().toString());
        holder.tvfecha.setText(listarmatriculas.get(position).getFecha().toString());
        holder.tvcarnet.setText(listarmatriculas.get(position).getCarnet().toString());
        holder.tvmateria.setText(listarmatriculas.get(position).getMateria().toString());
    }

    @Override
    public int getItemCount() {
        return listarmatriculas.size();
    }

    public static class matriculaViewHolder extends RecyclerView.ViewHolder {
        TextView tvmatricula, tvfecha, tvcarnet, tvmateria;

        public matriculaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvmatricula = itemView.findViewById(R.id.tvmatricula);
            tvfecha = itemView.findViewById(R.id.tvfecha);
            tvcarnet = itemView.findViewById(R.id.tvcarnet);
            tvmateria = itemView.findViewById(R.id.tvmateria);
        }
    }
}

