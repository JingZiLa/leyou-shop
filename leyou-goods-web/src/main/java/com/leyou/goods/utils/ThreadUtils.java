package com.leyou.goods.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * @Author Mirror
 * @CreateDate 2020/4/7.
 *线程工具类
 */
public class ThreadUtils {

    private static final ExecutorService es = Executors.newFixedThreadPool(10);

    public static void execute(Runnable runnable){
        es.submit(runnable);
    }
}