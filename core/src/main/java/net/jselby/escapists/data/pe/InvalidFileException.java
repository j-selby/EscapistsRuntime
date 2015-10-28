package net.jselby.escapists.data.pe;

import java.io.IOException;

public class InvalidFileException extends IOException {

    private static final long serialVersionUID = -7889848167364462651L;

    public InvalidFileException(String reason) {
        super(reason);
    }
}
