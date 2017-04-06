/*
* Copyright (c) 2012,TerenceYang
* All rights reserved.
*
* UploadImageTask
* Summary:文件上传类
*
*Version:1.0
*@author: TerenceYang
* @Date: 2012-10-10
*/

package com.allen.music.views.view.video.ffmpeg;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;


/**
 * Asynchronous task to upload file to server
 * @author Terence
 */
public class UploadImageTask extends AsyncTask<String, Integer, Boolean> {

	private static final String UPLOAD_URL = VideoConfig.UPLOAD_URL;
	private PhotoUploadCallBack photoUploadCallBack;
    public interface PhotoUploadCallBack{
		public void uploadSuccess(String fileName);
	}
	private  final String TAG = UploadImageTask.class.getName();

    /**
	 * 文件上传构造函
	 * @param captureUploader
	 */
	public UploadImageTask(PhotoUploadCallBack captureUploader) {
		photoUploadCallBack = captureUploader;
		}
	  @Override
	    protected Boolean doInBackground(String... imagename) {
	    	Log.d(TAG, "begin do in background");
			File image = new File(imagename[0]);
			if(image.exists()==false) return false;
			Boolean b = doFileUpload(image, UPLOAD_URL);
			if(b){//delete image in folder
//			     boolean deleted = image.delete();
//			     Log.d(TAG, "file deleted "+deleted);
			}
	       if(photoUploadCallBack !=null)
	    	   photoUploadCallBack.uploadSuccess(imagename[0]);
	       
	        return true;
	    }


    /**
     * Upload given file to given url, using raw socket
     * @see http://stackoverflow.com/questions/4966910/androidhow-to-upload-mp3-file-to-http-server
     *
     * @param file The file to upload
     * @param uploadUrl The uri the file is to be uploaded
     *
     * @return boolean true is the upload succeeded
     */
    private boolean doFileUpload(File file, String uploadUrl) {
    	HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1*1024*65;
        try
        {
         // open a URL connection to the Servlet
         URL url = new URL(uploadUrl);
         // Open a HTTP connection to the URL
         conn = (HttpURLConnection) url.openConnection();
         FileInputStream fileInputStream = new FileInputStream(file );

         // Allow Inputs
         conn.setDoInput(true);
         // Allow Outputs
         conn.setDoOutput(true);
         // Don't use a cached copy.
         conn.setUseCaches(false);
         // Use a post method.
         conn.setRequestMethod("POST");
         conn.setRequestProperty("Connection", "Keep-Alive");
         conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
         dos = new DataOutputStream( conn.getOutputStream() );
         dos.writeBytes(twoHyphens + boundary + lineEnd);
         dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + file.getName() + "\"" + lineEnd);
         dos.writeBytes(lineEnd);
        
         // create a buffer of maximum size

         bytesAvailable = fileInputStream.available();
         bufferSize = Math.min(bytesAvailable, maxBufferSize);
         buffer = new byte[bufferSize];
         // read file and write it into form...
         bytesRead = fileInputStream.read(buffer, 0, bufferSize);
         while (bytesRead > 0)
         {
          dos.write(buffer, 0, bufferSize);
          bytesAvailable = fileInputStream.available();
          bufferSize = Math.min(bytesAvailable, maxBufferSize);
          bytesRead = fileInputStream.read(buffer, 0, bufferSize);
         }
         // send multipart form data necesssary after file data...
         dos.writeBytes(lineEnd);
         dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
         // close streams
         Log.d(TAG,"File is written");
         fileInputStream.close();
         dos.flush();
         dos.close();
        }
        catch (MalformedURLException ex)
        {
             Log.e(TAG, "error: " + ex.getMessage(), ex);
        }
        catch (IOException ioe)
        {
             Log.e(TAG, "error: " + ioe.getMessage(), ioe);
        }catch(Exception e){
        	e.printStackTrace();
        }
        //------------------ read the SERVER RESPONSE
        try {
              inStream = new DataInputStream ( conn.getInputStream() );
              String str= null;//inStream.readLine();

              if (( str = inStream.readLine()) != null)
              {
                   Log.d(TAG,"Server Response "+str);
              }
              inStream.close();
              return (conn.getResponseCode() == 200 && str.startsWith("success"));
              
        }
        catch (IOException ioex){
             Log.e(TAG, "error: " + ioex.getMessage(), ioex);
        }catch(Exception e){
        	e.printStackTrace();
        }
		return false;
    }
}