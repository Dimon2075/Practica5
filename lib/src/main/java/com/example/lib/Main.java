package com.example.lib;

import com.example.lib.service.CompanyService;
import com.example.lib.ui.ConsoleMenu;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        CompanyService companyService = new CompanyService();
        ConsoleMenu menu = new ConsoleMenu(companyService);
        menu.start();
    }
}