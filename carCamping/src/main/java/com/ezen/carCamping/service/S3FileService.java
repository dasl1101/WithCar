package com.ezen.carCamping.service;
import com.amazonaws.AmazonClientException;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.UUID;


@Slf4j
@Service 
public class S3FileService {
    private static final Logger logger = LoggerFactory.getLogger(S3FileService.class);
	  // 버킷 이름 동적 할당 - application.properties에서 가져옴
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 버킷 주소 동적 할당
    @Value("${cloud.aws.s3.bucket.url}")
    private String defaultUrl;
  
    private final AmazonS3Client amazonS3Client; //s3 전송객체를 만드는 클래스
    @Autowired
    public S3FileService(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    public String upload(MultipartFile uploadFile) throws IOException {
        String origName = uploadFile.getOriginalFilename();
        String url;
        try {
            // 확장자를 찾기 위한 코드
            final String ext = origName.substring(origName.lastIndexOf('.'));
            // 파일이름 암호화
            final String saveFileName = getUuid() + ext;
            // 파일 객체 생성
            // System.getProperty => 시스템 환경에 관한 정보를 얻을 수 있다. (user.dir = 현재 작업 디렉토리를 의미함)
            File file = new File(System.getProperty("user.dir") + saveFileName);
           
            // 파일 변환
            uploadFile.transferTo(file);
            
            url =calcPath()+ saveFileName;
            // S3 파일 업로드
            uploadOnS3(url, file);
         
            // 파일 삭제
            file.delete();
        } catch (StringIndexOutOfBoundsException e) {
            url = null;
        }
        return url; // 아마존
    }

    private static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private void uploadOnS3(final String findName, final File file) {
        // AWS S3 전송 객체 생성
        final TransferManager transferManager = new TransferManager(this.amazonS3Client);
        // 요청 객체 생성
        final PutObjectRequest request = new PutObjectRequest(bucket, findName, file);
        // 업로드 시도
        final Upload upload =  transferManager.upload(request);
        
        try {
            upload.waitForCompletion();
        } catch (AmazonClientException amazonClientException) {
        	logger.error(amazonClientException.getMessage());
        } catch (InterruptedException e) {
        	logger.error(e.getMessage());
        }
    }
    
    public void deleteImage(String fileName) {
    	amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    private static String calcPath() {

		Calendar cal = Calendar.getInstance();

		String yearPath = String.valueOf(cal.get(Calendar.YEAR));

		String monthPath = yearPath + "." + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1);

		String datePath = monthPath + "." + new DecimalFormat("00").format(cal.get(Calendar.DATE))+"/";

		return datePath;
	}

}
