package com.epam.izh.rd.online.service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class SimpleDateService implements DateService {

    /**
     * Метод парсит дату в строку
     *
     * @param localDate дата
     * @return строка с форматом день-месяц-год(01-01-1970)
     */
    @Override
    public String parseDate(LocalDate localDate) {
        String s = "";
        try {
            s = localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * Метод парсит строку в дату
     *
     * @param string строка в формате год-месяц-день часы:минуты (1970-01-01 00:00)
     * @return дата и время
     */
    @Override
    public LocalDateTime parseString(String string) throws IllegalArgumentException {
        LocalDateTime localDateTime;
        try {
            localDateTime = LocalDateTime.parse(string, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Illegal date or date format");
        }
        return localDateTime;
    }

    /**
     * Метод конвертирует дату в строку с заданным форматом
     *
     * @param localDate исходная дата
     * @param formatter формат даты
     * @return полученная строка
     */
    @Override
    public String convertToCustomFormat(LocalDate localDate, DateTimeFormatter formatter) {
        String date = "";
        try {
            date = localDate.format(formatter);
        } catch (DateTimeException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Метод получает следующий високосный год
     *
     * @return високосный год
     */
    @Override
    public long getNextLeapYear() {
        int leapYear = Year.now().getValue();
        leapYear++;
        //Увеличиваем год на еденицу, пока он не будет вискокосным
        while (!Year.of(leapYear).isLeap()) {
            leapYear++;
        }
        return leapYear;
    }

    /**
     * Метод считает число секунд в заданном году
     *
     * @return число секунд
     */
    @Override
    public long getSecondsInYear(int year) {
        LocalDateTime firstDateTime = LocalDateTime.of(year, 01, 01, 00, 00);
        LocalDateTime secondDateTime = firstDateTime.plusYears(1);
        return firstDateTime.until(secondDateTime, ChronoUnit.SECONDS);
    }


}
