package com.example.lab1;

import androidx.appcompat.app.AppCompatActivity;
import org.mariuszgromada.math.mxparser.*;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private EditText display;
    private TextView previousCalculation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        previousCalculation=findViewById(R.id.previousCalculationView);
        display = findViewById(R.id.input);
        display.setShowSoftInputOnFocus(false);
        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getString(R.string.display).equals(display.getText().toString())) {
                    display.setText("");
                }
            }
        });
    }

    private void updateText(String strToAdd) {

        String oldStr = display.getText().toString();
        int cursorPos = display.getSelectionStart();
        String leftStr = oldStr.substring(0, cursorPos);
        String rightStr = oldStr.substring(cursorPos);
        if (getString(R.string.display).equals(display.getText().toString())) {
            display.setText(strToAdd);
            display.setSelection(cursorPos + strToAdd.length());
        } else {
            display.setText(String.format("%s%s%s", leftStr, strToAdd, rightStr));//
            display.setSelection(cursorPos + strToAdd.length());
        }
    }

    public void deleteAllBTN(View view) {
        display.setText("");
        previousCalculation.setText("");
    }

    public void bracketBTN(View view) {
        int cursorPos = display.getSelectionStart();
        int openPar = 0;
        int closedPar = 0;
        int textLen = display.getText().length();

        for (int i = 0; i < cursorPos; i++) {
            if (display.getText().toString().substring(i, i + 1).equals("(")) {
                openPar += 1;

            }
            if (display.getText().toString().substring(i, i + 1).equals(")")) {
                closedPar += 1;
            }
        }
        if (openPar == closedPar || display.getText().toString().substring(textLen - 1, textLen).equals("(")) {
            updateText("(");
        } else if (closedPar < openPar && !display.getText().toString().substring(textLen - 1, textLen).equals("(")) {
            updateText(")");
        }
        display.setSelection(cursorPos + 1);
    }

    public void percentBTN(View view) {
        updateText(getResources().getString(R.string.percent));
    }

    public void divBTN(View view) {
        updateText(getResources().getString(R.string.division));
    }

    public void sevenBTN(View view) {
        updateText(getResources().getString(R.string.seven));
    }

    public void eightBTN(View view) {
        updateText(getResources().getString(R.string.eight));
    }

    public void nineBTN(View view) {
        updateText(getResources().getString(R.string.nine));
    }

    public void mulBTN(View view) {
        updateText(getResources().getString(R.string.multiply));
    }

    public void fourBTN(View view) {
        updateText(getResources().getString(R.string.four));
    }

    public void fiveBTN(View view) {
        updateText(getResources().getString(R.string.five));
    }

    public void sixBTN(View view) {
        updateText(getResources().getString(R.string.six));
    }

    public void subBTN(View view) {
        updateText(getResources().getString(R.string.subtraction));
    }

    public void oneBTN(View view) {
        updateText(getResources().getString(R.string.one));
    }

    public void twoBTN(View view) {
        updateText(getResources().getString(R.string.two));
    }

    public void threeBTN(View view) {
        updateText(getResources().getString(R.string.three));
    }

    public void addBTN(View view) {
        updateText(getResources().getString(R.string.addition));
    }

    public void negativeBTN(View view) {
        updateText(getResources().getString(R.string.negative));
    }

    public void zeroBTN(View view) {
        updateText(getResources().getString(R.string.zero));
    }

    public void commaBTN(View view) {
        updateText(getResources().getString(R.string.comma));
    }

    public void resultBTN(View view) {
        String userExp = display.getText().toString();
        previousCalculation.setText(userExp);
        userExp = userExp.replaceAll(getResources().getString(R.string.division), "/");
        userExp = userExp.replaceAll(getResources().getString(R.string.multiply), "*");

        Expression exp = new Expression(userExp);
        String result = String.valueOf(exp.calculate());
        display.setText(result);
        display.setSelection(result.length());

    }

    public void backspaceBTN(View view) {
        int cursorPos = display.getSelectionStart();
        int textLen = display.getText().length();
        if (cursorPos != 0 && textLen != 0) {
            SpannableStringBuilder selection = (SpannableStringBuilder) display.getText();
            selection.replace(cursorPos - 1, cursorPos, "");
            display.setText(selection);
            display.setSelection(cursorPos - 1);
        }
    }

    public void trigSinBTN(View view) {
        updateText("sin(");
    }

    public void trigCosBTN(View view) {
        updateText("cos(");
    }

    public void trigTanBTN(View view) {
        updateText("tan(");
    }

    public void trigArcSinBTN(View view) {
        updateText("arcsin(");
    }

    public void trigArcCosBTN(View view) {
        updateText("arccos(");
    }

    public void trigArcTanBTN(View view) {
        updateText("arctan(");
    }

    public void naturalLogBTN(View view) {
        updateText("ln(");
    }

    public void logBTN(View view) {
        updateText("log(");
    }

    public void squareRootBTN(View view) {
        updateText("sqrt(");
    }

    public void absoluteValueBTN(View view) {
        updateText("abs(");
    }

    public void piBTN(View view) {
        updateText("pi");
    }

    public void eBTN(View view) {
        updateText("e");
    }

    public void xSquaredBTN(View view) {
        updateText("^(2)");
    }

    public void xPowerYBTN(View view) {
        updateText("^(");
    }

    public void isPrimeFunctionBTN(View view) {
        updateText("ispr(");
    }


}