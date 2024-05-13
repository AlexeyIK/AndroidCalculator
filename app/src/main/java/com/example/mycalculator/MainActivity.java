package com.example.mycalculator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import kotlin.text.Regex;

public class MainActivity extends AppCompatActivity {

    private EditText textField;
    private Double leftValue;
    private Double rightValue;
    private OperationType operationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textField = findViewById(R.id.textLine);
        textField.setShowSoftInputOnFocus(false);
        textField.setCursorVisible(false);
    }

    public void onClearButtonClick(View view) {
        textField.setText("");
    }

    public void onDigitBtnClick(View view) {
        if (view instanceof Button) {
            String btnText = ((Button) view).getText().toString();

            try {
                int digit = Integer.parseInt(btnText);
                String text = textField.getText().toString();
                text += digit;
                textField.setText(text);
            }
            catch (NumberFormatException e) {
                System.out.println("Pressed not a number");
            }
        }
    }

    public void onDeleteBtnClick(View view) {
        if (textField.getText().length() > 0) {
            String text = textField.getText().toString();
            text = text.substring(0, text.length() - 1);
            textField.setText(text);
        }
    }

    public void onAddBtnClick(View view) {
        Double value = ParseValue(textField.getText().toString());

        if (leftValue == null) {
            leftValue = value;
            operationType = OperationType.Addition;
            SetText("+ ");
        }
        else if (value != null) {
            rightValue = value;
            double answer = leftValue - rightValue;
            SetText(String.valueOf(answer));
            leftValue = answer;
        }
    }

    public void onSubtractBtnClick(View view) {
        Double value = ParseValue(textField.getText().toString());

        if (leftValue == null) {
            leftValue = value;
            operationType = OperationType.Subtraction;
            SetText("– ");
        }
        else if (value != null)  {
            rightValue = value;
            double answer = leftValue - rightValue;
            SetText(String.valueOf(answer));
            leftValue = answer;
        }
    }

    public void onMultiplyBtnClick(View view) {

    }

    public void onDivideBtnClick(View view) {

    }

    @SuppressLint("DefaultLocale")
    public void onEqualsBtnClick(View view) {
        if (leftValue == null)
            return;

        rightValue = ParseValue(textField.getText().toString());

        if (rightValue == null)
            return;

        switch (operationType) {
            case Addition:
                SetText(String.format("%.0f", leftValue + rightValue));
                break;

            case Subtraction:
                SetText(String.format("%.0f", leftValue - rightValue));
                break;

            case Dividing:
                SetText(String.format("%.0f", leftValue / rightValue));
                break;

            case Multiplying:
                SetText(String.format("%.0f", leftValue * rightValue));
                break;
        }

        operationType = null;
    }

    private Double ParseValue(String text) {
        try {
            return Double.parseDouble(text.replaceAll("\\D+", ""));
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    private void SetText(String text) {
        textField.setText(text);
    }
}