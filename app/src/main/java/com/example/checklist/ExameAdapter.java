package com.example.checklist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.checklist.Exame;

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

        // Verificar se exame não é null
        if (exame != null) {
            TextView dateTextView = convertView.findViewById(R.id.dateTextView);
            TextView hospitalTextView = convertView.findViewById(R.id.hospitalTextView);
            TextView exameTextView = convertView.findViewById(R.id.exameTextView);
            TextView medicoTextView = convertView.findViewById(R.id.medicoTextView);
            ImageView favoriteImageView = convertView.findViewById(R.id.favoriteImageView);
            ImageView favoriteRedImageView = convertView.findViewById(R.id.favoriteRedImageView);

            // Definindo os valores dos TextViews com base nos métodos da classe Exame
            if (dateTextView != null) {
                dateTextView.setText(exame.getData());
            }
            if (hospitalTextView != null) {
                hospitalTextView.setText(exame.getNomeHospital());
            }
            if (exameTextView != null) {
                exameTextView.setText(exame.getNomeExame());
            }
            if (medicoTextView != null) {
                medicoTextView.setText(exame.getMedicoEmail());
            }

            // Lógica de visibilidade dos ícones de favoritos
            if (favoriteImageView != null) {
                favoriteImageView.setVisibility(View.VISIBLE);
            }
            if (favoriteRedImageView != null) {
                favoriteRedImageView.setVisibility(View.INVISIBLE);
            }
        } else {
            Log.e("ExameAdapter", "Exame at position " + position + " is null");
        }

        return convertView;
    }
}