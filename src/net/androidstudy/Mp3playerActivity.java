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

//���α׷������� ������� ǥ���ϱ����ؼ� Runnable
public class Mp3playerActivity extends Activity implements Runnable , OnClickListener
{
    /** Called when the activity is first created. */
     // public static �� ���̸� ���������� �ȴ� --> ���� ��Ƽ��Ƽ���� ������ �� �ִ�.
	 public static Boolean isLoaded = false;
	 public static String mp3Path;
	 
	 MediaPlayer mp = null; // load ��ư�� Ŭ�� ���� �� ���� ���õ� ������ �ִ��� ������ ���θ� �Ǵ�
     TextView tv;
     //���ķ������� data�� ����Ű������
     ProgressBar mprogressbar;
     Button rw,play,stop,ff,load;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
		
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        //��ư, �ؽ�Ʈ�� ���� ���̵� ����
        load = (Button)findViewById(R.id.LOAD_BTN);
    	rw =(Button)findViewById(R.id.rw);
    	play =(Button)findViewById(R.id.play);
    	stop =(Button)findViewById(R.id.stop);
    	ff =(Button)findViewById(R.id.ff);
    	tv =(TextView)findViewById(R.id.FILE_PATH);
    	
    	//��ư�� ��� ���ϰ�
    	rw.setEnabled(false);
    	play.setEnabled(false);
    	stop.setEnabled(false);
    	ff.setEnabled(false);
    	
    	//ó������ load ��ư�� Ŭ�� �ǵ��� ����
        load.setOnClickListener(this);
        
    	     
               
    }

	@Override
	public void run() 
	{
		
		int current=0;
		
		while(mp!=null){
			try{
				//1�ʸ���
				Thread.sleep(1000);
				//���� ��ġ���� ����
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
			
			//������ ����ǰ� �ִٸ�
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
			
			//���� ������ ������ �Ǿ����� �˾ƿ�.
			int  current = mp.getCurrentPosition();
			
			if(current - 20000 < 0){
				//������ �����Ͱ����� ���ٸ� ó������ �̵�
				mp.seekTo(0);
			}else {
				
				mp.seekTo(current-20000);
			}
		}else if(v.getId()==R.id.ff){
			
			//���� ������ ������ �Ǿ����� �˾ƿ�.
			int  current = mp.getCurrentPosition();
			//������ �ѱ���
			int total=mp.getDuration();
			
			if(current + 20000 > total){
				//������ �����Ͱ����� ũ�ٸ� ������ �̵�
				mp.seekTo(total);
			}else {
				
				mp.seekTo(current +20000);
			}
			
		}
		else if(v.getId() == R.id.LOAD_BTN ) // load ��ư�� Ŭ���� ��
		{
			Intent i = new Intent(Mp3playerActivity.this,FileListActivity.class); // ����Ʈ �並���� ����Ʈ ����
			startActivity(i); //����
			
			//================================
			isLoaded = false; // �̹� mp3�� �ε� ��������� isLoaded������ true �̹Ƿ�.
			rw.setEnabled(false);
	    	play.setEnabled(false);
	    	stop.setEnabled(false);
	    	ff.setEnabled(false);
	    	//=================================

	    	play.setText("PLAY"); //�÷��̹�ư�� �ٽ� play �ؽ�Ʈ�� ����
	    	tv.setText(""); // ���� ��θ� ����.
	    	
	    	if( mp != null) //������ ���õȰ��� ���� ��
	    	{
		    	if( mp.isPlaying() ) //���� ������ �÷������ϋ�
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
	//������ ����� ���ǵ� ��������
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		if(mp !=null){
			mp.release();
		}
		super.onDestroy();
	}

	//�߰���
	@Override
	protected void onResume()  //�ٸ� ȭ�鿡 ���ٰ� �ٽ� �� ȭ������ ���ƿ� ��� �ڵ����� Ȧ���
	{
		super.onResume();
		
		if ( isLoaded ) // mp3������ ���� �Ǿ��� ���. 
		{
			//��ư Ȱ��ȭ
			rw.setEnabled(true);
	    	play.setEnabled(true);
	    	stop.setEnabled(true);
	    	ff.setEnabled(true);
	    	tv.setText(mp3Path);
	    	
	    	//mp ��ü ����
	    	mp = new MediaPlayer();
	        
	    	
	    	//============================================
	    	 try{
		        	//����mp3�����͸� ������ �޼ҵ�
		        	mp.setDataSource(mp3Path);
		        	//�÷��� �ϱ����� �غ����
		        	mp.prepare();
		        	
		        	
		        	
		        	mprogressbar =(ProgressBar)findViewById(R.id.progressbar);
		        	
		        	new Thread(Mp3playerActivity.this).start();
		        	
		        	//���α׷����� ������
		        	mprogressbar.setVisibility(ProgressBar.VISIBLE);
		        	//���α׷������� ������� �ʱ�ȭ
		        	mprogressbar.setProgress(0);
		        	//�ش����� ������ ���̸�ŭ
		        	
		        	mprogressbar.setMax(mp.getDuration());
		        	
		        	rw.setOnClickListener(this);
		        	play.setOnClickListener(this);
		        	stop.setOnClickListener(this);
		        	ff.setOnClickListener(this);
		        	
		        	
		        	mp.setOnCompletionListener(new OnCompletionListener() {
		    			
		    			@Override
		    			public void onCompletion(MediaPlayer mp) {
		    				//������ �� �÷��� �Ǿ��ٸ� �ʱ�ȭ����
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


