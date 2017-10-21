package com.blazingmuffin.health.mdmsystem.utils;

import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by lenovo on 10/21/2017.
 */

public final class WidgetUtils {
    public static final String getText(EditText editText) {
        return editText.getText().toString();
    }
    public static final String getText(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = radioGroup.findViewById(selectedId);
        return radioButton.getText().toString();
    }
}
