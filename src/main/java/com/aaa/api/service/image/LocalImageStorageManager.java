package com.aaa.api.service.image;

import com.aaa.api.exception.ImageStoreFailException;
import com.aaa.api.service.dto.request.ImageInfo;
import com.aaa.api.service.dto.response.ImageResourceResponse;
import com.aaa.api.service.dto.response.ImageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Primary
public class LocalImageStorageManager implements ImageStorageManager {

    @Value("${image.localPath}")
    private String localPath;

    @Override
    public ImageResponse uploadImage(final String fileName, final ImageInfo imageInfo) throws IOException {
        String filePath = localPath + fileName;
        File storedFilePath = new File(filePath);

        try (FileOutputStream fileOutputStream = new FileOutputStream(storedFilePath)) {
            fileOutputStream.write(imageInfo.extractByteImage());
        } catch (IOException e) {
            throw new ImageStoreFailException();
        }

        return ImageResponse.from(filePath);
    }

    @Override
    public ImageResourceResponse downloadImage(String imagePath) {
        return null;
    }

    @Override
    public void deleteImage(String fileName) {
        // TODO this method not complete
    }
    
}
