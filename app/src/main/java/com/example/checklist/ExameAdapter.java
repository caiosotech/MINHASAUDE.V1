package com.example.checklist;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

        if (exame != null) {
            TextView dateTextView = convertView.findViewById(R.id.dateTextView);
            TextView hospitalTextView = convertView.findViewById(R.id.hospitalTextView);
            TextView exameTextView = convertView.findViewById(R.id.exameTextView);
            TextView medicoTextView = convertView.findViewById(R.id.medicoTextView);
            ImageView favoriteImageView = convertView.findViewById(R.id.favoriteImageView);
            ImageView favoriteRedImageView = convertView.findViewById(R.id.favoriteRedImageView);

            if (dateTextView != null) {
                dateTextView.setText(exame.getData());
            }
            if (hospitalTextView != null) {
                hospitalTextView.setText(exame.getNomeHospital());
            }
            if (exameTextView != null) {
                exameTextView.setText(exame.getDescricaoExame()); // Atualizado para mostrar descrição do exame
            }
            if (medicoTextView != null) {
                medicoTextView.setText(exame.getNomeMedico()); // Atualizado para mostrar nome do médico
            }

            if (favoriteImageView != null) {
                favoriteImageView.setVisibility(View.VISIBLE);
            }
            if (favoriteRedImageView != null) {
                favoriteRedImageView.setVisibility(View.INVISIBLE);
            }

            // Adiciona um log para verificar o valor do anexo
            String anexo = exame.getAnexo();
            Log.d("ExameAdapter", "Anexo para exame na posição " + position + ": " + anexo);

            convertView.setOnClickListener(v -> {
                Intent intent = new Intent(context, VisuExameMedico.class);
                intent.putExtra("nomePaciente", exame.getNomePaciente()); // Atualizado
                intent.putExtra("cpfPaciente", exame.getPacienteCPF()); // Atualizado
                intent.putExtra("descricaoExame", exame.getDescricaoExame()); // Atualizado
                intent.putExtra("nomeHospital", exame.getNomeHospital());
                intent.putExtra("nomeMedico", exame.getNomeMedico()); // Atualizado para o nome do médico
                intent.putExtra("data", exame.getData());
                intent.putExtra("hora", exame.getHora()); // Atualizado
                intent.putExtra("anexo", anexo); // Atualizado
                context.startActivity(intent);
            });
        } else {
            Log.e("ExameAdapter", "Exame at position " + position + " is null");
        }

        return convertView;
    }
}