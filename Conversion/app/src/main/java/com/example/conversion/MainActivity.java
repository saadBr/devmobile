package com.example.conversion;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText editTextAmount;
    private Button btnCompute;
    private TextView textViewResult;
    private ListView listViewResults;
    private ArrayList<String> data;
    private ArrayAdapter<String> stringArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextAmount = findViewById(R.id.editTextAmount);
        btnCompute = findViewById(R.id.btnCompute);
        textViewResult = findViewById(R.id.textViewResult);
        listViewResults = findViewById(R.id.listViewResults);
        data = new ArrayList<>();
        stringArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, data);
        listViewResults.setAdapter(stringArrayAdapter);
        btnCompute.setOnClickListener(v -> {
            double amount = Double.parseDouble(editTextAmount.getText().toString());
            double result = amount * 10.43;
            textViewResult.setText(String.valueOf(result));
            data.add(amount + " Euro => " + result+" MAD");
            stringArrayAdapter.notifyDataSetChanged();
            editTextAmount.setText("");
        });
        }
}