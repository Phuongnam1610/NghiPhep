package com.game.nghiphep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.game.nghiphep.databinding.HomePageBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HomePageBinding binding = HomePageBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        binding.btnQuanLy.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("KEY_USER", 1);
            startActivity(intent);
            finish();
        });
        binding.btnNhanVien.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("KEY_USER", 0);
            startActivity(intent);
            finish();
        });
        setContentView(binding.getRoot());
    }
}