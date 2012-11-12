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

-(BOOL) needUpdate{
    RKRequest* request = [[RKClient sharedClient] requestWithResourcePath:@"/update.html"];
    [request setMethod:RKRequestMethodGET];
    RKResponse* response = [request sendSynchronously];
    NSString* result = [response bodyAsString];
    NSNumber* appver = [dataLayer getAppVersion];
    NSNumberFormatter* f = [[NSNumberFormatter alloc] init];
    switch (response.statusCode) {
        case 200:{
                NSNumber* newver = [f numberFromString:[result substringToIndex:1]];
                if(newver.longValue > appver.longValue){
                    //new version exists
                    return YES;
                }else{
                    return NO;
                }
            }
            break;
        case 304: //file not modified.
            return NO;
            break;
        default:
            return NO;
    }
}

-(BOOL)submitAvailablilityInformation:(NSArray*)value{
    NSArray* keys = [NSArray arrayWithObjects:@"userId",@"spaceIds", @"score1",@"score2", @"score3", @"score4", @"score5", @"score6", nil];
    NSArray* spaceIds = [NSArray arrayWithObjects:@"1",@"2",@"3",@"4",@"5",@"6", nil];
    
    NSArray* top = [NSArray arrayWithObjects:[DataLayer fetchUser].uid, spaceIds, nil];
    NSDictionary* info = [NSDictionary dictionaryWithObjects:[top arrayByAddingObjectsFromArray:value] forKeys:keys];
    NSError *error;
    NSData* jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    NSLog(@"%@", [info description]);
    RKRequest* request = [[RKClient sharedClient] requestWithResourcePath:@"/parkservice.park/AddUserReportingRequest"];

//    RKRequest* request = [[RKRequest alloc] initWithURL:[NSURL URLWithString:@"http://localhost:8080/parkservice.park/AddUserReportingRequest"]];
    [request setMethod:RKRequestMethodPOST];
    [request setHTTPBody:jsonData];
    [request setAdditionalHTTPHeaders:[NSDictionary dictionaryWithObject:@"application/json" forKey:@"content-type"]];
    NSDictionary* result = [[[request sendSynchronously] bodyAsString] objectFromJSONString];
    return [[result objectForKey:@"updateSuccessful"] boolValue];
}

