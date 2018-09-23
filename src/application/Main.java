package application;

import org.apache.log4j.Logger;
import java.io.IOException;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.info("Запуск программы");
        Occurencies occurencies = new OccurenciesResource();
        try {
            occurencies.getOccurencies(ReadFromFile.getSources(), ReadFromFile.getWords(), ReadFromFile.resultFile);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

    }
}
