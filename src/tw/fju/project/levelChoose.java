package tw.fju.project;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

public class levelChoose extends Activity
{
	private Button lv1;
	private RatingBar rate;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_choose);
        
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        lv1 = (Button)findViewById(R.id.bt_phase1);
        rate = (RatingBar)findViewById(R.id.ratingBar1);

        
        Bundle bundle = this.getIntent().getExtras();
		int finish1 = bundle.getInt("finish1");
		//int finish2 = bundle.getDouble("Price1");
		//int finish3 = bundle.getDouble("Price1");
		
		
		if(finish1==0)
		Toast.makeText(getApplicationContext(), "請先選擇難度,再選擇關卡開始!!", Toast.LENGTH_SHORT).show();
		else if(finish1==1)
		{
			Toast.makeText(getApplicationContext(), "	成就:[QB Blood]達成	", Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), "恭喜通過第一關!!", Toast.LENGTH_SHORT).show();
		}
        
		
        lv1.setOnClickListener(new Button.OnClickListener()
        {
			public void onClick(View v)
			{
                Intent intent = new Intent();
                intent.setClass(levelChoose.this, GamePlay.class);
                Bundle bundle = new Bundle();
                bundle.putInt("phase", 1);
                bundle.putFloat("difficulty", rate.getRating());
                intent.putExtras(bundle);
                
                startActivity(intent);
                
                levelChoose.this.finish();
			}
		});
	}
	public boolean onCreateOptionsMenu(Menu menu) {
        //參數1:群組id, 參數2:itemId, 參數3:item順序, 參數4:item名稱
      menu.add(0, 0, 0, "返回");
      return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
	        //依據itemId來判斷使用者點選哪一個item
	        switch(item.getItemId()) {
	            case 0:
	            {
	            	  Intent intent = new Intent();
	                  intent.setClass(levelChoose.this, Dodge_shootActivity.class);
	                  startActivity(intent);
	                  
	                  levelChoose.this.finish();
	            }
	            default:
	        }
	        return super.onOptionsItemSelected(item);
	    
}
}
