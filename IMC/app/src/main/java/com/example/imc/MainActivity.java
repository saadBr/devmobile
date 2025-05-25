package com.example.imc;

import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText editTextPoids;
    private EditText editTextTaille;
    private Button btnCalculer;
    private TextView textLabelIMC;
    private TextView textViewDesc;
    private ImageView imageViewResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextPoids = findViewById(R.id.editTextPoids);
        editTextTaille = findViewById(R.id.editTextTaille);
        btnCalculer = findViewById(R.id.btnIMC);
        textLabelIMC = findViewById(R.id.textLabelIMC);
        textViewDesc = findViewById(R.id.textViewCategory);
        imageViewResult = findViewById(R.id.imageViewResult);
        btnCalculer.setOnClickListener(v -> {
            double imc = 0;
            double poids,taille;
            try {
                poids = Double.parseDouble(editTextPoids.getText().toString());
                taille = Double.parseDouble(editTextTaille.getText().toString())/100;
            }catch (NumberFormatException exception){
                exception.printStackTrace();
                return;
            }
            imc = (double) poids / (taille*taille);
            textLabelIMC.setText(String.format("Votre IMC est : %.2f", imc));
            if (imc<18.5)
            {
                textViewDesc.setText("Maigreur");
                imageViewResult.setImageResource(R.drawable.maigre);
            } else if (imc>=18.5 && imc <25) {
                textViewDesc.setText("Normal");
                imageViewResult.setImageResource(R.drawable.normal);
            } else if (imc>=25 && imc <30) {
                textViewDesc.setText("Surpoids");
                imageViewResult.setImageResource(R.drawable.surpoids);
            }
            else if (imc>=30 && imc <40) {
                textViewDesc.setText("Obésité modérée");
                imageViewResult.setImageResource(R.drawable.obese);
            }
            else {
                textViewDesc.setText("Obésité sévère");
                imageViewResult.setImageResource(R.drawable.t_obese);
            }
            textViewDesc.setVisibility(VISIBLE);
            imageViewResult.setVisibility(VISIBLE);
        });
    }
}