-(void) sendLogs{
    //sends the log file to the server then erases its contents.
    NSArray *documentPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDir = [documentPaths objectAtIndex:0];
    NSString *logPath = [documentsDir stringByAppendingPathComponent:@"log.txt"];
    NSString *logContent = [[NSString alloc] initWithContentsOfFile:logPath encoding:NSUTF8StringEncoding error:NULL];
    
    NSArray* keys = [NSArray arrayWithObjects:@"userId", @"log", nil];
    NSArray* value = [NSArray arrayWithObjects:[DataLayer fetchUser].uid.stringValue, logContent, nil];
    NSDictionary* info = [NSDictionary dictionaryWithObjects:value forKeys:keys];
    NSError *error;
    NSData* jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    AbstractRequestObject* abs = [[AbstractRequestObject alloc] init];
    [abs setBody:jsonData];
    [abs setContentType:@"application/json"];
    [[RKClient sharedClient] post:@"/parkservice.park/AddUserActionLogRequest" params:abs delegate:self];
    char *saves = "LOG:\n";
    NSData *data = [[NSData alloc] initWithBytes:saves length:3];
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *appFile = [documentsDirectory stringByAppendingPathComponent:@"log.txt"];
    [data writeToFile:appFile atomically:YES];
}

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
    [spot setMaxTime:[NSNumber numberWithInt:120]];
    [spot setMinTime:[NSNumber numberWithInt:15]];
    [spot setMinuteInterval:[NSNumber numberWithInt:15]];
    [spot setRateCents:[NSNumber numberWithInt:0]];
    [spot setStreetName:@"Amherst St"];
    [spot setFullAddress:@"109-117 Amherst St, Cambridge, MA 02139"];
    [spot setSpotId:spotId];
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
    DLog(@"");
    //send request to server to get updated colors
    RKResponse* response;
    if(entityType==kGridEntity){
        //request grids
    }else if(entityType == kStreetEntity){
        //request street types
    }else if(entityType == kSpotEntity){
        //request spot types
        NSArray* keys = [NSArray arrayWithObjects:@"searchArea", nil];
        NSMutableDictionary* SWCorner = [[NSMutableDictionary alloc] initWithCapacity:2];
        [SWCorner setObject:@"42.353773" forKey:@"latitude"];
        [SWCorner setObject:@"-71.100329" forKey:@"longitude"];
        NSMutableDictionary* NECorner = [[NSMutableDictionary alloc] initWithCapacity:2];
        [NECorner setObject:@"42.361415" forKey:@"latitude"];
        [NECorner setObject:@"-71.089321" forKey:@"longitude"];
        NSDictionary* pilotBox = [[NSDictionary alloc] initWithObjectsAndKeys:SWCorner,@"southWestCorner", NECorner,@"northEastCorner", nil];
        NSArray* searchBoxes = [NSArray arrayWithObjects:pilotBox, nil];
        NSArray* wrapper = [NSArray arrayWithObject:searchBoxes];

        NSDictionary* info = [NSDictionary dictionaryWithObjects:wrapper forKeys:keys];
        NSError *error;
        NSData* jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
        RKRequest* request = [[RKClient sharedClient] requestWithResourcePath:@"/parkservice.park/GetUpdatedSpotLevelInfoRequest"];
        [request setMethod:RKRequestMethodPOST];
        [request setHTTPBody:jsonData];
        [request setAdditionalHTTPHeaders:[NSDictionary dictionaryWithObject:@"application/json" forKey:@"content-type"]];
        response = [request sendSynchronously];
        NSLog(@"\nREQUEST >>> %@", [info description]);
    }else{
        //error
        return;
    }
    NSLog(@"%@", response.bodyAsString);
    NSDictionary* overlayUpdates =[Parser parseUpdateSpotsResponse:[response bodyAsString]];

    [mapController updateOverlays:overlayUpdates OfType:entityType];
    //package them properly for the map controller's function.

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
    if([[[RKClient sharedClient] reachabilityObserver] isReachabilityDetermined]){
        return [[[RKClient sharedClient] reachabilityObserver] isReachableViaWiFi];
    }else{
        return NO;
    }

}
-(BOOL) isRecheableVia3G{
    if([[[RKClient sharedClient] reachabilityObserver] isReachabilityDetermined]){
        return [[[RKClient sharedClient] reachabilityObserver] isReachableViaWWAN];
    }else{
        return NO;
    }
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
                         @"42.357775,-71.094482,1,101",
                         @"42.357805,-71.094388,1,102",
                         @"42.357833,-71.0943,1,103",
                         @"42.357862,-71.094219,1,104",
                         @"42.357888,-71.094139,1,105",
                         @"42.357908,-71.094058,1,106",
                         //old spots non pilot.  
                         @"42.365354,-71.110843,1,1410,0,1",
                         @"42.365292,-71.110835,1,1412,0,2",
                         @"42.365239,-71.110825,1,1414,0,3",
                         @"42.365187,-71.110811,0,1416,0,4",
                         @"42.365140,-71.110806,0,1418,0,5",
                         @"42.365092,-71.110798,1,1420,0,6",
                         @"42.365045,-71.110790,1,1422,0,7",
                         @"42.364995,-71.110782,0,1424,0,8",
                         @"42.364947,-71.110768,0,1426,0,9",
                         @"42.364896,-71.110766,0,1428,0,10",
                         @"42.364846,-71.110752,1,1430,0,11",
                         @"42.364797,-71.110739,1,1432,0,12",

                         @"42.365348,-71.110924,1,1411,0,13",
                         @"42.365300,-71.110916,0,1413,0,14",
                         @"42.365251,-71.110905,1,1415,0,15",
                         @"42.365203,-71.110900,1,1417,0,16",
                         @"42.365154,-71.110892,1,1419,0,17",
                         @"42.365104,-71.110876,0,1421,0,18",
                         @"42.365049,-71.110868,1,1423,0,19",
                         @"42.364993,-71.110860,1,1425,0,20",
                         @"42.364943,-71.110849,1,1427,0,21",
                         @"42.364894,-71.110846,1,1429,0,22",
                         @"42.364846,-71.110835,0,1431,0,23",
                         @"42.364799,-71.110830,1,1433,0,24",

                         nil];
    int spotId = 1;
    for(NSString* spotString in spotData){
        NSArray* innerArray = [spotString componentsSeparatedByString:@","];
        //create the grid object
        Spot* spot = (Spot*)[NSEntityDescription insertNewObjectForEntityForName:@"Spot" inManagedObjectContext:[DataLayer managedObjectContext]];
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
        NSLog(@"%d remaining..\n", 31 - spotId);
    }
    
    NSError* error;
    if(![[DataLayer managedObjectContext] save:&error]){
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
    return [dataLayer saveUserWithEmail:@"m@m.com" Pass:@"a" License:@"license" UID:@(2) Balance:0];
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
    RKRequest* request = [[RKClient sharedClient] requestWithResourcePath:@"/parkservice.park/AuthRequest"];
    [request setMethod:RKRequestMethodPOST];
    [request setHTTPBody:jsonData];
    [request setAdditionalHTTPHeaders:[NSDictionary dictionaryWithObject:@"application/json" forKey:@"content-type"]];
    RKResponse* response = [request sendSynchronously];
    NSLog(@"\nREQUEST >>> %@", [info description]);
    NSDictionary* results = [Parser parseUserObjectString:[response bodyAsString]];
    if(results!=nil || [email isEqualToString:@"abc"]){

//        //UNUSED 
//        NSString* ccStub = [results objectForKey:@"creditCardStub"];
//        NSNumber* parkState = [results objectForKey:@"parkState"];
//        NSString* city = [results objectForKey:@"city"];
//        NSString* addr = [results objectForKey:@"addr"];
        NSString* license = [results objectForKey:@"license"];
//        NSString* name = [results objectForKey:@"name"];
        NSString* email = [results objectForKey:@"email"];
//        NSNumber* payment = [results objectForKey:@"payment"];
//        //unused
        NSNumberFormatter* f = [[NSNumberFormatter alloc] init];
        NSNumber* balance = [f numberFromString:[results objectForKey:@"balance"]];
        NSNumber* uid = [f numberFromString:[results objectForKey:@"uid"]];
        return [dataLayer saveUserWithEmail:email Pass:pass License:license UID:uid Balance:balance];
        
        }else{
        //bad login
        return nil;
    }
}

