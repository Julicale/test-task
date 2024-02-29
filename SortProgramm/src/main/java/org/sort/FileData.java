package org.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileData {//класс для сохранения лисстов с типаи данных
    private List<Integer> integersList = new ArrayList<>();
    private List<String> stringsList = new ArrayList<>();
    private List<Float> floatsList = new ArrayList<>();

    public FileData() {
    }

    public List<Integer> getIntegersList() {
        return integersList;
    }

    public void setIntegersList(List<Integer> integersList) {
        this.integersList = integersList;
    }

    public List<String> getStringsList() {
        return stringsList;
    }

    public void setStringsList(List<String> stringsList) {
        this.stringsList = stringsList;
    }

    public List<Float> getFloatsList() {
        return floatsList;
    }

    public void setFloatsList(List<Float> floatsList) {
        this.floatsList = floatsList;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.floatsList.toArray()) + "\n"
                + Arrays.toString(this.stringsList.toArray()) + "\n"
                + Arrays.toString(this.integersList.toArray());
    }
}
