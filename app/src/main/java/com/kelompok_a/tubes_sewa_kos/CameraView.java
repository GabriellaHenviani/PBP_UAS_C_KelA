package com.kelompok_a.tubes_sewa_kos;

import android.content.Context;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {


    private SurfaceHolder kHolder;
    private Camera kCamera;

    public CameraView(Context context, Camera camera) {
        super(context);
        kCamera  = camera;
        kCamera.setDisplayOrientation(90);
        kHolder = getHolder();
        kHolder.addCallback(this);
        kHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try{
            kCamera.setPreviewDisplay(kHolder);
            kCamera.startPreview();
        } catch (IOException e) {
            Log.d("Error", "Camera error on SurfaceCreated" +e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if(kHolder.getSurface() == null)
            return;
        try {
            kCamera.setPreviewDisplay(kHolder);
            kCamera.startPreview();
        } catch (IOException e) {
            Log.d("Error", "Camera error on SurfaceChanged" +e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        kCamera.stopPreview();
        kCamera.release();
    }
}
