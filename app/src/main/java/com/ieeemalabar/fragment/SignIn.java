package com.ieeemalabar.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ieeemalabar.ActivityMain;
import com.ieeemalabar.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by AKHIL on 26-Apr-16.
 */
public class SignIn extends Fragment {

    private FirebaseAuth mAuth;
    private Toast mToastText;
    Button mSignInButton;
    EditText mEmailField, mPasswordField;
    ProgressDialog pd;
    CheckBox check;
    Boolean tick = true;
    private DatabaseReference mDatabase;
    private static final String TAG = "SignIn";
    Boolean reset = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.signin, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mEmailField = (EditText) getView().findViewById(R.id.email0);
        mPasswordField = (EditText) getView().findViewById(R.id.password0);
        check = (CheckBox) getActivity().findViewById(R.id.checkBox0);
        mSignInButton = (Button) getView().findViewById(R.id.login);

        Typeface b_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/normalone.ttf");
        mEmailField.setTypeface(b_font);
        mPasswordField.setTypeface(b_font);
        mSignInButton.setTypeface(b_font);

        pd = new ProgressDialog(getActivity());
        pd.setCancelable(false);
        mToastText = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tick) {
                    mPasswordField.setInputType(InputType.TYPE_CLASS_TEXT);
                    mPasswordField.setSelection(mPasswordField.getText().length());
                    tick = false;
                } else {
                    mPasswordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mPasswordField.setSelection(mPasswordField.getText().length());
                    tick = true;
                }
            }
        });
        TextView forgot = (TextView) getActivity().findViewById(R.id.forgot0);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent forgot = new Intent(getContext(), Forgot.class);
                //startActivity(forgot);
                if (!isEmailValid(mEmailField.getText().toString())) {
                    mEmailField.setError("Invalid email id");
                    reset = false;
                } else {
                    mEmailField.setError(null);
                    reset = true;
                    hideSoftKeyboard(getActivity());
                    pd.setMessage("Please wait..");
                    pd.show();
                }
                if (reset) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.sendPasswordResetEmail(mEmailField.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        displayMessage("Reset mail sent");
                                        pd.hide();
                                    }else{
                                        displayMessage("Something went wrong!");
                                        pd.hide();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void displayMessage(final String message) {
        mToastText.setText(message);
        mToastText.show();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    private void signIn() {
        //Log.d(TAG, "signIn");
        if (!validateForm()) {
            return;
        }
        hideSoftKeyboard(getActivity());
        pd.setMessage("Logging In");
        pd.show();
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                        pd.hide();

                        if (task.isSuccessful()) {
                            displayMessage("Login success");
                            startActivity(new Intent(getActivity(), ActivityMain.class));
                            getActivity().finish();
                        } else {
                            displayMessage("Login Failed");
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEmailField.getText().toString()) || !isEmailValid(mEmailField.getText().toString())) {
            mEmailField.setError("Invalid email id");
            result = false;
        } else {
            mEmailField.setError(null);
        }

        if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
            mPasswordField.setError("Required");
            result = false;
        } else {
            mPasswordField.setError(null);
        }

        return result;
    }
}
