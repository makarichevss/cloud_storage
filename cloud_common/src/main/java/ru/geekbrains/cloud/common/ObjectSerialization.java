package ru.geekbrains.cloud.common;

import ru.geekbrains.cloud.common.files.FileSerializable;
import ru.geekbrains.cloud.common.operations.Operations;

import java.io.Serializable;
import java.util.Map;

public class ObjectSerialization implements Serializable {
    private Operations operations;
    private FileSerializable file;
    private Map<String, String> dir;

    public ObjectSerialization(Operations operations, FileSerializable file, Map<String, String> dir) {
        this.operations = operations;
        this.file = file;
        this.dir = dir;
    }

    public Operations getOperations() {
        return operations;
    }

    public FileSerializable getFile() {
        return file;
    }

    public Map<String, String> getDir() {
        return dir;
    }
}
