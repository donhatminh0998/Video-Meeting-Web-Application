package com.example.Video.Meeting.Web.Application.common.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Slf4j
//Service class to handle file upload and delete
public class FileService {
    //Method to upload a file to the server
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception{

        UUID uuid = UUID.randomUUID(); // Generate a unique identifier for the file
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); // Get the file extension (e.g., .png)

        // Create the saved file name by appending the UUID to the original file's extension
        String savedFileName = uuid.toString() + extension;
        // Full file path where the file will be stored
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;

        // Create a file output stream to write the file data to the specified location
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);

        fos.write(fileData); // Write the file data to the file system
        fos.close(); // Close the output stream


        return savedFileName; // Return the saved file name
    }

    // Method to delete a file from the server
    public void deleteFile(String filePath) throws Exception{
        // Create a File object with the given file path
        File deleteFile = new File(filePath);

        if(deleteFile.exists()) {
            // Check if the file exists
            deleteFile.delete(); //Delete the file
            log.info("file deleted."); // Log success message
        } else {
            log.info("file not found."); // Log if the file was not found
        }
    }

}
