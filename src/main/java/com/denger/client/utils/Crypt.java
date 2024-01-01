package com.denger.client.utils;

public class Crypt {
    public static String encrypt(String message) {
        StringBuilder result = new StringBuilder();
        for (char c : message.toCharArray()) {
            if (c >= 'A' && c < 'Z') {
                result.append((char)(c + 1));
            } else if (c == 'Z') {
                result.append('A');
            } else if (c >= 'a' && c < 'z') {
                result.append((char)(c + 1));
            } else if (c == 'z') {
                result.append('a');
            } else if (c >= 'А' && c < 'Я') {
                result.append((char)(c + 1));
            } else if (c == 'Я') {
                result.append('А');
            } else if (c >= 'а' && c < 'я') {
                result.append((char)(c + 1));
            } else if (c == 'я') {
                result.append('а');
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}