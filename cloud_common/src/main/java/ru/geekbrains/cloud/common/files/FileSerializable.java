package ru.geekbrains.cloud.common.files;

import java.io.Serializable;
import java.util.Arrays;

public class FileSerializable implements Serializable {

    private String path;
    private long length;
    private int part;
    private int partCount;
    private byte[] arr;

    public FileSerializable(String path, long length, int part, int partCount, byte[] arr) {
        this.path = path;
        this.length = length;
        this.part = part;
        this.partCount = partCount;
        this.arr = arr;
    }

    @Override
    public String toString() {
        return "FileSerializable{" +
                "path='" + path + '\'' +
                ", length=" + length +
                ", part=" + part +
                ", partCount=" + partCount +
                ", arr=" + Arrays.toString(arr) +
                '}';
    }

    public int getPartCount() {
        return partCount;
    }

    public String getPath() {
        return path;
    }

    public long getLength() {
        return length;
    }

    public int getPart() {
        return part;
    }

    public byte[] getArr() {
        return arr;
    }

}
