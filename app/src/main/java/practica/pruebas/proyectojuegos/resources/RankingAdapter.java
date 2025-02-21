package practica.pruebas.proyectojuegos.resources;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import practica.pruebas.proyectojuegos.R;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ViewHolder> {

    private List<RankingItem> rankingList;

    public RankingAdapter(List<RankingItem> rankingList) {
        this.rankingList = rankingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ranking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RankingItem item = rankingList.get(position);
        holder.textPosicion.setText(String.valueOf(item.getPosicion()));
        holder.textNombre.setText(item.getNombre());
        holder.textPuntuacion.setText(String.valueOf(item.getPuntuacion()));
    }

    @Override
    public int getItemCount() {
        return rankingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textPosicion, textNombre, textPuntuacion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textPosicion = itemView.findViewById(R.id.textPosicion);
            textNombre = itemView.findViewById(R.id.textNombre);
            textPuntuacion = itemView.findViewById(R.id.textPuntuacion);
        }
    }
}
