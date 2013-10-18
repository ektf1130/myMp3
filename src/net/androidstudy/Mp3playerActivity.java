package net.androidstudy;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

//프로그래스바의 진행률을 표시하기위해서 Runnable
public class Mp3playerActivity extends Activity implements Runnable , OnClickListener
{
    /** Called when the activity is first created. */
     // public static 를 붙이면 전역변수가 된다 --> 여러 액티비티에서 공유할 수 있다.
	 public static Boolean isLoaded = false;
	 public static String mp3Path;
	 
	 MediaPlayer mp = null; // load 버튼을 클릭 했을 때 현재 선택된 음악이 있는지 없는지 여부를 판단
     TextView tv;
     //에뮬레이터의 data를 가리키고있음
     ProgressBar mprogressbar;
     Button rw,play,stop,ff,load;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
		
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        //버튼, 텍스트뷰 등의 아이디 지정
        load = (Button)findViewById(R.id.LOAD_BTN);
    	rw =(Button)findViewById(R.id.rw);
    	play =(Button)findViewById(R.id.play);
    	stop =(Button)findViewById(R.id.stop);
    	ff =(Button)findViewById(R.id.ff);
    	tv =(TextView)findViewById(R.id.FILE_PATH);
    	
    	//버튼을 사용 못하게
    	rw.setEnabled(false);
    	play.setEnabled(false);
    	stop.setEnabled(false);
    	ff.setEnabled(false);
    	
    	//처음에는 load 버튼만 클릭 되도록 설정
        load.setOnClickListener(this);
        
    	     
               
    }

	@Override
	public void run() 
	{
		
		int current=0;
		
		while(mp!=null){
			try{
				//1초마다
				Thread.sleep(1000);
				//현제 위치값을 얻어옴
				current=mp.getCurrentPosition();
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if(mp.isPlaying()){
				mprogressbar.setProgress(current); 
			}
		}
	}

	@Override
	public void onClick(View v) {
		
		if(v.getId()==R.id.play){
			
			//음악이 실행되고 있다면
			if(mp.isPlaying())
			{
				mp.pause();
				play.setText("PLAY");
			}else{
				mp.start();
				play.setText("PAUSE");
			}
		}else if(v.getId()==R.id.stop){
			try{
				
				mp.stop();
				mp.prepare();
				mp.seekTo(0);
				mprogressbar.setProgress(0);
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}else if(v.getId()==R.id.rw){
			
			//현재 진행이 어디까지 되었는지 알아옴.
			int  current = mp.getCurrentPosition();
			
			if(current - 20000 < 0){
				//진행한 데이터값보다 적다면 처음으로 이동
				mp.seekTo(0);
			}else {
				
				mp.seekTo(current-20000);
			}
		}else if(v.getId()==R.id.ff){
			
			//현재 진행이 어디까지 되었는지 알아옴.
			int  current = mp.getCurrentPosition();
			//음악의 총길이
			int total=mp.getDuration();
			
			if(current + 20000 > total){
				//진행한 데이터값보다 크다면 끝으로 이동
				mp.seekTo(total);
			}else {
				
				mp.seekTo(current +20000);
			}
			
		}
		else if(v.getId() == R.id.LOAD_BTN ) // load 버튼이 클릭된 경
		{
			Intent i = new Intent(Mp3playerActivity.this,FileListActivity.class); // 리스트 뷰를담을 인텐트 생성
			startActivity(i); //시작
			
			//================================
			isLoaded = false; // 이미 mp3를 로드 했을경우라면 isLoaded변수가 true 이므로.
			rw.setEnabled(false);
	    	play.setEnabled(false);
	    	stop.setEnabled(false);
	    	ff.setEnabled(false);
	    	//=================================

	    	play.setText("PLAY"); //플레이버튼을 다시 play 텍스트로 설정
	    	tv.setText(""); // 파일 경로를 없앰.
	    	
	    	if( mp != null) //음악이 선택된것이 있을 때
	    	{
		    	if( mp.isPlaying() ) //현재 음악이 플레이중일떄
		    	{
		    		try{
			    		mp.stop();
						mp.prepare();
						mp.seekTo(0);
						mprogressbar.setProgress(0);
		    		}catch(Exception e)
		    		{
		    			e.printStackTrace();
		    		}
		    	}
	    	}
		}
		
			
	}
	//어플이 종료시 음악도 같이종료
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		if(mp !=null){
			mp.release();
		}
		super.onDestroy();
	}

	//추가됨
	@Override
	protected void onResume()  //다른 화면에 갔다가 다시 본 화면으로 돌아온 경우 자동으로 홀출됨
	{
		super.onResume();
		
		if ( isLoaded ) // mp3파일이 선택 되었을 경우. 
		{
			//버튼 활성화
			rw.setEnabled(true);
	    	play.setEnabled(true);
	    	stop.setEnabled(true);
	    	ff.setEnabled(true);
	    	tv.setText(mp3Path);
	    	
	    	//mp 객체 생성
	    	mp = new MediaPlayer();
	        
	    	
	    	//============================================
	    	 try{
		        	//실제mp3데이터를 얻어오는 메소드
		        	mp.setDataSource(mp3Path);
		        	//플레이 하기위해 준비상태
		        	mp.prepare();
		        	
		        	
		        	
		        	mprogressbar =(ProgressBar)findViewById(R.id.progressbar);
		        	
		        	new Thread(Mp3playerActivity.this).start();
		        	
		        	//프로그래스바 보여줌
		        	mprogressbar.setVisibility(ProgressBar.VISIBLE);
		        	//프로그래스바의 진행률을 초기화
		        	mprogressbar.setProgress(0);
		        	//해당음악 파일의 길이만큼
		        	
		        	mprogressbar.setMax(mp.getDuration());
		        	
		        	rw.setOnClickListener(this);
		        	play.setOnClickListener(this);
		        	stop.setOnClickListener(this);
		        	ff.setOnClickListener(this);
		        	
		        	
		        	mp.setOnCompletionListener(new OnCompletionListener() {
		    			
		    			@Override
		    			public void onCompletion(MediaPlayer mp) {
		    				//음악이 다 플레이 되었다면 초기화해줌
		    				play.setText("PLAY");
		    				mprogressbar.setProgress(0);
		    				
		    			}
		    		});
		     
		        }catch(Exception e){
		        	
		        	e.printStackTrace();
		        }
	    	
	    	
	    	
		}
		
	}
	
	
	
}


