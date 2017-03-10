package wuxian.me.andbootstrapdemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.os.IResultReceiver;
import android.view.SurfaceView;
import android.view.View;

import com.google.zxing.Result;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wuxian.me.andbootstrapdemo.base.BaseActionbarActivity;
import wuxian.me.zxingscanner.IScanResult;
import wuxian.me.zxingscanner.QRCodeScannerImpl;
import wuxian.me.zxingscanner.scanview.ScanView;

/**
 * Created by wuxian on 10/3/2017.
 */

public class QRCodeActivity extends BaseActionbarActivity implements IScanResult {

    private QRCodeScannerImpl mScanner;

    @BindView(R.id.surface)
    SurfaceView mSurfaceView;

    @BindView(R.id.scanview)
    ScanView mScanview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        try {
            mScanner = new QRCodeScannerImpl(mSurfaceView, mScanview, this);
        } catch (Exception e) {
            finish();
        }
    }

    @Override
    protected View getSubview() {
        return inflate(R.layout.activity_qrcode);
    }

    @Override
    protected boolean useCustomToolbar() {
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();

        mScanner.quit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mScanner.startScan();
    }

    @NonNull
    @Override
    protected String pageTitle() {
        return "QRCode";
    }

    @Nullable
    @Override
    protected List<MenuItemData> getMenuItemDatas() {
        return null;
    }

    @Override
    public void onScanResult(Result result, Bitmap bitmap) {

    }
}
