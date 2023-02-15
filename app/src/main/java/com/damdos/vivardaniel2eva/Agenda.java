package com.damdos.vivardaniel2eva;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import com.damdos.vivardaniel2eva.model.Contacto;
import com.damdos.vivardaniel2eva.model.FirebaseDatabaseHelper;
import com.damdos.vivardaniel2eva.model.RecyclerViewConfiguracion;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class Agenda extends AppCompatActivity {
    private Button boton_nuevo_contacto;
    private RecyclerView lista_contactos;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        this.setTitle("Agenda");
        firebaseAuth = FirebaseAuth.getInstance();
        lista_contactos = findViewById(R.id.listaAgenda);
        new FirebaseDatabaseHelper().listaContactos(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Contacto> contactoList, List<String> claves) {
                new RecyclerViewConfiguracion().setConfig(lista_contactos, Agenda.this, contactoList, claves);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });


        /**lista_contactos.setLayoutManager(new LinearLayoutManager(this));
        Query query = firebaseFirestore.collection("contacto");
        FirestoreRecyclerOptions<Contacto> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Contacto>().setQuery(query, Contacto.class).build();
        contactoAdaptador = new ContactoAdaptador(firestoreRecyclerOptions);
        contactoAdaptador.notifyDataSetChanged();
        lista_contactos.setAdapter(contactoAdaptador);**/

        boton_nuevo_contacto = findViewById(R.id.boton_agregar_contacto);
        boton_nuevo_contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Agenda.this, NuevoContacto.class));
            }
        });


    }

   /** @Override
    protected void onStart() {
        super.onStart();
         contactoAdaptador.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        contactoAdaptador.stopListening();

    }**/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu_agenda, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nuevoContactoMenu:
                startActivity(new Intent(this, NuevoContacto.class));
                return true;
            case R.id.cerrar_sesion:
                firebaseAuth.signOut();
                invalidateOptionsMenu();
                RecyclerViewConfiguracion.cerrarSersion();

                startActivity(new Intent(this, MainActivity.class));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


}