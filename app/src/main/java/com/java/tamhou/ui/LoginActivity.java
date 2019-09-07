package com.java.tamhou.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.java.tamhou.MainActivity;
import com.java.tamhou.R;
import com.java.tamhou.ui.storage.AccountCloud;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.input_account)
    EditText usernameText;
    @BindView(R.id.input_password)
    EditText passwordText;
    @BindView(R.id.btn_login)
    Button loginButton;
    @BindView(R.id.btn_newUser)
    Button newUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();
                if (username.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please type in your username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please type in your password", Toast.LENGTH_SHORT).show();
                    return;
                }
//                System.out.println(username);
//                System.out.println(password);
//

                if(AccountCloud.load(username, password, getApplicationContext()))
                {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    bundle.putString("password", password);
                    intent.putExtra("account_bundle", bundle);
                    startActivity(intent);
                    finish();
                    Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(LoginActivity.this, "Login fail", Toast.LENGTH_SHORT).show();
            }
        });
        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();
                AccountCloud.create(username, password);
                // newUser(username, password);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("password", password);
                intent.putExtra("account_bundle", bundle);
                startActivity(intent);
                finish();
                Toast.makeText(LoginActivity.this, "Welcome to join! ", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
