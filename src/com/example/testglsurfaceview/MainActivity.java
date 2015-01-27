package com.example.testglsurfaceview;

import java.io.IOException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;


/*
*测试修改
*/
public class MainActivity extends Activity implements Callback {
	private FrameLayout frameLayout;

	private Camera camera;
	private SurfaceView surface;
	private SurfaceHolder holder;
	private GLSurfaceView surfaceView;
	private MyRender render;

	private int width, height;
	private boolean isPreview = false;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		WindowManager wManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_main);

		Display display = wManager.getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();
		frameLayout = (FrameLayout) findViewById(R.id.framlayout);
		/**
		 * 先添加GLSurfaceView
		 */
		surfaceView = new GLSurfaceView(this);
		surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);

		surfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
		render = new MyRender();
		surfaceView.setRenderer(render);
		frameLayout.addView(surfaceView, new LayoutParams(320, 240));

		/**
		 * 再添加SurfaceView图层
		 */
		surface = new SurfaceView(this);
		holder = surface.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		frameLayout.addView(surface, new LayoutParams(640, 480));
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (!isPreview) {
			camera = Camera.open();
		}
		try {
			if (camera != null) {
				Parameters parameters = camera.getParameters();
//				parameters.setPictureSize(width, height);
				parameters.setPreviewSize(640, 480);
				parameters.set("orientation", "landscape");
				parameters.setRotation(0);
				camera.setParameters(parameters);
				camera.setPreviewDisplay(holder);
				camera.startPreview();
				isPreview = true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (camera != null) {
			if (isPreview) {
				camera.stopPreview();
				camera.release();
				camera = null;
			}
		}
	}

}
