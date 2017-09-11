Drop Database if exists FrigorificoDB;

Create Database FrigorificoDB;

use Frigorifico;

Create Table EspecificacionProducto(
	ID int primary key,
	nombre varchar(40) not null,
	minStock int not null,
    stockCritico int not null,
    maxStock int not null,
    eliminado bit not null
);

DELIMITER //

Create Procedure AltaEspProducto(pID int, pNombre varchar(40), pMinStock int, pStockCritico int, pMaxStock int)
BEGIN
	IF EXISTS(SELECT * FROM EspecificacionProducto WHERE ID = pID AND eliminado = 1) THEN
		UPDATE EspecificacionProducto
		SET Nombre = pNombre, minStock = pMinStock, stockCritico = pStockCritico, maxStock = pMaxStock, eliminado = 0
        WHERE ID = pID;
	ELSE
		INSERT INTO EspecificacionProducto VALUES(pID, pNombre, pMinStock, pStockCritico, pMaxStock, 0);
	END IF;  
 END//
 
 Create procedure BajaEspProducto(pID int) -- Cambiar por baja logica en futuras iteraciones
 BEGIN
	DELETE 
    FROM EspecificacionProducto
    WHERE ID = pID;
 END//
 
 Create Procedure ModificarProducto(pID int, pNombre varchar(40), pMinStock int, pStockCritico int, pMaxStock int)
 BEGIN
	UPDATE EspecificacionProducto
    SET Nombre = pNombre, minStock = pMinStock, stockCritico = pStockCritico, maxStock = pMaxStock, eliminado = 0
    WHERE ID = pID;
 END//
 