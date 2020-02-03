package com.arithmeticcoding;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class ArithmeticCoding {
    private  static Map<Character,LowUpperBoundry>table=new HashMap<>();
    private static Scanner sc=new Scanner(System.in);
    private static String seq;
    private static double lastval;
    private static double range1,range2;
    public static String compressForGui(String seqq) {
         //seq=sc.nextLine();
        seq=seqq;
        setTable(seq);
        compress(seq);
        return String.valueOf(lastval);
    }
    public static String compressForFile(String seqq) {
        //seq=sc.nextLine();
        seq=seqq;
        setTable(seq);
        compress(seq);
        return String.valueOf(lastval)+","+String.valueOf(seq.length());
    }
    public static void setTableDecompress(ArrayList<charAndProb>prob){
        //double cont2 = 0.0;
        table.clear();
        double cont=0.0;
        for(int i=0;i<prob.size();i++){
            table.put(prob.get(i).symbol,new LowUpperBoundry(cont,BigDecimal.valueOf(cont+prob.get(i).prob)/**set precision*/
                    .setScale(3, RoundingMode.HALF_UP)
                    .doubleValue()));
            cont=BigDecimal.valueOf(cont+prob.get(i).prob)/**set precision*/
                    .setScale(3, RoundingMode.HALF_UP)
                    .doubleValue();
        }
    }
    public static String getTableForFile(){
        StringBuilder data= new StringBuilder();
        int cont=0;
        for(char x:table.keySet()){
            if(cont<table.size()-1)
                data.append(x).append(",").append(BigDecimal.valueOf(table.get(x).upper-table.get(x).low)/**set precision*/
                        .setScale(3, RoundingMode.HALF_UP)
                        .doubleValue()).append("\n");
            else data.append(x).append(",").append(BigDecimal.valueOf(table.get(x).upper-table.get(x).low)/**set precision*/
                    .setScale(3, RoundingMode.HALF_UP)
                    .doubleValue());
            cont++;
        }
        return data.toString();
    }
    public static String getRangeForGui(){
        String range=range1+" , "+range2;
        return range;
    }
    private static void compress(String seq) {
        double lower=0.0,higher=1.0,range=0.0;
        for(int i=0;i<seq.length();i++){
            range=higher-lower;
            higher= lower+ (range) * table.get(seq.charAt(i)).upper;
            lower= lower+ (range) * table.get(seq.charAt(i)).low;

        }
        //System.out.println("the range is: "+lower+" "+higher);
        //System.out.println("value: "+(lower+higher)/(double)2);
        lastval=(lower+higher)/(double)2;
        range1=lower;
        range2=higher;
    }
    public static String deCompress(double value,int numChars) {
        char currChar;
        String deCompressedseq="";
        currChar=getCurrChar(value);
        if(currChar==Character.MIN_VALUE){
            System.out.println("Not Found!");
            return null;
        }
        deCompressedseq+=currChar;
        double lower=table.get(currChar).low,higher=table.get(currChar).upper,range=0.0;
        for(int i=0;i<numChars-1;i++){
            range=higher-lower;
            value=(value-lower)/range;
            currChar=getCurrChar(value);
            deCompressedseq+=currChar;
            higher=  table.get(currChar).upper;
            lower= table.get(currChar).low;
        }
        //System.out.println(deCompressedseq);
        return deCompressedseq;
    }

    private static char getCurrChar(double value) {
        for(char x:table.keySet()){
            if(value>=table.get(x).low&&value<=table.get(x).upper){
                return x;
            }
        }
        return Character.MIN_VALUE;
    }
    private static void setTable(String seq) {
        table.clear();
        ArrayList<Character>arrSeq=new ArrayList<>();
        for(int i=0;i<seq.length();i++)
            arrSeq.add(seq.charAt(i));
        Collections.sort(arrSeq);
        double currLow=0;
        double prob;
        for (int i = 0; i < arrSeq.size(); i++) {
            double cont = 1;
            for (int j = i + 1; j < arrSeq.size(); j++) {
                if (arrSeq.get(i) == arrSeq.get(j)) {
                    arrSeq.remove(j--);
                    cont++;
                }
            }
            prob = cont / seq.length();
            prob = BigDecimal.valueOf(prob)/**set precision*/
                    .setScale(3, RoundingMode.HALF_UP)
                    .doubleValue();
            table.put(arrSeq.get(i),new LowUpperBoundry(currLow,BigDecimal.valueOf(prob+currLow)
                    .setScale(3, RoundingMode.HALF_UP)
                    .doubleValue()));
            currLow+=prob;
            currLow = BigDecimal.valueOf(currLow)
                    .setScale(3, RoundingMode.HALF_UP)
                    .doubleValue();
            arrSeq.remove(i--);
        }

    }

}
