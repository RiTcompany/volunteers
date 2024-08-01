package org.example.exceptions;

public class FileNotDownloadedException extends AbstractException {
    public FileNotDownloadedException(String message) {
        super(message, "Не удалось скачать файл, обратитесь в поддержку");
    }
}
