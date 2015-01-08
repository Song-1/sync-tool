package com.test.www.oss;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GetAllFileByPath {

    public static void main(String[] args) {
        //目录
        File dataDir = new File("E:/music");
        //存放目录及其子目录下的所有文件对象
        List<File> myfile = new ArrayList<File>();
        //开始遍历
        listDirectory(dataDir, myfile);
        
        System.out.println("目录下包含 " + myfile.size() + "个文件：");
        for(File file : myfile){
            System.out.println(file.getAbsolutePath());    
        }
    }
    /** *//**
     * 遍历目录及其子目录下的所有文件并保存
     * @param path 目录全路径
     * @param myfile 列表：保存文件对象
     */
    public static void listDirectory(File path, List<File> myfile){
        if (!path.exists()){
            System.out.println("文件名称不存在!");
        }
        else
        {
            if (path.isFile()){
                myfile.add(path);
            } else{
                File[] files = path.listFiles();
                for (int i = 0; i < files.length; i++  ){
                    listDirectory(files[i], myfile);
                }
            }
        }
    }
}