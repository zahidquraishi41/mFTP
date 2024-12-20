package com.zapp.mFTP;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements SFTPServer.SFTPListener {
    private CardView cvServerInfo;
    private TextView tvServerAddress;
    private TextView tvPassword;
    private Button btnStartServer;
    private SFTPServer sftpServer;
    private TextView tvUsername;
    private Intent notificationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cvServerInfo = findViewById(R.id.cv_server_info);
        tvServerAddress = findViewById(R.id.tv_server_address);
        tvUsername = findViewById(R.id.tv_username);
        tvPassword = findViewById(R.id.tv_password);
        btnStartServer = findViewById(R.id.btn_start_server);
        sftpServer = new SFTPServer(this);
        notificationService = new Intent(this, NotificationService.class);
        btnStartServer.setOnClickListener(v -> {
            if (PermsManager.hasStorage(this)) sftpServer.toggle(this);
            else Toast.makeText(this, "Storage permission not granted.", Toast.LENGTH_SHORT).show();
        });
        cvServerInfo.setVisibility(View.GONE);
        if (!PermsManager.hasStorage(this)) PermsManager.acquireStorage(this);
        if (!PermsManager.isIgnoringBatteryOptimization(this))
            PermsManager.acquireIgnoreBatteryOptimisation(this);
    }


    @Override
    public void onServerStart(String serverAddress, String username, String password) {
        startForegroundService(notificationService);
        tvUsername.setText(username);
        tvPassword.setText(password);
        tvServerAddress.setText(serverAddress);
        cvServerInfo.setVisibility(View.VISIBLE);
        btnStartServer.setText(R.string.stop_server);
    }

    @Override
    public void onServerStop() {
        stopService(notificationService);
        cvServerInfo.setVisibility(View.GONE);
        btnStartServer.setText(R.string.start_server);
    }

    @Override
    public void onServerError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
}