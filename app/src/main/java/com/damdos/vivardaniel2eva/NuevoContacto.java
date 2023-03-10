package com.damdos.vivardaniel2eva;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.damdos.vivardaniel2eva.model.Contacto;
import com.damdos.vivardaniel2eva.model.FirebaseDatabaseHelper;
import com.damdos.vivardaniel2eva.model.Validaciones;
import java.util.List;

public class NuevoContacto extends AppCompatActivity {
    Button boton_guardar;
    EditText nombre, direccion, correo, telefono, alias;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_contacto);
        this.setTitle("Nuevo Contacto");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        alias = findViewById(R.id.aliasContacto);
        nombre = findViewById(R.id.nombreContacto);
        direccion = findViewById(R.id.direccionContacto);
        correo = findViewById(R.id.correoContacto);
        telefono = findViewById(R.id.telefonoContacto);
        boton_guardar = findViewById(R.id.boton_guardar_contacto);
        boton_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aliasContacto = alias.getText().toString().trim();
                String nombreContacto = nombre.getText().toString().trim();
                String direccionContacto = direccion.getText().toString().trim();
                String correoContancto = correo.getText().toString().trim();
                String telefonoContacto = telefono.getText().toString().trim();
                if(nombreContacto.isEmpty() && direccionContacto.isEmpty() && correoContancto.isEmpty() && telefonoContacto.isEmpty()){
                    Toast.makeText(NuevoContacto.this, "Faltan datos", Toast.LENGTH_SHORT).show();

                }else {
                    if(!Validaciones.validarNombre(nombreContacto)){
                        nombre.setError("Nombre no v??lido (rango m??nimo 2 caracteres m??ximo 15)");
                    }else if (!Validaciones.validarDireccion(direccionContacto)) {
                        direccion.setError("Correo no v??lido (rango m??nimo 2 caracteres m??ximo 15)");
                    }else if (!Validaciones.validarCorreo(correoContancto)){
                        correo.setError("Correo no v??lido");
                    }else if (!Validaciones.validarTelefono(telefonoContacto)){
                        telefono.setError("Tel??fono no v??lido");
                    }else{
                        nuevoContacto(aliasContacto, nombreContacto,direccionContacto,correoContancto,telefonoContacto);
                    }
                }

            }
        });
    }

    /**
     * Inserta los datos de un nuevo contacto en la base de datos.
     * @param nombreContacto recoge un String que se asignar?? al nombre del contacto.
     * @param direccionContacto recoge un String que se asignar?? a la direcci??n del contacto.
     * @param correoContancto recoge un String que se asignar?? al correo del contacto.
     * @param telefonoContacto recoge un String que se asignar?? al tel??fono del contacto.
     */
    private void nuevoContacto(String aliasContacto,String nombreContacto, String direccionContacto, String correoContancto, String telefonoContacto) {
        Contacto contacto = new Contacto();
        contacto.setAlias(aliasContacto);
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

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}