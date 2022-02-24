package es.ikergarciagutierrez.accdat.flora.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.ikergarciagutierrez.accdat.flora.R;
import es.ikergarciagutierrez.accdat.flora.model.entity.Flora;
import es.ikergarciagutierrez.accdat.flora.model.entity.Imagen;
import es.ikergarciagutierrez.accdat.flora.view.adapter.viewholder.FloraViewHolder;
import es.ikergarciagutierrez.accdat.flora.viewmodel.AddFloraViewModel;
import es.ikergarciagutierrez.accdat.flora.viewmodel.MainActivityViewModel;

public class Adapter extends RecyclerView.Adapter<FloraViewHolder> {

    private Context context;

    private List<Flora> floraList;
    private List<Imagen> imagenList;

    private View.OnClickListener listener;

    public Adapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public FloraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flora, parent, false);
        return new FloraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FloraViewHolder holder, int position) {

        Flora flora = floraList.get(position);
        Imagen imagen = imagenList.get(position);

        holder.etFloraNombre.setText(flora.getNombre());
        holder.etFloraFamilia.setText(flora.getFamilia());
        // holder.ivFlora.setImageResource(Integer.parseInt(imagen.getNombre()));
    }

    @Override
    public int getItemCount() {
        if(floraList == null) {
            return 0;
        }
        return floraList.size();
    }

    public void setFloraList(List<Flora> floraList) {
        this.floraList = floraList;
        notifyDataSetChanged();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
    
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }
}
