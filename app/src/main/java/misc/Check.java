package misc;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class Check {

    public static boolean isEmpty(TextInputLayout til) {
        String s = Objects.requireNonNull(til.getEditText()).getText().toString();
        if (s.isEmpty()) {
            til.setError("Campo obbligatorio");
            return true;
        }
        return false;
    }

    public static boolean pwdAreEquals(TextInputLayout tilverify, TextInputLayout tilequal) {
        String verify = Objects.requireNonNull(tilverify.getEditText()).getText().toString();
        String equal = Objects.requireNonNull(tilequal.getEditText()).getText().toString();
        if (!verify.equals(equal)) {
            tilverify.setError("Le password non corrispondo");
            return false;
        }
        return true;
    }
}
