package Clases;

public class NumeroALetras {
    private static final String[] UNIDADES = {"cero", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve"};
    private static final String[] DECENAS = {"", "diez", "veinte", "treinta", "cuarenta", "cincuenta", "sesenta", "setenta", "ochenta", "noventa"};
    private static final String[] DIEZ_DIEZ = {"", "once", "doce", "trece", "catorce", "quince", "dieciséis", "diecisiete", "dieciocho", "diecinueve"};
    private static final String[] CENTENAS = {"", "cien", "doscientos", "trescientos", "cuatrocientos", "quinientos", "seiscientos", "setecientos", "ochocientos", "novecientos"};

    public static String convertir(double monto) {
        long parteEntera = (long) monto;
        int parteDecimal = (int) Math.round((monto - parteEntera) * 100);
        String enteraStr = convertirEntero(parteEntera);
        return enteraStr + " con " + String.format("%02d", parteDecimal) + "/100";
    }

    private static String convertirEntero(long n) {
        if (n == 0) return "cero";
        if (n == 1) return "uno";
        StringBuilder sb = new StringBuilder();
        if (n >= 1000000) {
            long millones = n / 1000000;
            if (millones == 1) sb.append("un millón");
            else sb.append(convertirEntero(millones)).append(" millones");
            n %= 1000000;
            if (n > 0) sb.append(" ");
        }
        if (n >= 1000) {
            long miles = n / 1000;
            if (miles == 1) sb.append("mil");
            else sb.append(convertirEntero(miles)).append(" mil");
            n %= 1000;
            if (n > 0) sb.append(" ");
        }
        if (n >= 100) {
            long cent = n / 100;
            if (cent == 1 && n % 100 == 0) sb.append("cien");
            else sb.append(CENTENAS[(int) cent]);
            n %= 100;
            if (n > 0) sb.append(" ");
        }
        if (n >= 10 && n <= 19) {
            sb.append(DIEZ_DIEZ[(int) n - 10]);
            return sb.toString();
        }
        if (n >= 20) {
            sb.append(DECENAS[(int) (n / 10)]);
            n %= 10;
            if (n > 0) sb.append(" y ");
        }
        if (n > 0) {
            sb.append(UNIDADES[(int) n]);
        }
        return sb.toString();
    }
}
