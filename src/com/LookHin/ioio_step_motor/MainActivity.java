package com.LookHin.ioio_step_motor;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;


/*
 * Step Motor ULN2003 Driver
 * IN 1 = PIN 1
 * IN 2 = PIN 2
 * IN 3 = PIN 3
 * IN 4 = PIN 4
 * VCC = 5V
 * GND = GND
 */


public class MainActivity extends IOIOActivity {
	
	private ToggleButton toggleButton1;
	private Button button1;
	private Button button2;
	private Button button3;
	
	private String left_or_right_or_stop = "Left";
	private int StepCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case R.id.action_about:
        	//Toast.makeText(getApplicationContext(), "Show About", Toast.LENGTH_SHORT).show();
        	
        	Intent about = new Intent(this, AboutActivity.class);
    		startActivity(about);
    		
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    } 
    
    
    class Looper extends BaseIOIOLooper {
		
    	private DigitalOutput digital_led1;
    		
		private DigitalOutput logic_pin_1;
		private DigitalOutput logic_pin_2;
		private DigitalOutput logic_pin_3;
		private DigitalOutput logic_pin_4;
		

		@Override
		protected void setup() throws ConnectionLostException {

			digital_led1 = ioio_.openDigitalOutput(0,true);
			
			logic_pin_1 = ioio_.openDigitalOutput(1, false);
			logic_pin_2 = ioio_.openDigitalOutput(2, false);
			logic_pin_3 = ioio_.openDigitalOutput(3, false);
			logic_pin_4 = ioio_.openDigitalOutput(4, false);
			
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(getApplicationContext(), "IOIO Connect", Toast.LENGTH_SHORT).show();
				}
			});
			
			
			// Set To Left
			button1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					left_or_right_or_stop = "Left";
				}
			});
			
			// Set To Right
			button2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					left_or_right_or_stop = "Right";
				}
			});
			
			// Set To Stop
			button3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					left_or_right_or_stop = "Stop";
				}
			});
			
		}

		@Override
		public void loop() throws ConnectionLostException {
			
			digital_led1.write(!toggleButton1.isChecked());
			
			if(StepCount == 0){
				logic_pin_1.write(false);
				logic_pin_2.write(false);
				logic_pin_3.write(false);
				logic_pin_4.write(true);
			}else if(StepCount == 1){
				logic_pin_1.write(false);
				logic_pin_2.write(false);
				logic_pin_3.write(true);
				logic_pin_4.write(true);
			}else if(StepCount == 2){
				logic_pin_1.write(false);
				logic_pin_2.write(false);
				logic_pin_3.write(true);
				logic_pin_4.write(false);
			}else if(StepCount == 3){
				logic_pin_1.write(false);
				logic_pin_2.write(true);
				logic_pin_3.write(true);
				logic_pin_4.write(false);
			}else if(StepCount == 4){
				logic_pin_1.write(false);
				logic_pin_2.write(true);
				logic_pin_3.write(false);
				logic_pin_4.write(false);
			}else if(StepCount == 5){
				logic_pin_1.write(true);
				logic_pin_2.write(true);
				logic_pin_3.write(false);
				logic_pin_4.write(false);
			}else if(StepCount == 6){
				logic_pin_1.write(true);
				logic_pin_2.write(false);
				logic_pin_3.write(false);
				logic_pin_4.write(false);
			}else if(StepCount == 7){
				logic_pin_1.write(true);
				logic_pin_2.write(false);
				logic_pin_3.write(false);
				logic_pin_4.write(true);
			}else{
				logic_pin_1.write(false);
				logic_pin_2.write(false);
				logic_pin_3.write(false);
				logic_pin_4.write(false);
			}
			
			if(left_or_right_or_stop.equals("Right")){
				StepCount++;
				
				if(StepCount >= 8){
					StepCount = 0;
				}
			}else if(left_or_right_or_stop.equals("Left")){
				StepCount--;
				
				if(StepCount <= 0){
					StepCount = 7;
				}
			}else if(left_or_right_or_stop.equals("Stop")){
				
			}
			
						
			try{
				Thread.sleep(1);
			}catch(InterruptedException e){
				
			}
			
		}
	}

	@Override
	protected IOIOLooper createIOIOLooper() {
		return new Looper();
	}
    
}
