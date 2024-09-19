package com.check_point.users_managment.utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;




public class FileUtil {

    public static void saveToFile(String path, String fileNameAndExtension, Object object) throws IOException {
        ObjectMapper objectMapper =  new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }
        String fullPath = path + fileNameAndExtension;
        objectMapper.writeValue(new File(fullPath), object);
    }



    public static <T> T fetchSpecificFileFromHardDrive(String path, String fileName, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper =  new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }
        String fullPath = path + fileName;
        return objectMapper.readValue(new File(fullPath), clazz);
    }



    public static <T> T fetchAnyFileFromHardDrive(String path, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper =  new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            throw new IOException("No files found in the directory: " + path);
        }
        File fileToRead = files[0];
        return objectMapper.readValue(fileToRead, clazz);
    }


    public static boolean deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            file.delete();
        }catch (Exception e){
            System.out.println("Failed to delete file");
            return false;
        }
        return true;
    }

    public static boolean deleteFile(String path, String fileName) {
        try {
            File file = new File(path + fileName);
            file.delete();
        }catch (Exception e){
            System.out.println("Failed to delete file");
            return false;
        }
        return true;
    }

}
