package com.epam.izh.rd.online;

import com.epam.izh.rd.online.repository.FileRepository;
import com.epam.izh.rd.online.repository.SimpleFileRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileRepositoryTest {

    private static final String TEST_DIR_COUNT_PATH = "testDirCountFiles";
    private static final String TEST_DIR_CREATE_PATH = "testDirCreateFile";
    private static final String TEST_FILE_TO_CREATE = "newFile.txt";
    private static final String TEST_FILE_TO_COPY_ONE = "readme.txt";
    private static final String TEST_FILE_TO_COPY_TWO = "sensitive_data.txt";

    private static FileRepository fileRepository;

    @BeforeAll
    static void setup() {
        fileRepository = new SimpleFileRepository();
    }

    @BeforeEach
    void clean() {
        deleteFile(getFile(TEST_DIR_CREATE_PATH + "/" + TEST_FILE_TO_CREATE));
        deleteFile(getFile(TEST_DIR_CREATE_PATH + "/" + TEST_FILE_TO_COPY_ONE));
        deleteFile(getFile(TEST_DIR_CREATE_PATH + "/" + TEST_FILE_TO_COPY_TWO));
    }

    private void deleteFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    @DisplayName("Тест метода FileRepository.countDirsInDirectory(String path)")
    void testCountDirsInDirectory() {
        assertEquals(7, fileRepository.countDirsInDirectory(TEST_DIR_COUNT_PATH));
    }

    @Test
    @DisplayName("Тест метода FileRepository.countFilesInDirectory(String path)")
    void testCountFilesInDirectory() {
        assertEquals(10, fileRepository.countFilesInDirectory(TEST_DIR_COUNT_PATH));
    }

    @Test
    @DisplayName("Тест метода FileRepository.createFile(String path)")
    void testCreateFile() {
        fileRepository.createFile(TEST_DIR_CREATE_PATH, TEST_FILE_TO_CREATE);
        assertTrue(getFile(TEST_DIR_CREATE_PATH + "/" + TEST_FILE_TO_CREATE).exists());
    }

    @Test
    @DisplayName("Тест метода FileRepository.readFileFromResources(String fileName)")
    void testReadFileFromResources() {
        assertEquals("Ya-hoo!", fileRepository.readFileFromResources("readme.txt"));
    }

    @Test
    @DisplayName("Тест метода FileRepository.copyTXTFiles(String from, String to)")
    void testCopyTXTFiles() {
        fileRepository.copyTXTFiles("", TEST_DIR_CREATE_PATH);
        assertTrue(compareTxtFiles("", TEST_DIR_CREATE_PATH));
    }


    private File getFile(String path) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(path);
        if (resource != null) {
            // Использован URL декодер, т.к. если в пути к директории есть русские буквы
            // которые приведут к тому что при создании экземпляров класса File
            // конструктор не будет понимать закодированных русских букв
            try {
                return new File(URLDecoder.decode(resource.getFile(), StandardCharsets.UTF_8.name()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return new File("");
            }
        }
        return new File("");
    }

    private boolean compareTxtFiles(String from, String to) {
        char separator = File.separatorChar;
        //Получаем абсолютный пути директорий
        String absolutePathFrom = new File("").getAbsolutePath()
                + separator + "src" + separator + "main" + separator
                + "resources" + separator + from;
        String absolutePathTo = new File("").getAbsolutePath()
                + separator + "src" + separator + "main" + separator
                + "resources" + separator + to;
        File dir = new File(absolutePathTo);
        // проверяем существует ли директория для копирования
        if (!dir.exists() || !dir.isDirectory()) {
            return false;
        } else {
            //получаем массив файлов в исходной директрории
            File[] listFiles = new File(absolutePathFrom).listFiles();
            for (File file : listFiles) {
                //Проверяем является ли file файлом и проверяем его расширение .txt
                if (file.isFile() && file.getName().length() > 4
                        && file.getName().substring(file.getName().length() - 4).equals(".txt")) {

                    try (FileReader fileReaderFrom = new FileReader(absolutePathFrom + separator + file.getName());
                         BufferedReader bufferedReaderFrom = new BufferedReader(fileReaderFrom);
                         FileReader fileReaderTo = new FileReader(absolutePathTo + separator + file.getName());
                         BufferedReader bufferedReaderTo = new BufferedReader(fileReaderTo)) {
                        char[] arrayFrom = new char[1024];
                        char[] arrayTo = new char[1024];
                        int lengthFrom;
                        //считывать файлы построчно до их конца, либо до первого не соответствия
                        // в случае не соответствия вернуть false
                        while ((lengthFrom = bufferedReaderFrom.read(arrayFrom, 0, arrayFrom.length))
                                == (bufferedReaderTo.read(arrayTo, 0, arrayTo.length))
                                && lengthFrom != -1) {
                            if (!Arrays.equals(arrayFrom, arrayTo)) {
                                return false;
                            }
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return false;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        }
        //Если все проверки прошли - возвращаем true
        return true;
    }
}
