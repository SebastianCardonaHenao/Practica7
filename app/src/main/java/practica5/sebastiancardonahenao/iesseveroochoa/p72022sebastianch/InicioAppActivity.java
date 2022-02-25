package practica5.sebastiancardonahenao.iesseveroochoa.p72022sebastianch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.ChangeEventListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.SimpleFormatter;

import practica5.sebastiancardonahenao.iesseveroochoa.p72022sebastianch.adapter.ChatAdapter;
import practica5.sebastiancardonahenao.iesseveroochoa.p72022sebastianch.model.Conferencia;
import practica5.sebastiancardonahenao.iesseveroochoa.p72022sebastianch.model.EmpresaActivity;
import practica5.sebastiancardonahenao.iesseveroochoa.p72022sebastianch.model.FirebaseContract;
import practica5.sebastiancardonahenao.iesseveroochoa.p72022sebastianch.model.Mensaje;

public class InicioAppActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth auth;
    TextView tvDatosUsr,tvConf;
    ArrayList<Conferencia> listaConferencias;
    Spinner spElige;
    Conferencia conferencia;
    public static FirebaseUser usrFB;
    EditText etMensaje;
    Button btEnviar;
    RecyclerView rvChat;
    ChatAdapter adapter;
    final static String TAG = "LOG DATA: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_app);

        // Find By ID de los objetos
        tvDatosUsr = findViewById(R.id.tvDatosUsr);
        spElige = findViewById(R.id.spElige);
        tvConf = findViewById(R.id.tvConf);
        etMensaje = findViewById(R.id.etMensaje);
        btEnviar = findViewById(R.id.btEnviar);

        // Guardado de la autenticación y asignación del correo al text
        auth = FirebaseAuth.getInstance();
        usrFB = auth.getCurrentUser();
        tvDatosUsr.setText(usrFB.getEmail());

        btEnviar.setOnClickListener(e -> enviarMensaje());

        //RECIVLER VIEW
        rvChat = findViewById(R.id.rvMensajes);
        rvChat.setLayoutManager(new LinearLayoutManager(this));

        leerConferencias();
        iniciarConferenciasIniciadas();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void defineAdaptador() {
        //consulta en Firebase
        Query query = FirebaseFirestore.getInstance()
                //coleccion conferencias
                .collection(FirebaseContract.ConferenciaEntry.COLLECTION_NAME)
                //documento: conferencia actual
                .document(conferencia.getId())
                //colección chat de la conferencia
                .collection(FirebaseContract.ChatEntry.COLLECTION_NAME)
                //obtenemos la lista ordenada por fecha
                .orderBy(FirebaseContract.ChatEntry.FECHA_CREACION,
                        Query.Direction.DESCENDING);
        //Creamos la opciones del FirebaseAdapter
        FirestoreRecyclerOptions<Mensaje> options = new
                FirestoreRecyclerOptions.Builder<Mensaje>()
                //consulta y clase en la que se guarda los datos
                .setQuery(query, Mensaje.class)
                .setLifecycleOwner(this)
                .build();
        Log.d(TAG, options.getSnapshots().size()+""+conferencia.getNombre());
        //si el usuario ya habia seleccionado otra conferencia, paramos la escucha
        if (adapter != null) {
            adapter.stopListening();
        }
        //Creamos el adaptador
        adapter = new ChatAdapter(options);
        rvChat.setAdapter(adapter);
        //comenzamos a escuchar. Normalmente solo tenemos un adaptador, esto tenemos que
        //hacerlo en el evento onStar, como indica la documentación
        adapter.startListening();
        //Podemos reaccionar ante cambios en la query( se añade un mesaje).Nosotros,
                // //lo que necesitamos es mover el scroll
                // del recyclerView al inicio para ver el mensaje nuevo
                adapter.getSnapshots().addChangeEventListener(new ChangeEventListener() {
                    @Override
                    public void onChildChanged(@NonNull ChangeEventType type, @NonNull
                            DocumentSnapshot snapshot, int newIndex, int oldIndex) {
                        rvChat.smoothScrollToPosition(0);
                    }
                    @Override
                    public void onDataChanged() {
                        Log.d(TAG, options.getSnapshots().size()+""+conferencia.getNombre());
                    }
                    @Override
                    public void onError(@NonNull FirebaseFirestoreException e) {
                    }
                });
    }
    //es necesario parar la escucha
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }



    private void leerConferencias() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        listaConferencias=new ArrayList<Conferencia>();
        db.collection(FirebaseContract.ConferenciaEntry.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " +
                                        document.getData());
                                listaConferencias.add(document.toObject(Conferencia.class));
                            }
                            cargaSpinner();
                        } else {
                            Log.d(TAG, ""+getText(R.string.doc_error),
                                    task.getException());
                        }
                    }
                });
    }

    private void cargaSpinner() {
        ArrayAdapter<Conferencia> spinnerArrayAdapter = new ArrayAdapter<>(
                (this), android.R.layout.simple_spinner_dropdown_item, listaConferencias);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spElige.setAdapter(spinnerArrayAdapter);
        spElige.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                conferencia = (Conferencia) spElige.getSelectedItem();
                defineAdaptador();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }


    private void verEmpresa() {
        Intent i = new Intent(this, EmpresaActivity.class);
        startActivity(i);
    }

    private void signOut(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        showSnackbar(R.string.sign_out);
                    }
                });
        startActivity(new Intent(InicioAppActivity.this, MainActivity.class));
        finish();
    }

    private void showSnackbar (int texto){
        Snackbar.make(findViewById(android.R.id.content).getRootView(), getString(texto), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itLogOut:
                signOut();
                break;
            case R.id.itEmpresa:
                verEmpresa();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivOjo:
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String fecha = simpleDateFormat.format(conferencia.getFecha());
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle(getText(R.string.info_conf));
                dialog.setMessage(conferencia.getNombre()+"\n" +
                        getText(R.string.fehca)+fecha+"\n"+
                        getText(R.string.horario)+conferencia.getHorario()+"\n"+
                        getText(R.string.sala)+conferencia.getSala());
                dialog.show();
                break;
        }
    }

    private void iniciarConferenciasIniciadas() {
        //https://firebase.google.com/docs/firestore/query-data/listen
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef =
                db.collection(FirebaseContract.ConferenciaIniciadaEntry.COLLECTION_NAME)
                        .document(FirebaseContract.ConferenciaIniciadaEntry.ID);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    String conferenciaIniciada=snapshot.getString(FirebaseContract
                                    .ConferenciaIniciadaEntry.CONFERENCIA);
                    tvConf.setText("C.iniciada: "+conferenciaIniciada);
                    Log.d(TAG, "Conferencia iniciada: " + snapshot.getData());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    private void enviarMensaje() {
        String body = etMensaje.getText().toString();
        if (!body.isEmpty()) {
            //usuario y mensaje
            Mensaje mensaje = new Mensaje(usrFB.getEmail(), body);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(FirebaseContract.ConferenciaEntry.COLLECTION_NAME)
                    //documento conferencia actual
                    .document(conferencia.getId())
                    //subcolección de la conferencia
                    .collection(FirebaseContract.ChatEntry.COLLECTION_NAME)
                    //añadimos el mensaje nuevo
                    .add(mensaje);
            etMensaje.setText("");
            ocultarTeclado();
        }
    }
    /**
     * Permite ocultar el teclado
     */
    private void ocultarTeclado() {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(etMensaje.getWindowToken(), 0);
        }
    }

}