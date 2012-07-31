//
//  Server.m
//  Parq
//
//  Created by Michael Xia on 6/1/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

/* 
 The network layer handles all interactions with the server.  these methods can be accessed by
 retrieving the data layer object from the app delegate, which can be reached by all classes.
 All pushes to, and fetches from, will go thorugh this layer.  There is a single instance.  
 */

#import "Grid.h"
#import "Parser.h"
#import "User.h"
#import "NetworkLayer.h"
#import "PQMapViewController.h"
#import "AbstractRequestObject.h"

//microblock constants
#define MICROBLOCK_REF_LAT 42.245802 //42.245802
#define MICROBLOCK_REF_LON -71.321869 
#define GRID_MICROBLOCK_COLUMNS 40
#define GRID_MICROBLOCK_ROWS 40

#define STREET_MICROBLOCK_COLUMNS 160
#define STREET_MICROBLOCK_ROWS 160      //25,600 total for street level.  

#define SPOT_MICROBLOCK_COLUMNS 640
#define SPOT_MICROBLOCK_ROWS 640    //409600 total mbids per city for spot level.  

#define GRID_MICROBLOCK_LENGTH_DEGREES 0.01
#define STREET_MICROBLOCK_LENGTH_DEGREES 0.0025
#define SPOT_MICROBLOCK_LENGTH_DEGREES 0.000625

#define MID_BOUNDING_ERROR -1

@implementation NetworkLayer

@synthesize dataLayer;
@synthesize mapController;

-(void) testAsync{

    NSArray* keys = [NSArray arrayWithObjects:@"email", @"password", nil];
    NSArray* value = [NSArray arrayWithObjects:@"miguel@parqme.com", @"a", nil];
    NSDictionary* info = [NSDictionary dictionaryWithObjects:value forKeys:keys];
    NSError *error;
    NSData* jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    AbstractRequestObject* abs = [[AbstractRequestObject alloc] init];
    [abs setBody:jsonData];
    [abs setContentType:@"application/json"];
    [[RKClient sharedClient] post:@"/parkservice.auth" params:abs delegate:self];
    NSLog(@"\nREQUEST >>> %@", [info description]);
    
}

-(void) decideUIType{
    //send request to server, store the int value.  
//    RKRequest* req = [[RKRequest alloc] initWithURL:[NSURL URLWithString:@"http://75.101.132.219/ui.php"]];
//    RKResponse* result = [req sendSynchronously];
//    int type = [result.bodyAsString intValue];
    int type = 0;
    [dataLayer setUIType:type];
}


#pragma mark - RESTKIT callbacks
//ALL SERVER REQUESTS WILL GET HANDLED BY THIS METHOD ASYNCHRONOUSLY.  
-(void) request:(RKRequest *)request didLoadResponse:(RKResponse *)response{    
    //handle background data download to improve the user experience.  
    NSLog(@"\nRESULT >>> %@", [response bodyAsString]);
    
    //add overlays returned using
    [mapController addNewOverlays:nil OfType:kGridEntity];
    //update overlays returned using this.  
    [mapController updateOverlays:nil OfType:kGridEntity];
    
    //this method will be saving the server responses
    [dataLayer store:kGridEntity WithData:nil];
}

-(void) request:(RKRequest *)request didFailLoadWithError:(NSError *)error{
    if(error.code==-1004){
        NSLog(@"body%s\n", request.HTTPBodyString.UTF8String);
        NSLog(@"couldn't connect to server\n");
    }
}

-(void) requestDidTimeout:(RKRequest *)request{
    NSLog(@"timed out\n");    
}

-(void) request:(EntityType) entityType WithIDs:(NSArray*)IDsToRequest{
    if(IDsToRequest.count>0){
        
        //*debug DO NOTHING
        return;
        //end debug
        
        //request from the server overlay information like lat/long etc.  
        NSArray* boundingBoxes = [self convertGridLevelIDs:IDsToRequest];
        NSNumber* lastUpdateTimeLong = [NSNumber numberWithLong:[[NSDate date] timeIntervalSince1970]];
        NSDictionary* requestDictionary = [[NSDictionary alloc] initWithObjectsAndKeys:boundingBoxes, @"boxes",lastUpdateTimeLong, @"lastUpdateTime", nil];
        
        //NSLog(@"bounding boxes: %s\n", requestDictionary.description.UTF8String);
        NSError *error;
        NSData* jsonData = [NSJSONSerialization dataWithJSONObject:requestDictionary options:0 error:&error];
        AbstractRequestObject* abs = [[AbstractRequestObject alloc] init];
        [abs setBody:jsonData];
        [abs setContentType:@"application/json"];
        [[RKClient sharedClient] post:@"/parkservice.auth" params:abs delegate:self];
    }
}

