package com.example.colegio_sabados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Ocultar la barra de titulo por defecto
        getSupportActionBar().hide();
    }
    public void Estudiantes(View view){
        Intent int_estudiantes= new Intent(this,EstudiantesActivity.class);
        startActivity(int_estudiantes);
    }
    public void Materias(View view){
        Intent int_materias= new Intent(this,MateriasActivity.class);
        startActivity(int_materias);
    }
    public void Matriculas(View view){
        Intent int_matriculas= new Intent(this,MatriculasActivity.class);
        startActivity(int_matriculas);
    }
}