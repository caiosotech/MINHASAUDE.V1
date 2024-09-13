/*package com.example.checklist;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TarefaAdapter extends BaseAdapter {

    private List<Tarefa> tarefas;
    private Context context;

    public TarefaAdapter(Context context, List<Tarefa> tarefas) {
        this.context = context;
        this.tarefas = tarefas;
    }

    @Override
    public int getCount() {
        return tarefas.size();
    }

    @Override
    public Object getItem(int position) {
        return tarefas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_tarefa, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = convertView.findViewById(R.id.titleTextView);
            holder.subtitleTextView = convertView.findViewById(R.id.subtitleTextView);
            holder.dateTextView = convertView.findViewById(R.id.dateTextView);
            holder.deadlineTextView = convertView.findViewById(R.id.deadlineTextView);
            // holder.tagsTextView = convertView.findViewById(R.id.tagsTextView);
            holder.backgroundImageView = convertView.findViewById(R.id.backgroundImageView);
            // holder.tagsBackground = convertView.findViewById(R.id.tagsBackground);
            holder.favoriteImageView = convertView.findViewById(R.id.favoriteImageView);
            holder.favoriteRedImageView = convertView.findViewById(R.id.favoriteRedImageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Tarefa tarefa = tarefas.get(position);
        holder.titleTextView.setText(tarefa.getTitulo());
        holder.subtitleTextView.setText(tarefa.getSubtitulo());
        holder.dateTextView.setText("Date: " + formatDate(tarefa.getData()));
        holder.deadlineTextView.setText("Hour: " + formatTime(tarefa.getHora()));
        holder.tagsTextView.setText(tarefa.getTag());

        // Verifica se a tarefa está favoritada e configura a visibilidade dos ícones
        if (tarefa.isFavorited()) {
            holder.favoriteImageView.setVisibility(View.GONE);
            holder.favoriteRedImageView.setVisibility(View.VISIBLE);
        } else {
            holder.favoriteImageView.setVisibility(View.VISIBLE);
            holder.favoriteRedImageView.setVisibility(View.GONE);
        }


        // Logs para depuração
        Log.d("TarefaAdapter", "Data da tarefa: " + tarefa.getData());
        Log.d("TarefaAdapter", "Hora da tarefa: " + tarefa.getHora());

        // Calcula o tempo restante e atualiza a cor de fundo e dos textos
        long currentTime = System.currentTimeMillis();
        long deadlineTime = convertToMillis(tarefa.getData(), tarefa.getHora());

        long timeRemaining = deadlineTime - currentTime;
        long twelveHoursInMillis = 12 * 60 * 60 * 1000;
        long sixHoursInMillis = 6 * 60 * 60 * 1000;

        // Logs para depuração
        Log.d("TarefaAdapter", "Tempo restante em milissegundos: " + timeRemaining);

        if (timeRemaining <= 0) {
            // Se o prazo já passou, deixe vermelho
            Log.d("TarefaAdapter", "Tempo restante menor ou igual a zero, vermelho");
            holder.backgroundImageView.setImageResource(R.drawable.retangulobackgroundred);
            holder.tagsBackground.setBackgroundResource(R.drawable.rounded_rectanglered);
            holder.titleTextView.setTextColor(Color.parseColor("#E50000"));
            holder.subtitleTextView.setTextColor(Color.parseColor("#E50000"));
        } else if (timeRemaining <= sixHoursInMillis) {
            // Se faltar menos de 6 horas, deixe vermelho
            Log.d("TarefaAdapter", "Tempo restante menor ou igual a seis horas, vermelho");
            holder.backgroundImageView.setImageResource(R.drawable.retangulobackgroundred);
            holder.tagsBackground.setBackgroundResource(R.drawable.rounded_rectanglered);
            holder.titleTextView.setTextColor(Color.parseColor("#E50000"));
            holder.subtitleTextView.setTextColor(Color.parseColor("#E50000"));
        } else if (timeRemaining <= twelveHoursInMillis) {
            // Se faltar entre 6 e 12 horas, deixe amarelo
            Log.d("TarefaAdapter", "Tempo restante entre seis e doze horas, amarelo");
            holder.backgroundImageView.setImageResource(R.drawable.retangulobackgroundyellow);
            holder.tagsBackground.setBackgroundResource(R.drawable.rounded_rectangleyellow);
            holder.titleTextView.setTextColor(Color.parseColor("#E37B00"));
            holder.subtitleTextView.setTextColor(Color.parseColor("#E37B00"));
        } else {
            // Caso contrário, deixe verde
            Log.d("TarefaAdapter", "Tempo restante maior que doze horas, verde");
            holder.backgroundImageView.setImageResource(R.drawable.retangulobackgroundgreen);
            holder.tagsBackground.setBackgroundResource(R.drawable.rounded_rectanglegreen);
            holder.titleTextView.setTextColor(Color.parseColor("#4FA983"));
            holder.subtitleTextView.setTextColor(Color.parseColor("#4FA983"));
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView backgroundImageView;
        TextView titleTextView;
        TextView subtitleTextView;
        TextView dateTextView;
        TextView deadlineTextView;
        TextView tagsTextView;
        View tagsBackground;
        ImageView favoriteImageView;
        ImageView favoriteRedImageView;
    }


    private long convertToMillis(String date, String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        try {
            Date parsedDate = dateFormat.parse(date + " " + time);
            return parsedDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String formatDate(String date) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat targetFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        try {
            Date originalDate = originalFormat.parse(date);
            return targetFormat.format(originalDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private String formatTime(String time) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat targetFormat = new SimpleDateFormat("HH:mm 'hrs'", Locale.getDefault());
        try {
            Date originalTime = originalFormat.parse(time);
            return targetFormat.format(originalTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
}
*/