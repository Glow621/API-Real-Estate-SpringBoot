CREATE TABLE provincias(
	idProvincia int NOT NULL,
	nombre varchar(50) NULL,
    CONSTRAINT pkProvincias PRIMARY KEY (idProvincia)
)

CREATE TABLE clubes (
	idClub int IDENTITY(1,1) NOT NULL,
	nombre varchar(100) NULL,
	idProvincia int NULL,
	nroZona int NULL,
	CONSTRAINT pk_Clubes PRIMARY KEY (idClub), 
	CONSTRAINT fk_Clubes_Provincias FOREIGN KEY(idProvincia) REFERENCES Provincias
)



