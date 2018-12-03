/*
finished commenting
*/
package proyecto;

import java.util.Calendar;

/**
 * Clase Fecha.
 * 
 * @author German Navarro.
 */
public class Fecha {

    private int day;
    private int month;
    private int year;

    //Constructor con los parámetros con día, mes y año.
    public Fecha(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    // Métodos set y get para los 3 atributos.
    public int getDia() {
        return this.day;
    }

    public int getMes() {
        return this.month;
    }

    public int getAnyo() {
        return this.year;
    }

    public void setDia(int day) {
        this.day = day;
    }

    public void setMes(int month) {
        this.month = month;
    }

    public void setAnyo(int year) {
        this.year = year;
    }

    /**
     * Muestra la fecha en formato corto (31-01-2001).
     *
     * @return La fecha como texto en formato 31-01-2001
     */
    public String corta() {
        return this.day + "-" + this.month + "-" + this.year;
    }

    /**
     * Convierte un texto recibido de la clase String '31-01-2001' y lo
     * convierte al formato de la clase Fecha.
     *
     * @param texto Fecha del tipo {@code String}.
     * @return Fecha del tipo {@code Fecha}.
     */
    public static Fecha stringToFecha(String texto) {
        Fecha fecha = null;
        String[] array = texto.split("-");
        fecha = new Fecha(
                Integer.parseInt(array[2]),
                Integer.parseInt(array[1]),
                Integer.parseInt(array[0])
        );
        return fecha;
    }

    /**
     * Compara si la fecha recibida por parámetro es anterior a la fecha de la
     * clase.
     *
     * @param fecha Fecha con formato de la clase Fecha.
     * @return {@code true} si la fecha es anterior a la fecha de la clase.
     * {@code false} si la fecha no es anterior.
     */
    public boolean menorQue(Fecha fecha) {
        boolean lower = false;
        if (this.year < fecha.year) {
            lower = true;
        } else if (this.year == fecha.year && this.month < fecha.month) {
            lower = true;
        } else if (this.year == fecha.year && this.month == fecha.month && this.day <= fecha.day) {
            lower = true;
        } else {
            lower = false;
        }
        return lower;
    }

    /**
     * Compara si la fecha recibida por parámetro es posterior a la fecha de la
     * clase.
     *
     * @param fecha Fecha con formato de la clase Fecha.
     * @return {@code true} si la fecha es posterior a la fecha de la clase.
     * {@code false} si la fecha no es posterior.
     */
    public boolean mayorQue(Fecha fecha) {
        boolean upper = false;
        if (this.year > fecha.year) {
            upper = true;
        } else if ((this.year == fecha.year) && (this.month > fecha.month)) {
            upper = true;
        } else if ((this.year == fecha.year) && (this.month == fecha.month) && (this.day >= fecha.day)) {
            upper = true;
        } else {
            upper = false;
        }
        return upper;
    }

    /**
     * Compara si la fecha recibida por parámetro es anterior o igual a la fecha
     * de la clase.
     *
     * @param fecha Fecha con formato de la clase Fecha.
     * @return {@code true} si la fecha es anterior o igual a la fecha de la
     * clase. {@code false} si la fecha es posterior.
     */
    public boolean menorIgualQue(Fecha fecha) {
        boolean lower = false;
        if (this.year < fecha.year) {
            lower = true;
        } else if (this.year == fecha.year && this.month < fecha.month) {
            lower = true;
        } else if (this.year == fecha.year && this.month == fecha.month && this.day <= fecha.day) {
            lower = true;
        } else {
            lower = false;
        }
        return lower;
    }

    /**
     * Compara si la fecha recibida por parámetro es posterior o igual a la
     * fecha de la clase.
     *
     * @param fecha Fecha con formato de la clase Fecha.
     * @return {@code true} si la fecha es posterior o igual a la fecha de la
     * clase. {@code false} si la fecha es anterior.
     */
    public boolean mayorIgualQue(Fecha fecha) {
        boolean upper = false;
        if (this.year > fecha.year) {
            upper = true;
        } else if ((this.year == fecha.year) && (this.month > fecha.month)) {
            upper = true;
        } else if ((this.year == fecha.year) && (this.month == fecha.month) && (this.day >= fecha.day)) {
            upper = true;
        } else {
            upper = false;
        }
        return upper;
    }

    /**
     * Calcula si la fecha del sistema comparada con la fecha de la clase es
     * mayor que la edad recibida por parámetro.
     *
     * @param edad Número entero que representa una edad.
     * @return {@code true} si la fecha recibida es mayor que el cálculo. 
     * {@code false} si el cálculo es inferior o igual.
     */
    public boolean mayorDe(int edad) {
        Calendar cal = Calendar.getInstance();
        boolean mayorDe = true;

        int anyo = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH) + 1;
        int dia = cal.get(Calendar.DAY_OF_MONTH);

        //EL MES DEL SISTEMA ME DA -1 MES **************************************************
        if (anyo - this.year < edad) {
            mayorDe = false;
        } else if (anyo - this.year == edad && this.month <= mes && this.day < dia) {
            mayorDe = false;
        } else {
            mayorDe = true;
        }
        return mayorDe;

    }

    /**
     * Convierte la fecha de 'hoy' a un formato '31-01-2001' como {@code String}.
     * @return La fecha de hoy como {@code String}.
     */
    public static String hoyDiaMesAnyo() {
        Calendar cal = Calendar.getInstance();

        int anyo = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH) + 1;
        int dia = cal.get(Calendar.DAY_OF_MONTH);

        //EL MES DEL SISTEMA ME DA -1 MES **************************************************
        return dia + "-" + mes + "-" + anyo;

    }

    /**
     * Convierte la fecha de 'hoy' a un formato '2001-01-31' como {@code String}.
     * @return La fecha de hoy como {@code String}.
     */
    public static String hoyAnyoMesDia() {
        Calendar cal = Calendar.getInstance();

        int anyo = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH) + 1;
        int dia = cal.get(Calendar.DAY_OF_MONTH);

        //EL MES DEL SISTEMA ME DA -1 MES **************************************************
        return anyo + "-" + mes + "-" + dia;

    }
}
