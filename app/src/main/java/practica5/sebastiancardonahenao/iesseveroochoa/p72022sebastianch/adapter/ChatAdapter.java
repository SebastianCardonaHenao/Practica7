package practica5.sebastiancardonahenao.iesseveroochoa.p72022sebastianch.adapter;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import practica5.sebastiancardonahenao.iesseveroochoa.p72022sebastianch.InicioAppActivity;
import practica5.sebastiancardonahenao.iesseveroochoa.p72022sebastianch.R;
import practica5.sebastiancardonahenao.iesseveroochoa.p72022sebastianch.model.Mensaje;

public class ChatAdapter extends FirestoreRecyclerAdapter<Mensaje, ChatAdapter.ChatHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public ChatAdapter(@NonNull FirestoreRecyclerOptions<Mensaje> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatHolder holder, int position, @NonNull Mensaje model) {
        holder.setMensaje(model);
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mensaje,parent,false);
        return new ChatHolder(itemView);
    }

    public class ChatHolder extends RecyclerView.ViewHolder {
        LinearLayoutCompat lyMensaje,linearLayoutCompat;
        TextView tvMensaje;
        CardView cvContenedor;
        private Mensaje mensaje;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            cvContenedor = itemView.findViewById(R.id.cvContenedor);
            lyMensaje = itemView.findViewById(R.id.lyMensaje);
            linearLayoutCompat =itemView.findViewById(R.id.llMj);
            tvMensaje = itemView.findViewById(R.id.tvMensajeAEnviar);
        }

        public void setMensaje(Mensaje mensaje) {
            this.mensaje = mensaje;
            verificaLado();
        }

        void verificaLado(){
            tvMensaje.setText(mensaje.getUsuario() + "=>" + mensaje.getBody());
            if(mensaje.usuario.equals(InicioAppActivity.usrFB.getEmail())){
                cvContenedor.setCardBackgroundColor(Color.YELLOW);
                linearLayoutCompat.setGravity(Gravity.RIGHT);
            }else{
                cvContenedor.setCardBackgroundColor(Color.WHITE);
                linearLayoutCompat.setGravity(Gravity.LEFT);
            }
        }
    }
}