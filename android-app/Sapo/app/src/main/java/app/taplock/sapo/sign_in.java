package app.taplock.sapo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class sign_in extends AppCompatActivity implements View.OnTouchListener {
    EditText old_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        old_pwd  = (EditText)findViewById(R.id.password);

        old_pwd.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int DRAWABLE_RIGHT = 2;
        if (event.getAction() == MotionEvent.ACTION_UP) {

            if (event.getRawX() >= (old_pwd.getRight() - old_pwd.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                if (old_pwd.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {

                    old_pwd.setInputType(InputType.TYPE_CLASS_TEXT |                           InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    old_pwd.setCompoundDrawablesWithIntrinsicBounds(getApplicationContext().getResources().getDrawable(R.drawable.ic_lock), null, getApplicationContext().getResources().getDrawable(R.drawable.ic_pwd_visibility_off), null);
                    old_pwd.setSelection(old_pwd.getText().length());
                } else {

                    old_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    old_pwd.setCompoundDrawablesWithIntrinsicBounds(getApplicationContext().getResources().getDrawable(R.drawable.ic_lock), null, getApplicationContext().getResources().getDrawable(R.drawable.ic_pwd_visibility), null);
                    old_pwd.setSelection(old_pwd.getText().length());
                }
                return true;
            }
        }
        return false;
    }
}