-(User*) registerEmail:(NSString*) email AndPassword:(NSString*) pass AndPlate:(NSString*) plate{
    //pass is equal to email is for the confirm email in registering.  
    if(![self validateEmail:email] || ![pass isEqualToString:@"a"]){ //) || pass.length < 8){
        //password was too short, o|| pass.length < 8r email did not validate.
        return nil;
    }
    NSArray* keys = [NSArray arrayWithObjects:@"email", @"password", @"creditCard", nil];
    NSArray* value = [NSArray arrayWithObjects:email, pass, plate, nil];
    NSDictionary* info = [NSDictionary dictionaryWithObjects:value forKeys:keys];
    NSError *error;
    NSData* jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    RKRequest* request = [[RKClient sharedClient] requestWithResourcePath:@"/parkservice.user/pilotregister"];
    [request setMethod:RKRequestMethodPOST];
    [request setHTTPBody:jsonData];
    [request setAdditionalHTTPHeaders:[NSDictionary dictionaryWithObject:@"application/json" forKey:@"content-type"]];
    RKResponse* response = [request sendSynchronously];
    BOOL regResults = [Parser parseRegisterResponseString:[response bodyAsString]];
    if(regResults){
        NSArray* keys = [NSArray arrayWithObjects:@"email", @"password", nil];
        NSArray* value = [NSArray arrayWithObjects:email, pass, nil];
        NSDictionary* info = [NSDictionary dictionaryWithObjects:value forKeys:keys];
        NSError *error;
        NSData* jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
        RKRequest* request = [[RKClient sharedClient] requestWithResourcePath:@"/parkservice.park/AuthRequest"];
        [request setMethod:RKRequestMethodPOST];
        [request setHTTPBody:jsonData];
        [request setAdditionalHTTPHeaders:[NSDictionary dictionaryWithObject:@"application/json" forKey:@"content-type"]];
        RKResponse* response = [request sendSynchronously];
        NSDictionary* results = [Parser parseUserObjectString:[response bodyAsString]];
        if(results!=nil){
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
            
            User* user = (User*)[NSEntityDescription insertNewObjectForEntityForName:@"User" inManagedObjectContext:[DataLayer managedObjectContext]];
            
            [user setAddress:@"Room 9-206"];
            [user setName:@"Peter Parker"];
            [user setEmail:email];  //this should be returned by server.
            [user setPassword:pass];
            [user setLicense:[results objectForKey:@"license"]];
            [user setCity:@"Cambridge"];
            [user setPayment:[NSNumber numberWithInt:0]];
            NSNumberFormatter* f = [[NSNumberFormatter alloc] init];
            [user setUid:[f numberFromString:[results objectForKey:@"uid"]]];
            [user setBalance:[f numberFromString:[results objectForKey:@"balance"]]];
            NSError* error;
            if(![[DataLayer managedObjectContext] save:&error]){
                //logged in, but something wrong wtih core data. cannot store user.
                [[DataLayer managedObjectContext] deleteObject:user];
                return nil;
            }else{
                return user;
            }
        }else{
            //bad login
            return nil;
        }

    }else{
        return nil;
    }
    
}

