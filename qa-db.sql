CREATE DATABASE qa_db;
USE qa_db;

CREATE TABLE usuario (
	nombre VARCHAR(150) PRIMARY KEY NOT NULL,
	correo VARCHAR(150) NOT NULL,
	password VARCHAR(100) NOT NULL,
	foto_perfil LONGBLOB) ENGINE=INNODB;

CREATE TABLE cuestionario(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	nombre VARCHAR(150) NOT NULL,
	veces_jugado INT NOT NULL,
	ultimo_ganador VARCHAR(150),
	autor VARCHAR(150) NOT NULL,
	FOREIGN KEY (autor) REFERENCES usuario(nombre) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=INNODB;

CREATE TABLE pregunta(
	id_cuestionario BIGINT NOT NULL,
	numero INT NOT NULL,
	descripcion VARCHAR(300),
	imagen LONGBLOB,
	PRIMARY KEY (id_cuestionario, numero),
	FOREIGN KEY (id_cuestionario) REFERENCES cuestionario(id) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=INNODB;

CREATE TABLE respuesta(
	id_cuestionario BIGINT NOT NULL,
	numero_pregunta INT NOT NULL,
	letra CHAR NOT NULL,
	descripcion VARCHAR(300),
	imagen LONGBLOB,
	correcta BOOLEAN NOT NULL,
	PRIMARY KEY (id_cuestionario, numero_pregunta, letra),
	FOREIGN KEY (id_cuestionario) REFERENCES cuestionario(id) ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=INNODB;