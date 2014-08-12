package tw.fju.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle; 	
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class GamePlay extends Activity 
{
    private DrawView drawView;
    private SensorManager mSensorManager;
	int phase;
	float difficulty;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        Bundle bundle = this.getIntent().getExtras();
		phase = bundle.getInt("phase");
        difficulty = bundle.getFloat("difficulty");
		
        // no rotate
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Get an instance of the SensorManager
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Get Screen Size
        //getWindowManager().getDefaultDisplay().getMetrics(metrics);
        dialog();
        drawView = new DrawView(this);
        //setContentView(drawView);
    }
    
	protected void dialog() 
	{
		  AlertDialog.Builder builder = new Builder(GamePlay.this);
		  switch((int)difficulty){
		  	case 0:
		  		builder.setMessage("傻蛋級  Ready GO");break;
		  	case 1:
		  		builder.setMessage("普通級  Ready GO");break;
		  	case 2:
		  		builder.setMessage("專家級  Ready GO");break;
		  	case 3:
		  		builder.setMessage("噩夢級  Ready GO");break;
		  	case 4:
		  		builder.setMessage("地獄級  Ready GO");break;
		  	case 5:
		  		builder.setMessage("煉獄級  Ready GO");break;
		  	default:
		  }

		  builder.setTitle("");

		  builder.setPositiveButton("Play", new DialogInterface.OnClickListener() 
		  {
			  public void onClick(DialogInterface dialog, int which) 
			  {
			        setContentView(drawView);
			  }
		  });

		  builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
		  {
			  public void onClick(DialogInterface dialog, int which) 
			  {
                  Intent intent = new Intent();
                  intent.setClass(GamePlay.this, levelChoose.class);
                  Bundle bundle = new Bundle();
                  bundle.putInt("finish1", 0);
                  intent.putExtras(bundle);
                  startActivity(intent);
                  
                  GamePlay.this.finish();
			  }
		  });

		  builder.create().show();
		 }
	
	public boolean onCreateOptionsMenu(Menu menu) {
        //參數1:群組id, 參數2:itemId, 參數3:item順序, 參數4:item名稱
      menu.add(0, 0, 0, "Exit");
      return super.onCreateOptionsMenu(menu);
}

