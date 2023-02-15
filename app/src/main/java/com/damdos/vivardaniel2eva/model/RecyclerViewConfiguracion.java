package com.damdos.vivardaniel2eva.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.damdos.vivardaniel2eva.MainActivity;
import com.damdos.vivardaniel2eva.ModificarContacto;
import com.damdos.vivardaniel2eva.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class RecyclerViewConfiguracion {
    FirebaseAuth firebaseAuth;
    private static FirebaseUser usuario;
    private Context context;
    private ContactosAdapter contactosAdapter;
    public void setConfig(RecyclerView recyclerView, Context context, List<Contacto> listaContactos, List<String> listaClaves){
        firebaseAuth = FirebaseAuth.getInstance();
        usuario = firebaseAuth.getCurrentUser();
        this.context = context;
        this.contactosAdapter= new ContactosAdapter(listaContactos, listaClaves);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(contactosAdapter);
    }


    class ContactoItemView extends RecyclerView.ViewHolder{
        private TextView nombre, direccion, correo, telefono;
        private String clave;
        public ContactoItemView(ViewGroup parent){
            super(LayoutInflater.from(context).inflate(R.layout.activity_vista_contacto, parent, false));
            nombre = itemView.findViewById(R.id.nombre_contacto);
            direccion = itemView.findViewById(R.id.direccion_contacto);
            correo = itemView.findViewById(R.id.correo_contacto);
            telefono = itemView.findViewById(R.id.telefono_contacto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(usuario!=null){
                        Intent intent = new Intent(context, ModificarContacto.class);
                        intent.putExtra("clave", clave);
                        intent.putExtra("nombre", nombre.getText().toString());
                        intent.putExtra("direccion", direccion.getText().toString());
                        intent.putExtra("correo", correo.getText().toString());
                        intent.putExtra("telefono", telefono.getText().toString());

                        context.startActivity(intent);
                    }else{
                        context.startActivity(new Intent(context, MainActivity.class));
                    }
                }
            });
        }
        public void union(Contacto contacto, String clave){
            nombre.setText(contacto.getNombre());
            direccion.setText(contacto.getDireccion());
            correo.setText(contacto.getCorreo());
            telefono.setText(contacto.getTelefono());
            this.clave = clave;
        }
    }
    class ContactosAdapter extends RecyclerView.Adapter<ContactoItemView>{
    private List<Contacto> listaContactos;
    private List<String> listaClaves;

        public ContactosAdapter(List<Contacto> listaContactos, List<String> listaClaves) {
            this.listaContactos = listaContactos;
            this.listaClaves = listaClaves;
        }

        @NonNull
        @Override
        public ContactoItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ContactoItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ContactoItemView holder, int position) {
        holder.union(listaContactos.get(position), listaClaves.get(position));
        }

        @Override
        public int getItemCount() {
            return listaContactos.size();
        }
    }


    public static void cerrarSersion(){
        usuario=null;
    }
}
