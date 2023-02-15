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

    public FirebaseDatabaseHelper() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        databaseReference = firebaseDatabase.getInstance().getReference().child("usuarios").child(uid).child("contactos");
    }

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
    public void agregarContacto(Contacto contacto, final DataStatus dataStatus){
        String clave = databaseReference.push().getKey();
        databaseReference.child(clave).setValue(contacto).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dataStatus.DataIsInserted();
            }
        });
    }

    public void modificarContacto(String clave, Contacto contacto, final DataStatus dataStatus){
        databaseReference.child(clave).setValue(contacto).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dataStatus.DataIsUpdated();
            }
        });

    }

    public void borrarContacto(String clave, final DataStatus dataStatus){
        databaseReference.child(clave).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dataStatus.DataIsDeleted();
            }
        });
    }
}
