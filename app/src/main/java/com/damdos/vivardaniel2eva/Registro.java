package com.damdos.vivardaniel2eva;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.damdos.vivardaniel2eva.model.Usuario;
import com.damdos.vivardaniel2eva.model.Validaciones;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registro extends AppCompatActivity {
    Button boton_registrarse, boton_cancelar;
    EditText nombre, correoNuevo, passwordNuevo;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        firebaseAuth = FirebaseAuth.getInstance();


        nombre=findViewById(R.id.nombre);
        correoNuevo=findViewById(R.id.correoNuevo);
        passwordNuevo=findViewById(R.id.passwordNuevo);

        boton_cancelar = findViewById(R.id.boton_cancelar);
        boton_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registro.this, MainActivity.class));
                Toast.makeText(Registro.this, "Regrasando al inicio de sesión", Toast.LENGTH_SHORT).show();
            }
        });
        boton_registrarse = findViewById(R.id.boton_registrar);
        boton_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreUsuario = nombre.getText().toString().trim();
                String correoUsuario = correoNuevo.getText().toString().trim();
                String passwordUsiario = passwordNuevo.getText().toString().trim();

                if(nombreUsuario.isEmpty() &&  correoUsuario.isEmpty() && passwordUsiario.isEmpty()){

                    Toast.makeText(Registro.this, "Introduzca correctamente los datos", Toast.LENGTH_SHORT).show();

                }else{
                    if(!Validaciones.validarNombre(nombreUsuario)){
                        nombre.setError("Nombre no válido (rango mínimo 2 caracteres máximo 15)");
                    }else if (!Validaciones.validarCorreo(correoUsuario)){
                        correoNuevo.setError("Correo no válido");
                    }else if (!Validaciones.validarPassword(passwordUsiario)){
                        passwordNuevo.setError("Contraseña no válida (valores permitidos [0-9A-Za-z_] con rango de {3,15})");
                    }else{
                        usuarioRegistrado(nombreUsuario, correoUsuario, passwordUsiario);
                    }


                }
            }
        });

    }

    private void usuarioRegistrado(String nombreUsuario, String correoUsuario, String passwordUsiario) {
    firebaseAuth.createUserWithEmailAndPassword(correoUsuario, passwordUsiario).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String uid = currentUser.getUid();

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child(uid);
            userRef.setValue(new Usuario(nombreUsuario, correoUsuario, passwordUsiario));
            Toast.makeText(Registro.this, "Registro correcto", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(Registro.this, Agenda.class));


        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(Registro.this, "Error al registrarse", Toast.LENGTH_SHORT).show();
        }
    });
    }
}