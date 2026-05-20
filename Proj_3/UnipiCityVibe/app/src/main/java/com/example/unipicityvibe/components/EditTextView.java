package com.example.unipicityvibe.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.unipicityvibe.R;

import org.jetbrains.annotations.NotNull;

public class EditTextView extends ConstraintLayout {
    private EditText editText;
    private TextView textView;

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        LayoutInflater.from(context).inflate(R.layout.edit_text_view, this, true);
        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);

        if (editText == null || textView == null) {
            throw new IllegalStateException("Error: EditTextView: EditText or TextView is null ");
        }

        if (attrs != null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditTextView, defStyleAttr, defStyleRes);
            try {
                CharSequence labelText = a.getText(R.styleable.EditTextView_label_text);
                CharSequence editTextValue = a.getText(R.styleable.EditTextView_edit_text);
                int inputType = a.getInt(R.styleable.EditTextView_android_inputType, -1);

                if (labelText != null) textView.setText(labelText);
                if (editTextValue != null) editText.setText(editTextValue);
                editText.setInputType(inputType);

            } finally {
                a.recycle();
            }
        }
    }

    public EditTextView(@NotNull Context context){
        super(context);
        init(context, null, 0, 0);
    }
    public EditTextView(@NotNull Context context, @NotNull AttributeSet attrs){
        super(context, attrs);
        init(context, attrs, 0, 0);
    }
    public EditTextView(@NotNull Context context, @NotNull AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }
    public EditTextView(@NotNull Context context, @NotNull AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setEditText(String text){
        editText.setText(text);
    }
    public void setLabelText(String text){
        textView.setText(text);
    }
    public String getEditText(){
        return editText.getText().toString();
    }
    public String getLabelText(){
        return textView.getText().toString();
    }
}