-(SpotInfo*) getSpotInfoForId:(NSNumber*)spotId SpotNumber:(NSNumber*)spotNum GPS:(CLLocationCoordinate2D*)coord{
    //depending on which pieces we're given, we will submit different network responses.  
    
    //so we may use id and gps, or num and gps, etc.  
    SpotInfo* spot = [[SpotInfo alloc] init];
    [spot setMaxTime:[NSNumber numberWithFloat:120]];
    [spot setMinTime:[NSNumber numberWithFloat:15]];
    [spot setMinuteInterval:[NSNumber numberWithFloat:1]];
    [spot setRateCents:[NSNumber numberWithFloat:25]];
    [spot setStreetName:@"Howard St, MA"];

    [spot setSpotNumber:spotNum]; //this should come from server, act as a check.  
    
    return spot;
}
-(void) addOverlayOfType:(EntityType) entityType ToMapForIDs:(NSArray*) newIDs AndUpdateForIDs:(NSArray*) updateIDs{
    
    NSMutableArray* IDsToRequest = [[NSMutableArray alloc] init];
    NSMutableArray* tempNewIDs = [[NSMutableArray alloc] initWithArray:newIDs];
    //go through the newIDs and check if any aren't in Core Data.  
    for(NSNumber* mbid in newIDs){
        if (![dataLayer mbIdExistsInCoreData:mbid EntityType:entityType]){
            //if doesn't exist in core data, make a note to request from server
            [IDsToRequest addObject:mbid];
            [tempNewIDs removeObject:mbid];
        }
    }
    //tell the data layer to provide map with overlays it has.  
    [dataLayer fetch:entityType ForIDs:tempNewIDs];
    //request for those that aren't to fill in the holes.  
    [self request:entityType WithIDs:IDsToRequest];

    //call update on whole map
    CLLocationCoordinate2D NE = [mapController topRightOfMap];
    CLLocationCoordinate2D SW = [mapController botLeftOfMap];
    
    [self updateOverlayOfType:entityType WithNE:&NE SW:&SW];
}


-(void) updateOverlayOfType:(EntityType) entityType WithNE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft{
    //send request to server to get updated colors
    
    //package them properly for the map controller's function.  
    [mapController updateOverlays:nil OfType:kGridEntity];
}

#pragma mark - MICROBLOCK FUNCTIONS

-(NSMutableArray*) getMBIDsWithType:(EntityType) entityType NE:(CLLocationCoordinate2D*) topRight SW:(CLLocationCoordinate2D*) botLeft{
    NSMutableArray* microBlockIds = [[NSMutableArray alloc] init];
    
    int colLength;
    long topRightId;
    //ind 0 is row and 1 is col
    unsigned long topRightRowCol[2];
    long botLeftId;
    unsigned long botLeftRowCol[2];
    
    //depending on the entity type, call a different function.      
    if(entityType == kGridEntity){
        colLength = GRID_MICROBLOCK_COLUMNS;
        topRightId = [self get:kGridEntity MBIDForPoint:topRight AndArray:topRightRowCol];
        botLeftId = [self get:kGridEntity MBIDForPoint:botLeft AndArray:botLeftRowCol];
    }else if(entityType == kStreetEntity){
        colLength = STREET_MICROBLOCK_COLUMNS;
        topRightId = [self get:kStreetEntity MBIDForPoint:topRight AndArray:topRightRowCol];
        botLeftId = [self get:kStreetEntity MBIDForPoint:botLeft AndArray:botLeftRowCol];
    }else{
        colLength = SPOT_MICROBLOCK_COLUMNS;
        topRightId = [self get:kSpotEntity MBIDForPoint:topRight AndArray:topRightRowCol];
        botLeftId = [self get:kSpotEntity MBIDForPoint:botLeft AndArray:botLeftRowCol];
    }
    
    //TESTING - top row is not loading sometimes, due to how mapping function worked.  testing manual adjustment, pre-loading surrounding area for ui flow.    
    topRightId += colLength + 1;  //+1 col, and +1 row.  
    topRightRowCol[0]++; //+1 row
    topRightRowCol[1]++; //+1 col
    
    botLeftId -=1;// -1 col
    botLeftRowCol[1]--;    
    //end DEBUG
    
    for(int j= botLeftRowCol[0]; j<=topRightRowCol[0]; j++){
        long tempId = botLeftId;
        for(int i = botLeftRowCol[1]; i<=topRightRowCol[1]; i++){
            if(tempId>topRightId){
                //error, went beyond our limit.  
                return nil;
            }
            [microBlockIds addObject:[NSNumber numberWithLong:tempId]];
            tempId++;
        }
        botLeftId+=colLength;
    }
    //by generating blocks in increasing order, the array is inherently sorted.  
    return microBlockIds;
}

