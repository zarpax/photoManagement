package com.juanan.photoManagement.clientTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Test2 {

	public static void main(String... argv) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost uploadFile = new HttpPost("http://localhost:8080/fileService/upload");
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		//builder.addTextBody("message", "yes", ContentType.TEXT_PLAIN);

		// This attaches the file to the POST:
		File f = new File("D:/Amigasw.jpg");
		builder.addBinaryBody(
		    "file",
		    new FileInputStream(f),
		    ContentType.APPLICATION_OCTET_STREAM,
		    f.getName()
		);

		HttpEntity multipart = builder.build();
		uploadFile.setEntity(multipart);
		CloseableHttpResponse response = httpClient.execute(uploadFile);
		HttpEntity responseEntity = response.getEntity();	    
	}
}
