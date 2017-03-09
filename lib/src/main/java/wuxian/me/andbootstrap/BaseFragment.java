package wuxian.me.andbootstrap;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by wuxian on 9/3/2017.
 */

public class BaseFragment extends Fragment {

    protected void showToast(String msg, int duration) {
        Activity activity = getActivity();
        if (activity != null && activity instanceof BaseActivity) {
            ((BaseActivity) activity).showToast(msg, duration);
        }
    }

    protected void hideKeyboard(EditText edit) {
        Activity activity = getActivity();
        if (activity != null && activity instanceof BaseActivity) {
            ((BaseActivity) activity).hideKeyboard(edit);
        }
    }

    protected View inflate(@LayoutRes int resId) {
        return LayoutInflater.from(getActivity()).inflate(resId, null, false);
    }
}
