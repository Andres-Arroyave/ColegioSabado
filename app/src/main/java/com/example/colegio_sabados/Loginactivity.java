package com.example.colegio_sabados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.colegio_sabados.MainActivity;
import com.example.colegio_sabados.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Loginactivity extends AppCompatActivity {

    EditText jetusuario, jetcontraseña;
    String usuario, contraseña;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);
        getSupportActionBar().hide();
        jetusuario = findViewById(R.id.etusuario);
        jetcontraseña = findViewById(R.id.etcontraseña);
    }

    public void Login(View view) {
        usuario = jetusuario.getText().toString();
        contraseña = jetcontraseña.getText().toString();

        if (usuario.isEmpty() || contraseña.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            jetusuario.requestFocus();
        } else {
            db.collection("Usuarios")
                    .whereEqualTo("usuario", usuario)  // Filtrar por el campo "usuario"
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (!querySnapshot.isEmpty()) {
                                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);  // Obtener el primer documento
                                    String contraseñaCorrecta = document.getString("contraseña");
                                    if (contraseña.equals(contraseñaCorrecta)) {
                                        // Credenciales válidas, inicio de sesión exitoso
                                        Toast.makeText(Loginactivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                                        // Aquí puedes redirigir al usuario a la siguiente sección
                                        // por ejemplo, abrir una nueva actividad o fragmento
                                        Intent int_main = new Intent(Loginactivity.this, MainActivity.class);
                                        startActivity(int_main);
                                    } else {
                                        Toast.makeText(Loginactivity.this, "Las credenciales no coinciden", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Loginactivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Loginactivity.this, "Error al realizar la consulta", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


}
