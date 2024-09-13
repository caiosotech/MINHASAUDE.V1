package com.example.checklist;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/*public class ChecklistMain extends AppCompatActivity {

    private ListView listViewTarefas;
    private String currentTab = "ToDo";
    private List<Tarefa> listaTarefas;
    private List<Tarefa> listaTarefasFiltradas; // Lista para armazenar as tarefas filtradas
    private TarefaAdapter adapter;
    private int userId;
    private EditText searchEditText;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        userId = getIntent().getIntExtra("USER_ID", -1);
        Log.d("ChecklistMain", "User ID received: " + userId);

        // Inicializa a lista de tarefas
        listaTarefas = new ArrayList<>();
        listaTarefasFiltradas = new ArrayList<>(); // Inicializa a lista filtrada

        // Inicializa o ListView e o TarefaAdapter
        listViewTarefas = findViewById(R.id.listViewTarefas);
        searchEditText = findViewById(R.id.searchEditText);
        adapter = new TarefaAdapter(this, listaTarefasFiltradas);
        listViewTarefas.setAdapter(adapter);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        listViewTarefas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tarefa tarefaSelecionada = listaTarefasFiltradas.get(position);

                Intent intent = new Intent(ChecklistMain.this, AdicionarTarefaActivity.class);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("TAREFA_ID", tarefaSelecionada.getId());
                intent.putExtra("TITULO", tarefaSelecionada.getTitulo());
                intent.putExtra("SUBTITULO", tarefaSelecionada.getSubtitulo());
                intent.putExtra("DATA", tarefaSelecionada.getData());
                intent.putExtra("HORA", tarefaSelecionada.getHora());
                intent.putExtra("DESCRICAO", tarefaSelecionada.getDescricao());
                intent.putExtra("TAG", tarefaSelecionada.getTag());
                intent.putExtra("FAVORITA", tarefaSelecionada.isFavorited());
                startActivity(intent);
            }
        });

        // Adiciona um listener ao campo de pesquisa
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não necessário implementar
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filtra as tarefas enquanto o texto é digitado
                filtrarTarefas(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Não necessário implementar
            }
        });

        // Configura o clique no ícone Home
        ImageView iconHome = findViewById(R.id.iconHome);
        iconHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Volta para a tela inicial (pode ser a mesma activity ou outra)
                Intent intent = new Intent(ChecklistMain.this, ChecklistMain.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
                finish(); // Opcional: finaliza a atividade atual para evitar múltiplas instâncias
            }
        });

        // Configura o clique no ícone Favorito
        ImageView iconFav = findViewById(R.id.iconFav);
        iconFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Determina a aba atual (ToDo ou Completed) e carrega as tarefas favoritas
                if (currentTab.equals("ToDo")) {
                    carregarTarefasFavoritasDoBancoDeDados("ToDo");
                } else if (currentTab.equals("Completed")) {
                    carregarTarefasFavoritasDoBancoDeDados("Completed");
                }
            }
        });

        // Configura o clique no ícone de calendário
        ImageView iconCalendario = findViewById(R.id.iconCalendario);
        iconCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDateRangePicker();
            }
        });
        // Configura o clique no ícone de perfil
        ImageView iconPerfil = findViewById(R.id.iconPerfil);
        iconPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirPerfil();
            }
        });
        // Carrega as tarefas do banco de dados ao iniciar a atividade
        carregarTarefasDoBancoDeDados("ToDo"); // Carrega as tarefas "ToDo" por padrão
    }

    private void mostrarDateRangePicker() {
        // Remove o construtor de restrições de calendário que limita a seleção de datas para o futuro
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();

        // Cria o builder do DateRangePicker
        MaterialDatePicker.Builder<androidx.core.util.Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select date range");
        builder.setCalendarConstraints(constraintsBuilder.build());

        final MaterialDatePicker<androidx.core.util.Pair<Long, Long>> picker = builder.build();
        picker.show(getSupportFragmentManager(), picker.toString());

        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<androidx.core.util.Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(androidx.core.util.Pair<Long, Long> selection) {
                if (selection != null) {
                    SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String startDate = simpleFormat.format(new Date(selection.first));
                    String endDate = simpleFormat.format(new Date(selection.second));
                    Log.d("ChecklistMain", "Intervalo de datas selecionado: " + startDate + " - " + endDate);
                    filtrarTarefasPorIntervaloDeDatas(startDate, endDate);
                }
            }
        });
    }
    // Método para abrir a atividade de perfil
    private void abrirPerfil() {
        Intent intent = new Intent(this, PerfilActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }
    private void filtrarTarefasPorIntervaloDeDatas(String startDate, String endDate) {
        // Método para filtrar as tarefas pelo intervalo de datas selecionado
        listaTarefasFiltradas.clear();

        // Defina o formato da data para as datas de início e fim
        SimpleDateFormat intervalDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        // Defina o formato da data para as datas das tarefas
        SimpleDateFormat taskDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Date start;
        Date end;
        try {
            start = intervalDateFormat.parse(startDate);
            end = intervalDateFormat.parse(endDate);
            Log.d("ChecklistMain", "Data inicial analisada: " + start);
            Log.d("ChecklistMain", "Data final analisada: " + end);

            for (Tarefa tarefa : listaTarefas) {
                String tarefaData = tarefa.getData();
                Log.d("ChecklistMain", "Data da tarefa em String: " + tarefaData);

                Date taskDate;
                try {
                    taskDate = taskDateFormat.parse(tarefaData);
                    Log.d("ChecklistMain", "Data da tarefa: " + taskDate);

                    if (taskDate != null && !taskDate.before(start) && !taskDate.after(end)) {
                        listaTarefasFiltradas.add(tarefa);
                        Log.d("ChecklistMain", "Tarefa adicionada: " + tarefa.getTitulo());
                    }
                } catch (ParseException e) {
                    Log.e("ChecklistMain", "Erro ao analisar data da tarefa: " + tarefaData, e);
                }
            }
        } catch (ParseException e) {
            Log.e("ChecklistMain", "Erro ao analisar datas de início ou fim", e);
        }

        adapter.notifyDataSetChanged();

        // Define a visibilidade do ListView com base na presença de tarefas
        listViewTarefas.setVisibility(listaTarefasFiltradas.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void carregarTarefasFavoritasDoBancoDeDados(String tipo) {
        // Método para carregar as tarefas favoritas do banco de dados com base no tipo (ToDo ou Completed)
        TarefaDAO tarefaDAO = new TarefaDAO(this);

        // Limpa a lista de tarefas
        listaTarefas.clear();

        // Obtém as tarefas favoritas do banco de dados com base no tipo
        if (tipo.equals("ToDo")) {
            // Carrega as tarefas "ToDo" que também são favoritas
            listaTarefas.addAll(tarefaDAO.obterTarefasNaoFinalizadasFavoritasPorUsuario(userId));
        } else if (tipo.equals("Completed")) {
            // Carrega as tarefas "Completed" que também são favoritas
            listaTarefas.addAll(tarefaDAO.obterTarefasFinalizadasFavoritasPorUsuario(userId));
        }

        // Atualiza a lista filtrada com as tarefas favoritas
        listaTarefasFiltradas.clear();
        listaTarefasFiltradas.addAll(listaTarefas);

        // Notifica o adapter sobre as mudanças nos dados
        adapter.notifyDataSetChanged();

        // Define a visibilidade do ListView com base na presença de tarefas
        listViewTarefas.setVisibility(listaTarefasFiltradas.isEmpty() ? View.GONE : View.VISIBLE);
    }

    public void showToDoTasks(View view) {
        currentTab = "ToDo"; // Atualiza a aba atual para "ToDo"
        carregarTarefasDoBancoDeDados("ToDo");
    }

    public void showCompletedTasks(View view) {
        currentTab = "Completed"; // Atualiza a aba atual para "Completed"
        carregarTarefasDoBancoDeDados("Completed");
    }

    private void carregarTarefasDoBancoDeDados(String tipo) {
        // Método para carregar as tarefas do banco de dados com base no tipo (ToDo ou Completed)
        TarefaDAO tarefaDAO = new TarefaDAO(this);

        // Limpa a lista de tarefas
        listaTarefas.clear();

        // Obtém as tarefas do banco de dados com base no tipo
        if (tipo.equals("ToDo")) {
            // Carrega as tarefas ToDo que não são favoritas
            listaTarefas.addAll(tarefaDAO.obterTarefasNaoFinalizadasPorUsuario(userId));
        } else if (tipo.equals("Completed")) {
            // Carrega as tarefas "Completed" que não são favoritas
            listaTarefas.addAll(tarefaDAO.obterTarefasFinalizadasPorUsuario(userId));
        }

        // Atualiza a lista filtrada com as tarefas não favoritas
        listaTarefasFiltradas.clear();
        listaTarefasFiltradas.addAll(listaTarefas);

        // Notifica o adapter sobre as mudanças nos dados
        adapter.notifyDataSetChanged();

        // Define a visibilidade do ListView com base na presença de tarefas
        listViewTarefas.setVisibility(listaTarefasFiltradas.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void filtrarTarefas(String texto) {
        // Método para filtrar as tarefas pelo título
        listaTarefasFiltradas.clear();
        for (Tarefa tarefa : listaTarefas) {
            if (tarefa.getTitulo().toLowerCase().contains(texto.toLowerCase())) {
                listaTarefasFiltradas.add(tarefa);
            }
        }
        adapter.notifyDataSetChanged();

        // Define a visibilidade do ListView com base na presença de tarefas
        listViewTarefas.setVisibility(listaTarefasFiltradas.isEmpty() ? View.GONE : View.VISIBLE);
    }

    public void openAdicionarTarefaActivity(View view) {
        // Iniciar a AdicionarTarefaActivity e passar o ID do usuário como extra
        Intent intent = new Intent(this, AdicionarTarefaActivity.class);
        intent.putExtra("USER_ID", userId); // Passa o ID do usuário para a próxima atividade
        startActivity(intent);
    }

    private void carregarTarefasPorTag() {
        // Método para carregar as tarefas do banco de dados agrupadas por tag
        TarefaDAO tarefaDAO = new TarefaDAO(this);

        // Limpa a lista de tarefas
        listaTarefas.clear();

        // Obtém todas as tags disponíveis no banco de dados
        List<String> tags = tarefaDAO.obterTagsPorUsuario(userId);

        // Para cada tag, obtém as tarefas associadas a ela e as adiciona à lista de tarefas
        for (String tag : tags) {
            // Obtém as tarefas associadas à tag atual
            List<Tarefa> tarefasPorTag = tarefaDAO.obterTarefasPorTag(userId, tag);

            // Adiciona as tarefas à lista de tarefas
            listaTarefas.addAll(tarefasPorTag);
        }

        // Copia a lista original para a lista filtrada
        listaTarefasFiltradas.clear();
        listaTarefasFiltradas.addAll(listaTarefas);

        // Notifica o adapter sobre as mudanças nos dados
        adapter.notifyDataSetChanged();

        // Define a visibilidade do ListView com base na presença de tarefas
        listViewTarefas.setVisibility(listaTarefasFiltradas.isEmpty() ? View.GONE : View.VISIBLE);
    }

}
*/
