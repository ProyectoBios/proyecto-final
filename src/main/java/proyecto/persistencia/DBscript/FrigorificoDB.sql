Drop Database if exists FrigorificoDB;

Create Database FrigorificoDB;

use FrigorificoDB;

Create Table EspecificacionProducto(
	ID int primary key AUTO_INCREMENT,
	nombre varchar(40) not null,
	minStock int not null,
    stockCritico int not null,
    maxStock int not null,
    eliminado bit not null
);

Create Table PrecioProducto(
    IDproducto int not null,
    precio double not null,
	fechaIni datetime not null,
    fechaFin datetime null,
    
    foreign key (IDProducto) references EspecificacionProducto(ID),
    primary key (IDproducto, fechaIni)
);

Create Table Rack(
	letra varchar(1) primary key,
    dimAlto int not null,
    dimAncho int not null
);

INSERT INTO EspecificacionProducto VALUES(NULL, 'A', 20, 10, 100, 0);

INSERT INTO PrecioProducto VALUES(1, 30.0, '20170527', '20171105');
INSERT INTO PrecioProducto VALUES(1, 35.5, '20171105', NULL);

INSERT INTO Rack VALUES('A', 10, 20);

DELIMITER //

Create Procedure AltaEspProducto(pNombre varchar(40), pMinStock int, pStockCritico int, pMaxStock int, precio double, out id int) 
BEGIN
	INSERT INTO EspecificacionProducto VALUES(NULL, pNombre, pMinStock, pStockCritico, pMaxStock, 0);
        
	SET id = LAST_INSERT_ID();
        
	INSERT INTO PrecioProducto VALUES(id, precio, NOW(), NULL);
    
 END//
 
 Create procedure BajaEspProducto(pID int) -- Cambiar por baja logica en futuras iteraciones
 BEGIN
	DELETE 
    FROM PrecioProducto
    WHERE IDProducto = pID;	
 
	DELETE 
    FROM EspecificacionProducto
    WHERE ID = pID;
 END//
 
 Create Procedure ModificarProducto(pID int, pNombre varchar(40), pMinStock int, pStockCritico int, pMaxStock int, pPrecio double)
 BEGIN
	DECLARE transaccionActiva BIT;

	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		IF transaccionActiva THEN
			ROLLBACK;
        END IF;
	END;
    
    SET transaccionActiva = 1;
    START transaction;
 
	UPDATE EspecificacionProducto
    SET Nombre = pNombre, minStock = pMinStock, stockCritico = pStockCritico, maxStock = pMaxStock
	WHERE ID = pID;

	SET @precioActual = (SELECT precio FROM PrecioProducto WHERE IDproducto = pID AND fechaFin IS NULL);
    
    IF(@precioActual != pPrecio) then
		UPDATE PrecioProducto
        SET fechaFin = NOW()
        WHERE IDproducto = pID AND fechaFin IS NULL;
        
        INSERT INTO PrecioProducto VALUES(pID, pPrecio, NOW(), NULL);
	END IF;
    
    COMMIT;
 END//
 
 Create Procedure AltaRack(pLetra varchar(1), pDimAlto int, pDimAncho int)
 BEGIN
	INSERT INTO Rack VALUES(pLetra, pDimAlto, pDimAncho);
 END
 
 