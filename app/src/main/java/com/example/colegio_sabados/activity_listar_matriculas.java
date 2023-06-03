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

import java.util.ArrayList;

public class activity_listar_matriculas extends AppCompatActivity {

    RecyclerView rvmatriculas;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    ArrayList<Clsmatriculas> almatriculas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_matriculas);

        getSupportActionBar().hide();
        rvmatriculas=findViewById(R.id.rvlistarmatriculas);
        almatriculas = new ArrayList<>();

        rvmatriculas.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        rvmatriculas.setHasFixedSize(true);

        cargar_datos();
    }
    private void cargar_datos(){
        db.collection("Matriculas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                Clsmatriculas objmatricula=new Clsmatriculas();
                                objmatricula.setMatricula(document.getString("Matricula"));
                                objmatricula.setFecha(document.getString("Fecha"));
                                objmatricula.setCarnet(document.getString("Carnet"));
                                objmatricula.setMateria(document.getString("Materia"));
                                almatriculas.add(objmatricula);
                            }
                            Adaptermatricula admatricula = new Adaptermatricula(almatriculas);
                            rvmatriculas.setAdapter(admatricula);
                        } else {
                            Toast.makeText(activity_listar_matriculas.this, "Documentos no hallados", Toast.LENGTH_SHORT).show();
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