package com.epam.izh.rd.online.repository;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class SimpleFileRepository implements FileRepository {

    /**
     * Метод рекурсивно подсчитывает количество файлов в директории
     *
     * @param path путь до директори
     * @return файлов, в том числе скрытых
     */
    @Override
    public long countFilesInDirectory(String path) {
        long count = 0;
        //Получаем абсолютный путь до директории path
        String absolutePath = getAbsolutePath(path);
        File dir = new File(absolutePath);
        //Проверяем существует ли файл/директория по
        // пути absolutePath и является ли она директорией
        if (dir.exists() && dir.isDirectory()) {
            // Получаем массив объектов в директории
            File[] arrayFileOrDir = dir.listFiles();
            for (File fileOrDir : arrayFileOrDir) {
                if (fileOrDir.exists()) {
                    // Если элемент массива является директорией
                    // рекурсивно вызываем метод, иначе считаем за файл
                    if (fileOrDir.isDirectory()) {
                        count += this.countFilesInDirectory(path + File.separatorChar + fileOrDir.getName());
                    } else {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Метод рекурсивно подсчитывает количество папок в директории, считая корень
     *
     * @param path путь до директории
     * @return число папок
     */
    @Override
    public long countDirsInDirectory(String path) {
        long count = 0;
        //Получаем абсолютный путь до директории path
        String absolutePath = getAbsolutePath(path);
        File dir = new File(absolutePath);
        //Проверяем существует ли файл/директория по
        // пути absolutePath и является ли она директорией
        if (dir.exists() && dir.isDirectory()) {
            //считаем корневую директорию
            count++;
            // Получаем массив объектов в директории
            File[] arrayFileOrDir = dir.listFiles();
            for (File fileOrDir : arrayFileOrDir) {
                if (fileOrDir.exists()) {
                    // Если элемент массива является директорией
                    // рекурсивно вызываем метод
                    if (fileOrDir.isDirectory()) {
                        count += this.countDirsInDirectory(path + File.separatorChar + fileOrDir.getName());
                    }
                }
            }
        }
        return count;
    }

    /**
     * Метод копирует все файлы с расширением .txt
     *
     * @param from путь откуда
     * @param to   путь куда
     */
    @Override
    public void copyTXTFiles(String from, String to) {
        //Получаем абсолютный пути директорий
        String absolutePathFrom = getAbsolutePath(from);
        String absolutePathTo = getAbsolutePath(to);
        File dir = new File(absolutePathTo);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
        }
        //получаем массив файлов в исходной директрории
        File[] listFiles = new File(absolutePathFrom).listFiles();
        for (File file : listFiles) {
            //Проверяем является ли file файлом и проверяем его расширение .txt
            if (file.isFile() && file.getName().length() > 4
                    && file.getName().substring(file.getName().length() - 4).equals(".txt")) {

                try (FileReader fileReader = new FileReader(absolutePathFrom + File.separatorChar + file.getName());
                     BufferedReader bufferedReader = new BufferedReader(fileReader);
                     FileWriter fileWriter = new FileWriter(absolutePathTo + File.separatorChar + file.getName());
                     BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                    char[] array = new char[1024];
                    int length;
                    while ((length = bufferedReader.read(array, 0, array.length)) != -1) {
                        bufferedWriter.write(array, 0, length);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Метод создает файл на диске с расширением txt
     *
     * @param path путь до нового файла
     * @param name имя файла
     * @return был ли создан файл
     */
    @Override
    public boolean createFile(String path, String name) {
        // в данном методе используется получение пути через
        // ClassLoader т.к. в тесте используется он же и тест
        // проверяет наличие файла в папке target/test-classes
        ClassLoader classLoader = getClass().getClassLoader();
        URL resources = classLoader.getResource("");
        //Получаем абсолютный путь до директории path
        String absolutePath = new File(resources.getFile()).getAbsolutePath() + File.separatorChar + path;
        // Использован URL декодер, т.к. в пути к директории были русские буквы
        // которые приводили к тому что при создании экземпляров класса File
        // конструктор не понимал закодированных русских букв
        try {
            absolutePath = URLDecoder.decode(absolutePath, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //Получаем абсолютный путь до директории path
        File dir = new File(absolutePath);
        try {
            if (!dir.exists()) {
                dir.mkdir();
            }
            File newFile = new File(absolutePath + File.separatorChar + name);
            return newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Метод считывает тело файла .txt из папки src/main/resources
     *
     * @param fileName имя файла
     * @return контент
     */
    @Override
    public String readFileFromResources(String fileName) {
        if (fileName != null) {
            //Получаем абсолютный путь до файла fileName
            String absolutePath = getAbsolutePath(fileName);
            try (FileReader fileReader = new FileReader(absolutePath);
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String result = bufferedReader.readLine();
                return result;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "";
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    // Выделил в отдельный метод получение абсолютного пути
    // данный код использовался в каждом методе класса
    // кроме createFile
    private String getAbsolutePath(String dirOrFile) {
        char separator = File.separatorChar;
        if (dirOrFile == null) {
            return "";
        } else {
            return new File("").getAbsolutePath() + separator
                    + "src" + separator + "main" + separator + "resources" + separator + dirOrFile;
        }
    }
}
