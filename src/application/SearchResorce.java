package application;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;

public class SearchResorce implements Runnable {

    private static Logger LOGGER = Logger.getLogger(SearchResorce.class);

    private String source = null;
    private HashSet<String> wordsSet = null;
    private Object monitor;

    public SearchResorce(String source, HashSet<String> wordsSet, Object monitor) {
        this.source = source;
        this.wordsSet = wordsSet;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        LOGGER.info("Поток " + Thread.currentThread().getName() + " запущен");
        startTheSearch();
    }

    /**
     * выполняет поиск искоемых слов из "wordSet" в ресурсе "source"
     */
    public void startTheSearch() {
        LOGGER.info("Программа выполняется поиск слов в ресурсе: " + this.source);

        StringBuffer sentenceBuffer = new StringBuffer();
        boolean containsWord = false;
        int pointIndex;

        Scanner scannerSource = getScannerSource(source);
        while (scannerSource.hasNext()) {
            String readWord = scannerSource.next();

            if ((pointIndex = endSentence(readWord)) > 1) {
                String firstWord = readWord.substring(0, pointIndex + 1);
                String secondWord = readWord.substring(pointIndex + 1).trim();
                containsWord = isHaveWord(firstWord, wordsSet);

                sentenceBuffer.append(readWord.substring(0, pointIndex + 1) + " ");

                if (containsWord) {
                    writeToResultFile(sentenceBuffer);
                }

                sentenceBuffer.setLength(0);
                sentenceBuffer.append(secondWord);
            } else {
                sentenceBuffer.append(readWord + " ");
            }
        }
        scannerSource.close();

    }

    /**
     * @param source адрес ресурса
     * @return Scanner c потоком для чтения ресурса
     */
    public Scanner getScannerSource(String source) {
        boolean httpBollean = (source.trim().indexOf("http")) == 0;
        boolean ftpBoollean = (source.trim().indexOf("http")) == 0;
        Scanner sourceReader = null;
        if (httpBollean || ftpBoollean) {
            try {
                sourceReader = new Scanner(new URL(source).openStream());
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        } else {
            try {
                sourceReader = new Scanner(new File(source));
            } catch (FileNotFoundException e) {
                LOGGER.error(e.getMessage());
            }
        }
        return sourceReader;
    }

    /**
     * записывает предложение в результирующий файл
     *
     * @param string записываемое предложение
     */
    public void writeToResultFile(StringBuffer string) {
        synchronized (monitor) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ReadFromFile.resultFile), true))) {
                writer.write(String.valueOf(string) + "\n");
                writer.flush();
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    /**
     * проверяет строку на наличие символов '.', '!', '?'
     *
     * @param word проверяемая строка
     * @return индекс символа конца предложения
     */
    private int endSentence(String word) {
        int pointIndex;
        if ((pointIndex = word.indexOf(".")) > -1 ||
                (pointIndex = word.indexOf("!")) > -1
                || (pointIndex = word.indexOf("?")) > -1) {
            return pointIndex;
        } else {
            return -1;
        }
    }

    /**
     * @param word проверяемое слово
     * @return true, если слово есть в списке искоемых слов, иначе false
     */
    private boolean isHaveWord(String word, HashSet<String> set) {
        word = word.replaceAll("\\p{Punct}", "").replaceAll("\n", "");
        String lowWord = word.toLowerCase();
        return set.contains(lowWord);
    }
}
