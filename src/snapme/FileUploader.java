/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snapme;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author dipal
 */
public class FileUploader {
    public void upload(File file, String url) throws IOException{
        
            InputStream fileContent = new FileInputStream(file);
            String fileContentType = "application/octet-stream"; // Or whatever specific.
            String fileName = file.getName();
            
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext httpContext = new BasicHttpContext();
            httpContext.setAttribute(ClientContext.COOKIE_STORE, new BasicCookieStore());
        
            HttpPost httpPost = new HttpPost(url);
            MultipartEntity entity = new MultipartEntity();
            entity.addPart("uploadedfile", new InputStreamBody(fileContent, fileContentType, fileName));
            entity.addPart("hi", new StringBody("world"));
            httpPost.setEntity(entity);
            HttpResponse postResponse = httpClient.execute(httpPost, httpContext);
            
            System.out.println(EntityUtils.toString(postResponse.getEntity()));
    }
}
