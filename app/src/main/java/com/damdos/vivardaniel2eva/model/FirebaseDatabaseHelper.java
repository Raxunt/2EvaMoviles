package com.damdos.vivardaniel2eva.model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FirebaseDatabaseHelper {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<Contacto> listaContanctos = new ArrayList<>();
    FirebaseAuth firebaseAuth;


    public interface DataStatus{
        void DataIsLoaded(List<Contacto> contactoList, List<String> claves);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    /**
     * Método para asignar el árbol a la hora de insertar los datos en la BD.
     */
    public FirebaseDatabaseHelper() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        databaseReference = firebaseDatabase.getInstance().getReference().child("usuarios").child(uid).child("contactos");
    }

    /**
     *  Recupera la lista de contactos y los muestra en el RecyclerView.
     * @param dataStatus Carga los datos almacenados en la BD y los introduce en la lista RecyclerView.
     */
    public void listaContactos(final DataStatus dataStatus){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaContanctos.clear();
                List<String> claves = new ArrayList<>();
                for(DataSnapshot nodoClave : snapshot.getChildren()){
                   claves.add( nodoClave.getKey());
                   Contacto contacto = nodoClave.getValue(Contacto.class);
                   listaContanctos.add(contacto);
                }
                dataStatus.DataIsLoaded(listaContanctos, claves);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Método que nos almacena en la BD un contacto nuevo.
     * @param contacto Recoge a un contacto nuevo con todos sus atributos.
     * @param dataStatus apunta a la base de datos donde se almacenan los contactos.
     */
    public void agregarContacto(Contacto contacto, final DataStatus dataStatus){
        String clave = databaseReference.push().getKey();
        databaseReference.child(clave).setValue(contacto).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dataStatus.DataIsInserted();
            }
        });
    }

    /**
     * Actualiza a un contacto ya creado.
     * @param clave recoge el identificador del usuario existente por medio de un string.
     * @param contacto recoge la clase usuario.
     * @param dataStatus apunta a la base de datos donde se almacenan los contactos.
     */
    public void modificarContacto(String clave, Contacto contacto, final DataStatus dataStatus){
        databaseReference.child(clave).setValue(contacto).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dataStatus.DataIsUpdated();
            }
        });

    }

    /**
     *  Borra a un contacto de la base de datos.
     * @param clave recoge el identificador del usuario existente por medio de un string.
     * @param dataStatus apunta a la base de datos donde se almacenan los contactos.
     */
    public void borrarContacto(String clave, final DataStatus dataStatus){
        databaseReference.child(clave).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dataStatus.DataIsDeleted();
            }
        });
    }
}
