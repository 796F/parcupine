DROP DATABASE parq;

CREATE DATABASE parq;

USE parq;

CREATE TABLE User
(User_ID INT NOT NULL AUTO_INCREMENT,  
 Password TEXT(64) NOT NULL, 
 eMail TEXT(64) NOT NULL,
 Phone_Number TEXT(64),
 Is_Deleted BOOLEAN DEFAULT FALSE,
 LastUpdateDateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (User_ID));
 
CREATE TABLE PaymentAccount
(Account_ID INT NOT NULL AUTO_INCREMENT,  
 User_ID INT NOT NULL,
 Customer_ID TEXT(64), 
 Payment_Method_ID TEXT(64),
 CC_Stub Text(64),
 Is_Default_Payment BOOLEAN DEFAULT FALSE,
 Is_Deleted BOOLEAN DEFAULT FALSE,
 LastUpdateDateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (Account_ID),
 FOREIGN KEY (User_ID) references User(User_ID));
 
CREATE TABLE UserRole
(UserRole_ID INT NOT NULL AUTO_INCREMENT,
 Role_Name TEXT(64) NOT NULL,
 Role_DESC TEXT(256),
 Parking_Rate DECIMAL(5,3),
 LastUpdateDateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (UserRole_ID));

CREATE TABLE Admin
(Admin_ID INT NOT NULL AUTO_INCREMENT,
 UserName TEXT(64) NOT NULL,
 Password TEXT(64) NOT NULL,
 eMail TEXT(64) NOT NULL,
 Is_Deleted BOOLEAN DEFAULT FALSE,
 LastUpdateDateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (Admin_ID));
 
CREATE TABLE Client
(Client_ID INT NOT NULL AUTO_INCREMENT,
 Name TEXT(64) NOT NULL,
 Address TEXT(256),
 Client_Desc TEXT(256),
 Is_Deleted BOOLEAN DEFAULT FALSE,
 LastUpdateDateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (Client_ID)); 
 
CREATE TABLE AdminRole
(AdminRole_ID INT NOT NULL AUTO_INCREMENT,
 Role_Name TEXT(64) NOT NULL,
 Role_Desc TEXT(256),
 LastUpdateDateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (AdminRole_ID)); 
 
CREATE TABLE AdminClientRelationship
(AC_Rel_ID INT NOT NULL AUTO_INCREMENT,
 Admin_ID INT NOT NULL,
 Client_ID INT NOT NULL,
 AdminRole_ID INT NOT NULL,
 LastUpdateDateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (AC_Rel_ID),
 FOREIGN KEY (Admin_ID) references Admin(Admin_ID),
 FOREIGN KEY (Client_ID) references Client(Client_ID),
 FOREIGN KEY (AdminRole_ID) references AdminRole(AdminRole_ID));
 
CREATE TABLE ParkingLocation
(Location_ID INT NOT NULL AUTO_INCREMENT,
 Location_Identifier TEXT(16) NOT NULL,
 Client_ID INT NOT NULL,
 Location_Name TEXT(64),
 Is_Deleted BOOLEAN DEFAULT FALSE,
 LastUpdateDateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (Location_ID),
 FOREIGN KEY (Client_ID) references Client(Client_ID));
 
CREATE TABLE Geolocation
(Geolocation_ID INT NOT NULL AUTO_INCREMENT,
 Location_ID INT NOT NULL,
 Latitude DOUBLE NOT NULL,
 Longitude DOUBLE NOT NULL,
 LastUpdateDateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (Geolocation_ID),
 FOREIGN KEY (Location_ID) references ParkingLocation(Location_ID));
 
CREATE TABLE ParkingSpace
(Space_ID INT NOT NULL AUTO_INCREMENT,
 Space_Identifier TEXT(16) NOT NULL,
 Location_ID INT NOT NULL,
 Space_Name TEXT(64),
 Parking_Level TEXT(16),
 Is_Deleted BOOLEAN DEFAULT FALSE,
 LastUpdateDateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (Space_ID),
 FOREIGN KEY (Location_ID) references ParkingLocation(Location_ID));

CREATE TABLE ParkingRate
(CDR_ID INT NOT NULL AUTO_INCREMENT,
 Location_ID INT NOT NULL,
 Parking_Rate_Cents INT NOT NULL,
 Time_Increment_Mins INT NOT NULL,
 Priority INT NOT NULL,
 Min_Park_Mins INT,
 Max_Park_Mins INT,
 Space_ID INT,
 LastUpdateDateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (CDR_ID),
 FOREIGN KEY (Location_ID) references ParkingLocation(Location_ID),
 FOREIGN KEY (Space_ID) references ParkingSpace(Space_ID));
 
CREATE TABLE UserClientRelationship
(UC_Rel_ID INT NOT NULL AUTO_INCREMENT,
 User_ID INT NOT NULL,
 Client_ID INT NOT NULL,
 UserRole_ID INT NOT NULL,
 Priority INT NOT NULL,
 Location_ID INT,
 Space_ID INT,
 LastUpdateDateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (UC_Rel_ID),
 FOREIGN KEY (User_ID) references User(User_ID),
 FOREIGN KEY (Client_ID) references Client(Client_ID),
 FOREIGN KEY (UserRole_ID) references UserRole(UserRole_ID),
 FOREIGN KEY (Location_ID) references ParkingLocation(Location_ID),
 FOREIGN KEY (Space_ID) references ParkingSpace(Space_ID));

CREATE TABLE ParkingInstance
(ParkingInst_ID INT NOT NULL AUTO_INCREMENT,
 User_ID INT NOT NULL,
 Space_ID INT NOT NULL,
 Park_Began_Time DATETIME NOT NULL,
 Park_End_Time DATETIME NOT NULL,
 Is_Paid_Parking BOOLEAN NOT NULL,
 LastUpdateDateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (ParkingInst_ID),
 FOREIGN KEY (User_ID) references User(User_ID),
 FOREIGN KEY (Space_ID) references ParkingSpace(Space_ID));
 
CREATE TABLE Payment
(Payment_ID INT NOT NULL AUTO_INCREMENT,
 ParkingInst_ID INT NOT NULL,
 Payment_Type TEXT(64) NOT NULL,
 Account_ID INT,
 Payment_Ref_Num TEXT(64) NOT NULL,
 Payment_DateTime DATETIME NOT NULL,
 Amount_Paid_Cents INT NOT NULL,
 LastUpdateDateTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
 PRIMARY KEY (Payment_ID),
 FOREIGN KEY (ParkingInst_ID) references ParkingInstance(ParkingInst_ID),
 FOREIGN KEY (Account_ID) references PaymentAccount(Account_ID));

