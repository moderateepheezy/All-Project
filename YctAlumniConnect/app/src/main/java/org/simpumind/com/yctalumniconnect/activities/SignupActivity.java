package org.simpumind.com.yctalumniconnect.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.simpumind.com.yctalumniconnect.R;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    @Bind(R.id.input_first_name) EditText _firstName;
    @Bind(R.id.input_otheer_name) EditText _otherName;
    @Bind(R.id.input_last_name) EditText _lastName;
    @Bind(R.id.date_of_bith) EditText _dateOfBirth;
    @Bind(R.id.year_of_degree) EditText _yearOfDegree;
    @Bind(R.id.input_email) EditText _email;
    @Bind(R.id.input_phone) EditText _phoneNumber;
    @Bind(R.id.input_username) EditText _userName;
    @Bind(R.id.input_password) EditText _password;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;
    @Bind(R.id.p_image) ImageView p_image;


    private TextView mErrorField;

    private DatePicker datePicker;
    private Calendar calendar;
    private EditText dateView;
    private int year, month, day;
    Spinner spinner;
    Spinner courseSpinner;

    private static Bitmap bitmap;

    private static String sexT;
    private static String courseT;
    String[] title = {
            "Sex",
            "Male.",
            "Female."
    };

    private static int RESULT_LOAD_IMAGE = 100;

    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
          coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .cordinate);
        spinner = (Spinner) findViewById(R.id.sex);
        courseSpinner = (Spinner) findViewById(R.id.courses);
        dateView = (EditText) findViewById(R.id.date_of_bith);


        mErrorField = (TextView) findViewById(R.id.error_messages);
        _signupButton = (Button) findViewById(R.id.btn_signup);
        _loginLink = (TextView) findViewById(R.id.link_login);

        p_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
            }
        });

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
        getResources().getStringArray(R.array.course));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(spinnerAdapter);
        courseSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        int position = courseSpinner.getSelectedItemPosition();
                        if (position != 0) {
                            courseT = courseSpinner.getItemAtPosition(position).toString();
                            Toast.makeText(getApplicationContext(), "You have selected " +
                                    courseT, Toast.LENGTH_LONG).show();
                        }
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }

                }
        );

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, title);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        int position = spinner.getSelectedItemPosition();
                        if (position != 0) {
                            sexT = spinner.getItemAtPosition(position).toString();
                            Toast.makeText(getApplicationContext(), "You have selected " + title[+position], Toast.LENGTH_LONG).show();
                        }
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }

                }
        );

        dateView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                setDate();
                return false;
            }
        });

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        String firstName = _firstName.getText().toString();
        String lasttName = _lastName.getText().toString();
        String otherName = _otherName.getText().toString();
        String coursee = courseT;
        String dateOfBirth = _dateOfBirth.getText().toString();
        String yearOfDegree = _yearOfDegree.getText().toString();
        String sex = sexT;
        String username = _userName.getText().toString();
        String emal = _email.getText().toString();
        String pass = _password.getText().toString();
        String phoneNumber = _phoneNumber.getText().toString();

        ParseUser user = new ParseUser();
       // Bitmap bitmap = BitmapFactory.decodeFile("picturePath");
        // Convert it to byte
        bitmap = ((BitmapDrawable) p_image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        getResizedBitmap(bitmap, 200).compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();
        ParseFile file = new ParseFile( "profile.png", image);
       // file.saveInBackground();
        user.setUsername(username);
        user.setPassword(pass);
        user.setEmail(emal);
        user.put("firstName", firstName);
        user.put("otherName", otherName);
        user.put("lastName", lasttName);
        user.put("sex", sex);
        user.put("course", coursee);
        user.put("phoneNumber", phoneNumber);
        user.put("dateOfBirth", dateOfBirth);
        user.put("yearOfDegree", yearOfDegree);
       // user.put("userImage", file);

       // user.saveInBackground();
        Toast.makeText(getApplicationContext(),
                "Successfully Signed up " + username + " " + pass + " file: " + file.toString(),
                Toast.LENGTH_LONG).show();

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(),
                            "Successfully Signed up, please log in.",
                            Toast.LENGTH_LONG).show();
                    Intent t = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(t);
                    finish();
                }else{
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    switch(e.getCode()) {
                        case ParseException.USERNAME_TAKEN:
                            mErrorField.setText("Sorry, this username has already been taken.");
                            break;
                        case ParseException.USERNAME_MISSING:
                            mErrorField.setText("Sorry, you must supply a username to register.");
                            break;
                        case ParseException.PASSWORD_MISSING:
                            mErrorField.setText("Sorry, you must supply a password to register.");
                            break;
                        default:
                            mErrorField.setText(e.getLocalizedMessage());
                    }

                }
            }
        });
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String firstName = _firstName.getText().toString();
        String lasttName = _lastName.getText().toString();
        String otherName = _otherName.getText().toString();
        String coursee = courseT;
        String dateOfBirth = _dateOfBirth.getText().toString();
        String yearOfDegree = _yearOfDegree.getText().toString();
        String sex = sexT;
        String username = _userName.getText().toString();
        String email = _email.getText().toString();
        String pass = _password.getText().toString();
        String phoneNumber = _phoneNumber.getText().toString();



        if(p_image.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.logor).getConstantState()){
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Please add an image file from gallery", Snackbar.LENGTH_LONG);

            snackbar.show();
            valid = false;
        }

        if (firstName.isEmpty() || firstName.length() < 3) {
            _firstName.setError("at least 3 characters");
            valid = false;
        } else {
            _firstName.setError(null);
        }

        if (username.isEmpty() || username.length() < 3) {
            _userName.setError("at least 3 characters");
            valid = false;
        } else {
            _userName.setError(null);
        }

        if (phoneNumber.isEmpty() || phoneNumber.length() < 11) {
            _phoneNumber.setError("must be 11 digits");
            valid = false;
        } else {
            _phoneNumber.setError(null);
        }

        if (otherName.isEmpty() || otherName.length() < 3) {
            _otherName.setError("at least 3 characters");
            valid = false;
        } else {
            _otherName.setError(null);
        }

        if (yearOfDegree.isEmpty() || yearOfDegree.length() < 3) {
            _yearOfDegree.setError("at least 3 characters");
            valid = false;
        } else {
            _yearOfDegree.setError(null);
        }

        if(dateOfBirth.equals("Date of Birth")){
            valid = false;
        }

        if (lasttName.isEmpty() || lasttName.length() < 3) {
            _lastName.setError("at least 3 characters");
            valid = false;
        } else {
            _lastName.setError(null);
        }

        if(sex.equals("Sex")){
            valid = false;
        }

        if(coursee.equals("Choose course studied")){
            valid = false;
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _email.setError("enter a valid email address");
            valid = false;
        } else {
            _email.setError(null);
        }

        if (pass.isEmpty() || pass.length() < 4 || pass.length() > 10) {
            _password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _password.setError(null);
        }

        return valid;
    }

    public void setDate() {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            //from the gallery
                if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    bitmap = BitmapFactory.decodeFile(picturePath);
                    p_image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}
