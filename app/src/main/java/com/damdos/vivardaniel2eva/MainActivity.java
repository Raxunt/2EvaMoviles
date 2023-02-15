package com.damdos.vivardaniel2eva;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.damdos.vivardaniel2eva.model.Validaciones;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private Switch aSwitch;
    boolean modoNoche;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Button boton_entrar, boton_nuevo_usu;
    private EditText correo, password;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * Código para realizar el cambio de Preferencias determinando que modo de colores deseamos entre claro/oscuro
         */
        getSupportActionBar().hide();

        aSwitch = findViewById(R.id.cambio_estilo);
        sharedPreferences = getSharedPreferences( "MODE", Context.MODE_PRIVATE);
        modoNoche = sharedPreferences.getBoolean("night", false);

        if(modoNoche){
            aSwitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }
        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modoNoche){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", false);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", true);

                }

                editor.apply();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        correo=findViewById(R.id.correo);
        password=findViewById(R.id.password);
        /**
         * Boton para acceder a la pantalla de nuevo usuario
         */
        boton_nuevo_usu = findViewById(R.id.boton_nuevaCuenta);
        boton_nuevo_usu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Registro.class));
                Toast.makeText(MainActivity.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * Botón para acceder a la agenda tras validar las credenciales correctas almacenadas en la base de datos.
         */
        boton_entrar = findViewById(R.id.boton_login);
        boton_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correoUsuario = correo.getText().toString().trim();
                String passwordUsuario = password.getText().toString().trim();
                if(correoUsuario.isEmpty() && passwordUsuario.isEmpty()){
                    Toast.makeText(MainActivity.this, "¡Datos introducidos incorrectos!", Toast.LENGTH_SHORT).show();
                }else{
                    if (!Validaciones.validarCorreo(correoUsuario)){
                        correo.setError("Correo no válido");
                    }else if (!Validaciones.validarPassword(passwordUsuario)){
                        password.setError("Contraseña no válida (valores permitidos [0-9A-Za-z_] con rango de {3,15})");
                    }else{
                        loginUsuario(correoUsuario, passwordUsuario);
                    }

                }
            }
        });
    }

    /**
     * Función que nos permite comparar las credenciales que se introducen y contrastarlas contra la base de datos. Si son correctas
     * se pasará al menú de agenda arrojándonos un toast con un mensaje de exito al loguear, por lo contrario nos arrojará
     * un toast con el texto de credenciales incorrectas volviendo a introducir los datos o creando una cuenta nueva como ultima opción.
     * @param correoUsuario de tipo String introducido por el usuario validandola en la base de datos.
     * @param passwordUsuario de tipo String introducido por el usuario validandola en la base de datos.
     */
    private void loginUsuario(String correoUsuario, String passwordUsuario) {
        firebaseAuth.signInWithEmailAndPassword(correoUsuario, passwordUsuario).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                finish();
                startActivity(new Intent(MainActivity.this, Agenda.class));
                    Toast.makeText(MainActivity.this, "Credenciales correctas", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(MainActivity.this, "Error.", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Error al buscar al usuario.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Nos impide retroceder estando en el menú de inicio de sesión.
     */
    @Override
    public void onBackPressed() {
        // do nothing
    }
}