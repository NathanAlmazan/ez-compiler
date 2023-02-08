package com.language.ez;

import com.language.ez.lexical.LAnalyzer;
import com.language.ez.syntax.SAnalyzer;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EzLanguage {

    public static void main(String[] args) throws Exception {
        // read the ez code file
        String filePath;
        if (args.length == 0) {
            Scanner input = new Scanner(System.in);
            filePath = input.nextLine();
            input.close();
        }
        else filePath = args[0];

        try {
            File ezCode = new File(filePath);

            if (!FilenameUtils.getExtension(ezCode.getName()).equals("ez")) throw new Exception("Invalid file extension.");

            Scanner scanner = new Scanner(ezCode);

            List<String> statements = new ArrayList<>();
            while (scanner.hasNextLine()) statements.add(scanner.nextLine());

            scanner.close();

            String directory = ezCode.getParent() + "\\";

            // start lexical analyzer and catch errors if any
            try {
                LAnalyzer lexicalLAnalyzer = new LAnalyzer(statements);
                lexicalLAnalyzer.analyzeStatements(); // start scanning for lexemes/tokens
                lexicalLAnalyzer.optimizeLexemes(); // simplify tokens for better readability and performance later in the syntax analyzer

                // System.out.println("Tokens:");
                // System.out.println(lexicalLAnalyzer.generateTokenTable());
                String lexemesPath = directory + ezCode.getName().split("\\.")[0] + "LEX.txt";
                FileWriter lexemeWriter = new FileWriter(lexemesPath);
                lexemeWriter.write(lexicalLAnalyzer.generateTokenTable());
                lexemeWriter.close();

                SAnalyzer syntaxAnalyzer = new SAnalyzer(lexicalLAnalyzer.getLexemes());
                syntaxAnalyzer.analyzeExpressions();
                syntaxAnalyzer.analyzePrimaryStatements();
                syntaxAnalyzer.analyzeSecondaryStatements();
                syntaxAnalyzer.generateCode();

                // System.out.println("Statements:");
                //System.out.println(syntaxAnalyzer.generateAbstractSyntaxTree());
                String syntaxPath = directory + ezCode.getName().split("\\.")[0] + "SYN.txt";
                FileWriter syntaxWriter = new FileWriter(syntaxPath);
                syntaxWriter.write(syntaxAnalyzer.generateAbstractSyntaxTree());
                syntaxWriter.close();

                // System.out.println("Output: ");
                // System.out.println(syntaxAnalyzer.getCode());

                String outputPath = "C:\\ezlang\\scripts\\" + ezCode.getName().split("\\.")[0] + ".py";
                FileWriter outputWriter = new FileWriter(outputPath);
                outputWriter.write(syntaxAnalyzer.getCode());
                outputWriter.close();

                Runtime.getRuntime().exec("cmd /c start \"EZ LANGUAGE\" cmd.exe /K \"python " + outputPath + "\"");

            } catch (Exception e) {
                FileWriter logWriter = new FileWriter("C:\\ezlang\\scripts\\logger.py");
                logWriter.write("print('" + e.getMessage() + "')");
                logWriter.close();

                Runtime.getRuntime().exec("cmd /c start \"EZ LANGUAGE\" cmd.exe /K \"python C:\\ezlang\\scripts\\logger.py");
                // e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
