package com.epam.izh.rd.online.service;

public class SimpleTextService implements TextService {

    /**
     * Реализовать функционал удаления строки из другой строки.
     * <p>
     * Например для базовой строки "Hello, hello, hello, how low?" и строки для удаления ", he"
     * метод вернет "Hellollollo, how low?"
     *
     * @param base   - базовая строка с текстом
     * @param remove - строка которую необходимо удалить
     */
    @Override
    public String removeString(String base, String remove) {
        if (base != null && remove != null) {
            return base.replaceAll(remove, "");
        } else {
            return null; //TODO
        }
    }

    /**
     * Реализовать функционал проверки на то, что строка заканчивается знаком вопроса.
     * <p>
     * Например для строки "Hello, hello, hello, how low?" метод вернет true
     * Например для строки "Hello, hello, hello!" метод вернет false
     */
    @Override
    public boolean isQuestionString(String text) {
        if (text != null && text.length() > 0) {
            return (text.charAt(text.length() - 1) == '?');
        }
        return false; //TODO
    }

    /**
     * Реализовать функционал соединения переданных строк.
     * <p>
     * Например для параметров {"Smells", " ", "Like", " ", "Teen", " ", "Spirit"}
     * метод вернет "Smells Like Teen Spirit"
     */
    @Override
    public String concatenate(String... elements) {
        StringBuilder result = new StringBuilder();
        if (elements != null) {
            for (String s : elements) {
                if (s != null) {
                    result.append(s);
                }
            }
        }
        return result.toString(); //TODO
    }

    /**
     * Реализовать функционал изменения регистра в вид лесенки.
     * Возвращаемый текст должен начинаться с прописного регистра.
     * <p>
     * Например для строки "Load Up On Guns And Bring Your Friends"
     * метод вернет "lOaD Up oN GuNs aNd bRiNg yOuR FrIeNdS".
     */
    @Override
    public String toJumpCase(String text) {
        StringBuilder result = new StringBuilder();
        if (text != null) {
            result.append(text.toLowerCase());
            int difference = 'A' - 'a';
            char charBuffer;
            for (int i = 1; i < result.length(); i += 2) {
                charBuffer = result.charAt(i);
                if (charBuffer > 'a' && charBuffer < 'z') {
                    result.setCharAt(i, (char) (charBuffer + difference));
                }
            }
            return result.toString();
        } else {
            return "";
        }
    }

    /**
     * Метод определяет, является ли строка палиндромом.
     * <p>
     * Палиндром - строка, которая одинаково читается слева направо и справа налево.
     * <p>
     * Например для строки "а роза упала на лапу Азора" вернется true, а для "я не палиндром" false
     */
    @Override
    public boolean isPalindrome(String string) {
        if (string != null && string.length() > 0) {
            // создаём строку без пробелов и в lower case
            // создаём на её основе StringBuilder, "реверсим" его
            // в качестве результата сравниваем со строкой, которая была основой StringBuilder
            String stringWithoutSpace = string.replaceAll("\\s", "").toLowerCase();
            StringBuilder reverseString = new StringBuilder(stringWithoutSpace);
            reverseString.reverse();
            return stringWithoutSpace.equals(reverseString.toString());
        } else {
            return false;
        }
    }
}
