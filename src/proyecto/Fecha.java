/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;


public class Fecha {

    private int day;
    private int month;
    private int year;
   
    //Constructor con los parámetros con día, mes y año.
    
    public Fecha(int day, int month, int year){
        this.day=day;
        this.month=month;
        this.year=year;
    }
    
    // Métodos set y get para los 3 atributos.
    public int getDia(){
        return this.day;
    }
    public int getMes(){
        return this.month;
    }
    public int getAnyo(){
        return this.year;
    }
    public void setDia(int day){
        this.day=day;
    }
    public void setMes(int month){
        this.month=month;
    }
    public void setAnyo(int year){
        this.year=year;
    }
    //corta(): mostrará la fecha en formato corto (02-09-2003). 
    public String corta(){
        return this.day+"-"+this.month+"-"+this.year;
    }
    
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
    
     public LocalDate datePicker() {
        String fecha = this.year + "/" + this.month + "/" + this.day;
        final DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy/MMM/dd");
        final LocalDate fechaConvertida = LocalDate.parse(fecha,formato);
        return fechaConvertida;
    }
     public boolean menorQue(Fecha fecha){
        boolean lower=false;
        if (this.year < fecha.year ){
            lower=true;
        }else if (this.year  == fecha.year && this.month <fecha.month  ){
            lower=true;
        }else if ( this.year ==fecha.year && this.month == fecha.month &&  this.day < fecha.day ){ 
            lower=true;
        }else{
            lower=false;
        }
        return lower;
    }
     public boolean mayorQue(Fecha fecha){
        boolean upper=false;
        if (this.year > fecha.year){
            upper=true;
        }else if((this.year == fecha.year) && ( this.month >fecha.month ) ){
            upper=true;
        }else if((this.year == fecha.year ) && (this.month == fecha.month ) && (this.day > fecha.day )){
            upper=true;
        }else{
            upper=false;
        }
        return upper;
    }
      public boolean mayorDe(int edad){
        Calendar cal = Calendar.getInstance();
        boolean mayorDe=true;
  
        int anyo= cal.get(Calendar.YEAR);
        int mes= cal.get(Calendar.MONTH)+1;
        int dia= cal.get(Calendar.DAY_OF_MONTH);
        
        //EL MES DEL SISTEMA ME DA -1 MES **************************************************
     
        if (anyo-this.year<edad) {
            mayorDe=false;
        }else if (anyo-this.year==edad && this.month<=mes && this.day<dia){
            mayorDe=false;
        }else{
            mayorDe=true;
        }
        return mayorDe;
    
    }
}
