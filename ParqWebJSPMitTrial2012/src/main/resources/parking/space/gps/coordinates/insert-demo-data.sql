-- The following are the GPS coordinate for the 6 parking spots for the MIT DEMO
-- "42.357767, -71.094471, 1:",
-- "42.357789, -71.094409, 2:",
-- "42.357812, -71.094344, 3:",
-- "42.357838, -71.094275, 4:",
-- "42.357855, -71.094215, 5:",
-- "42.357875, -71.094151, 6:",

DELETE FROM geopoint WHERE location_id = (SELECT location_id FROM parkinglocation WHERE location_identifier = "MIT-SEPT-DEMO");
DELETE FROM parkingspace WHERE location_id = (SELECT location_id FROM parkinglocation WHERE location_identifier = "MIT-SEPT-DEMO");
DELETE FROM parkinglocation WHERE location_identifier = "MIT-SEPT-DEMO";
DELETE FROM parkinggrid WHERE grid_name = "MIT-SEPT-DEMO";
DELETE FROM client WHERE name = "MIT-SEPT-DEMO";


-- create the demo client
INSERT INTO client (name, payment_method) VALUES ("MIT-SEPT-DEMO", "NORMAL");

-- creat a single parking grid for the demo
INSERT INTO parkinggrid (grid_name, latitude, longitude) VALUES ("MIT-SEPT-DEMO", 42.357, -71.0935);

-- create the parking street
INSERT INTO parkinglocation (location_identifier, client_id, grid_id) VALUES
	("MIT-SEPT-DEMO", 
	 (SELECT client_id FROM client WHERE name = "MIT-SEPT-DEMO"),
	 (SELECT grid_id FROM parkinggrid WHERE grid_name = "MIT-SEPT-DEMO")); 
	
-- creat the start and end point GPS cor for the parking street
INSERT INTO geopoint (location_id, latitude, longitude, sort_order) VALUES
	((SELECT location_id FROM parkinglocation WHERE location_identifier = "MIT-SEPT-DEMO"), 42.357767, -71.094471, 1);
INSERT INTO geopoint (location_id, latitude, longitude, sort_order) VALUES
	((SELECT location_id FROM parkinglocation WHERE location_identifier = "MIT-SEPT-DEMO"), 42.357881, -71.094151, 2);
	
-- create the parking spaces
INSERT INTO parkingspace (location_id, space_identifier, latitude, longitude, segment) VALUES
	((SELECT location_id FROM parkinglocation WHERE location_identifier = "MIT-SEPT-DEMO"),
	 "MIT-SEPT-DEMO-SPACE-1", 42.357767, -71.094471, 1);
INSERT INTO parkingspace (location_id, space_identifier, latitude, longitude, segment) VALUES
	((SELECT location_id FROM parkinglocation WHERE location_identifier = "MIT-SEPT-DEMO"),
	 "MIT-SEPT-DEMO-SPACE-2", 42.357789, -71.094409, 1);
INSERT INTO parkingspace (location_id, space_identifier, latitude, longitude, segment) VALUES
	((SELECT location_id FROM parkinglocation WHERE location_identifier = "MIT-SEPT-DEMO"),
	 "MIT-SEPT-DEMO-SPACE-3", 42.357812, -71.094344, 1);
INSERT INTO parkingspace (location_id, space_identifier, latitude, longitude, segment) VALUES
	((SELECT location_id FROM parkinglocation WHERE location_identifier = "MIT-SEPT-DEMO"),
	 "MIT-SEPT-DEMO-SPACE-4", 42.357838, -71.094275, 1);
INSERT INTO parkingspace (location_id, space_identifier, latitude, longitude, segment) VALUES
	((SELECT location_id FROM parkinglocation WHERE location_identifier = "MIT-SEPT-DEMO"),
	 "MIT-SEPT-DEMO-SPACE-5", 42.357855, -71.094215, 1);
INSERT INTO parkingspace (location_id, space_identifier, latitude, longitude, segment) VALUES
	((SELECT location_id FROM parkinglocation WHERE location_identifier = "MIT-SEPT-DEMO"),
	 "MIT-SEPT-DEMO-SPACE-6", 42.357875, -71.094151, 1);

