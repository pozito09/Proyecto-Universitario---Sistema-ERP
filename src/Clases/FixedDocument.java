package Clases;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class FixedDocument extends PlainDocument {
    private final int maxLength;
    private final boolean onlyDigits;
    private final boolean startWith;

    public FixedDocument(int maxLength, boolean onlyDigits) {
        this(maxLength, onlyDigits, false);
    }

    public FixedDocument(int maxLength, boolean onlyDigits, boolean startWith) {
        this.maxLength = maxLength;
        this.onlyDigits = onlyDigits;
        this.startWith = startWith;
    }

    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) return;
        if (onlyDigits && !str.matches("\\d*")) return;

        String current = getText(0, getLength());
        String nuevo = current.substring(0, offset) + str + current.substring(offset);

        // Validar que empiece con 9
        if (startWith && nuevo.length() > 0 && nuevo.charAt(0) != '9') return;

        if (nuevo.length() > maxLength) return;

        super.insertString(offset, str, attr);
    }
}
