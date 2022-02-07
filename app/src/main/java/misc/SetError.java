package misc;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class SetError {

    public static void setErrorToggle(TextInputLayout tilpwd) {
        Objects.requireNonNull(tilpwd.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tilpwd.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
}
