package com.language.ez;

import com.language.ez.lexical.LAnalyzer;
import com.language.ez.lexical.Lexeme;
import com.language.ez.syntax.SAnalyzer;
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
                LAnalyzer lexicalLAnalyzer = new LAnalyzer(statements);
                lexicalLAnalyzer.analyzeStatements(); // start scanning for lexemes/tokens
                lexicalLAnalyzer.optimizeLexemes(); // simplify tokens for better readability and performance later in the syntax analyzer

                System.out.println("Tokens:");

                for (Lexeme lexeme : lexicalLAnalyzer.getLexemes()) {
                    System.out.println(lexeme.getToken() + " -> " + lexeme.getValue());
                }

                System.out.println();
                System.out.println("Statements:");

                SAnalyzer syntaxAnalyzer = new SAnalyzer(lexicalLAnalyzer.getLexemes());
                syntaxAnalyzer.analyzeExpressions();
                syntaxAnalyzer.analyzePrimaryStatements();
                syntaxAnalyzer.analyzeSecondaryStatements();
                syntaxAnalyzer.printNodes();

            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
