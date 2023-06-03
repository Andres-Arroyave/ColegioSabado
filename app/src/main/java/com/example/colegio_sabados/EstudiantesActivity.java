package com.example.colegio_sabados;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;

import java.util.HashMap;
import java.util.Map;

public class EstudiantesActivity extends AppCompatActivity {

    EditText jetcarnet,jetnombre,jetcarrera,jetsemestre;
    CheckBox jcbactivo;
    String carnet,nombre,carrera,semestre,id_documento;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiantes);
        getSupportActionBar().hide();

        jetcarnet=findViewById(R.id.etcarnet);
        jetnombre=findViewById(R.id.etnombre);
        jetcarrera=findViewById(R.id.etcarrera);
        jetsemestre=findViewById(R.id.etsemestre);
        jcbactivo=findViewById(R.id.cbactivo);
        id_documento="";
    }



    public void Adicionar(View view) {
        carnet = jetcarnet.getText().toString();
        nombre = jetnombre.getText().toString();
        carrera = jetcarrera.getText().toString();
        semestre = jetsemestre.getText().toString();
        if (carnet.isEmpty() || nombre.isEmpty() || carrera.isEmpty() || semestre.isEmpty()) {
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetcarnet.requestFocus();
        } else {
            // Create a new student with a first and last name
            Map<String, Object> estudiante = new HashMap<>();
            estudiante.put("Carnet", carnet);
            estudiante.put("Nombre", nombre);
            estudiante.put("Carrera", carrera);
            estudiante.put("Semestre", semestre);
            estudiante.put("Activo", "Si");
            //Consultar para verificar que no exista
            id_documento = "";
            Consultar_doc();
            if (id_documento.equals("")) {
// Add a new document with a generated ID
                db.collection("Estudiantes")
                        .add(estudiante)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                Limpiar_campos();
                                Toast.makeText(EstudiantesActivity.this, "Documento guardado", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Log.w(TAG, "Error adding document", e);
                                Toast.makeText(EstudiantesActivity.this, "Error guardando documento", Toast.LENGTH_SHORT).show();
                            }
                        });
            }else{
                Toast.makeText(this, "Carnet ya registrado", Toast.LENGTH_SHORT).show();
            }
        }
    }//Fin adicionar

    public void consultar(View view){
        Consultar_doc();
    }

    private void Consultar_doc(){
        carnet=jetcarnet.getText().toString();
        if(carnet.isEmpty()){
            Toast.makeText(this, "Numero de carnet es requerido", Toast.LENGTH_SHORT).show();
            jetcarnet.requestFocus();
        }else{
            db.collection("Estudiantes")
                    .whereEqualTo("Carnet",carnet)
                   .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    id_documento=document.getId();

                                    jetnombre.setText(document.getString("Nombre"));
                                    jetcarrera.setText(document.getString("Carrera"));
                                    jetsemestre.setText(document.getString("Semestre"));
                                    if(document.getString("Activo").equals("Si")){
                                        jcbactivo.setChecked(true);
                                    }else{
                                        jcbactivo.setChecked(false);
                                    }
                                }
                            } else {
                                Toast.makeText(EstudiantesActivity.this, "Documento no hallado", Toast.LENGTH_SHORT).show();
                               // Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
    }//Fin Consultar

    public void Modificar(View view) {
        if (!id_documento.equals((""))) {
            carnet = jetcarnet.getText().toString();
            nombre = jetnombre.getText().toString();
            carrera = jetcarrera.getText().toString();
            semestre = jetsemestre.getText().toString();
            if (carnet.isEmpty() || nombre.isEmpty() || carrera.isEmpty() || semestre.isEmpty()) {
                Toast.makeText(this, "Datos Requeridos", Toast.LENGTH_SHORT).show();
                jetcarnet.requestFocus();
            } else {
                Map<String, Object> estudiante = new HashMap<>();
                estudiante.put("Carnet", carnet);
                estudiante.put("Nombre", nombre);
                estudiante.put("Carrera", carrera);
                estudiante.put("Semestre", semestre);
                if(jcbactivo.isChecked()){
                estudiante.put("Activo", "Si");
                }else{
                    estudiante.put("Activo", "No");
                }

                db.collection("Estudiantes").document(id_documento)
                        .set(estudiante)

                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EstudiantesActivity.this, "Documento actualizado", Toast.LENGTH_SHORT).show();
                                Limpiar_campos();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EstudiantesActivity.this, "Error actualizando el documento", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        } else {
            Toast.makeText(this, "Debe primero consultar para modificar", Toast.LENGTH_SHORT).show();
        }
    }//Fin modificar

    public void Eliminar(View view){
    if(!id_documento.equals("")){
        db.collection("Estudiantes").document(id_documento)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Limpiar_campos();
                        Toast.makeText(EstudiantesActivity.this, "Documento eliminado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EstudiantesActivity.this, "Error Eliminando documento", Toast.LENGTH_SHORT).show();
                    }
                });
     }else{
        Toast.makeText(this, "Debe primero consultar para eliminar", Toast.LENGTH_SHORT).show();
        jetcarnet.requestFocus();
     }
    }

    public void Anular(View view){
        if(!id_documento.equals("")){
            carnet = jetcarnet.getText().toString();
            nombre = jetnombre.getText().toString();
            carrera = jetcarrera.getText().toString();
            semestre = jetsemestre.getText().toString();
            if (carnet.isEmpty() || nombre.isEmpty() || carrera.isEmpty() || semestre.isEmpty()) {
                Toast.makeText(this, "Datos Requeridos", Toast.LENGTH_SHORT).show();
                jetcarnet.requestFocus();
            } else {
                Map<String, Object> estudiante = new HashMap<>();
                estudiante.put("Carnet", carnet);
                estudiante.put("Nombre", nombre);
                estudiante.put("Carrera", carrera);
                estudiante.put("Semestre", semestre);
                estudiante.put("Activo", "No");
                jcbactivo.setChecked(false);
                db.collection("Estudiantes").document(id_documento)
                        .set(estudiante)

                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EstudiantesActivity.this, "Documento actualizado", Toast.LENGTH_SHORT).show();
                                Limpiar_campos();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EstudiantesActivity.this, "Error actualizando el documento", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }else{
            Toast.makeText(this, "Debe primero consultar para anular", Toast.LENGTH_SHORT).show();
            jetcarnet.requestFocus();
        }
    }

    private void Limpiar_campos(){
        jetcarnet.setText("");
        jetnombre.setText("");
        jetcarrera.setText("");
        jetsemestre.setText("");
        jcbactivo.setChecked(false);
        jetcarnet.requestFocus();
        id_documento="";
    }

    public void Cancelar(View view){
        Limpiar_campos();
    }

    public void Consulta_general(View view){
        Intent intlist = new Intent(this,ListarEstudiantesActivity.class);
        startActivity((intlist));
    }

    public void Regresar(View view){
        Intent intmain=new Intent(this,MainActivity.class);
        startActivity(intmain);
    }



}