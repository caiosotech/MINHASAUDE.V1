/*package com.example.checklist;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AdicionarTarefaActivity extends AppCompatActivity {

    private EditText tituloEditText, subtituloEditText, descricaoEditText;
    private EditText dataEditText, horaEditText;
    private Button cancelarButton, salvarButton, deletarButton;
    private ImageView imagemIconFinalizar;
    private ImageView estrelaImageView;
    private boolean isFavorited = false; // Variável para controlar o estado de favoritar
    private int userId;
    private Intent intent;
    private boolean tarefaAdicionada = false; // Variável para controlar se a tarefa foi adicionada
    private EditText tagsEditText;
    private String[] opcoesTags = {"Urgent", "Important", "Personal", "Work", "Study", "Health", "Leisure", "Financial", "Travel", "Social", "Projects", "Shopping", "Food", "Cleaning", "Exercise", "Reminder", "Birthday", "Documents", "Technology", "Creative"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);
        userId = getIntent().getIntExtra("USER_ID", -1);
        intent = getIntent();

        tagsEditText = findViewById(R.id.tagsEditText);
        tituloEditText = findViewById(R.id.tituloEditText);
        subtituloEditText = findViewById(R.id.subtituloEditText);
        descricaoEditText = findViewById(R.id.descricaoEditText);
        dataEditText = findViewById(R.id.dataEditText);
        horaEditText = findViewById(R.id.horaEditText);
        cancelarButton = findViewById(R.id.cancelarButton);
        salvarButton = findViewById(R.id.salvarButton);
        deletarButton = findViewById(R.id.deletarButton);
        imagemIconFinalizar = findViewById(R.id.imagemIconFinalizar);
        estrelaImageView = findViewById(R.id.estrelaImageView);

        // Configura um OnClickListener para exibir o diálogo de lista quando clicado
        tagsEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exibirDialogoListaTags();
            }
        });

        // Configura o OnClickListener para alternar o estado de favoritar
        estrelaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorite();
            }
        });

        // Verifique se a tarefa foi adicionada
        // Por exemplo, se você estiver recuperando essa informação de algum lugar, como um banco de dados
        if (tarefaAdicionada || intent.hasExtra("TAREFA_ID")) {
            imagemIconFinalizar.setVisibility(View.VISIBLE);
            // Adiciona o OnClickListener para o botão de finalizar apenas se a tarefa foi adicionada
            imagemIconFinalizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalizarAtividade();
                }
            });
        } else {
            imagemIconFinalizar.setVisibility(View.GONE);
        }
        cancelarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelarAdicaoTarefa();
            }
        });

        salvarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarTarefa();
            }
        });

        dataEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatePicker();
            }
        });

        horaEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarTimePicker();
            }
        });

        if (intent.hasExtra("TAREFA_ID")) {
            deletarButton.setVisibility(View.VISIBLE);
            deletarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletarTarefa();
                }
            });
        } else {
            deletarButton.setVisibility(View.INVISIBLE);
        }

        preencherCamposComDadosDaTarefa();
    }

    private void mostrarDatePicker() {
        final Calendar calendario = Calendar.getInstance();
        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dataSelecionada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        dataEditText.setText(dataSelecionada);
                    }
                }, ano, mes, dia);
        datePickerDialog.show();
    }

    private void mostrarTimePicker() {
        final Calendar calendario = Calendar.getInstance();
        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int minuto = calendario.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String horaFormatada = String.format("%02d", hourOfDay);
                        String minutoFormatado = String.format("%02d", minute);
                        String horaSelecionada = horaFormatada + ":" + minutoFormatado;
                        horaEditText.setText(horaSelecionada);
                    }
                }, hora, minuto, true);
        timePickerDialog.show();
    }

    private void preencherCamposComDadosDaTarefa() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("TITULO") && intent.hasExtra("SUBTITULO")
                && intent.hasExtra("DATA") && intent.hasExtra("HORA") && intent.hasExtra("DESCRICAO")) {
            tituloEditText.setText(intent.getStringExtra("TITULO"));
            subtituloEditText.setText(intent.getStringExtra("SUBTITULO"));
            dataEditText.setText(intent.getStringExtra("DATA"));
            horaEditText.setText(intent.getStringExtra("HORA"));
            descricaoEditText.setText(intent.getStringExtra("DESCRICAO"));
            tagsEditText.setText(intent.getStringExtra("TAG"));

            // Recupere o estado de favorito do banco de dados ou do objeto Tarefa
            boolean favorita = intent.getBooleanExtra("FAVORITA", false);

            // Atualize isFavorited com base no estado recuperado
            isFavorited = favorita;

            atualizarImagemEstrela();
            Log.d("RecuperarTag", "Tag: " + intent.getStringExtra("TAG"));
            Log.d("SalvarTarefa", "isFavorited antes de criar a tarefa: " + isFavorited); // Adiciona um log para verificar isFavorited antes de criar a tarefa

        }
    }


    private void exibirDialogoListaTags() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione uma Tag");

        // Adicione as opções de tags ao diálogo de lista
        builder.setItems(opcoesTags, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Obtém a tag selecionada pelo usuário
                String tagSelecionada = opcoesTags[which];

                // Insira a tag selecionada no EditText
                tagsEditText.setText(tagSelecionada);
            }
        });

        // Cria e exibe o diálogo de lista
        builder.create().show();
    }

    private void salvarTarefa() {
        String titulo = tituloEditText.getText().toString();
        String subtitulo = subtituloEditText.getText().toString();
        String data = dataEditText.getText().toString();
        String hora = horaEditText.getText().toString();
        String descricao = descricaoEditText.getText().toString();
        String tag = tagsEditText.getText().toString();

        Log.d("SalvarTarefa", "Tag antes de salvar: " + tag); // Adiciona um log para verificar a tag antes de salvar

        if (titulo.isEmpty() || subtitulo.isEmpty() || data.isEmpty() || hora.isEmpty() || descricao.isEmpty() || tag.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        Tarefa tarefa = new Tarefa(titulo, subtitulo, data, hora, descricao, tag, isFavorited);

        TarefaDAO tarefaDAO = new TarefaDAO(this);
        if (!intent.hasExtra("TAREFA_ID")) {
            long resultado = tarefaDAO.inserirTarefa(tarefa, userId);
            if (resultado != -1) {
                Toast.makeText(this, "Tarefa salva com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("AdicionarTarefaActivity", "Erro ao salvar a tarefa no banco de dados");
                Toast.makeText(this, "Falha ao salvar a tarefa", Toast.LENGTH_SHORT).show();
            }
        } else {
            int tarefaId = intent.getIntExtra("TAREFA_ID", -1);
            if (tarefaId != -1) {
                tarefa.setId(tarefaId);
                int linhasAfetadas = tarefaDAO.atualizarTarefa(tarefa);
                if (linhasAfetadas > 0) {
                    Toast.makeText(this, "Tarefa atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Falha ao atualizar a tarefa", Toast.LENGTH_SHORT).show();
                }
            }
        }
        Log.d("SalvarTarefa", "isFavorited antes de criar a tarefa: " + isFavorited); // Adiciona um log para verificar isFavorited antes de criar a tarefa

        Intent intent = new Intent(this, ChecklistMain.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    private void cancelarAdicaoTarefa() {
        Intent intent = new Intent(this, ChecklistMain.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
        finish();
    }

    private void deletarTarefa() {
        int tarefaId = intent.getIntExtra("TAREFA_ID", -1);
        if (tarefaId != -1) {
            TarefaDAO tarefaDAO = new TarefaDAO(this);
            int linhasAfetadas = tarefaDAO.deletarTarefa(tarefaId);
            if (linhasAfetadas > 0) {
                Toast.makeText(this, "Tarefa deletada com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Falha ao deletar a tarefa", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(this, ChecklistMain.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
            finish();
        }
    }

    private void finalizarAtividade() {
        int tarefaId = intent.getIntExtra("TAREFA_ID", -1);
        if (tarefaId != -1) {
            Tarefa tarefa = new Tarefa();
            tarefa.setId(tarefaId);
            TarefaDAO tarefaDAO = new TarefaDAO(this);
            int linhasAfetadas = tarefaDAO.marcarComoFinalizada(tarefa);
            if (linhasAfetadas > 0) {
                Toast.makeText(this, "Tarefa finalizada com sucesso!", Toast.LENGTH_SHORT).show();
                Intent updateIntent = new Intent(this, ChecklistMain.class);
                updateIntent.putExtra("USER_ID", userId);
                startActivity(updateIntent);
                finish();
            } else {
                Toast.makeText(this, "Erro ao finalizar a tarefa", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "ID da tarefa inválido", Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleFavorite() {
        isFavorited = !isFavorited;
        atualizarImagemEstrela();
        Log.d("ToggleFavorite", "Novo estado de isFavorited: " + isFavorited);

    }

    private void atualizarImagemEstrela() {
        if (isFavorited) {
            estrelaImageView.setImageResource(R.drawable.iconfavred);
        } else {
            estrelaImageView.setImageResource(R.drawable.iconfav);
        }
    }
}
*/