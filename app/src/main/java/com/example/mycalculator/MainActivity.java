package com.example.mycalculator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;
import java.util.Set;

import kotlin.text.Regex;

public class MainActivity extends AppCompatActivity {

    private EditText textField;
    private Double leftValue;
    private Double rightValue;
    private OperationType operationType = null;
    private boolean wasOperationInput = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textField = findViewById(R.id.textLine);
        textField.setShowSoftInputOnFocus(false);
//        textField.setCursorVisible(false);
    }

    public void onClearButtonClick(View view) {
        operationType = null;
        wasOperationInput = false;
        SetText("");
    }

    public void onDigitBtnClick(View view) {
        if (view instanceof Button) {
            String btnText = ((Button) view).getText().toString();

            try {
                int digit = Integer.parseInt(btnText);

                if (wasOperationInput) {
                    SetText(String.valueOf(digit));
                    wasOperationInput = false;
                }
                else {
                    AddText(String.valueOf(digit));
                }
            }
            catch (NumberFormatException e) {
                throw new RuntimeException("Pressed not a number");
            }
        }
    }

    public void onPointBtnClick(View view) {
        String value = textField.getText().toString();

        if (value.matches("\\d+\\.{0}")) {
            AddText(".");
        }
    }

    public void onDeleteBtnClick(View view) {
        if (textField.getText().length() > 0) {
            String text = textField.getText().toString();
            text = text.substring(0, text.length() - 1);
            SetText(text);
        }
    }

    public void onAddBtnClick(View view) {
        SetArithmeticOperation(OperationType.Addition);
    }

    public void onSubtractBtnClick(View view) {
        SetArithmeticOperation(OperationType.Subtraction);
    }

    public void onMultiplyBtnClick(View view) {
        SetArithmeticOperation(OperationType.Multiplying);
    }

    public void onDivideBtnClick(View view) {
        SetArithmeticOperation(OperationType.Dividing);
    }

    private void SetArithmeticOperation(OperationType newOperation) {
        Double value = ParseValue(textField.getText().toString());
        if (value == null)
            return;

        if (operationType == null) {
            leftValue = value;
            switch (newOperation) {
                case Addition:
                    AddText("+");
                    break;
                case Subtraction:
                    AddText("−");
                    break;
                case Dividing:
                    AddText("÷");
                    break;
                case Multiplying:
                    AddText("×");
                    break;
            }
        }
        else {
            rightValue = CalculateAnswer(value);
            SetArithmeticOperation(newOperation);
        }

        operationType = newOperation;
        wasOperationInput = true;
    }

    public void onEqualsBtnClick(View view) {
        if (leftValue == null || operationType == null)
            return;

        rightValue = ParseValue(textField.getText().toString());

        if (rightValue == null)
            return;

        CalculateAnswer(rightValue);
        operationType = null;
        wasOperationInput = false;
    }

    public void onPlusMinusClick(View view) {
        Double value = ParseValue(textField.getText().toString());

        if (value == null)
            return;

        value *= -1;
        SetText(String.valueOf(value));
    }

    public void onPercentBtnClick(View view) {
        if (wasOperationInput || operationType == null)
            return;

        Double value = ParseValue(textField.getText().toString());
        if (value == null)
            return;

        value = leftValue * (value / 100);
        SetText(String.valueOf(value));
        rightValue = value;
    }

    @SuppressLint("DefaultLocale")
    private Double CalculateAnswer(Double rightValue) {
        if (rightValue == null)
            return null;

        Double answer = null;

        switch (operationType) {
            case Addition:
                answer = leftValue + rightValue;
                break;

            case Subtraction:
                answer = leftValue - rightValue;
                break;

            case Dividing:
                answer = leftValue / rightValue;
                break;

            case Multiplying:
                answer = leftValue * rightValue;
                break;
        }

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        numberFormat.setGroupingUsed(false);
        numberFormat.setMaximumFractionDigits(9);
        SetText(numberFormat.format(answer));
        operationType = null;
        return answer;
    }

    private Double ParseValue(String text) {
        try {
            return Double.parseDouble(text.replaceAll("\\^[0-9, -, +]", ""));
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    private void SetText(String text) {
        textField.setText(text);
        textField.setSelection(textField.getText().length());
    }

    private void AddText(String textToAdd) {
        String text = textField.getText().toString();
        text += textToAdd;
        textField.setText(text);
        textField.setSelection(textField.getText().length());
    }
}