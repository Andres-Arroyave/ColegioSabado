package com.example.colegio_sabados;

public class Clsmatriculas {

    private String matricula;
    private String fecha;
    private String carnet;
    private String materia;

    public Clsmatriculas(String matricula, String fecha, String carnet, String materia){
        this.matricula = matricula;
        this.fecha = fecha;
        this.carnet = carnet;
        this.materia = materia;
    }
    public Clsmatriculas() {
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }
}
