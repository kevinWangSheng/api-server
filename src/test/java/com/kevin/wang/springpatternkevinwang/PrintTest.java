package com.kevin.wang.springpatternkevinwang;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wang
 * @create 2023-2023-24-20:12
 */
public class PrintTest {
    private static AtomicInteger  atomicInteger = new AtomicInteger(0);
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.execute(()->{
            while(atomicInteger.get() < 100){
                System.out.println(Thread.currentThread().getName()+":"+atomicInteger.incrementAndGet());
            }
        });
        executorService.execute(()->{
            while(atomicInteger.get() < 100){
                System.out.println(Thread.currentThread().getName()+":"+atomicInteger.incrementAndGet());
            }
        });
    }


}
