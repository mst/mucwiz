package com.mucwiz;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.mucwiz.model.Quiz;
import com.mucwiz.webservice.MucwizApi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class OpenQuizActivity extends Activity {
	Timer t;

	ArrayAdapter<String> adapter;
	ArrayList<String> listItems=new ArrayList<String>();

	@Override
    public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_quiz);
        
        TextView quizName = (TextView)findViewById(R.id.quiz_name);
        quizName.setText(Quiz.getInstance().getKey());
        
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        
        final ListView lv = (ListView)findViewById(R.id.player_list); 
		lv.setAdapter(adapter);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		TimerTask tt = new TimerTask() {
			
			@Override
			public void run() {
				try {
					final Quiz quiz = MucwizApi.getQuiz(Quiz.getInstance().getKey());
					Quiz.setInstance(quiz);
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							listItems.clear();
							listItems.addAll(quiz.getPlayers());
							adapter.notifyDataSetChanged();
						}
					});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		t = new Timer();
		t.schedule(tt, 5000);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		t.cancel();
	}
}