public boolean onOptionsItemSelected(MenuItem item) {
	        //依據itemId來判斷使用者點選哪一個item
	        switch(item.getItemId()) {
	            case 0:
	            {
	            	  Intent intent = new Intent();
	                  intent.setClass(GamePlay.this, levelChoose.class);
	                  Bundle bundle = new Bundle();
	                  bundle.putInt("finish1", 0);
	                  intent.putExtras(bundle);
	                  startActivity(intent);
	                  
	                  GamePlay.this.finish();
	            }
	            default:
	        }
	        return super.onOptionsItemSelected(item);
	    }
	
	
	
	
	public class DrawView extends View implements OnTouchListener, SensorEventListener 
    {
        private Sensor mAccelerometer;
        //List<Point> points = new ArrayList<Point>();
		float speed = 30;         // mouse speed
		float ballspeed = 1;      // ball speed  ��۪�ܨC���_�l�t�׺��W ��}�l�|GG
		int ball_count = 10;
        Paint paint = new Paint();
        Point mouse_point = new Point();
        Point boss_point = new Point();
        Point destination = new Point();
    	int hole_size = 10;
    	int mouse_size = 20;
    	Point[] Hole = new Point[20];
    	int gameStatu = 0;         // 0 wait 1 start 2 die
    	float collide_parameter = 20;
    	int sublevel = 1;          //1  第一關 .. 4    返回 
    	Bitmap boss_pic;
    	int boss_kill=0;
    	
    	public DrawView(Context context) 
    	{
            super(context);
            setFocusable(true);
            setFocusableInTouchMode(true);
            
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
            
            ball_count += (int)difficulty;
            hole_size += (int)difficulty;
            
            for (int i=0; i<ball_count; i++)
            	Hole[i] = new Point();
            
            destination.x = 0;
            destination.y = 0;
            
            this.setOnTouchListener(this);

            paint.setColor(Color.WHITE);
            paint.setAntiAlias(true);
            
            getWindow().setBackgroundDrawableResource(R.drawable.stage1);
        }
    	
    	

        @Override
        public void onDraw(Canvas canvas) {
        	// start region
        	if (gameStatu!=2)
        	{
	        	paint.setColor(Color.YELLOW);
	        	canvas.drawRect(0, 0, 50, 100, paint);
	        	// end region
	        	paint.setColor(Color.RED);
	        	canvas.drawRect(480-50, 800-100, 480, 800, paint);
	        	
    	        // hole
    	        paint.setColor(Color.WHITE);
            	hole(canvas);
            	
			    // ball out
		        for (int i=0; i<ball_count; i++)
	        		if (Hole[i].state == 1 && (Hole[i].x > 500 || Hole[i].y < -100) )
	        			Hole[i].state = 0;
		        
		        paint.setColor(Color.BLUE);
		        boss(canvas);
		        
	        	// mouse
	        	paint.setColor(Color.GREEN);
	        	if (gameStatu == 1)
	        	{
		        		canvas.drawCircle(mouse_point.x, mouse_point.y, mouse_size, paint);
		
		        	// hole moving
		        	for (int i=0; i<ball_count; i++)
		        	{
		        		Hole[i].x+=ballspeed;
		        		if (i%2==0)
		        			Hole[i].y+=ballspeed;
		        	}
		        	if (ballspeed < 5)
		        		ballspeed += 0.01;
		        	
		        	// boss moving
		        	if(mouse_point.x>50 && mouse_point.y>100){
		        	boss_point.x += (( mouse_point.x - boss_point.x ) * 1/40)*(difficulty+1) /2;
		        	boss_point.y += (( mouse_point.y - boss_point.y ) * 1/40)*(difficulty+1) /2; 
		        	}
		        	
		        	
		        }
	        	// next stage
	        	if (mouse_point.x > 480-50 && mouse_point.y > 800-100)
	        	{
	        		gameStatu = 0;
	        		mouse_point.x = 0;
	        		mouse_point.y = 0;
	        		destination.x = 0;
	        		destination.y = 0;
	        		
	        		sublevel++;
	        		
	        		if(sublevel==3)
	        			getWindow().setBackgroundDrawableResource(R.drawable.stage2);
	        		else if(sublevel == 4)
	        		{
	        			if(boss_kill >= 5)
	        				Toast.makeText(getApplicationContext(), "	成就:[努力不懈]達成	", Toast.LENGTH_SHORT).show();
	                    Intent intent = new Intent();
	                    intent.setClass(GamePlay.this, levelChoose.class);
	                    
	                    Bundle bundle = new Bundle();
	                    bundle.putInt("finish1", 1);
	                    intent.putExtras(bundle);
	                    startActivity(intent);
	                    
	                    GamePlay.this.finish();
	        		}
	        			
	        	}
	        	
	        	mouse_point.x += ( destination.x - mouse_point.x ) * 1/10;
	        	mouse_point.y += ( destination.y - mouse_point.y ) * 1/10; 
	        	
	        	// collide
	        	for (int i = 0; i < ball_count; i++)
	        	{
	        		if (collides(Hole[i] ,mouse_point))
	        		{
	        			gameStatu = 2;
	        			ballspeed = 1;
	        			boss_point.x = 400;
	        			boss_point.y = 700;
	        		}
	        		if(( (sublevel == 3) && collides(boss_point ,mouse_point)))
	        		{
	        			gameStatu = 2;
	        			ballspeed = 1;
	        			boss_point.x = 400;
	        			boss_point.y = 700;
	        			boss_kill += 1;
	        		}
	        	}
	     
        	}
        	else
        	{
        		if(boss_kill == 5)
        			Toast.makeText(getApplicationContext(), "	成就:[QB KILL]達成	", Toast.LENGTH_SHORT).show();
        		else
        			Toast.makeText(getApplicationContext(), "你屎了!!再來一次吧!!", Toast.LENGTH_SHORT).show();
    		    initial();
        	}
        }
        
        public void initial()
        {
        	destination.x = 0;
        	destination.y = 0;
            mouse_point.x = 0;
		    mouse_point.y = 0;		
		    gameStatu = 0;
        }
        
        // ball
        public void hole(Canvas canvas)
        {
        	
        	for (int i = 0; i < ball_count; i++)
        	{
        		// create ball
        		if (gameStatu == 0 )
        		{
	        		Hole[i].x = (int)(Math.random()* (480 - hole_size)) + hole_size/2 + 50; 
	        		Hole[i].y = (int)(Math.random()* (800 - hole_size)) + hole_size/2 + 100;
	    			Hole[i].state = 1;
        		}
        		if (gameStatu == 1 && Hole[i].state == 0)
        		{
        			if (i%2 == 0)
        			{
        				Hole[i].x = (int)(Math.random()* (480 - hole_size)) + hole_size/2 + 50;
        				Hole[i].y = 10;
        			}
        			else
        			{
        				Hole[i].x = 10;
        				Hole[i].y = (int)(Math.random()* (800 - hole_size)) + hole_size/2 + 100;        				
        			}
        			Hole[i].state = 1;
        		}
        		// draw ball
        		canvas.drawCircle(Hole[i].x, Hole[i].y, hole_size, paint);
        	}
        	
        	// game start
        	gameStatu = 1;
     
        }
        
        //boss
        public void boss(Canvas canvas)
        {
        	boss_pic = BitmapFactory.decodeResource(getResources(), R.drawable.qb);
        		// create ball
        		if (gameStatu == 0)
        		{
        			boss_point.x = (int)(Math.random()* (480 - hole_size)) + hole_size/2 + 50; 
        			boss_point.y = (int)(Math.random()* (800 - hole_size)) + hole_size/2 + 100;
                	boss_point.size = 40;
        		}
        		if(sublevel == 3)
        		{
        			canvas.drawBitmap(boss_pic, (float)(boss_point.x-35), (float)(boss_point.y-46.5), paint);
        			//canvas.drawCircle(boss_point.x, boss_point.y, hole_size, paint);
        		}
        }
        
        public boolean collides(Point a, Point b) {
        	 return a.x < b.x + b.size -collide_parameter &&
        	            a.x + a.size > b.x +collide_parameter &&
        	            a.y < b.y + b.size -collide_parameter &&
        	            a.y + a.size > b.y +collide_parameter;
          }
        

        public boolean onTouch(View view, MotionEvent event) {
        	if (event.getX() - mouse_point.x < 300 && event.getY() - mouse_point.y < 300)
        	{
    			destination.x = event.getX();
    			destination.y = event.getY();
        	}
            invalidate();
            return true;
        }

    	public void onAccuracyChanged(Sensor sensor, int accuracy) {
    		// TODO Auto-generated method stub
    		
    	}

    	public void onSensorChanged(SensorEvent event) {
    		// TODO Auto-generated method stubl
    		if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
                return;
    		if (mouse_point.x > 0 && mouse_point.x < 480 && mouse_point.y > 0 && mouse_point.y < 840 - 0)
    		{
	    		if(event.values[0] > 1)
	    			destination.x = mouse_point.x - speed;
	    		if(event.values[0] < -1)
	    			destination.x = mouse_point.x + speed;
	    		
	    		if(event.values[1] > 1)
	    			destination.y = mouse_point.y + speed;
	    		if(event.values[1] < -1)
	    			destination.y = mouse_point.y - speed;
    		}
            invalidate();
            if (mouse_point.x < 0 )
            	mouse_point.x = 0+10 ;
            if (mouse_point.x > 480 )
            	mouse_point.x = 480-10;
            if (mouse_point.y < 0 )
            	mouse_point.y = 0+10;
            if (mouse_point.y > 780)
            	mouse_point.y = 780-10;
    		
    	}
    	

        
    }

    class Point {
        float x, y, size = 50;
        int state = 0; // 1 in screen 0 out bound
        
        @Override
        public String toString() {
            return x + ", " + y;
        }
    }
}