-(long) get:(EntityType) entityType MBIDForPoint:(CLLocationCoordinate2D*) coord AndArray:(unsigned long*) RowCol{
    double dy;
    double dx;
    long row;
    long col;
    long colLength;
    dy = coord->latitude - MICROBLOCK_REF_LAT;
    dx = coord->longitude - MICROBLOCK_REF_LON;
    if(entityType == kGridEntity){
        row = dy/GRID_MICROBLOCK_LENGTH_DEGREES;
        col = dx/GRID_MICROBLOCK_LENGTH_DEGREES;
        colLength = GRID_MICROBLOCK_COLUMNS;
        if(dx < 0 || dy < 0  || row> GRID_MICROBLOCK_ROWS || col > colLength){
            return MID_BOUNDING_ERROR;
        }
    }else if(entityType ==kStreetEntity){
        row = dy/STREET_MICROBLOCK_LENGTH_DEGREES;
        col = dx/STREET_MICROBLOCK_LENGTH_DEGREES;
        colLength = STREET_MICROBLOCK_COLUMNS;
        if(dx < 0 || dy < 0  || row> STREET_MICROBLOCK_ROWS || col > colLength){
            return MID_BOUNDING_ERROR;
        }
    }else{
        row = dy/SPOT_MICROBLOCK_LENGTH_DEGREES;
        col = dx/SPOT_MICROBLOCK_LENGTH_DEGREES;
        colLength = SPOT_MICROBLOCK_COLUMNS;
        if(dx < 0 || dy < 0  || row> SPOT_MICROBLOCK_ROWS || col > colLength){
            return MID_BOUNDING_ERROR;
        }
    }
    RowCol[0] = row;
    RowCol[1] = col;
    long microblock_id = row * colLength + col;
    return microblock_id;
    
}

#pragma mark - MBID TO BOUNDING BOX
//return minimal number of bounding boxes to represent the given microblocks.  
-(NSArray*) convertGridLevelIDs:(NSArray*) microBlockIds{
    //this list is pre-sorted, smallest to largest.  
    NSMutableArray* results = [[NSMutableArray alloc] init];
    NSMutableArray* temp = [[NSMutableArray alloc] initWithArray:microBlockIds copyItems:YES];
    NSLog(@"id list: %s\n", microBlockIds.description.UTF8String);
    //start from the lowest id number, and detect if +1 exists, and if +COLUMNS exists.  
    while(temp.count>0){
        //assume index 0 is start,
        NSNumber* startId = [microBlockIds objectAtIndex:0];
        NSNumber* nextID = [NSNumber numberWithLong: startId.longValue+1];
        int rightCount = 1, upCount = 1;
        while([temp containsObject:nextID]){
            rightCount++;
            //try to proceed to right
            nextID = [NSNumber numberWithLong:nextID.longValue+1];
        }
        //return to index 0 as start
        nextID = [NSNumber numberWithLong:startId.longValue+GRID_MICROBLOCK_COLUMNS];
        while([microBlockIds containsObject:nextID]){
            upCount++;
            //try to proceed upwards
            nextID = [NSNumber numberWithLong:nextID.longValue+GRID_MICROBLOCK_COLUMNS];
        }
        BoundingBox* bb = [[BoundingBox alloc] init];
        bb.botLeft = [self botLeftOf:kGridEntity WithID:startId];
        if(rightCount > upCount){
            //create bonding box going right
            bb.topRight = [self topRightOf:kGridEntity WithId:[NSNumber numberWithLong:startId.longValue+rightCount]];
            //add th bounding box to output
            [results addObject:bb];
            //remove indexes 0, 1, 2, ...
            NSIndexSet* toRemoveList = [[NSIndexSet alloc] initWithIndexesInRange:NSMakeRange(0, rightCount)];
            [temp removeObjectsAtIndexes:toRemoveList];
        }else{
            //create bounding box going up
            bb.topRight = [self topRightOf:kGridEntity WithId:[NSNumber numberWithLong:startId.longValue+upCount*GRID_MICROBLOCK_COLUMNS]];
            [results addObject:bb];
            for(int i=0; i<upCount; i++){
                //removes objects 0, 35, 70, ...
                [temp removeObject:[NSNumber numberWithLong:startId.longValue+i*GRID_MICROBLOCK_COLUMNS]];
            }
        }
    }
    //keep track of which side is longer, go with the larger box.  
    
    //remove the id's that were consumed, repeat until no more ID's are in the list.  
    NSLog(@"resulting boxes: %s\n", results.description.UTF8String);
    return results;
}
//given a microblock ID for grid, return the bottom left lat/lon
-(CLLocationCoordinate2D) botLeftOf:(EntityType)entityType WithID:(NSNumber*) gridId{
    int column = gridId.longValue % GRID_MICROBLOCK_COLUMNS;
    int row = gridId.longValue / GRID_MICROBLOCK_COLUMNS;
    double lat = MICROBLOCK_REF_LAT + row * GRID_MICROBLOCK_LENGTH_DEGREES;
    double lon = MICROBLOCK_REF_LON + column * GRID_MICROBLOCK_LENGTH_DEGREES;
    return CLLocationCoordinate2DMake(lat, lon);
}
//given a microblock ID for grid, return the top right lat/lon
-(CLLocationCoordinate2D) topRightOf:(EntityType)entityType  WithId:(NSNumber*) gridId{
    int column = gridId.longValue % GRID_MICROBLOCK_COLUMNS;
    int row = gridId.longValue / GRID_MICROBLOCK_COLUMNS;
    double lat = MICROBLOCK_REF_LAT + (row+1) * GRID_MICROBLOCK_LENGTH_DEGREES;
    double lon = MICROBLOCK_REF_LON + (column+1) * GRID_MICROBLOCK_LENGTH_DEGREES;
    return CLLocationCoordinate2DMake(lat, lon);
}

