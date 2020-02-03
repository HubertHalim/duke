package duke.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * File storage object to handle storage in a local file
 * Implements storage interface
 */
public class FileStorage implements Storage {

    private String fileName;

    /**
     * File storage constructor
     * @param fileName
     */
    public FileStorage(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Create storage if it doesn't exist
     * @throws IOException
     */
    public void createStorage() throws IOException {
        //relative path
        String relativePath = fileName;
        File file = new File(relativePath);
        if (file.createNewFile()) {
            System.out.println(relativePath + " Save File Created");
        } else System.out.println("File " + relativePath + " already exists");
    }

    /**
     * Check if file that is to be created already exists
     * @return
     */
    public boolean checkFileExistence() {
        //relative path
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Load data from storage file
     * @return data in the form of a string
     * @throws IOException
     */
    public String loadStorage() throws IOException {
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        int content;
        String result = "";
        while ((content = fis.read()) != -1) {
            result = result + ((char) content);
        }
        return result;
    }

    /**
     * Store data to storage file
     * @param data in the form of a string
     * @throws IOException
     */
    public void storeData(String data) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.write(data.getBytes());
        fos.flush();
        fos.close();
    }
}