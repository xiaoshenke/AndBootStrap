package wuxian.me.andbootstrap;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by wuxian on 4/3/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Toast mToast = null;

    public void showToast(String msg, int duration) {

        if (mToast == null) {
            mToast = Toast.makeText(this, msg, duration);
        } else {
            mToast.setDuration(duration);
            mToast.setText(msg);
        }

        mToast.show();
    }

    public void hideKeyboard(EditText edit) {
        if (edit == null) {
            return;
        }
        edit.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }

    protected View inflate(@LayoutRes int resId) {
        return LayoutInflater.from(this).inflate(resId, null, false);
    }
}
