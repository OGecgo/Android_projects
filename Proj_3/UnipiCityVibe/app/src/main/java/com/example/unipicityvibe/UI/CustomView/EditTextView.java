package com.example.unipicityvibe.UI.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
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
        LayoutInflater.from(context).inflate(R.layout.custom_edit_text_view, this, true);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);

        // test values
        if (editText == null || textView == null) {
            throw new IllegalStateException("[EditTextView] EditText or TextView is null ");
        }

        // take attributes from upper View and set to that View
        // additional attributes for EditTextView are located in attr.xml
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

    // ------ Constructors ------
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
    // ------ End Constructors ------

    public void setEditText(String text){
        editText.setText(text);
    }
    public void setLabelText(String text){
        textView.setText(text);
    }
    public CharSequence getEditText(){
        return editText.getText();
    }
    public CharSequence getLabelText(){
        return textView.getText();
    }
}
