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
    eliminado bit,
    
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
    descripcionEntrega varchar(150),
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

Create Table Empleado(
	ci varchar(8) primary key,
    nombre varchar(30) not null,
    contrasenia varchar(30) not null,
    fechaNac date not null,
    fechaContratacion date not null,
    telefono varchar(10) not null,
    rol varchar(15) not null
);

Create Table Repartidor(
	ci varchar(8) primary key,
    vencLibreta date not null,
    foreign key (ci) references Empleado(ci)
);

Create Table Vehiculo(
	matricula varchar(8) primary key,
    marca varchar(20) not null,
    modelo varchar(20) not null,
    cargaMax int not null
);

Create Table Viaje(
	id int primary key AUTO_INCREMENT,
    ciRepartidor varchar(8) not null,
    matriculaVehiculo varchar(8) not null,
    fechaHora datetime not null,
    finalizado bit not null,
    
    foreign key (ciRepartidor) references Repartidor(ci),
    foreign key (matriculaVehiculo) references Vehiculo(matricula)
);

Create Table PedidosViaje(
	idViaje int not null,
    idPedido int not null,
    foreign key (idViaje) references Viaje(id),
    foreign key (idPedido) references OrdenPedido(idOrden),
    primary key (idViaje, idPedido)
);

INSERT INTO EspecificacionProducto VALUES(NULL, 'Mortadela', 20, 10, 100, 0);
INSERT INTO EspecificacionProducto VALUES(NULL, 'Chorizo', 100, 50, 500, 0);
INSERT INTO EspecificacionProducto VALUES(NULL, 'Longaniza', 140, 60, 650, 0);

INSERT INTO PrecioProducto VALUES(1, 30.0, '20170527', '20171105');
INSERT INTO PrecioProducto VALUES(1, 10.0, '20171105', NULL);
INSERT INTO PrecioProducto VALUES(2, 25.0, '20171105', NULL);
INSERT INTO PrecioProducto VALUES(3, 30.0, '20171105', NULL);

INSERT INTO Rack VALUES('A', 10, 20);
INSERT INTO Rack VALUES('D', 12, 18);
INSERT INTO Rack VALUES('C', 10, 20);
INSERT INTO Rack VALUES('B', 10, 20);

INSERT INTO Lote VALUES(NULL, NOW(), '20180507', 50, 1, 'A', 1,1, 0);
INSERT INTO Lote VALUES(NULL, NOW(), '20180322', 30, 1, 'A', 3, 5, 0);
INSERT INTO Lote VALUES(NULL, NOW(), '20180111', 10, 1, 'A', 4, 4, 0);
INSERT INTO Lote VALUES(NULL, NOW(), '20180524', 1000, 1, 'A', 1, 2, 0);

INSERT INTO Lote VALUES(NULL, NOW(), '20180422', 30, 2, 'A', 1, 3, 0);
INSERT INTO Lote VALUES(NULL, NOW(), '20180530', 150, 2, 'A', 1, 4, 0);

INSERT INTO Lote VALUES(NULL, NOW(), '20180530', 60, 3, 'A', 2, 1, 0);
INSERT INTO Lote VALUES(NULL, NOW(), '20180530', 10, 3, 'A', 2, 2, 0);

INSERT INTO Cliente VALUES('Disco', '1234567890', 'disco@disco.com');
INSERT INTO Cliente VALUES('Carniceria Pepe', '0987654321', 'pepe@gmail.com');

INSERT INTO OrdenPedido VALUES(NULL, NOW(), 'pendiente', '', NOW(), 'Solomeo Paredes 2020', 'Peteco', 5075, 1116.5, 6191.5, 'Carniceria Pepe'); 

INSERT INTO LineaPedido VALUES(1, 1, 200, 2000.0, 1);
INSERT INTO LineaPedido VALUES(1, 2, 45, 1125.0, 2);
INSERT INTO LineaPedido VALUES(1, 3, 65, 1950.0, 3);

INSERT INTO Empleado VALUES('12345678', 'Pedro Rodriguez','1234' , '19840324', '20171009', '091789456', 'repartidor');
INSERT INTO Empleado VALUES('32165498', 'Pepe Martin','abcd' ,'19800515', '20160520', '092879654', 'repartidor');
INSERT INTO Empleado VALUES('36328662', 'Alvaro Martinez','1234' ,'19840329', '20160520', '092879654', 'gerente');

INSERT INTO Repartidor VALUES('12345678', '20220814');
INSERT INTO Repartidor VALUES('32165498', '20200622');

INSERT INTO Vehiculo VALUES('SBU 3940', 'Volkswagen', 'Worker', 9000);
INSERT INTO Vehiculo VALUES('SAF 4589', 'Hyundai', 'Hd45', 2500);

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
	INSERT INTO Lote VALUES(NULL, NOW(), pFechaVencimiento, pCantUn, pIDProd, pLetra, pFila, pColumna, 0);
    
    SET id = LAST_INSERT_ID();
 END//
 
 Create Procedure BajaLote(pId int)
 BEGIN
	DELETE 
    FROM Lote
    WHERE idLote = pId;
 END//
 
 Create Procedure BuscarClientes(pNombre varchar(30))
 BEGIN
	SELECT * 
    FROM Cliente
    WHERE lower(nombre) LIKE lower(concat('%', trim(pNombre), '%'));
 END//
 
 Create Procedure AltaOrdenDePedido(pEstado varchar(20), pDireccion varchar(40), pContacto varchar(40), pSubtotal double, pImpuestos double, pTotal double, pCliente varchar(30), out id int)
 BEGIN
	INSERT INTO OrdenPedido VALUES(NULL, NOW(), pEstado, '', NOW(), pDireccion, pContacto, pSubtotal, pImpuestos, pTotal, pCliente);
    
    SET id = LAST_INSERT_ID();
 END//
 
 Create Procedure AltaLineaPedido(pOrden int, pNumero int, pCantidad int, pImporte double, pProducto int)
 BEGIN
	INSERT INTO LineaPedido VALUES(pOrden, pNumero, pCantidad, pImporte, pProducto);
 END//
 
 Create Procedure AltaViaje(pCiRep varchar(8), pMatricula varchar(8), out id int)
 BEGIN
	INSERT INTO Viaje VALUES(NULL, pCiRep, pMatricula, NOW(), 0);
    
    Set id = LAST_INSERT_ID();
 END//
 
 Create Procedure ListarIdViajeYVehiculoXRepartidor(pCiRepartidor varchar(8))
 BEGIN
	SELECT Viaje.id, Viaje.matriculaVehiculo, Viaje.fechaHora
    FROM Viaje
    WHERE Viaje.finalizado = 0 AND Viaje.ciRepartidor = pCiRepartidor
    ORDER BY fechaHora asc;
 
 END//
 
 