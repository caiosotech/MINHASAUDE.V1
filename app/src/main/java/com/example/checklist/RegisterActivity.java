/*package com.example.checklist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private CheckBox termsCheckBox;
    private RelativeLayout registerButton;
    private ImageView buttonVoltarRegister;
    private TextView termsOfUseTextView;  // Novo TextView para os termos de uso

    private ChecklistDao checklistDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_paciente);

        // Inicializar as views
        usernameEditText = findViewById(R.id.usernameEditText);
        // emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        // confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        termsCheckBox = findViewById(R.id.termsOfUseCheckBox);
        registerButton = findViewById(R.id.registerButton);
        buttonVoltarRegister = findViewById(R.id.buttonVoltarRegister);
        termsOfUseTextView = findViewById(R.id.termsOfUseTextView);  // Inicializar o novo TextView

        // Inicializar o ChecklistDao
        checklistDao = ChecklistDatabase.getInstance(this).checklistDao();

        // Configurar o clique do botão de registro
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obter os dados inseridos pelo usuário
                String username = usernameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                boolean termsAccepted = termsCheckBox.isChecked();

                // Verificar se os campos estão vazios
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) ||
                        TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verificar se a senha e a confirmação de senha correspondem
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verificar se o usuário aceitou os termos de uso
                if (!termsAccepted) {
                    Toast.makeText(RegisterActivity.this, "Please accept the Terms of Use", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Executar a operação de registro em uma thread separada
                new RegisterAsyncTask().execute(username, email, password);
            }
        });

        // Configurar o clique do botão de voltar
        buttonVoltarRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Voltar para a tela de login
                finish();
            }
        });

        // Configurar o clique do TextView dos termos de uso
        termsOfUseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mostrar os termos de uso em um dialog
                showTermsOfUseDialog();
            }
        });
    }

    private void showTermsOfUseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Terms of Use");
        builder.setMessage("1. Acceptance of Terms\n\n" +
                "By creating an account and using the TaskBro app, you agree to comply with and be legally bound by the following terms and conditions below. These Terms of Use apply to all visitors, users and others who access or use the app.\n\n" +
                "2. Use of the Application\n\n" +
                "You agree to use the application only for lawful purposes and in accordance with the Terms of Use. You are responsible for ensuring that your use of the application complies with all applicable laws, rules and regulations.\n\n" +
                "3. Account Registration\n\n" +
                "To access certain functionality of the Application, you may be required to create an account. You must provide accurate, complete and up-to-date information during registration and maintain the confidentiality of your account and password. You are responsible for all activities that occur under your account.\n\n" +
                "4. User Content\n\n" +
                "You are solely responsible for the content that you upload, post or display on the application. You must not post content that is illegal, offensive, harmful or violates the rights of third parties.\n\n" +
                "5. Intellectual Property\n\n" +
                "The App and its original content, features and functionality are and will remain the exclusive property of the TaskBro App and its licensors. The App is protected by copyright, trademark and other intellectual property laws.\n\n" +
                "6. Privacy\n\n" +
                "Your privacy is important to us. Our Privacy Policy explains how we collect, use and protect your personal information when you use the app. We recommend that you read our Privacy Policy carefully.\n\n" +
                "7. Termination\n\n" +
                "We may terminate or suspend your account immediately, without prior notice or liability, for any reason whatsoever, including without limitation if you breach the Terms of Use. Upon termination, your right to use the App will immediately cease.\n\n" +
                "8. Limitation of Liability\n\n" +
                "To the maximum extent permitted by applicable law, the TaskBro app will not be liable for any indirect, incidental, special, consequential or punitive damages, including without limitation, loss of profits, data, use, goodwill or other intangible losses, resulting of (i) your use of or inability to use the application; (ii) any unauthorized access to or use of our servers and/or any personal information stored therein.\n\n" +
                "9. Changes to Terms\n\n" +
                "We reserve the right to modify or replace these Terms of Use at any time. If a revision is material, we will try to provide at least 30 days' notice before any new terms take effect. What constitutes a material change will be determined at our sole discretion.\n\n" +
                "10. Contact\n\n" +
                "If you have any questions about these Terms of Use, please contact us.");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class RegisterAsyncTask extends AsyncTask<String, Void, Long> {
        @Override
        protected Long doInBackground(String... params) {
            String username = params[0];
            String email = params[1];
            String password = params[2];

            // Verificar se o usuário já existe no banco de dados
            int userCountByUsername = checklistDao.getUserCountByUsername(username);
            if (userCountByUsername > 0) {
                showToastOnUiThread("Username already exists");
                return null;
            }

            // Verificar se o email já existe no banco de dados
            int userCountByEmail = checklistDao.getUserCountByEmail(email);
            if (userCountByEmail > 0) {
                showToastOnUiThread("The email already exists");
                return null;
            }

            // Criar um novo objeto User com os dados inseridos pelo usuário
            User newUser = new User(username, email, password);

            // Inserir o novo usuário no banco de dados usando o DAO correspondente
            long userId = checklistDao.insertUser(newUser);

            // Retornar o ID do novo usuário
            return userId;
        }

        @Override
        protected void onPostExecute(Long userId) {
            if (userId != null && userId != -1L) {
                // Atualizar o ID do usuário com o valor retornado pelo DAO
                User newUser = new User(usernameEditText.getText().toString().trim(),
                        emailEditText.getText().toString().trim(),
                        passwordEditText.getText().toString());
                newUser.setId(userId.intValue());

                // Exibir uma mensagem de sucesso
                showToastOnUiThread("Successful registration");

                // Redirecionar o usuário para a tela de login
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                // Encerrar esta atividade para evitar que o usuário volte para a tela de registro ao pressionar o botão Voltar
                finish();

                // Adicionar logs para verificar se os dados estão sendo inseridos corretamente no banco de dados
                Log.d("RegisterActivity", "Novo usuário registrado:");
                Log.d("RegisterActivity", "ID do usuário: " + newUser.getId());
                Log.d("RegisterActivity", "Nome de usuário: " + newUser.getUsername());
                Log.d("RegisterActivity", "Email: " + newUser.getEmail());
                Log.d("RegisterActivity", "Senha: " + newUser.getPassword());
            }
        }

        private void showToastOnUiThread(final String message) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
*/


