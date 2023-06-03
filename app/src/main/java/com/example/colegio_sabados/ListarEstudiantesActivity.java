package com.example.colegio_sabados;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;

public class ListarEstudiantesActivity extends AppCompatActivity {

    RecyclerView rvestudiantes;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    ArrayList<ClsEstudiantes>alestudiantes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_estudiantes);
        //ocultar la barra de titulo, asociar objeto xml con java
        getSupportActionBar().hide();
        rvestudiantes=findViewById(R.id.rvlistarestudiantes);
        alestudiantes = new ArrayList<>();

        rvestudiantes.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        rvestudiantes.setHasFixedSize(true);

        cargar_datos();

    }//fin on create

    private void cargar_datos(){
        db.collection("Estudiantes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                ClsEstudiantes objestudiante=new ClsEstudiantes();
                                objestudiante.setCarnet(document.getString("Carnet"));
                                objestudiante.setNombre(document.getString("Nombre"));
                                objestudiante.setCarrera(document.getString("Carrera"));
                                objestudiante.setSemestre(document.getString("Semestre"));
                                objestudiante.setActivo(document.getString("Activo"));
                                alestudiantes.add(objestudiante);
                            }
                            AdapterEstudiante adestudiante = new AdapterEstudiante(alestudiantes);
                            rvestudiantes.setAdapter(adestudiante);
                        } else {
                            Toast.makeText(ListarEstudiantesActivity.this, "Documentos no hallados", Toast.LENGTH_SHORT).show();
                            //Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void Regresar(View view) {
        Intent intmain = new Intent(this, MainActivity.class);
        startActivity(intmain);
    }
}