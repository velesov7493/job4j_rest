package ru.job4j.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Keygen {

    public static void main(String[] args) {
        BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
        System.out.println("masterkey: " + enc.encode("masterkey"));
        System.out.println("7777: " + enc.encode("7777"));
        System.out.println("9999: " + enc.encode("9999"));
        System.out.println("service: " + enc.encode("service"));
    }
}
