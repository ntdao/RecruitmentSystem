package com.recruitmentsystem.file;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService {
    // tạo thư mục nếu không tồn tại
    // lưu tệp đã tải lên từ đối tượng MultipartFile vào một tệp trong hệ thống tệp.
    public String uploadImage(MultipartFile file, String fileDir, String root){
        // 1. Check if image is not empty
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file [" + file.getSize() + "]");
        }
        // 2. If file is an image

        // 3. Grab some metadata from file if any
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println(fileName);

        // Get the file extension
        String extension = FilenameUtils.getExtension(fileName);
        fileName = UUID.randomUUID() + "." + extension;
        System.out.println(fileName);

        String uploadDir = root + fileDir;
        String filePath = null;
        try {
            filePath = saveFile(uploadDir, fileName, file);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        System.out.println(filePath);

        return fileDir + fileName;
    }
    private String saveFile(String uploadDir, String fileName,
                                  MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
//            System.out.println(filePath.toString());
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
//            System.out.println(filePath.toString());
            return filePath.toString();
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }
}
