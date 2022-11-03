package ru.netology;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {
    private int productNum;
    private int amount;
    String[] listLog;
    private List<String[]> saveLog = new ArrayList<>();

    public int getProductNum() {
        return productNum;
    }

    public ClientLog() {
    }

    public ClientLog(int productNum, int amount) {
        this.productNum = productNum;
        this.amount = amount;
    }

    public List<String[]> getSaveLog() {
        return saveLog;
    }

    public List<String[]> log(int productNum, int amount) throws IOException {
        String s1 = Integer.toString(productNum + 1);
        String s2 = Integer.toString(amount);
        listLog = new String[]{s1, s2};
        saveLog.add(listLog);
        return saveLog;
    }

    public void exportAsCSV(File txtFile) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(txtFile, true),
                CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_ESCAPE_CHARACTER)) {
            if (txtFile.length() == 0) {
                String[] s = "productNum,amount".split(",");
                writer.writeNext(s);
            }
            for (String[] strings : getSaveLog()) {
                writer.writeNext(strings);
            }
        }
    }
}
