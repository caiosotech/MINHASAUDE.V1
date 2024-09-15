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
                dateTextView.setText(exame.getData()); // Data do Exame
            }
            if (hospitalTextView != null) {
                hospitalTextView.setText(exame.getNomeHospital()); // Nome do Hospital
            }
            if (exameTextView != null) {
                exameTextView.setText(exame.getNomeExame()); // Nome do Exame
            }
            if (medicoTextView != null) {
                medicoTextView.setText(exame.getMedicoEmail()); // Nome do Médico
            }

            // Lógica de visibilidade dos ícones de favoritos
            if (favoriteImageView != null) {
                favoriteImageView.setVisibility(View.VISIBLE);
            }
            if (favoriteRedImageView != null) {
                favoriteRedImageView.setVisibility(View.INVISIBLE);
            }

            // Lógica de clique no item
            convertView.setOnClickListener(v -> {
                Intent intent = new Intent(context, VisuExameMedico.class);
                intent.putExtra("nomePaciente", "Nome do Paciente"); // Substitua por dados reais se disponíveis
                intent.putExtra("cpfPaciente", "CPF do Paciente"); // Substitua por dados reais se disponíveis
                intent.putExtra("descricaoExame", exame.getNomeExame()); // Ajuste conforme necessário
                intent.putExtra("nomeHospital", exame.getNomeHospital());
                intent.putExtra("nomeMedico", exame.getMedicoEmail());
                intent.putExtra("data", exame.getData());
                intent.putExtra("hora", "Hora do Exame"); // Substitua por dados reais se disponíveis
                intent.putExtra("anexo", "Nome do Arquivo"); // Substitua por dados reais se disponíveis
                context.startActivity(intent);
            });
        } else {
            Log.e("ExameAdapter", "Exame at position " + position + " is null");
        }

        return convertView;
    }
}