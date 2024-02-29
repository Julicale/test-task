package org.sort;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        int argsSize = args.length;
        String prefix = "";
        boolean canAppend = false;
        /*
         * путь куда сохранить файлы
         */
        String pathSave = null;
        FileData filesData = new FileData();

        try {
            pathSave = new File(".").getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<String> filePaths = getPaths(args);

        if(filePaths != null && !filePaths.isEmpty()){
            for (String path : filePaths){
                FileData fileData = readFile(path);

                if(fileData != null){
                    filesData.getIntegersList().addAll(fileData.getIntegersList());
                    filesData.getStringsList().addAll(fileData.getStringsList());
                    filesData.getFloatsList().addAll(fileData.getFloatsList());
                }
            }
        }else {
            print("Не задан ни один путь к файлу");
            return;
        }

        for (int i = 0; i<argsSize; i++) {
            switch (args[i]){
                case "-a": canAppend = true; break;//добавляет к существующему файлу данные
                case "-o"://создать путь для сохранения
                    if (i+1 < argsSize){
                        pathSave = args[i+1];
                    }
                    break;
                case "-p"://создать префикс для названия файла
                    if (i+1 < argsSize){
                        prefix = args[i + 1];
                    }
                    break;
                case "-s"://короткая статистика

                    if(filesData.getIntegersList().isEmpty()){
                        print("Данных Integer нет");
                    }
                    else print("Количество элементов в файле Integer " + filesData.getIntegersList().size());

                    if(filesData.getStringsList().isEmpty()){
                        print("Данных Strings нет");
                    }
                    else print("Количество элементов в файле String " + filesData.getStringsList().size());

                    if(filesData.getFloatsList().isEmpty()){
                        print("Данных Floats нет");
                    }
                    else print("Количество элементов в файле Float " + filesData.getFloatsList().size());
                    break;
                case "-f"://полная статистика
                    //полная статистика Integers
                    print("Максимальное значение в Integers = " + Collections.max(filesData.getIntegersList()));
                    print("Минимальное значение в Integers = " + Collections.min(filesData.getIntegersList()));
                    int sumIntegers = 0;
                    for (int d = 0; d < filesData.getIntegersList().size(); d++){
                        sumIntegers = sumIntegers + filesData.getIntegersList().get(d);
                    }
                    print("Сумма Integers = " + sumIntegers);
                    int averageNumber = 0;
                    averageNumber = sumIntegers / filesData.getIntegersList().size();
                    print("Среднее число Integers = " + averageNumber);

                    //полная статистика Float
                    print("Максимальное значение в Float = " + Collections.max(filesData.getFloatsList()));
                    print("Минимальное значение в Float = " + Collections.min(filesData.getFloatsList()));
                    float sumFloat = 0;
                    for (int d = 0; d < filesData.getFloatsList().size(); d++){
                        sumFloat = sumFloat + filesData.getFloatsList().get(d);
                    }
                    print("Сумма Float = " + sumFloat);
                    float averageFloat = 0;
                    averageFloat = sumFloat / filesData.getFloatsList().size();
                    print("Среднее число Float = " + averageFloat);
                    //полная статистика String
                    String longString = filesData.getStringsList().stream().max(Comparator.comparingInt(String::length)).get();
                    String shortString = filesData.getStringsList().stream().min(Comparator.comparingInt(String::length)).get();
                    print("Самая длинная строка " + " \"" + longString + "\" ");
                    print("Самая короткая строка " + " \"" + shortString + "\" ");
                    break;
                default:
                    break;
            }
        }
        saveFiles(filesData, pathSave, prefix, canAppend);//сохранение файлов
    }

    /**
     *
     * @param args массив вводимых данных
     * @return из массива нвходим путь к файлам и запоминаем
     */
    private static List<String> getPaths(String[] args){
        List<String> filePaths = new ArrayList<>();

        for (int j = 0; j < args.length; j++){
            if(args[j].contains(".txt")){
                filePaths.add(args[j]);
            }
        }
        return filePaths;
    }

    private static void print(String text){
        System.out.println(text);
    }

    /**
     *
     * @param path путь к файлу
     * @return возвращается список класса FileData
     */
    private static FileData readFile(String path) throws FileNotFoundException {

        File file = new File(path);
        Scanner scanner;
        Pattern patternInteger = Pattern.compile("^\\d+$");
        Pattern patternString = Pattern.compile("[A-z]|[А-я]");
        Pattern patternFloat = Pattern.compile("\\d[.,]\\d+[Ee\\-]+\\d+|\\d[.,]\\d+");

        FileData fileData = new FileData();

        if(!file.exists()){
            print("Файл ("+ path +")  не найден");
            return null;
        }
        else {
            scanner = new Scanner(file);
            ArrayList<String> inputData = new ArrayList<String>();//строки для данныйх из файла
            while (scanner.hasNextLine()) {
                inputData.add(scanner.nextLine());
            }
            for (int j = 0; j < inputData.size(); j++) {

                Matcher matcherInteger = patternInteger.matcher(inputData.get(j));
                Matcher matcherString = patternString.matcher(inputData.get(j));
                Matcher matcherFloat = patternFloat.matcher(inputData.get(j));

                if (matcherInteger.find()) {
                    float f = Float.parseFloat(inputData.get(j));
                    if(f > Integer.MAX_VALUE || f < Integer.MIN_VALUE){
                        fileData.getFloatsList().add(Float.parseFloat(inputData.get(j)));
                    }else {
                        fileData.getIntegersList().add(Integer.parseInt(inputData.get(j)));
                    }
                } else if (matcherFloat.find()) {
                    fileData.getFloatsList().add(Float.parseFloat(inputData.get(j)));
                }else if (matcherString.find()) {
                    fileData.getStringsList().add((inputData.get(j)));
                }
            }
            return fileData;
        }
    }

    /**
     * Сохранине файла
     * @param fileData класс содержащий данные файлов
     * @param pathSave путь для сохранинея
     * @param prefix префикс
     * @param canAppend может ли добавить к уже сужествующим данным в файлах
     */
    private  static void saveFiles(FileData fileData, String pathSave, String prefix, boolean canAppend){

        String nameFileIntegers = prefix + "integers.txt";
        String nameFileString = prefix + "strings.txt";
        String nameFileFloat = prefix + "floats.txt";

        FileWriter fileWriter = null;
        try {
            write(fileData.getIntegersList(), fileWriter, pathSave, nameFileIntegers, canAppend);
            write(fileData.getStringsList(), fileWriter, pathSave, nameFileString, canAppend);
            write(fileData.getFloatsList(), fileWriter, pathSave, nameFileFloat, canAppend);
            if(fileWriter != null){
                fileWriter.close();
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> void write(List<T> list, FileWriter fileWriter, String path, String nameFile, boolean canAppend) throws IOException {
        fileWriter = new FileWriter(new File(path, nameFile), canAppend);
        for(T text : list){
            fileWriter.write(text + "\n");
        }
        fileWriter.flush();
    }
}