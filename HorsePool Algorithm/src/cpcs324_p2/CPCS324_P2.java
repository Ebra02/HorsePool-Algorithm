/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpcs324_p2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Mn9_1
 */
public class CPCS324_P2 {

    private static final int CHAR_SET_SIZE = 256;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {

        File file = new File("input.txt");
        File output = new File("pattern.txt");
        Scanner sc = new Scanner(file);
        sc.useDelimiter("\\Z");
        String TextContent = "";
        System.out.println("How many Lines you want to read from the text file?");
        Scanner scan = new Scanner(System.in);
        int LinesNumbers = scan.nextInt();

        for (int i = 1; i <= LinesNumbers; i++) {
            if (sc.hasNextLine()) {
                TextContent += sc.nextLine().toLowerCase();
            }else {
                System.out.println("The input file is empty or number of lines in the input is small ");
                System.exit(1);
            }

        }

        System.out.println("\nWhat is the length of each pattern?");
        int patternLength = scan.nextInt();

        System.out.println("\nHow many patterns to be generated?");
        int NumberOfPattern = scan.nextInt();

        PrintWriter pen = new PrintWriter(output);
        String[] PatTable = new String[NumberOfPattern];
        for (int i = 0; i < NumberOfPattern; i++) {
            Random random = new Random();
            int start = random.nextInt(TextContent.length() - patternLength + 1);
            String RandomPatteen = TextContent.substring(start, start + patternLength);
            PatTable[i] = RandomPatteen;
            pen.print(RandomPatteen + "\n");
        }

        System.out.println("\n" + NumberOfPattern + " patterns, each of length " + patternLength + " have been generated in file pattern.txt");

        pen.flush();
        int l = 0;
        long startTimeForBF = System.nanoTime();
        for (int i = 0; i < NumberOfPattern; i++) {
            l += Bruteforce(TextContent, PatTable[i], patternLength);
        }
        long endTimeForBF = System.nanoTime();
        double BF = (endTimeForBF - startTimeForBF) / NumberOfPattern;
        System.out.println("\nAverage Time of search in Brute Force approach: " + BF+"\n");
        
        

        int h = 0;
        System.out.println("HorsePool Shift Table");
        long startTimeForHP = System.nanoTime();
        for (int i = 0; i < NumberOfPattern; i++) {
            System.out.println("Here is the shift table for pattern number " + (i + 1));
            h += Horspool(TextContent, PatTable[i], patternLength);
        }
        long endTimeForHP = System.nanoTime();
        double HP = (endTimeForHP - startTimeForHP) / NumberOfPattern;
        System.out.println("\nAverage Time of search in Horsepool approach: " + HP);

        if (HP < BF) {
            System.out.println("\nFor this instance HorsePool approsch is better than Bruteforce approach");
        } else if ((HP > BF)) {
            System.out.println("\nFor this instance Bruteforce approsch is better than HorsePool approach");
        }
    }

    public static int Bruteforce(String s, String PatTable, int patlength) {
        int v = 0;
        for (int i = 0; i <= s.length() - patlength; i++) {
            int j;
            for (j = 0; j < patlength; j++) {
                if (s.charAt(i + j) != PatTable.charAt(j)) {
                    break;
                }
            }
            if (j == patlength) {
                v++;

            }

        }
        return v;
    }

    private static HashMap<Character, Integer> ShiftTable(String pattern, String TextContent, int patlength) {
        HashMap<Character, Integer> shiftTable = new HashMap<>();
        HashSet<Character> PatternChar = new HashSet<>();
        for (char c : pattern.toCharArray()) {

            PatternChar.add(c);

        }

        for (char c : PatternChar) {
            int Shift = 0;
            for (int j = pattern.length() - 1; j >= 0; j--) {
                if (pattern.charAt(j) == c) {
                    Shift = pattern.length() - j - 1;
                    break;
                }
            }
            if (Shift == 0) {
                Shift = patlength;
            }
            shiftTable.put(c, Shift);
        }

        System.out.println(shiftTable);

        return shiftTable;
    }

    private static int Horspool(String TextContent, String PatTable, int patlength) {

        HashMap<Character, Integer> shiftTable = ShiftTable(PatTable, TextContent, patlength);
        int i = patlength - 1;
        int v = 0;

        while (i <= TextContent.length() - 1) {
            int k = 0;

            while (k <= patlength - 1 && PatTable.charAt(patlength - 1 - k) == TextContent.charAt(i - k)) {
                k++;

            }
            if (k == patlength) {
                v++;
            }


            i += shiftTable.getOrDefault(TextContent.charAt(i), patlength);

        }

        return v;
    }
}
