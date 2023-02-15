package com.damdos.vivardaniel2eva;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.damdos.vivardaniel2eva.model.Contacto;
import com.damdos.vivardaniel2eva.model.FirebaseDatabaseHelper;
import com.damdos.vivardaniel2eva.model.Usuario;
import com.damdos.vivardaniel2eva.model.Validaciones;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NuevoContacto extends AppCompatActivity {
    Button boton_guardar;
    EditText nombre, direccion, correo, telefono;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_contacto);
        this.setTitle("Nuevo Contacto");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nombre = findViewById(R.id.nombreContacto);
        direccion = findViewById(R.id.direccionContacto);
        correo = findViewById(R.id.correoContacto);
        telefono = findViewById(R.id.telefonoContacto);
        boton_guardar = findViewById(R.id.boton_guardar_contacto);
        boton_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreContacto = nombre.getText().toString().trim();
                String direccionContacto = direccion.getText().toString().trim();
                String correoContancto = correo.getText().toString().trim();
                String telefonoContacto = telefono.getText().toString().trim();
                if(nombreContacto.isEmpty() && direccionContacto.isEmpty() && correoContancto.isEmpty() && telefonoContacto.isEmpty()){
                    Toast.makeText(NuevoContacto.this, "Faltan datos", Toast.LENGTH_SHORT).show();

                }else {
                    if(!Validaciones.validarNombre(nombreContacto)){
                        nombre.setError("Nombre no válido (rango mínimo 2 caracteres máximo 15)");
                    }else if (!Validaciones.validarDireccion(direccionContacto)) {
                        direccion.setError("Correo no válido (rango mínimo 2 caracteres máximo 15)");
                    }else if (!Validaciones.validarCorreo(correoContancto)){
                        correo.setError("Correo no válido");
                    }else if (!Validaciones.validarTelefono(telefonoContacto)){
                        telefono.setError("Teléfono no válido");
                    }else{
                        nuevoContacto(nombreContacto,direccionContacto,correoContancto,telefonoContacto);
                    }
                }

            }
        });
    }

    private void nuevoContacto(String nombreContacto, String direccionContacto, String correoContancto, String telefonoContacto) {
        Contacto contacto = new Contacto();
        contacto.setNombre(nombreContacto);
        contacto.setDireccion(direccionContacto);
        contacto.setCorreo(correoContancto);
        contacto.setTelefono(telefonoContacto);
        new FirebaseDatabaseHelper().agregarContacto(contacto, new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Contacto> contactoList, List<String> claves) {

            }

            @Override
            public void DataIsInserted() {
                Toast.makeText(NuevoContacto.this, "Contacto creado", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });


        /**contactoRef= FirebaseDatabase.getInstance().getReference("usuarios").child("contactos");
        Contacto c = new Contacto(nombreContacto, direccionContacto, correoContancto, telefonoContacto );

        contactoRef.push().setValue(c);
        Toast.makeText(NuevoContacto.this, "Contacto creado", Toast.LENGTH_SHORT).show();
        **/
       /** Map<String, Object> map = new HashMap<>();
        map.put("nombre", nombreContacto);
        map.put("direccion", direccionContacto);
        map.put("correo", correoContancto);
        map.put("telefono", telefonoContacto);**/

        /**firebaseFirestore.collection("contacto").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(NuevoContacto.this, "Se creó el contacto con exito", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NuevoContacto.this, "Error al insertar el contacto", Toast.LENGTH_SHORT).show();

            }
        });**/
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}