package com.damdos.vivardaniel2eva;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.damdos.vivardaniel2eva.model.FirebaseDatabaseHelper;

public class Nota extends AppCompatActivity {
private Button botonCancelar, botonGuardar;
private EditText titulo, contenido;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);
        titulo = findViewById(R.id.tituloNota);
        contenido = findViewById(R.id.contenidoNota);
        botonCancelar = findViewById(R.id.botonCancelarNota);
        botonGuardar = findViewById(R.id.botonGuardarNota);
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Nota.this, Agenda.class));
            }
        });

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tituloNota = titulo.getText().toString().trim();
                String contenidoNota = contenido.getText().toString().trim();



                nuevaNota(tituloNota, contenidoNota);
            }
        });


    }
    private void nuevaNota(String tituloNota, String contenidoNota) {

    }
}