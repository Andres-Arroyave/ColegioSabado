package com.example.colegio_sabados;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.colegio_sabados.R;
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

public class MatriculasActivity extends AppCompatActivity {

    EditText jetcarnet, jetmatricula, jetfecha, jetmateria;
    TextView jtvnombre, jtvcarrera, jtvmateria, jtvcreditos;
    String carnet, matricula, fecha, materia, id_documento, id_materia;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matriculas);

        jetcarnet = findViewById(R.id.etcarnet);
        jetmatricula = findViewById(R.id.etmatricula);
        jetfecha = findViewById(R.id.etfecha);
        jetmateria = findViewById(R.id.etmateria);

        jtvnombre = findViewById(R.id.tvnombre);
        jtvcarrera = findViewById(R.id.tvcarrera);
        jtvmateria = findViewById(R.id.tvmateria);
        jtvcreditos = findViewById(R.id.tvcreditos);
        id_documento = "";
        id_materia = "";
    }

    public void Consularcarnet(View view) {
        carnet = jetcarnet.getText().toString();
        if (carnet.isEmpty()) {
            Toast.makeText(this, "Número de carnet es requerido", Toast.LENGTH_SHORT).show();
            jetcarnet.requestFocus();
        } else {
            db.collection("Estudiantes")
                    .whereEqualTo("Carnet", carnet)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    id_documento = document.getId();
                                    jtvnombre.setText(document.getString("Nombre"));
                                    jtvcarrera.setText(document.getString("Carrera"));
                                }
                            } else {
                                Toast.makeText(MatriculasActivity.this, "Documento no encontrado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void Consultarmateria(View view) {
        materia = jetmateria.getText().toString();
        if (materia.isEmpty()) {
            Toast.makeText(this, "Código de materia es requerido", Toast.LENGTH_SHORT).show();
            jetmateria.requestFocus();
        } else {
            db.collection("Materias")
                    .whereEqualTo("Codigo", materia)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    id_materia = document.getId();
                                    jtvmateria.setText(document.getString("Materia"));
                                    jtvcreditos.setText(document.getString("Creditos"));
                                }
                                if (task.getResult().isEmpty()) {
                                    Toast.makeText(MatriculasActivity.this, "Código no encontrado", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MatriculasActivity.this, "Error consultando la materia", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void AdicionarMat(View view) {
        carnet = jetcarnet.getText().toString();
        matricula = jetmatricula.getText().toString();
        fecha = jetfecha.getText().toString();
        materia = jetmateria.getText().toString();
        if (carnet.isEmpty() || matricula.isEmpty() || fecha.isEmpty() || materia.isEmpty()) {
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetmatricula.requestFocus();
        } else {
            ConsultarMatriculaExistente();
        }
    }

    private void ConsultarMatriculaExistente() {
        db.collection("Matriculas")
                .whereEqualTo("Matricula", matricula)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                GuardarMatricula();
                            } else {
                                Toast.makeText(MatriculasActivity.this, "La matrícula ya existe", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MatriculasActivity.this, "Error consultando la matrícula", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void GuardarMatricula() {
        Map<String, Object> matriculas = new HashMap<>();
        matriculas.put("Matricula", matricula);
        matriculas.put("Carnet", carnet);
        matriculas.put("Fecha", fecha);
        matriculas.put("Materia", materia);

        db.collection("Matriculas")
                .add(matriculas)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Limpiar_campos();
                        Toast.makeText(MatriculasActivity.this, "Matricula guardada", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MatriculasActivity.this, "Error guardando matricula", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void Limpiar_campos() {
        jetmatricula.setText("");
        jetcarnet.setText("");
        jetfecha.setText("");
        jetmateria.setText("");
        jtvnombre.setText("");
        jtvcarrera.setText("");
        jtvmateria.setText("");
        jtvcreditos.setText("");
        id_documento = "";
        id_materia = "";
    }

    public void Consulta_matriculas(View view){
        Intent intlist = new Intent(this,activity_listar_matriculas.class);
        startActivity((intlist));
    }
}
