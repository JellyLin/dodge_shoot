package tw.fju.project;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class help extends Activity 
{	
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
    	                  intent.setClass(help.this, Dodge_shootActivity.class);
    	                  startActivity(intent);
    	                  
    	                  help.this.finish();
    	            }
    	            default:
    	        }
    	        return super.onOptionsItemSelected(item);
    	    
    }
    
}