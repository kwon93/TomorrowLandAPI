package com.aaa.api.service.image;

import com.aaa.api.exception.FileDeleteFailException;
import com.aaa.api.exception.FileNotExistException;
import com.aaa.api.exception.ImageExtractFailException;
import com.aaa.api.exception.ImageStoreFailException;
import com.aaa.api.service.dto.request.ImageInfo;
import com.aaa.api.service.dto.response.ImageResourceResponse;
import com.aaa.api.service.dto.response.ImageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Primary
@Service
public class LocalImageStorageManager implements ImageStorageManager {

    @Value("${image.localPath}")
    private String localPath;

    @Override
    public ImageResponse uploadImage(final String fileName, final ImageInfo imageInfo) throws IOException {
        String filePath = extractFilePath(fileName);
        File baseFile = makeBaseFile(filePath);
        storeImageToBaseFile(imageInfo, baseFile);
        return ImageResponse.from(filePath);
    }

    @Override
    public ImageResourceResponse downloadImage(String imagePath) {
        try {
            byte[] bytes = extractStoredFileFrom(imagePath);
            return ImageResourceResponse.toLocalStorageResponse(bytes);
        } catch (IOException ioException) {
            throw new ImageExtractFailException(ioException);
        }
    }

    @Override
    public void deleteImage(String imagePath) {
        Path storedFilePath = Paths.get(imagePath);
        fileExistValidation(storedFilePath);
        deleteFileBy(storedFilePath);
    }

    private void storeImageToBaseFile(ImageInfo imageInfo, File storedFilePath) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(storedFilePath)) {
            fileOutputStream.write(imageInfo.extractByteImage());
        } catch (IOException ioException) {
            throw new ImageStoreFailException(ioException);
        }

    }

    private void deleteFileBy(Path storedFilePath) {
        try {
            Files.delete(storedFilePath);
        } catch (IOException e) {
            throw new FileDeleteFailException(e);
        }
    }

    private void fileExistValidation(Path storedFilePath) {
        if (isNotExistFile(storedFilePath)) {
            throw new FileNotExistException();
        }
    }

    private boolean isNotExistFile(Path storedFilePath) {
        return !Files.exists(storedFilePath);
    }

    private byte[] extractStoredFileFrom(String imagePath) throws IOException {
        return StreamUtils.copyToByteArray(new FileInputStream(imagePath));
    }

    private File makeBaseFile(String filePath) {
        return new File(filePath);
    }

    private String extractFilePath(String fileName) {
        return localPath + fileName;
    }

}
