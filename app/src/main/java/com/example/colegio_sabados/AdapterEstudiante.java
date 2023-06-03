package com.example.colegio_sabados;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterEstudiante extends RecyclerView.Adapter<AdapterEstudiante.estudianteViewHolder> {

    ArrayList<ClsEstudiantes> listarestudiantes;

    public AdapterEstudiante(ArrayList<ClsEstudiantes> listarestudiantes) {
        this.listarestudiantes = listarestudiantes;
    }

    @NonNull
    @Override
    public AdapterEstudiante.estudianteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.estudiantesresource,null,false);
        return new AdapterEstudiante.estudianteViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEstudiante.estudianteViewHolder holder, int position) {
        holder.tvcarnet.setText(listarestudiantes.get(position).getCarnet().toString());
        holder.tvnombre.setText(listarestudiantes.get(position).getNombre().toString());
        holder.tvcarrera.setText(listarestudiantes.get(position).getCarrera().toString());
        holder.tvsemestre.setText(listarestudiantes.get(position).getSemestre().toString());
        if(listarestudiantes.get(position).getActivo().equals("Si"))
        holder.cbactivo.setChecked(true);
        else{
            holder.cbactivo.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {return listarestudiantes.size();
    }
    public static class estudianteViewHolder extends RecyclerView.ViewHolder {
        TextView tvcarnet, tvnombre, tvcarrera, tvsemestre;
        CheckBox cbactivo;

        public estudianteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvcarnet = itemView.findViewById(R.id.tvcarnet);
            tvnombre = itemView.findViewById(R.id.tvnombre);
            tvcarrera = itemView.findViewById(R.id.tvcarrera);
            tvsemestre = itemView.findViewById(R.id.tvsemestre);
            cbactivo = itemView.findViewById(R.id.cbactivo);
        }
    }
}
