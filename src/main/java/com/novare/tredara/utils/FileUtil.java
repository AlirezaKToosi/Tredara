package com.novare.tredara.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

public final class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    private FileUtil() {
        throw new IllegalStateException("FileUtil class");
    }

    public static String getExtension(InputStream inputStream) throws IOException {
        try{
            return URLConnection.guessContentTypeFromStream(inputStream).split("/")[1];
        }catch (IOException e){
            logger.error("Could not determine file extension.");
            throw  new IOException("FileUtil has an error, cannot retrieve extension file");
        }
    }

    public static String getStorageFileName(InputStream inputStream) throws IOException {
        String randomFileName = String.valueOf(System.currentTimeMillis());
        String extension = FileUtil.getExtension(inputStream);
        return randomFileName + "." + extension;
    }

    /*
     * This method extracts the argument images | string, you must get the data after data:image\/png;base64,
     * */
    public static String getImageFromBase64(String base64Image){
        try {
            return base64Image.substring(base64Image.indexOf(",") + 1);
        }catch (Exception e){
            logger.error("Could not extract the image string.");
            return null;
        }
    }

}