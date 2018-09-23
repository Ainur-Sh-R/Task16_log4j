package application;

import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadFromFile {

    private static Logger LOGGER = Logger.getLogger(ReadFromFile.class);

    private static String sourcesFile = "src\\res\\Sources.txt";
    private static String wordsFile = "src\\res\\Words.txt";
    public static final String resultFile = "src\\res\\ResultOfSearch.txt";

    /**
     * @return массив адресов ресурсов
     */
    public static String[] getSources(){
        return getArrayFromFile(sourcesFile);
    }

    /**
     * @return массив слов
     */
    public static String[] getWords(){
        String[] lines = getArrayFromFile(wordsFile);
        ArrayList<String> wordList = new ArrayList<>();
        for (String wordLine : lines){
            wordLine = wordLine.replaceAll("\\p{Punct}", "").replaceAll("\n", "");
            wordLine = wordLine.toLowerCase();
            String[] arrayWords = wordLine.split(" ");
            wordList.addAll(Arrays.asList(arrayWords));
        }
        String[] wordsAray = new String[wordList.size()];
        wordList.toArray(wordsAray);
        return  wordsAray;
    }


    /**
     * @param file имя файла
     * @return массив строк c файла
     */
    private static String[] getArrayFromFile(String file) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader
                (new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        String[] sources = new String[lines.size()];
        lines.toArray(sources);

        return sources;
    }


    /**
     * назначение sourcesFile
     * @param sourcesFile имя файла с описанием адресов ресурсов
     */
    public static void setSourcesFile(String sourcesFile) {
        ReadFromFile.sourcesFile = sourcesFile;
    }

    /**
     * назначение wordsFile
     * @param wordsFile имя файла с искоемыми словами
     */
    public static void setWordsFile(String wordsFile) {
        ReadFromFile.wordsFile = wordsFile;
    }


    /**
     * записывает имена файлов из массива в файл "sourcesFile"
     * @param fieldName путь к папке с файлами
     */
    public static void writeToSourcesFile(String fieldName) {
        String[] strings = getFileNames(fieldName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ReadFromFile.sourcesFile)))) {
            for (String string : strings) {
                writer.write(string + "\n");
                writer.flush();
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * @param fieldName путь к папке с файлами
     * @return массив имен файлов
     */
    private static String[] getFileNames(String fieldName) {
        File folder = new File(fieldName);
        File[] listOfFiles = folder.listFiles();
        List<String> results = new ArrayList<>();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                results.add(fieldName + listOfFiles[i].getName());
            }
        }
        return results.toArray(new String[0]);
    }

}
