package client.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Test {

    public static void main(String[] args) throws Exception{
        /*
        File file = new File("res/testfile.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while((st = br.readLine()) != null) System.out.println(st);
        //System.out.println("\u951f\u65a4\u94d0");
        */
        File file = new File("res/testfile.txt");
        FileReader fr = new FileReader(file);
        int i;
        while((i = fr.read()) != -1) System.out.print((char)i);
    }

}