+ (RKRequest *)requestWithResourcePath:(NSString *)resourcePath delegate:(id<PQNetworkLayerDelegate>)delegate httpBody:(NSData *)data {
    RKRequest* request = [[RKClient sharedClient] requestWithResourcePath:resourcePath];
    request.delegate = [[self class] requestDelegate];
    request.method = RKRequestMethodPOST;
    request.HTTPBody = data;
    request.additionalHTTPHeaders = @{ @"content-type" : @"application/json"};
    request.userData = delegate;
    return request;
}

+ (void)fetchUserPointsBalanceWithUid:(unsigned long long)uid andDelegate:(id<PQNetworkLayerDelegate>)delegate {
    NSDictionary *info = [NSDictionary dictionaryWithObject:[NSNumber numberWithUnsignedLongLong:uid] forKey:@"userId"];
    NSError *error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    [[self requestWithResourcePath:@"/parkservice.park/GetUserScoreRequest" delegate:delegate httpBody:jsonData] send];
}

+ (void)parkPaygWithSpotId:(unsigned long long)spotId delegate:(id<PQNetworkLayerDelegate>)delegate {
    User *user = [DataLayer fetchUser];

    NSDictionary *info =
        @{  @"uid" : user.uid,
            @"userInfo" :
                @{ @"email"    : user.email,
                   @"password" : user.password
                },
            @"durationMinutes" : @(120),
            @"spotId"          : @(spotId)
        };

    NSError *error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    [[self requestWithResourcePath:@"/parkservice.park/pilotpark" delegate:delegate httpBody:jsonData] send];
}

+ (void)parkPrepaidWithDuration:(NSTimeInterval)durationSeconds spotId:(unsigned long long)spotId delegate:(id<PQNetworkLayerDelegate>)delegate {
    User *user = [DataLayer fetchUser];

    NSDictionary *info =
        @{  @"uid" : user.uid,
            @"userInfo" :
                @{  @"email"    : user.email,
                    @"password" : user.password
                },
            @"durationMinutes" : @(durationSeconds/60),
            @"spotId"          : @(spotId)
        };

    NSError *error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    [[self requestWithResourcePath:@"/parkservice.park/pilotpark" delegate:delegate httpBody:jsonData] send];
}

+ (void)unparkWithDelegate:(id<PQNetworkLayerDelegate>)delegate {
    User *user = [DataLayer fetchUser];

    NSDictionary *info = [NSDictionary dictionaryWithObjects:@[user.uid, @{@"email" : user.email, @"password" : user.password}, [DataLayer parkingReference]] forKeys:@[@"uid", @"userInfo", @"parkingReferenceNumber"]];

    NSError *error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    [[self requestWithResourcePath:@"/parkservice.park/pilotunpark" delegate:delegate httpBody:jsonData] send];
}

