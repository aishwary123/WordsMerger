package com.example.models;

public enum DirectoryProcessorType {

    TWO_FILES_PROCESSOR(2);

    private int mergerSize;

    DirectoryProcessorType(int mergerSize) {
        this.mergerSize = mergerSize;
    }

    public int getMergerSize() {
        return this.mergerSize;
    }

    public static DirectoryProcessorType fromOrdinal(int ordinal) {
        switch (ordinal) {
            case 2:
                return DirectoryProcessorType.TWO_FILES_PROCESSOR;
        }
        return null;
    }
}
