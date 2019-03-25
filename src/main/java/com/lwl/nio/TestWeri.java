package com.lwl.nio;

import java.util.*;

/**
 * @author lwl
 * @date 2019/3/22 15:30
 * @description
 */
public class TestWeri {
    public static void main(String[] args) throws UnsupportedOperationException{
        List<String> list = new ArrayList<>();
        List<String> objects = Arrays.asList("sdf", "dff");
        list.add("ddd");
        list.add("fff");
        Set<String> set = new HashSet<>();
        set.add("sdf");
        set.add("fff");
//        Iterator<String> iterator = set.iterator();
//        Iterator<String> iterator = list.iterator();
        Iterator<String> iterator = objects.iterator();
        int i = 0;
        while (iterator.hasNext()){
            String next = iterator.next();
            System.out.println(next);
            if (i == 0){
                iterator.remove();
                i++;
            }
        }
        System.out.println("--------------------");
        for (String s : list) {
            System.out.println(s);
        }
    }
}