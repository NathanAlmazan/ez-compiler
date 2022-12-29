package com.language.ez;

import com.language.ez.lexical.Analyzer;
import com.language.ez.lexical.Lexeme;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EzLanguage {
    public static void main(String[] args) {
        // read the ez code file
        try {
            File ezCode = new File(args[0]);
            Scanner scanner = new Scanner(ezCode);

            List<String> statements = new ArrayList<>();
            while (scanner.hasNextLine()) statements.add(scanner.nextLine());

            scanner.close();

            try {
                Analyzer lexicalAnalyzer = new Analyzer(statements);
                lexicalAnalyzer.analyzeStatements();
                lexicalAnalyzer.analyzeLexemes();

                for (Lexeme lexeme : lexicalAnalyzer.getLexemes()) {
                    System.out.println(lexeme.getToken() + " -> " + lexeme.getValue());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
