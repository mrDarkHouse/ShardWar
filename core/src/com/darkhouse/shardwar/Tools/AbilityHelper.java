package com.darkhouse.shardwar.Tools;

import java.util.Random;

public class AbilityHelper {
    private static Random r;

    public static void init(){
        r = new Random();
    }

    public static int rollNum(int bound){
        return r.nextInt(bound);
    }
}
