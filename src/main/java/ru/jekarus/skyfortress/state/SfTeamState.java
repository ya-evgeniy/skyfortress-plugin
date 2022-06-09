package ru.jekarus.skyfortress.state;

import java.util.Arrays;

public class SfTeamState {

    public static final int MAX_LEVEL = 32;
    private static final int[] EXP_TO_LEVEL = new int[33];

    static {
        int exp = 0;
        for (int i = 0; i < 33; i++) {
            EXP_TO_LEVEL[i] = exp;
            exp += i * 10;
        }
        /*
        exp     level
        0       1
        10      2
        30      3
        60      4
        100     5
        150     6
        210     7
        280     8
        360     9
        450     10
        550     11
        660     12
        780     13
        910     14
        1050    15
        1200    16
        1360    17
        1530    18
        1710    19
        1900    20
        2100    21
        2310    22
        2530    23
        2760    24
        3000    25
        3250    26
        3510    27
        3780    28
        4060    29
        4350    30
        4650    31
        4960    32
        ...     32
        */
    }

    public int health = 100;

    public double experience = 0.0;

    public int getLevel() {
        int level = Arrays.binarySearch(EXP_TO_LEVEL, (int) experience);
        if(level < 0) level = -level - 2;
        if(level == -1) throw new RuntimeException("negative experience " + experience);
        return level;
    }


}
