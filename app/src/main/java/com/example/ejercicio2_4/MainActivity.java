package com.example.ejercicio2_4;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ejercicio2_4.configuraciones.SQLiteConexion;
import com.example.ejercicio2_4.configuraciones.Transacciones;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity  {

    Button btnBorrarFirma, btnGuardar, btnListaFirmas;
    EditText txtDescripcion;

    Lienzo lienzo;

    SQLiteConexion conexion;
    boolean estado;

    Bitmap ima;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBorrarFirma = (Button) findViewById(R.id.btnBorrarFirma);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnListaFirmas = (Button)findViewById(R.id.btnListaFirmas);

        txtDescripcion = (EditText) findViewById(R.id.txtDescripcion);
        lienzo = (Lienzo) findViewById(R.id.lienzo);

        conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);
        estado = true;


        btnBorrarFirma.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setMessage("¿Está seguro que desea borrar la firma y su descripcion?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i){
                                limpiar();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i){
                            }
                        }).show();

            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                guardarFirma();
            }
        });

        btnListaFirmas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivitySignaturess.class);
                startActivity(intent);
            }
        });

    }

    private void guardarFirma(){
        if(lienzo.borrado) {
            Toast.makeText(getApplicationContext(), "Debe ingresar una firma para continuar", Toast.LENGTH_LONG).show();
            return;

        } else if(txtDescripcion.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Debe ingresar una descripcion para continuar", Toast.LENGTH_LONG).show();
            return;

        } else {
            SQLiteDatabase db = conexion.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Transacciones.descripcion, txtDescripcion.getText().toString());

            ByteArrayOutputStream bay = new ByteArrayOutputStream(10480);

            Bitmap bitmap = Bitmap.createBitmap(lienzo.getWidth(), lienzo.getHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            lienzo.draw(canvas);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , bay);
            byte[] bl = bay.toByteArray();

            String img= Base64.encodeToString(bl,Base64.DEFAULT);
            values.put(Transacciones.imagen, img);

            Long result = db.insert(Transacciones.tablaSignatures, Transacciones.id, values);
            Toast.makeText(getApplicationContext(), "Firma guardada con exito", Toast.LENGTH_LONG).show();

            limpiar();

            db.close();
        }

    }

    private void limpiar() {
        lienzo.nuevoDibujo();
        txtDescripcion.setText("");
    }
}