+ (void)extendWithDuration:(NSTimeInterval)durationSeconds andDelegate:(id<PQNetworkLayerDelegate>)delegate {
    User *user = [DataLayer fetchUser];

    NSDictionary *info = [NSDictionary dictionaryWithObjects:@[user.uid, @{@"email" : user.email, @"password" : user.password}, [DataLayer parkingReference], @(durationSeconds/60)] forKeys:@[@"uid", @"userInfo", @"parkingReferenceNumber", @"durationMinutes"]];

    NSError *error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    [[self requestWithResourcePath:@"/parkservice.park/pilotrefill" delegate:delegate httpBody:jsonData] send];
}

// Assuming that availability will always contain exactly six spots to report.
// If this assumption no longer holds then more checking will have to be done.
+ (void)reportAvailability:(NSDictionary *)availability delegate:(id<PQNetworkLayerDelegate>)delegate {
    NSArray *spaces = availability.allKeys;
    NSDictionary *info =
        @{  @"userId"   : [DataLayer fetchUser].uid,
            @"spaceIds" : spaces,
            @"score1"   : availability[spaces[0]],
            @"score2"   : availability[spaces[1]],
            @"score3"   : availability[spaces[2]],
            @"score4"   : availability[spaces[3]],
            @"score5"   : availability[spaces[4]],
            @"score6"   : availability[spaces[5]]
        };

    NSError *error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    [[self requestWithResourcePath:@"/parkservice.park/AddUserReportingRequest" delegate:delegate httpBody:jsonData] send];
}

#pragma mark - RKRequestDelegate
+ (id<RKRequestDelegate>)requestDelegate {
    static id<RKRequestDelegate> requestDelegate;
    if (requestDelegate == nil)
        requestDelegate = ((PQAppDelegate *)[UIApplication sharedApplication].delegate).networkLayer;
    return requestDelegate;
}

- (void)request:(RKRequest *)request didLoadResponse:(RKResponse *)response {
    NSString *endpoint = response.URL.lastPathComponent;
    if ([endpoint isEqualToString:@"pilotpark"]) {
        id endTime = nil, parkingReference = nil;
        BOOL success = response.isSuccessful;
        if (success) {
            NSDictionary *result = [[response bodyAsString] objectFromJSONString];
            endTime = result[@"endTime"];
            parkingReference = result[@"parkingReferenceNumber"];
            success = [result[@"resp"] isEqualToString:@"OK"]
                && endTime != nil && parkingReference != nil;
        }
        [request.userData afterParkingOnBackend:success endTime:[NSDate dateWithTimeIntervalSince1970:[endTime doubleValue]/1000] parkingReference:parkingReference];
    } else if ([endpoint isEqualToString:@"pilotrefill"]) {
        id endTime = nil, parkingReference = nil;
        BOOL success = response.isSuccessful;
        if (success) {
            NSDictionary *result = [[response bodyAsString] objectFromJSONString];
            endTime = result[@"endTime"];
            parkingReference = result[@"parkingReferenceNumber"];
            success = [result[@"resp"] isEqualToString:@"OK"]
                && endTime != nil && parkingReference != nil;
        }
        [request.userData afterExtendingOnBackend:success endTime:[NSDate dateWithTimeIntervalSince1970:[endTime doubleValue]/1000] parkingReference:parkingReference];
    } else if ([endpoint isEqualToString:@"pilotunpark"]) {
        NSDictionary *result = [[response bodyAsString] objectFromJSONString];
        [request.userData afterUnparkingOnBackend:(response.isSuccessful && [result[@"resp"] isEqualToString:@"OK"])];
    } else if ([endpoint isEqualToString:@"AddUserReportingRequest"]) {
        NSDictionary *result = [[response bodyAsString] objectFromJSONString];
        [request.userData afterReportingOnBackend:[result[@"statusCode"] integerValue]];
    } else if ([endpoint isEqualToString:@"GetUserScoreRequest"]) {
        NSDictionary *result = [[response bodyAsString] objectFromJSONString];
        [request.userData afterFetchingBalanceOnBackend:[result[@"score1"] integerValue]];
    }
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
@end
