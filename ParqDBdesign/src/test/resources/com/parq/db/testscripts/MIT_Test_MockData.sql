-- create a new test client call "MIT_TEST" 
INSERT INTO client (name, address, client_desc, payment_method) 
	VALUES ('MIT_TEST', 'ParqTestAddress', 'ParqTestClient', 'prepaid');

-- Create a parking grid
INSERT INTO parkinggrid (grid_name, latitude, longitude)
	VALUES ('MIT_Campus', 42.362, -71.099);
	
-- create a new parking location for MIT_TEST test client, Volvo Garage, VoGa as the location identifier
-- according to google map, the MIT volvo garage is located at 42.361985,-71.098958
INSERT INTO parkinglocation (client_id, grid_id, location_identifier, location_name, location_type)  
	VALUE ((SELECT client_id FROM client WHERE name= 'MIT_TEST'), 
	       (SELECT grid_id FROM parkinggrid WHERE grid_name = 'MIT_Campus'),
			'VoGa', 'Volvo_Garage', 'street_parking');
INSERT INTO geopoint (location_id, latitude, longitude, sort_order)
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'VoGa'), 42.361985, -71.098958, 1);
-- setup the parking rate for volvo garage, $1.00 for 10 minute parking, no min or max parking limit 
INSERT INTO parkingrate (parking_rate_cents, priority, location_id, time_increment_mins)  
	VALUE ( 100, -100, (SELECT location_id FROM parkinglocation WHERE location_identifier = 'VoGa' ), 10);
-- insert 4 test parking spaces into the volvo garage
INSERT INTO parkingspace (location_id, space_identifier, space_name, parking_level, latitude, longitude) 
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'VoGa' ), '0001', 'MIT_TEST: space num 1', '1', 42.361985, -71.098958);
INSERT INTO parkingspace (location_id, space_identifier, space_name, parking_level, latitude, longitude) 
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'VoGa' ), '0002', 'MIT_TEST: space num 2', '1', 42.361985, -71.098958);
INSERT INTO parkingspace (location_id, space_identifier, space_name, parking_level, latitude, longitude)
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'VoGa' ), '0003', 'MIT_TEST: space num 3', '1', 42.361985, -71.098958);
INSERT INTO parkingspace (location_id, space_identifier, space_name, parking_level, latitude, longitude)
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'VoGa' ), '0004', 'MIT_TEST: space num 4', '1', 42.361985, -71.098958);


-- create a new parking location for MIT_TEST test client, Albany Garage, AlGa as the location identifier
-- according to google map, the MIT Albany garage is located at 42.362008,-71.092789
INSERT INTO parkinglocation (client_id, grid_id, location_identifier, location_name, location_type)  
	VALUE ((SELECT client_id FROM client WHERE name= 'MIT_TEST'),
			(SELECT grid_id FROM parkinggrid WHERE grid_name = 'MIT_Campus'),
			'AlGa', 'Albany_Garage', 'street_parking');
INSERT INTO geopoint (location_id, latitude, longitude, sort_order)
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'AlGa'), 42.362008, -71.092789, 1);
-- setup the parking rate for Albany garage, $0.50 for 15 minute parking, no min or max parking limit 
INSERT INTO parkingrate (parking_rate_cents, priority, location_id, time_increment_mins)  
	VALUE ( 50, -100, (SELECT location_id FROM parkinglocation WHERE location_identifier = 'AlGa' ), 15);
-- insert 6 test parking spaces into the Albany garage
INSERT INTO parkingspace (location_id, space_identifier, space_name, parking_level, latitude, longitude)
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'AlGa' ), '0005', 'MIT_TEST: space num 5', '1', 42.362008, -71.092789);
INSERT INTO parkingspace (location_id, space_identifier, space_name, parking_level, latitude, longitude)
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'AlGa' ), '0006', 'MIT_TEST: space num 6', '1', 42.362008, -71.092789);
INSERT INTO parkingspace (location_id, space_identifier, space_name, parking_level, latitude, longitude)
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'AlGa' ), '0007', 'MIT_TEST: space num 7', '1', 42.362008, -71.092789);
INSERT INTO parkingspace (location_id, space_identifier, space_name, parking_level, latitude, longitude)
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'AlGa' ), '0008', 'MIT_TEST: space num 8', '1', 42.362008, -71.092789);
INSERT INTO parkingspace (location_id, space_identifier, space_name, parking_level, latitude, longitude)
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'AlGa' ), '0009', 'MIT_TEST: space num 9', '1', 42.362008, -71.092789);
INSERT INTO parkingspace (location_id, space_identifier, space_name, parking_level, latitude, longitude)
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'AlGa' ), '0010', 'MIT_TEST: space num 9', '1', 42.362008, -71.092789);

-- create an admin user for MIT_TEST client call MitTestAdmin
INSERT INTO admin (email, password, client_id) 
	VALUES ('ParqTestUser@parq.com', 'mit', (SELECT client_id FROM client WHERE name= 'MIT_TEST'));



-- The following delete sql script delete the above test data
DELETE FROM admin WHERE email = 'ParqTestUser@parq.com';
-- Delete the parking location and parking space data 
DELETE FROM payment WHERE parkinginst_id
	IN (SELECT parkinginst_id FROM parkinginstance WHERE space_id
	IN (SELECT space_id FROM parkingspace WHERE location_id
	IN (SELECT location_id FROM parkinglocation WHERE location_identifier
	IN ('stdm', 'AlGa', 'VoGa'))));
DELETE FROM parkinginstance WHERE space_id
	IN (SELECT space_id FROM parkingspace WHERE location_id
	IN (SELECT location_id FROM parkinglocation WHERE location_identifier
	IN ('stdm', 'AlGa', 'VoGa')));
DELETE FROM parkingrate WHERE location_id 
	IN (SELECT location_id FROM parkinglocation WHERE location_identifier
	IN ('stdm', 'AlGa', 'VoGa'));
DELETE FROM parkingspace WHERE location_id IN (
	SELECT location_id FROM parkinglocation WHERE location_identifier
	IN ('stdm', 'AlGa', 'VoGa'));
DELETE FROM geopoint WHERE location_id IN (
	SELECT location_id FROM parkinglocation WHERE location_identifier
	IN ('stdm', 'AlGa', 'VoGa'));
DELETE FROM parkinglocation WHERE location_identifier IN ('stdm', 'AlGa', 'VoGa');
DELETE FROM parkinggrid WHERE grid_name = 'MIT_Campus';
-- Delete the MIT_TEST client
DELETE FROM client WHERE name = 'MIT_TEST';

