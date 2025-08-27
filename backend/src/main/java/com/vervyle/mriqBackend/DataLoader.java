package com.vervyle.mriqBackend;

import com.vervyle.mriqBackend.exception.EmptyDirectoryException;
import com.vervyle.mriqBackend.exception.IOProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class DataLoader {

    @Value("${data.url}")
    private String baseURL;

    public boolean directoryExists(String directoryName) {
        var path = Paths.get(baseURL, directoryName);
        return Files.exists(path);
    }

    public List<File> getAllFiles(String dir) {
        var path = Paths.get(baseURL, dir);
        File directory = new File(path.toUri());
        File[] files = directory.listFiles();
        if (files != null) {
            if (files.length != 0) {
                return Arrays.stream(files).toList();
            }
            else {
                throw new EmptyDirectoryException("No files in directory.");
            }
        }
        else {
            throw new EmptyDirectoryException("No files in directory.");
        }
    }

    public Resource getImages(String dir) {
        var files = getAllFiles(dir);
        try {
            File tempFile = File.createTempFile(dir, ".zip");
            tempFile.deleteOnExit();
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tempFile));

            for (File file : files) {
                if (file.isFile()) {
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zos.putNextEntry(zipEntry);

                    try (FileInputStream fis = new FileInputStream(file)) {
                        byte[] buffer = new byte[2048];
                        int len;
                        while ((len = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }
                    }
                    zos.closeEntry();
                }
            }
            zos.close();
            return new FileSystemResource(tempFile);
        }
        catch (IOException e) {
            throw new IOProcessingException(e.getMessage());
        }
    }
}
