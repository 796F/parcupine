-- create a new test client call "MIT_TEST" 
INSERT INTO client (name, address, client_desc) 
	VALUES ('MIT_TEST', 'ParqTestAddress', 'ParqTestClient');

-- create a new parking location for MIT_TEST test client, Volvo Garage, VoGa as the location identifier
INSERT INTO parkinglocation (client_id, location_identifier, location_name)  
	VALUE ((SELECT client_id FROM client WHERE name= 'MIT_TEST'), 'VoGa', 'Volvo_Garage');
-- according to google map, the MIT volvo garage is located at 42.361985,-71.098958
INSERT INTO geolocation (location_id, latitude, longitude)  
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'VoGa' ),  42.361985, -71.098958);
-- setup the parking rate for volvo garage, $1.00 for 10 minute parking, no min or max parking limit 
INSERT INTO parkingrate (parking_rate_cents, priority, location_id, time_increment_mins)  
	VALUE ( 100, -100, (SELECT location_id FROM parkinglocation WHERE location_identifier = 'VoGa' ), 10);
-- insert 4 test parking spaces into the volvo garage
INSERT INTO parkingspace (location_id, space_identifier, space_name, parking_level) 
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'VoGa' ), '0001', 'MIT_TEST: space num 1', '1');
INSERT INTO parkingspace (location_id, space_identifier, space_name, parking_level) 
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'VoGa' ), '0002', 'MIT_TEST: space num 2', '1');
INSERT INTO parkingspace (location_id, space_identifier, space_name, parking_level)
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'VoGa' ), '0003', 'MIT_TEST: space num 3', '1');
INSERT INTO parkingspace (location_id, space_identifier, space_name, parking_level)
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'VoGa' ), '0004', 'MIT_TEST: space num 4', '1');
	
-- create a new parking location for MIT_TEST test client, Albany Garage, AlGa as the location identifier
INSERT INTO parkinglocation (client_id, location_identifier, location_name)  
	VALUE ((SELECT client_id FROM client WHERE name= 'MIT_TEST'), 'AlGa', 'Albany_Garage');
-- according to google map, the MIT Albany garage is located at 42.362008,-71.092789
INSERT INTO geolocation (location_id, latitude, longitude)  
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'AlGa' ),  42.362008, -71.092789);
-- setup the parking rate for Albany garage, $0.50 for 15 minute parking, no min or max parking limit 
INSERT INTO parkingrate (parking_rate_cents, priority, location_id, time_increment_mins)  
	VALUE ( 50, -100, (SELECT location_id FROM parkinglocation WHERE location_identifier = 'AlGa' ), 15);
-- insert 6 test parking spaces into the Albany garage
INSERT INTO parkingspace (location_id, space_identifier, space_name, parking_level)
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'AlGa' ), '0005', 'MIT_TEST: space num 5', '1');
INSERT INTO parkingspace (location_id, space_identifier, space_name, parking_level)
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'AlGa' ), '0006', 'MIT_TEST: space num 6', '1');
INSERT INTO parkingspace (location_id, space_identifier, space_name, parking_level)
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'AlGa' ), '0007', 'MIT_TEST: space num 7', '1');
INSERT INTO parkingspace (location_id, space_identifier, space_name, parking_level)
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'AlGa' ), '0008', 'MIT_TEST: space num 8', '1');
INSERT INTO parkingspace (location_id, space_identifier, space_name, parking_level) 
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'AlGa' ), '0009', 'MIT_TEST: space num 9', '1');
INSERT INTO parkingspace (location_id, space_identifier, space_name, parking_level)  
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'AlGa' ), '0010', 'MIT_TEST: space num 9', '1');

-- create a new parking location for MIT_TEST test client, Henry Steinbrenner Stadium, stdm as the location identifier
INSERT INTO parkinglocation (client_id, location_identifier, location_name)  
	VALUE ((SELECT client_id FROM client WHERE name= 'MIT_TEST'), 'stdm', 'Henry_Steinbrenner_Stadium');
-- according to google map, the MIT Henry Steinbrenner Stadium is located at 42.358000,-71.097983
INSERT INTO geolocation (location_id, latitude, longitude)  
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'stdm' ),  42.358, -71.097983);
-- setup the parking rate for Henry Steinbrenner Stadium, $5.00 for 1 hour parking, no min or max parking limit 
INSERT INTO parkingrate (parking_rate_cents, priority, location_id, time_increment_mins)  
	VALUE ( 500, -100, (SELECT location_id FROM parkinglocation WHERE location_identifier = 'stdm' ), 60);
-- insert 2 test parking spaces into the Henry Steinbrenner Stadium
INSERT INTO parkingspace (location_id, space_identifier, space_name, parking_level)
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'stdm' ), '0011', 'MIT_TEST: space num 11', '1');
INSERT INTO parkingspace (location_id, space_identifier, space_name, parking_level)  
	VALUE ((SELECT location_id FROM parkinglocation WHERE location_identifier = 'stdm' ), '0012', 'MIT_TEST: space num 12', '1');

-- create an admin user for MIT_TEST client call MitTestAdmin
INSERT INTO admin (email, password ) VALUES ('ParqTestUser@parq.com', 'mit');
-- associate the MitTestAdmin with the MIT_TEST client 
INSERT INTO adminclientrelationship (client_id, admin_id, adminrole_id)
	VALUE ((SELECT client_id FROM client WHERE name = 'MIT_TEST'),
	(SELECT admin_id FROM admin WHERE username = 'MitTestAdmin'),
	(SELECT adminrole_id FROM adminrole WHERE role_name = 'admin' LIMIT 1));
 



-- The following delete sql script delete the above test data
-- Delete the admin user along with the client relationship
DELETE FROM adminclientrelationship WHERE client_id = (SELECT client_id FROM client WHERE name = 'MIT_TEST');
DELETE FROM admin WHERE username = 'MitTestAdmin';
-- Delete the parking location and parking space data 
DELETE FROM geolocation WHERE location_id 
	IN (SELECT location_id FROM parkinglocation WHERE location_identifier
	IN ('stdm', 'AlGa', 'VoGa'));
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
DELETE FROM parkinglocation WHERE location_identifier IN ('stdm', 'AlGa', 'VoGa');
-- Delete the MIT_TEST client
DELETE FROM client WHERE name = 'MIT_TEST';

