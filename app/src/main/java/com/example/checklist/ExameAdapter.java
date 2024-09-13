package com.example.checklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ExameAdapter extends ArrayAdapter<Exame> {

    private Context context;
    private List<Exame> exames;

    public ExameAdapter(Context context, List<Exame> exames) {
        super(context, 0, exames);
        this.context = context;
        this.exames = exames;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_exame, parent, false);
        }

        Exame exame = exames.get(position);

        TextView dateTextView = convertView.findViewById(R.id.dateTextView);
        TextView hospitalTextView = convertView.findViewById(R.id.hospitalTextView);
        TextView exameTextView = convertView.findViewById(R.id.exameTextView);
        TextView medicoTextView = convertView.findViewById(R.id.medicoTextView);
        ImageView favoriteImageView = convertView.findViewById(R.id.favoriteImageView);
        ImageView favoriteRedImageView = convertView.findViewById(R.id.favoriteRedImageView);

        // Definindo os valores dos TextViews com base nos métodos da classe Exame
        dateTextView.setText(exame.getData());
        hospitalTextView.setText(exame.getNomeHospital());
        exameTextView.setText(exame.getNomeExame());
        medicoTextView.setText(exame.getMedicoEmail()); // Ajusta para mostrar o email do médico

        // Lógica de visibilidade dos ícones de favoritos
        favoriteImageView.setVisibility(View.VISIBLE);
        favoriteRedImageView.setVisibility(View.INVISIBLE);

        return convertView;
    }
}
