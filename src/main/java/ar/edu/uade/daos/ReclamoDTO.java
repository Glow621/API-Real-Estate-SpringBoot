package ar.edu.uade.daos;

public class ReclamoDTO {


    private String ubicacion;
    private String descripcion;
    private String estado;
    private String dniPersona;
    private Integer idUnidad;
    private String tipoReclamo;

    public String getUbicacion() {
        return ubicacion;
    }
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getDniPersona() {
        return dniPersona;
    }
    public void setDniPersona(String dniPersona) {
        this.dniPersona = dniPersona;
    }
    public Integer getIdUnidad() {
        return idUnidad;
    }
    public void setIdUnidad(Integer idUnidad) {
        this.idUnidad = idUnidad;
    }
    public String getTipoReclamo() {
        return tipoReclamo;
    }
    public void setTipoReclamo(String idTipoReclamo) {
        this.tipoReclamo = idTipoReclamo;
    }

    

    
}