#pragma mark - network status methods

-(BOOL) isRecheableViaWifi{
    return [[[RKClient sharedClient] reachabilityObserver] isReachableViaWiFi];
}
-(BOOL) isRecheableVia3G{
    return [[[RKClient sharedClient] reachabilityObserver] isReachableViaWWAN];
}

//subscribe to status change, maybe use?
//[[NSNotificationCenter defaultCenter] addObserver:self 
//                                         selector:@selector(reachabilityStatusChanged:) 
//                                             name:RKReachabilityDidChangeNotification object:nil];
-(void) loadSpotData{
    
    
//    NSArray* spotData = [NSArray arrayWithObjects:
//                         @"42.365354,-71.110843,1,1410,0,1",
//                         @"42.365292,-71.110835,1,1412,0,2",
//                         @"42.365239,-71.110825,1,1414,0,3",
//                         @"42.365187,-71.110811,1,1416,0,4",
//                         @"42.365140,-71.110806,1,1418,0,5",
//                         @"42.365092,-71.110798,1,1420,0,6",
//                         @"42.365045,-71.110790,1,1422,0,7",
//                         @"42.364995,-71.110782,1,1424,0,8",
//                         @"42.364947,-71.110768,1,1426,0,9",
//                         @"42.364896,-71.110766,1,1428,0,10",
//                         @"42.364846,-71.110752,1,1430,0,11",
//                         @"42.364797,-71.110739,1,1432,0,12",
//                         
//                         @"42.365348,-71.110924,1,1411,0,13",
//                         @"42.365300,-71.110916,1,1413,0,14",
//                         @"42.365251,-71.110905,1,1415,0,15",
//                         @"42.365203,-71.110900,1,1417,0,16",
//                         @"42.365154,-71.110892,1,1419,0,17",
//                         @"42.365104,-71.110876,0,1421,0,18",
//                         @"42.365049,-71.110868,1,1423,0,19",
//                         @"42.364993,-71.110860,1,1425,0,20",
//                         @"42.364943,-71.110849,1,1427,0,21",
//                         @"42.364894,-71.110846,1,1429,0,22",
//                         @"42.364846,-71.110835,0,1431,0,23",
//                         @"42.364799,-71.110830,1,1433,0,24",
//                         nil];
    NSArray* spotData = [NSArray arrayWithObjects:
                         //lat      ,lon      ,status, spotNum
                         @"42.357775,-71.094482,1,1400",
                         @"42.357805,-71.094388,1,1401",
                         @"42.357833,-71.0943,1,1402",
                         @"42.357862,-71.094219,1,1403",
                         @"42.357888,-71.094139,1,1404",
                         @"42.357908,-71.094058,1,1405",
                         nil];
    int spotId = 0;
    for(NSString* spotString in spotData){
        NSArray* innerArray = [spotString componentsSeparatedByString:@","];
        //create the grid object
        Spot* spot = (Spot*)[NSEntityDescription insertNewObjectForEntityForName:@"Spot" inManagedObjectContext:dataLayer.managedObjectContext];
        NSNumberFormatter * f = [[NSNumberFormatter alloc] init];
        [f setNumberStyle:NSNumberFormatterNoStyle];
        [spot setSpotId:[NSNumber numberWithInt:spotId]];
        spotId++;
        NSNumber* lon = [f numberFromString:[innerArray objectAtIndex:1]];
        [spot setLon:lon];
        NSNumber* lat = [f numberFromString:[innerArray objectAtIndex:0]];
        [spot setLat:lat];
        [spot setStatus:[f numberFromString:[innerArray objectAtIndex:2]]];
        [spot setSpotNumber:[f numberFromString:[innerArray objectAtIndex:3]]];
        CLLocationCoordinate2D coord = CLLocationCoordinate2DMake(lat.floatValue, lon.floatValue);
        unsigned long arr[2];
        NSNumber* mbid = [NSNumber numberWithLong:[self get:kSpotEntity MBIDForPoint:&coord AndArray:arr]];
        [spot setMicroblock:mbid];
        NSLog(@"%d remaining..\n", 24 - spotId);
    }
    
    NSError* error;
    if(![dataLayer.managedObjectContext save:&error]){
        //oh noes, cant' store this grid.  wtf to do.
        NSLog(@"error saving!!!\n");
    }
}

