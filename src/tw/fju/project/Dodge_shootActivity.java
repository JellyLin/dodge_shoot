package tw.fju.project;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Dodge_shootActivity extends Activity 
{
	private Button bt1 ,bt2;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        bt1 = (Button)findViewById(R.id.button1);
        bt2 = (Button)findViewById(R.id.button2);
        
        bt1.setOnClickListener(new Button.OnClickListener()
        {

			public void onClick(View v)
			{
                Intent intent = new Intent();
                intent.setClass(Dodge_shootActivity.this, levelChoose.class);
                
                Bundle bundle = new Bundle();
                bundle.putInt("finish1", 0);
                intent.putExtras(bundle);
                
                startActivity(intent);
                
                Dodge_shootActivity.this.finish();
			}
		});
        bt2.setOnClickListener(new Button.OnClickListener()
        {

			public void onClick(View v)
			{
                Intent intent = new Intent();
                intent.setClass(Dodge_shootActivity.this, help.class);

                startActivity(intent);
                
                Dodge_shootActivity.this.finish();
			}
		});
    }
    
}