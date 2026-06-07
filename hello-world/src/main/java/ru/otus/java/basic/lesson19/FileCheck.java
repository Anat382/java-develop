package ru.otus.java.basic.lesson19;

import java.io.File;

public class FileCheck {
    private String path;
    private String dir;
    private String name;
    private File file;
    private File dirFile;

    public FileCheck(String dir, String name) {
        this.dir = dir;
        this.name = name;
        this.path = dir + name;
        this.file = new File(path);
        this.dirFile = new File(dir);
    }

    public String getAbsPath() {
        return file.getAbsolutePath();
    }

    public String getPath() {
        return path;
    }

    public File getDirFile() {
        return dirFile;
    }

    public String getDir() {
        return dir;
    }

    public String getNameFile() {
        return name;
    }

    public File getfile() {
        return file;
    }

    public void setPath(String pathFile) {
        this.path = pathFile;
    }

    public void setDirFile(String dirFiles) {
        this.dirFile = new File(dirFiles);
    }

    public void setDir(String dirF) {
        this.dir = dirF;
    }

    public void setNameFile(String nameFile) {
        this.name = nameFile;
    }

    public void setfile(String pathFile) {
        this.file = new File(pathFile);
    }

    public boolean fileCheckExists() {
        return file.exists() && file.isFile();
    }
}