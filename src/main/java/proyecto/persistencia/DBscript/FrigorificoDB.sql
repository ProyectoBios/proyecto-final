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

Create Table Lote(
	idLote int primary key AUTO_INCREMENT,
    fechaIngreso datetime not null,
    fechaVencimiento datetime not null,
    cantUnidades int not null,
    IDProducto int not null,
    letraRack varchar(1) not null,
    fila int not null,
    columna int not null,
    
    foreign key(IDProducto) references EspecificacionProducto(ID),
    foreign key(letraRack) references Rack(letra),    
    constraint ubicacion UNIQUE (letraRack, fila, columna)
);

Create Table Cliente(
	nombre varchar(30) primary key,
    telefono varchar(10) not null,
    correo varchar(40) not null
);

Create Table OrdenPedido(
	idOrden int primary key AUTO_INCREMENT,
	fecha datetime not null,
    estado varchar(20) not null,
    ultimaActEst datetime not null,
    direccionEnvio varchar(40) not null,
    contacto varchar(40) not null,
    subtotal double not null,
    impuestos double not null,
    total double not null,
    nombreCliente varchar(30) not null,
    
    foreign key(nombreCliente) references Cliente(nombre)
);

Create Table LineaPedido(
	idOrden int not null,
	numero int not null,
    cantidad int not null,
    importe double not null,
    idProducto int not null,
    
    foreign key (idProducto) references EspecificacionProducto(ID),
    foreign key (idOrden) references OrdenPedido(idOrden),
    primary key (idOrden, numero)
);

INSERT INTO EspecificacionProducto VALUES(NULL, 'Mortadela', 20, 10, 100, 0);

INSERT INTO PrecioProducto VALUES(1, 30.0, '20170527', '20171105');
INSERT INTO PrecioProducto VALUES(1, 35.5, '20171105', NULL);

INSERT INTO Rack VALUES('A', 10, 20);

INSERT INTO Lote VALUES(NULL, NOW(), '20180507', 50, 1, 'A', 1,1);
INSERT INTO Lote VALUES(NULL, NOW(), '20180315', 30, 1, 'A', 3, 5);
INSERT INTO Lote VALUES(NULL, NOW(), '20180111', 10, 1, 'A', 4, 4);


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
 END//
  
 Create Procedure BajaRack(pLetra varchar(1)) -- El rack debe estar vacio
 BEGIN
	DELETE 
    FROM Rack
    WHERE letra = pLetra;
 END//
 
 Create Procedure AltaLote(pFechaVencimiento datetime, pCantUn int, pIDProd int, pLetra varchar(1), pFila int, pColumna int, out id int)
 BEGIN
	INSERT INTO Lote VALUES(NULL, NOW(), pFechaVencimiento, pCantUn, pIDProd, pLetra, pFila, pColumna);
    
    SET id = LAST_INSERT_ID();
 END//
 
 Create Procedure BajaLote(pId int)
 BEGIN
	DELETE 
    FROM Lote
    WHERE idLote = pId;
 END
 
 
 