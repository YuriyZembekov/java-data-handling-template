package com.epam.izh.rd.online.service;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleRegExpService implements RegExpService {

    /**
     * Метод должен читать файл sensitive_data.txt (из директории resources) и маскировать в нем конфиденциальную информацию.
     * Номер счета должен содержать только первые 4 и последние 4 цифры (1234 **** **** 5678). Метод должен содержать регулярное
     * выражение для поиска счета.
     *
     * @return обработанный текст
     */
    @Override
    public String maskSensitiveData() {
        String data = null;
        // регулярное выражение - 4 группы цифр с возможным пробельным символом между ними
        Pattern pattern = Pattern.compile("(\\d{4}\\s?){3}(\\d{4})");
        Matcher matcher;
        String result = "";
        try (FileReader reader = new FileReader("./src/main/resources/sensitive_data.txt");
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            //В цикле читаем файл построчно, до тех пор пока не будет совпадения
            // по регулярному выражению или до конца файла
            do {
                data = bufferedReader.readLine();
                if (data != null) {
                    matcher = pattern.matcher(data);
                    while (matcher.find()) {
                        int start = matcher.start();
                        int end = matcher.end();
                        // склеиваем результат - часть строки до совпадения + первые 4 символа совпадения
                        // + "звёздочки" + последние 4 символа совпадения + оставшаяся часть строки
                        data = data.substring(0, start) + matcher.group().substring(0, 4)
                                + " **** **** " + matcher.group().substring(matcher.group().length() - 4)
                                + data.substring(end);
                        //обновляем строку в "матчере", на случай последнего совпадения копируем в result
                        matcher = pattern.matcher(data);
                        result = data;
                    }
                }
            } while (data != null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Метод должен считыввать файл sensitive_data.txt (из директории resources) и заменять плейсхолдер ${payment_amount} и ${balance} на заданные числа. Метод должен
     * содержать регулярное выражение для поиска плейсхолдеров
     *
     * @return обработанный текст
     */
    @Override
    public String replacePlaceholders(double paymentAmount, double balance) {
        String data = "";

        try (FileReader reader = new FileReader("./src/main/resources/sensitive_data.txt");
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            //В цикле читаем файл построчно, до тех пор пока не будет совпадения
            // по плейсхолдерам или до конца файла
            do {
                data = bufferedReader.readLine();
                if (data != null && data.contains("${payment_amount}") && data.contains("${balance}")) {
                    String paymentAmountString;
                    String balanceString;
                    // Если значения целые - округлить до long, если нет оставить как есть.
                    paymentAmountString = (paymentAmount % 1 == 0) ? String.valueOf((long) paymentAmount) : String.valueOf(paymentAmount);
                    balanceString = (balance % 1 == 0) ? String.valueOf((long) balance) : String.valueOf(balance);
                    data = data.replace("${payment_amount}", paymentAmountString);
                    data = data.replace("${balance}", balanceString);
                    break;
                }
            } while (data != null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (data == null) {
            data = "";
        }
        return data;
    }
}
