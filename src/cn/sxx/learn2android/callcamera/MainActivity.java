package cn.sxx.learn2android.callcamera;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements CvCameraViewListener2
{
	 private static final String  TAG              = "CallCamera::MainAc";
	
	  private CameraBridgeViewBase mOpenCvCameraView;
//	  private boolean mIsJavaCamera = true;
//	  private MenuItem mItemSwitchCamera = null;
	  private Mat mRgba;
	  private Button mBtn = null;
	  private boolean	 isProcess = false;
    
    private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) 
    {
        @Override
        public void onManagerConnected(int status) {
            switch (status) 
            {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                 //   mCameraView.setOnTouchListener(MainActivity.this);
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    
    public MainActivity()
	{
    	Log.i(TAG, "Instantiated new " + this.getClass());
	}
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//保持屏幕常亮#as long as this window is visible to the user, keep the device's screen turned on and bright.
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		setContentView(R.layout.activity_main);
	
//		  if (mIsJavaCamera)
		      mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.java_surface_view);
//		  else
//		      mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.native_surface_view);
		  
		 mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
		 mOpenCvCameraView.setCvCameraViewListener(this);
		 
		 mBtn = (Button) findViewById(R.id.buttonGray);
		 mBtn.setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{
				isProcess = !isProcess;				
			}
		});
	}	
 
	//相机开启时该方法将会调用，一般在该函数内部新建Mat用于存储图像。
	@Override
	public void onCameraViewStarted(int width, int height)
	{
		mRgba = new Mat(height, width, CvType.CV_8UC4);		
	}

	//onCameraViewStarted函数调用后将自动调用该帧处理函数，帧处理算法即写在该函数内部
	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame)
	{
		if(isProcess)
		{
			
		    Imgproc.cvtColor(inputFrame.gray(), mRgba, Imgproc.COLOR_GRAY2RGBA, 4);			
		}

		   else
		     mRgba = inputFrame.rgba();
		   return mRgba;
	}
	
	@Override
	public void onCameraViewStopped()
	{
		mRgba.release();		
	}
	    
	  @Override
	    public void onPause()
	    {
	        super.onPause();
	        if (mOpenCvCameraView != null)
			{
	        	mOpenCvCameraView.disableView();
			}
	    }

	    @Override
	    public void onResume()
	    {
	        super.onResume();
	        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_10, this, mLoaderCallback);
	    }

	    public void onDestroy() 
	    {
	        super.onDestroy();
	        if (mOpenCvCameraView != null)
			{
	        	mOpenCvCameraView.disableView();
			}
	    }
	    
	    
}
