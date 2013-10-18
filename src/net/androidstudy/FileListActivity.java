package net.androidstudy;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Environment;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FileListActivity extends ListActivity
{
 
 	
 private List<String> item = null;
 private List<String> path = null;
 private String root;
 private TextView myPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myPath = (TextView)findViewById(R.id.path);
        
        root = Environment.getExternalStorageDirectory().getPath();
        
        getDir(root);
    }
    
    private void getDir(String dirPath)
    {
     myPath.setText("Location: " + dirPath);
     item = new ArrayList<String>();
     path = new ArrayList<String>();
     File f = new File(dirPath);
     File[] files = f.listFiles();
     
     if(!dirPath.equals(root))
     {
      item.add(root);
      path.add(root);
      item.add("../");
      path.add(f.getParent()); 
     }
     
     for(int i=0; i < files.length; i++)
     {
      File file = files[i];
      
      if(!file.isHidden() && file.canRead()){
       path.add(file.getPath());
          if(file.isDirectory()){
           item.add(file.getName() + "/");
          }else{
           item.add(file.getName());
          }
      } 
     }

     ArrayAdapter<String> fileList =
       new ArrayAdapter<String>(this, R.layout.row, item);
     setListAdapter(fileList); 
    }

 @Override
 protected void onListItemClick(ListView l, View v, int position, long id) {
  // TODO Auto-generated method stub
  File file = new File(path.get(position));
  
  
  if (file.isDirectory())
  {
   if(file.canRead())
   {
    getDir(path.get(position));
   }
   else
   {
    new AlertDialog.Builder(this)
     .setIcon(R.drawable.icon)
     .setTitle("[" + file.getName() + "] folder can't be read!")
     .setPositiveButton("OK", null).show(); 
   } 
  //파일인 경우
  }
  else 
  {
   new AlertDialog.Builder(this)
     .setIcon(R.drawable.icon)
     .setTitle("[" + path.get(position) + "]")
     .setPositiveButton("OK", null).show();

   //추가됨.
   //static 선언된 것은 클래스명.변수로 접근가능
  // static 변수에 대해 이해 필요 .
   Mp3playerActivity.mp3Path = path.get(position);   
   String ext = Mp3playerActivity.mp3Path.substring(Mp3playerActivity.mp3Path.lastIndexOf('.')+1, Mp3playerActivity.mp3Path.length());
   
   
   if(ext.equals("MP3") || ext.equals("mp3"))  // || --> or 
   {
	   Mp3playerActivity.isLoaded = true;	   
	   finish();
   
   }
   

  }

}
}