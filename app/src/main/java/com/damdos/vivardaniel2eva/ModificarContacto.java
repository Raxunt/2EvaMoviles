package com.damdos.vivardaniel2eva;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.damdos.vivardaniel2eva.model.Contacto;
import com.damdos.vivardaniel2eva.model.FirebaseDatabaseHelper;
import com.damdos.vivardaniel2eva.model.Validaciones;

import java.util.List;

public class ModificarContacto extends AppCompatActivity {
     Button boton_modificar, boton_borrar, boton_cancelar, boton_llamar;
     EditText nombre, direccion, correo, telefono;

    private String clave;
    private String nom;
    private String direc;
    private String corr;
    private String telf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_contacto);
        this.setTitle("Modificar Contacto");
        clave = getIntent().getStringExtra("clave");
        nom = getIntent().getStringExtra("nombre");
        direc = getIntent().getStringExtra("direccion");
        corr = getIntent().getStringExtra("correo");
        telf = getIntent().getStringExtra("telefono");


        nombre = findViewById(R.id.nombreContactoExistente);
        nombre.setText(nom);
        direccion = findViewById(R.id.direccionContactoExistente);
        direccion.setText(direc);
        correo = findViewById(R.id.correoContactoExistente);
        correo.setText(corr);
        telefono = findViewById(R.id.telefonoContactoExistente);
        telefono.setText(telf);
        boton_modificar = findViewById(R.id.boton_guardar_contacto_existente);
        boton_borrar = findViewById(R.id.boton_borrar_contacto_existente);
        boton_cancelar = findViewById(R.id.boton_cancelar_contacto_existente);
        boton_llamar = findViewById(R.id.boton_llamar);

        boton_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contacto contacto = new Contacto();
                contacto.setNombre(nombre.getText().toString());
                contacto.setDireccion(direccion.getText().toString());
                contacto.setCorreo(correo.getText().toString());
                contacto.setTelefono(telefono.getText().toString());

                if (!Validaciones.validarNombre(nombre.getText().toString())) {
                    nombre.setError("Nombre no válido (rango mínimo 2 caracteres máximo 15)");
                } else if (!Validaciones.validarDireccion(direccion.getText().toString())) {
                    direccion.setError("Correo no válido (rango mínimo 2 caracteres máximo 15)");
                } else if (!Validaciones.validarCorreo(correo.getText().toString())) {
                    correo.setError("Correo no válido");
                } else if (!Validaciones.validarTelefono(telefono.getText().toString())) {
                    telefono.setError("Teléfono no válido");
                } else {

                    new FirebaseDatabaseHelper().modificarContacto(clave, contacto, new FirebaseDatabaseHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<Contacto> contactoList, List<String> claves) {

                        }

                        @Override
                        public void DataIsInserted() {

                        }

                        @Override
                        public void DataIsUpdated() {
                            Toast.makeText(ModificarContacto.this, "Modificación exitosa", Toast.LENGTH_SHORT).show();


                        }

                        @Override
                        public void DataIsDeleted() {

                        }
                    });
                }
            }
        });

        boton_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FirebaseDatabaseHelper().borrarContacto(clave, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Contacto> contactoList, List<String> claves) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {
                        Toast.makeText(ModificarContacto.this, "Eliminación exitosa", Toast.LENGTH_SHORT).show();
                        finish(); return;
                    }
                });
            }
        });
        boton_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); return;
            }
        });

        boton_llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + telf)));
            }
        });
    }
}