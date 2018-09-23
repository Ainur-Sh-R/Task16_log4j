package application;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class OccurenciesResource implements Occurencies {

    private static Logger LOGGER = Logger.getLogger(OccurenciesResource.class);

    @Override
    public void getOccurencies(String[] sources, String[] words, String res) throws IOException {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Программа начала работу");

        clearFile(res);
        HashSet<String> wSet = new HashSet<>(Arrays.asList(words));
        Object monitor = new Object();

        ExecutorService executorService = Executors.newFixedThreadPool(8);

        for (String source : sources) {
            SearchResorce searchResorce = new SearchResorce(source, wSet, monitor);
            executorService.execute(searchResorce);
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        }

        long finishTime = (System.currentTimeMillis() - startTime);
        LOGGER.info("Программа завершила работу: " + (finishTime/1000 )+ " секунд");
    }


    /**
     * выполняет очистку файла
     *
     * @param filename имя файла
     */
    private void clearFile(String filename) throws IOException {
        FileOutputStream writer = null;
        writer = new FileOutputStream(filename);
        writer.write(("Произведена очистка файла" + "\n").getBytes());
        LOGGER.info("Произведена очистка файла");
        writer.close();
    }

}
