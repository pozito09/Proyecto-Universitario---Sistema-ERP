package Clases;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

// ── DESCRIPCIÓN: Filtro de documento para campos de texto que limita la longitud máxima y puede restringir a solo dígitos ──
public class FixedDocument extends PlainDocument {
    private final int maxLength;
    private final boolean onlyDigits;
    private final boolean mustStartWith9;

    // ── DESCRIPCIÓN: Constructor que define longitud máxima y si solo acepta dígitos ──
    public FixedDocument(int maxLength, boolean onlyDigits) {
        this(maxLength, onlyDigits, false);
    }

    // ── DESCRIPCIÓN: Constructor completo que agrega la opción de que el texto empiece con dígito 9 ──
    public FixedDocument(int maxLength, boolean onlyDigits, boolean mustStartWith9) {
        this.maxLength = maxLength;
        this.onlyDigits = onlyDigits;
        this.mustStartWith9 = mustStartWith9;
    }

    // ── DESCRIPCIÓN: Valida la entrada antes de insertarla: verifica dígitos, longitud máxima y prefijo ──
    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) return;
        if (onlyDigits && !str.matches("\\d*")) return;

        String current = getText(0, getLength());
        String nuevo = current.substring(0, offset) + str + current.substring(offset);

        if (mustStartWith9 && nuevo.length() > 0 && nuevo.charAt(0) != '9') return;

        if (nuevo.length() > maxLength) return;

        super.insertString(offset, str, attr);
    }
}
