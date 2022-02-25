package practica5.sebastiancardonahenao.iesseveroochoa.p72022sebastianch.model;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import practica5.sebastiancardonahenao.iesseveroochoa.p72022sebastianch.R;

public class EmpresaActivity extends AppCompatActivity implements View.OnClickListener {

    TextView nombre,direccion,telefono;
    Empresa empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        // FIND VIEWS
        nombre = findViewById(R.id.tvNombre);
        direccion = findViewById(R.id.tvDireccion);
        telefono = findViewById(R.id.tvTelefono);
        obtenDatosEmpresa();

    }

    void obtenDatosEmpresa(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Obtenemos los datos de la empresa a enseñar
        DocumentReference docRef=db.collection(FirebaseContract.EmpresaEntry.COLLECTION_NAME).
                document(FirebaseContract.EmpresaEntry.ID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //leemos los datos
                empresa = documentSnapshot.toObject(Empresa.class);
                //mostramos los datos y asignamos eventos
                asignaValoresEmpresa();
            }
        });
    }

    private void asignaValoresEmpresa() {
        nombre.setText(empresa.nombre);
        direccion.setText(empresa.direccion);
        telefono.setText(empresa.telefono);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvDireccion:
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, empresa.getUriLocalizacion());
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");
                // Attempt to start an activity that can handle the Intent
                startActivity(mapIntent);
                break;
            case R.id.tvTelefono:
                Intent i = new Intent(Intent.ACTION_DIAL,empresa.getUriTelefono());
                startActivity(i);
                break;
        }
    }

}