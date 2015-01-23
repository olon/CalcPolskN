package com.tsukanov.vladimir.calcpolskn;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CalcActivity extends Activity {

    private static final String KEY_INDEX_RESULT = "result";
    private static final String KEY_INDEX_MEMORY = "memory";
    private static final String KEY_INDEX_FIELD = "field";

    private float addMemoryNew = 0f;
    private String field = null;

    private TextView mResultTextView, mEntryFieldTextView;

    private void errorSigns(String s, char c){
        if (s.length() == 0 && c == '-') mEntryFieldTextView.setText("" + c);//0 --> -
        else if (s.length() == 0) mEntryFieldTextView.setText("0" + c);//""  --> "0" + c
        else if ((s.length() == 1 && s.charAt(0) == '-') || //"-" + c --> "-"
                (s.length() > 1 && s.charAt(s.length()-2) == '('
                        && s.charAt(s.length()-1) == '-') ||//"(-" + c  --> "(-"
                (s.lastIndexOf("(") == s.length()-1 && c != '-'));//"(" --> "(-"
        else if (s.length() > 1 && PPN.isOperator(s.charAt(s.length()-1)) &&
                s.lastIndexOf("(") != (s.length()-2)) //"sings" --> c & "(-" + c  --> "(-"
            mEntryFieldTextView.setText(s.substring(0, s.length()-1) + c);
        else if (s.length() > 1 && s.lastIndexOf(".") == s.length() - 1)//"1." --> "1" + c
            mEntryFieldTextView.setText(s.substring(0, s.length()-1) + c);
        else mEntryFieldTextView.setText(s + c);
    }
    private void errorNumberAfterRightParenthesis(String s, char c){
        if (s.length() > 3 && s.lastIndexOf(")") == s.length()-1);
        else mEntryFieldTextView.setText(s + c);
    }

    private void checkField(String eql, String set){
        if (eql.equals(mEntryFieldTextView.getText())) mEntryFieldTextView.setText(set);
        field = (String)mEntryFieldTextView.getText();
    }

    private static int isOperatorCheck(String s){
        int max = 0;
        int[] a = {s.lastIndexOf("+"), s.lastIndexOf("-"), s.lastIndexOf("*"), s.lastIndexOf("/"), s.lastIndexOf("%"),
                s.lastIndexOf("^")};
        for (int i = 0 ; i < a.length; i++){
            if (max < a[i]) max = a[i];
        }
        return max;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        mResultTextView = (TextView) findViewById(R.id.result_text_view);
        mEntryFieldTextView = (TextView) findViewById(R.id.entry_field_text_view);

        if (savedInstanceState != null){
            mResultTextView.setText(savedInstanceState.getString(KEY_INDEX_RESULT));
            addMemoryNew = savedInstanceState.getFloat(KEY_INDEX_MEMORY, 0f);
            mEntryFieldTextView.setText(savedInstanceState.getString(KEY_INDEX_FIELD));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_INDEX_RESULT, String.valueOf(mResultTextView.getText()));
        outState.putFloat(KEY_INDEX_MEMORY, addMemoryNew);
        outState.putString(KEY_INDEX_FIELD, String.valueOf(mEntryFieldTextView.getText()));
    }

    public void ButtonClickHandler(View view) {
        checkField("0", "");

        switch(view.getId()) {
            case R.id.numre_one_button:
                errorNumberAfterRightParenthesis(field, '1');
                break;
            case R.id.numre_two_button:
                errorNumberAfterRightParenthesis(field, '2');
                break;
            case R.id.numre_three_button:
                errorNumberAfterRightParenthesis(field, '3');
                break;
            case R.id.numre_four_button:
                errorNumberAfterRightParenthesis(field, '4');
                break;
            case R.id.numre_five_button:
                errorNumberAfterRightParenthesis(field, '5');
                break;
            case R.id.numre_six_button:
                errorNumberAfterRightParenthesis(field, '6');
                break;
            case R.id.numre_seven_button:
                errorNumberAfterRightParenthesis(field, '7');
                break;
            case R.id.numre_eight_button:
                errorNumberAfterRightParenthesis(field, '8');
                break;
            case R.id.numre_nine_button:
                errorNumberAfterRightParenthesis(field, '9');
                break;
            case R.id.numre_zero_button:
                errorNumberAfterRightParenthesis(field, '0');
                break;
            case R.id.plus_sign_button:
                errorSigns(field, '+');
                break;
            case R.id.minus_sign_button:
                errorSigns(field, '-');
                break;
            case R.id.multiply_sign_button:
                errorSigns(field, '*');
                break;
            case R.id.devision_sign_button:
                errorSigns(field, '/');
                break;
            case R.id.remainder_button:
                errorSigns(field, '%');
                break;
            case R.id.degree_sign_button:
                errorSigns(field, '^');
                break;
            case R.id.left_parenthesis_button: {
                if (field.length() > 0)
                    if (Character.isDigit(field.charAt(field.length() - 1)) || //"9" !!--> "9("
                            field.lastIndexOf(".") == field.length() - 1) ; // "." !!--> ".("
                    else
                        mEntryFieldTextView.setText(field + "(");
                else
                    mEntryFieldTextView.setText(field + "(");
            }
            break;
            case R.id.right_parenthesis_button: {
                if (field.length() == 0 || //"" !!--> ")"
                        field.lastIndexOf("(") == field.length() - 1 || //"(" !!--> "()"
                        (field.length() > 1 && field.lastIndexOf("(") == field.length() - 2 &&
                                !Character.isDigit(field.charAt(field.length() - 1))) || //"((" !!--> "(()"
                        PPN.isOperator(field.charAt(field.length() - 1)) || //"...+" !!--> "...+)"
                        (field.lastIndexOf(".") == field.length() - 1)) ; // "." !!--> ".)"
                else mEntryFieldTextView.setText(field + ")");
            }
            break;
            case R.id.point_button: {
                if ("".equals(field))
                    mEntryFieldTextView.setText("0.");
                else if (field.length() > 1 && (isOperatorCheck(field) < field.lastIndexOf(".") ||//"9.9" !!--> "9.9."
                        field.lastIndexOf(".") == field.length() - 1 ||//"0." !!--> "0.."
                        field.lastIndexOf("(") == field.length() - 1 || //"(" !!--> "(." + c
                        field.lastIndexOf(")") == field.length() - 1 || //")" !!--> ")." + c
                        PPN.isOperator(field.charAt(field.length() - 1)))) ; //c !!--> c + "."
                else mEntryFieldTextView.setText(field + ".");
            }
            break;
            case R.id.add_memory_button: {
                if (PPN.getMemoryAdd() == (float) 0.0)
                    Toast.makeText(this, getString(R.string.memory_add_nothing_save), Toast.LENGTH_SHORT).show();
                else {
                    addMemoryNew += PPN.getMemoryAdd();
                }
                checkField("", "0");
            }
            break;
            case R.id.read_memory_button: {
                if (addMemoryNew == (float) 0.0) ;
                else
                    mEntryFieldTextView.setText("" + addMemoryNew);
                checkField("", "0");
            }
            break;
            case R.id.clear_memory_button:
                checkField("", "0");
                addMemoryNew = 0;
                break;
            case R.id.backspace_button: {
                if ((field.length() == 1) || "".equals(field)) field = "0";
                else
                    field = field.substring(0, field.length() - 1);
                mEntryFieldTextView.setText(field);
            }
            break;
            case R.id.clear_button:
                mEntryFieldTextView.setText("0");
                break;
            case R.id.result_button: {
                checkField("", "0");
                int countLeft = 0;
                int countRight = 0;
                if (field.lastIndexOf("(") >= 0) {
                    for (int i = 0; i < field.length(); i++) {
                        if (field.charAt(i) == '(') countLeft++;
                        if (field.charAt(i) == ')') countRight++;
                    }
                }
                if (countLeft != countRight)
                    Toast.makeText(this, getString(R.string.result_miss_parenthesis), Toast.LENGTH_SHORT).show();
                else if (PPN.isOperator(field.charAt(field.length() - 1)))
                    Toast.makeText(this, getString(R.string.result_forget_after_signs), Toast.LENGTH_SHORT).show();
                else
                    mResultTextView.setText(PPN.eval(field));
            }
            break;
        }
    }
}
