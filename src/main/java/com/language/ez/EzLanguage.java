package com.language.ez;

import com.language.ez.lexical.Analyzer;
import com.language.ez.lexical.Lexeme;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EzLanguage {
    public static void main(String[] args) throws Exception {
        // read the ez code file
        if (args.length == 0) throw new Exception("Please input the file path.");

        try {
            File ezCode = new File(args[0]);

            if (!FilenameUtils.getExtension(ezCode.getName()).equals("ez")) throw new Exception("Invalid file extension.");

            Scanner scanner = new Scanner(ezCode);

            List<String> statements = new ArrayList<>();
            while (scanner.hasNextLine()) statements.add(scanner.nextLine());

            scanner.close();

            // start lexical analyzer and catch errors if any
            try {
                Analyzer lexicalAnalyzer = new Analyzer(statements);
                lexicalAnalyzer.analyzeStatements(); // start scanning for lexemes/tokens
                lexicalAnalyzer.optimizeLexemes(); // simplify tokens for better readability and performance later in the syntax analyzer

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
