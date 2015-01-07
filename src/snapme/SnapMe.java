/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package snapme;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import org.apache.http.HttpEntity; 
import org.apache.http.HttpResponse; 
import org.apache.http.client.ClientProtocolException; 
import org.apache.http.client.HttpClient; 
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost; 
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity; 
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody; 
import org.apache.http.entity.mime.content.StringBody; 
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient; 
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils; 


/**
 *
 * @author hirayami
 */
public class SnapMe {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Status st = new Status();
        
        Thread alt = new Thread(new ActionListener(st));
        alt.start();
        
        Thread eg = new Thread(new EnvironmentGrabber(st));
        eg.start();
        /*try {
            Webcam webcam = Webcam.getDefault();
            webcam.open();
            ImageIO.write(webcam.getImage(), "PNG", new File("hello-world.png"));
            webcam.close();
        } catch (IOException ex) {
            //Logger.getLogger(SnapMe.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    
}

class Status
{
    boolean action;
    int delay;
    
    Status() {
        action = false;
        delay = 15;
    }
    
    public void setAction(boolean action) {
        this.action = action;
    }
    
    public boolean getAction() {
        return this.action;
    }

    void setDelay(int second) {
        this.delay = second;
    }
}

class ActionListener implements Runnable
{
    Status status;
    
    ActionListener(Status status) {
        this.status = status;
        //System.out.println(this.status);
    }
    
    @Override
    public void run() {
        try {
            while(true) {
                URL url = new URL("http://localhost/html/snap-me-web/");
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                String strTemp = "";
                while (null != (strTemp = br.readLine())) {
                    //System.out.println(strTemp);
                    String[] config = strTemp.split(":");
                    switch (config[0]) {
                        case "action":
                            switch (config[1]) {
                                case "start":
                                    this.status.setAction(true);
                                    break;
                                case "stop":
                                    this.status.setAction(false);
                                    break;
                            }   break;
                        case "delay":
                            this.status.setDelay(Integer.parseInt(config[1]));
                            break;
                    }
                }

                Thread.sleep(5000);
            }
        } catch (InterruptedException ex) {
                Logger.getLogger(ActionListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ActionListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ActionListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class EnvironmentGrabber implements Runnable
{
    Status status;
    public EnvironmentGrabber(Status status) {
        this.status = status;
    }
    
    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(this.status.delay*1000);
                if (this.status.getAction()) {
                    System.out.println("Getting env");
                    Webcam webcam = Webcam.getDefault();
                    if (webcam!=null) {
                        if (webcam.open()) 
                        {
                            ImageIO.write(webcam.getImage(), "PNG", new File("hello-world.png"));
                            webcam.close();
                            uploadData();
                        }
                    }
                }
            } catch (NullPointerException ne) {
                
            } catch (InterruptedException ex) {
                Logger.getLogger(EnvironmentGrabber.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(EnvironmentGrabber.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WebcamException we) {
                
            }
            finally {
                
            }
        }
    }

    private void uploadData() {
        
        String url = "http://localhost/html/snap-me-web/upload.php";
        File file = new File("hello-world.png");
        FileUploader fileUploader = new FileUploader();
        try {
            fileUploader.upload( file, url);
        } catch (IOException ex) {
            Logger.getLogger(EnvironmentGrabber.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
