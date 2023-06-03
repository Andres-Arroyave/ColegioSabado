package com.example.colegio_sabados;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

public class MateriasActivity extends AppCompatActivity {

    EditText jetcodigo,jetmateria,jetcreditos,jetprofesor;
    CheckBox jcbactivo;
    String codigo,materia,creditos,profesor,id_materia;
    Button jbtnactivar, jbtnadicionar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materias);
        getSupportActionBar().hide();

        jetcodigo=findViewById(R.id.etcodigo);
        jetmateria=findViewById(R.id.etmateria);
        jetcreditos=findViewById(R.id.etcreditos);
        jetprofesor=findViewById(R.id.etprofesor);
        jcbactivo=findViewById(R.id.cbactivo);

        jbtnadicionar=findViewById(R.id.btnadicionar);
        jbtnactivar=findViewById(R.id.btnactivar);

        id_materia="";
    }

    public void Adicionar(View view) {
        codigo = jetcodigo.getText().toString();
        materia = jetmateria.getText().toString();
        creditos = jetcreditos.getText().toString();
        profesor = jetprofesor.getText().toString();
        if (codigo.isEmpty() || materia.isEmpty() || creditos.isEmpty() || profesor.isEmpty()) {
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        } else {
            // Create a new subject
            Map<String, Object> Materia = new HashMap<>();
            Materia.put("Codigo", codigo);
            Materia.put("Materia", materia);
            Materia.put("Creditos", creditos);
            Materia.put("Profesor", profesor);
            Materia.put("Activo", "Si");

            // Consultar para verificar si la materia ya existe
            Consultar_cod();
            if (id_materia.equals("")) {
                // No existe la materia, agregarla
                db.collection("Materias")
                        .add(Materia)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Limpiar_campos();
                                Toast.makeText(MateriasActivity.this, "Materia guardada", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MateriasActivity.this, "Error guardando Materia", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                // La materia ya existe, actualizarla
                Materia.put("Activo", jcbactivo.isChecked() ? "Si" : "No");
                db.collection("Materias").document(id_materia)
                        .set(Materia)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Limpiar_campos();
                                Toast.makeText(MateriasActivity.this, "Materia actualizada", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MateriasActivity.this, "Error actualizando la materia", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    public void Activar(View view){
        if (!id_materia.equals((""))) {
            Map<String, Object> Materia = new HashMap<>();
            if(!jcbactivo.isChecked()){
                Materia.put("Activo", "Si");
            }
            db.collection("Materias").document(id_materia)
                    .update(Materia)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MateriasActivity.this, "Materia activada", Toast.LENGTH_SHORT).show();
                            Limpiar_campos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MateriasActivity.this, "Error activando la materia", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Debe primero consultar para activar", Toast.LENGTH_SHORT).show();
        }
    }

    public void consultar(View view){
                Consultar_cod();
    }

    private void Consultar_cod(){
        codigo=jetcodigo.getText().toString();
        if(codigo.isEmpty()){
            Toast.makeText(this, "Codigo de materia es requerido", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }else{
            db.collection("Materias")
                    .whereEqualTo("Codigo",codigo)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    jetcodigo.setEnabled(false);
                                    jetmateria.setEnabled(true);
                                    jetcreditos.setEnabled(true);
                                    jetprofesor.setEnabled(true);
                                    jbtnadicionar.setEnabled(true);
                                    jbtnactivar.setEnabled(true);
                                    jcbactivo.setEnabled(true);

                                    id_materia=document.getId();

                                    jetmateria.setText(document.getString("Materia"));
                                    jetcreditos.setText(document.getString("Creditos"));
                                    jetprofesor.setText(document.getString("Profesor"));
                                    if(document.getString("Activo").equals("Si")){
                                        jcbactivo.setChecked(true);
                                    }else{
                                        jcbactivo.setChecked(false);
                                    }
                                }
                                if (task.getResult().isEmpty()) {
                                    Toast.makeText(MateriasActivity.this, "Codigo no hallado, Complete para adicionar", Toast.LENGTH_SHORT).show();
                                    jetmateria.setEnabled(true);
                                    jetcreditos.setEnabled(true);
                                    jetprofesor.setEnabled(true);
                                    jbtnadicionar.setEnabled(true);
                                    jbtnactivar.setEnabled(true);
                                    jcbactivo.setEnabled(true);
                                }
                            } else {
                                Toast.makeText(MateriasActivity.this, "Error consultando la materia", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void Limpiar_campos(){
        jetcodigo.setText("");
        jetmateria.setText("");
        jetcreditos.setText("");
        jetprofesor.setText("");
        jcbactivo.setChecked(false);
        jetcodigo.requestFocus();
        id_materia="";
        jetcodigo.setEnabled(true);
        jetmateria.setEnabled(false);
        jetcreditos.setEnabled(false);
        jetprofesor.setEnabled(false);
        jbtnadicionar.setEnabled(false);
        jbtnactivar.setEnabled(false);
        jcbactivo.setEnabled(false);
    }
    public void Cancelar(View view){
        Limpiar_campos();

    }

    public void Regresar(View view){
        Intent intmain=new Intent(this,MainActivity.class);
        startActivity(intmain);
    }
}