-(void) loadStreetData{
 // STORE THE street's information into core data.     
}

- (BOOL) validateEmail: (NSString *) candidate {
    NSString *emailRegex = @"[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}";
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", emailRegex];
    
    return [emailTest evaluateWithObject:candidate];
}
-(User*) loginEmail:(NSString*) email AndPassword:(NSString*) pass{
    //send info to server.  
    
    //initial check of email.
    if(![self validateEmail:email]){ //) || pass.length < 8){
        //password was too short, o|| pass.length < 8r email did not validate.
        return nil;
    }
    
    NSArray* keys = [NSArray arrayWithObjects:@"email", @"password", nil];
    NSArray* value = [NSArray arrayWithObjects:email, pass, nil];
    NSDictionary* info = [NSDictionary dictionaryWithObjects:value forKeys:keys];
    NSError *error;
    NSData* jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    RKRequest* request = [[RKClient sharedClient] requestWithResourcePath:@"/parkservice.auth"];
    [request setMethod:RKRequestMethodPOST];
    [request setHTTPBody:jsonData];
    [request setAdditionalHTTPHeaders:[NSDictionary dictionaryWithObject:@"application/json" forKey:@"content-type"]];
    RKResponse* response = [request sendSynchronously];
    NSLog(@"\nREQUEST >>> %@", [info description]);
    NSDictionary* results = [Parser parseUserObjectString:[response bodyAsString]];  
    NSNumber* uid = [results objectForKey:@"uid"];
    if(uid.longValue > 0 || [email isEqualToString:@"abc"]){
        
//        //UNUSED 
//        NSString* ccStub = [results objectForKey:@"creditCardStub"];
//        NSNumber* parkState = [results objectForKey:@"parkState"];
//        NSString* city = [results objectForKey:@"city"];
//        NSString* addr = [results objectForKey:@"addr"];
//        NSString* license = [results objectForKey:@"license"];
//        NSString* name = [results objectForKey:@"name"];
//        NSString* email = [results objectForKey:@"email"];
//        NSNumber* balance = [results objectForKey:@"balance"];
//        NSNumber* payment = [results objectForKey:@"payment"];
//        //unused 

        User* user = (User*)[NSEntityDescription insertNewObjectForEntityForName:@"User" inManagedObjectContext:dataLayer.managedObjectContext];
        
        [user setAddress:@"77 Massachusetts Avenue"];
        [user setName:@"Eric Baczuk"];
        [user setEmail:@"eric.baczuk@gmail.com"];
        [user setLicense:@"EBY776J"];
        [user setCity:@"Cambridge"];
        [user setPayment:[NSNumber numberWithInt:0]];
        [user setUid:[NSNumber numberWithLong:0]];
        [user setBalance:[NSNumber numberWithInt:100]];
        NSError* error;
        if(![dataLayer.managedObjectContext save:&error]){
            //logged in, but something wrong wtih core data. cannot store user.  
            [dataLayer.managedObjectContext deleteObject:user];
            return nil;
        }else{
            return user;
        }
    }else{
        //bad login
        return nil;
    }
}
@end
