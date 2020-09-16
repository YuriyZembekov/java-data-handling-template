package com.epam.izh.rd.online.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class SimpleBigNumbersService implements BigNumbersService {

    /**
     * Метод делит первое число на второе с заданной точностью
     * Например 1/3 с точностью 2 = 0.33
     *
     * @param range точность
     * @return результат
     */
    @Override
    public BigDecimal getPrecisionNumber(int a, int b, int range) throws ArithmeticException {
        if (b != 0) {
            BigDecimal aBigDecimal = new BigDecimal(a);
            BigDecimal bBigDecimal = new BigDecimal(b);
            return aBigDecimal.divide(bBigDecimal, range, RoundingMode.HALF_UP);
        } else {
            throw new ArithmeticException("division by zero");
        }
    }

    /**
     * Метод находит простое число по номеру
     *
     * @param range номер числа, считая с числа 2
     * @return простое число
     */
    @Override
    public BigInteger getPrimaryNumber(int range) throws IllegalArgumentException {
        if (range > 0) {
            int result = 2;
            boolean isPrime;
            // внешний цикл перебирает значения
            for (int number = 2; number <= Integer.MAX_VALUE; number++) {
                isPrime = true;
                // цикл проверяет простое ли число
                for (int i = 2; i <= (number / i); i++) {
                    if (number % i == 0) {
                        isPrime = false;
                        break;
                    }
                }
                if (isPrime) {
                    range--;
                }
                if (range == 0) {
                    result = number;
                    break;
                }
            }
            return BigInteger.valueOf(result);
        } else {
            throw new IllegalArgumentException("method parameter must be positive");
        }
    }
}
