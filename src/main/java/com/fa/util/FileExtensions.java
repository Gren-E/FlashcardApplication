package com.fa.util;

public enum FileExtensions {

    PDF ("pdf");

    private final String extension;

    FileExtensions(String extension) {
        this.extension = extension;
    }

    public String get() {
        return extension;
    }

}
