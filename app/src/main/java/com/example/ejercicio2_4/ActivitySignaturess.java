package com.example.ejercicio2_4;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ejercicio2_4.Modelo.Signaturess;
import com.example.ejercicio2_4.configuraciones.Transacciones;
import com.example.ejercicio2_4.configuraciones.SQLiteConexion;

import java.util.ArrayList;

public class ActivitySignaturess extends AppCompatActivity {

    Button btnNuevaFirma;

    SQLiteConexion conexion;
    RecyclerView.Adapter adapter;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<Signaturess> firmasList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signaturess);

        btnNuevaFirma = (Button) findViewById(R.id.btnNuevaFirma);

        conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        firmasList = new ArrayList<>();
        listaFirmas();

        adapter = new Adapter(firmasList);
        recyclerView.setAdapter(adapter);

        btnNuevaFirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void listaFirmas(){
        SQLiteDatabase db = conexion.getReadableDatabase();
        Signaturess firmas = null;

        Cursor cursor = db.rawQuery("SELECT * FROM "+ Transacciones.tablaSignatures,null);

        while (cursor.moveToNext()){
            firmas = new Signaturess();
            firmas.setId(cursor.getInt(0));
            firmas.setDescripcion(cursor.getString(1));
            firmas.setImagen(cursor.getString(2));
            firmasList.add(firmas);
        }
    }

}