//package com.recruitmentsystem.file;
//
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.amazonaws.services.s3.model.S3Object;
//import com.amazonaws.services.s3.model.S3ObjectInputStream;
//import com.amazonaws.util.IOUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//@Service
//@Slf4j
//public class StorageService {
//
//    @Value("${application.bucket.name}")
//    private String bucketName;
//
//    @Autowired
//    private AmazonS3 s3Client;
//
//    public String uploadFile(MultipartFile file) {
//        File fileObj = convertMultiPartFileToFile(file);
//        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//        s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
//        fileObj.delete();
//        return "File uploaded : " + fileName;
//    }
//
//
//    public byte[] downloadFile(String fileName) {
//        S3Object s3Object = s3Client.getObject(bucketName, fileName);
//        S3ObjectInputStream inputStream = s3Object.getObjectContent();
//        try {
//            byte[] content = IOUtils.toByteArray(inputStream);
//            return content;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//    public String deleteFile(String fileName) {
//        s3Client.deleteObject(bucketName, fileName);
//        return fileName + " removed ...";
//    }
//
//
//    private File convertMultiPartFileToFile(MultipartFile file) {
//        File convertedFile = new File(file.getOriginalFilename());
//        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
//            fos.write(file.getBytes());
//        } catch (IOException e) {
//            log.error("Error converting multipartFile to file", e);
//        }
//        return convertedFile;
//    }
//
//    public void uploadImage(Integer customerId,
//                                           MultipartFile file) {
//        String profileImageId = UUID.randomUUID().toString();
//        try {
//            s3Service.putObject(
//                    s3Buckets.getCustomer(),
//                    "profile-images/%s/%s".formatted(customerId, profileImageId),
//                    file.getBytes()
//            );
//        } catch (IOException e) {
//            throw new RuntimeException("failed to upload profile image", e);
//        }
//        customerDao.updateCustomerProfileImageId(profileImageId, customerId);
//    }
//
//    public byte[] getCustomerProfileImage(Integer customerId) {
//        var customer = customerDao.selectCustomerById(customerId)
//                .map(customerDTOMapper)
//                .orElseThrow(() -> new ResourceNotFoundException(
//                        "customer with id [%s] not found".formatted(customerId)
//                ));
//
//        if (StringUtils.isBlank(customer.profileImageId())) {
//            throw new ResourceNotFoundException(
//                    "customer with id [%s] profile image not found".formatted(customerId));
//        }
//
//        byte[] profileImage = s3Service.getObject(
//                s3Buckets.getCustomer(),
//                "profile-images/%s/%s".formatted(customerId, customer.profileImageId())
//        );
//        return profileImage;
//    }
//}
