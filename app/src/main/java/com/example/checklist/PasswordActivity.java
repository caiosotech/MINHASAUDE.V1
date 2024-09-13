/*package com.example.checklist;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import android.graphics.Color; // Importe a classe Color

public class PasswordActivity extends AppCompatActivity {

    private int userId;
    private ChecklistDao checklistDao;
    private static final String TAG = "PasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        // Inicializando o DAO
        checklistDao = ChecklistDatabase.getInstance(this).checklistDao();

        // Obtendo o ID do usuário passado pela tela anterior
        userId = getIntent().getIntExtra("USER_ID", -1);

        // Log para verificar se o ID do usuário está sendo passado corretamente
        Log.d(TAG, "User ID: " + userId);

        // Referenciando elementos do layout
        ImageView botaoVoltarHome = findViewById(R.id.buttonVoltarHome);
        EditText currentPasswordEditText = findViewById(R.id.editTextCurrentPassword);
        EditText newPasswordEditText = findViewById(R.id.editTextNewPassword);
        EditText confirmNewPasswordEditText = findViewById(R.id.editTextConfirmNewPassword);
        RelativeLayout botaoChangePassword = findViewById(R.id.buttonChangePassword);
        TextView messageTextView = findViewById(R.id.textViewMessage);

        // Adicionando OnClickListener ao botão de voltar
        botaoVoltarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Voltando para a tela de perfil e passando o ID do usuário de volta
                Intent intent = new Intent(PasswordActivity.this, PerfilActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
                finish(); // Finalizando a atividade atual para evitar empilhamento desnecessário
            }
        });

        botaoChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword = currentPasswordEditText.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();
                String confirmNewPassword = confirmNewPasswordEditText.getText().toString();

                // Validar os campos de entrada
                if (TextUtils.isEmpty(currentPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmNewPassword)) {
                    messageTextView.setText("Please fill in all fields.");
                    return;
                }

                if (!newPassword.equals(confirmNewPassword)) {
                    messageTextView.setText("New passwords do not match.");
                    return;
                }

                // Verificar a senha atual e alterar para a nova senha
                LiveData<User> userLiveData = checklistDao.getUserById(userId);
                userLiveData.observe(PasswordActivity.this, new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        if (user != null) {
                            Log.d(TAG, "User found: " + user.getId());
                            if (user.getPassword().equals(currentPassword)) {
                                // Atualizar a senha no objeto do usuário
                                user.setPassword(newPassword);

                                // Atualizar o usuário no banco de dados em uma thread separada
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        checklistDao.updatePassword(user);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                messageTextView.setText("Password changed successfully.");
                                                messageTextView.setTextColor(Color.GREEN); // Define a cor do texto para verde
                                            }
                                        });
                                    }
                                }).start();
                            } else {
                                messageTextView.setText("Current password is incorrect.");
                                messageTextView.setTextColor(Color.RED); // Define a cor do texto para vermelho
                            }
                        } else {
                            messageTextView.setText("User not found.");
                            messageTextView.setTextColor(Color.RED); // Define a cor do texto para vermelho
                            Log.d(TAG, "User is null");
                        }
                        // Remover o Observer após obter o valor
                        userLiveData.removeObserver(this);
                    }
                });
            }
        });
    }
}
*/