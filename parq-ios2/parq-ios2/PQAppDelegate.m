//
//  PQAppDelegate.m
//  parq-ios2
//
//  Created by Mark Yen on 3/16/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "PQAppDelegate.h"
#import "DataLayer.h"
#import "NetworkLayer.h"

@implementation PQAppDelegate

@synthesize window = _window;
@synthesize managedObjectContext = __managedObjectContext;
@synthesize managedObjectModel = __managedObjectModel;
@synthesize persistentStoreCoordinator = __persistentStoreCoordinator;
@synthesize undoManager;
@synthesize dataLayer;    
@synthesize networkLayer;
@synthesize userAction;
@synthesize locationManager;
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    
    //check if after study date.
    NSDate* studyEnd = [NSDate dateWithTimeIntervalSince1970:1347288851];
    if([[NSDate date] earlierDate:studyEnd] == studyEnd){
        [dataLayer setLoggedIn:NO];
    }else{
        NSLog(@"%f", [studyEnd timeIntervalSinceDate:[NSDate date]]);
    }

    NSLog(@"app did finish launching\n");
    
    NSManagedObjectContext* context  = [self managedObjectContext];
    if(!context){
        //this is coming out as nil.
        //context = [[NSManagedObjectContext alloc] init];
        NSLog(@"null\n");
    }
    //prepare the RK client for server calls upon load
    NSURL* url = [NSURL URLWithString:@"http://75.101.132.219:80/"];
    [RKClient clientWithBaseURL:url];

    //set up data layer
    dataLayer = [[DataLayer alloc] init];
    dataLayer.managedObjectContext = __managedObjectContext;

    networkLayer = [[NetworkLayer alloc] init];
    networkLayer.dataLayer = dataLayer;
    locationManager=[[CLLocationManager alloc] init];
    locationManager.desiredAccuracy=kCLLocationAccuracyNearestTenMeters;
    [locationManager startUpdatingLocation];

    if([dataLayer isFirstLaunch]){
        //first launch!  do something special.
        [networkLayer decideUIType];
        char *saves = "LOG:\n";
        NSData *data = [[NSData alloc] initWithBytes:saves length:3];
        NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
        NSString *documentsDirectory = [paths objectAtIndex:0];
        NSString *appFile = [documentsDirectory stringByAppendingPathComponent:@"log.txt"];
        [data writeToFile:appFile atomically:YES];
    }    
    [dataLayer logString:[NSString stringWithFormat:@"%s", __PRETTY_FUNCTION__]];
    return YES;
}
							
- (void)applicationWillResignActive:(UIApplication *)application
{
    NSLog(@"app about to resign active state\n");
    
    /*
     Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
     Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
     */
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    NSLog(@"app did enter backgrond\n");
    [dataLayer logString:[NSString stringWithFormat:@"%s", __PRETTY_FUNCTION__]];
    [locationManager stopUpdatingLocation];

    /*
     Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
     If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
     */
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    NSLog(@"app about to enter foreground\n");
    //clear alert badges
    [[UIApplication sharedApplication] setApplicationIconBadgeNumber:0];
    [locationManager startUpdatingLocation];
    [dataLayer logString:[NSString stringWithFormat:@"%s", __PRETTY_FUNCTION__]];
    /*
     Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
     */
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    NSLog(@"app became active\n");
    /*
     Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
     */
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    NSLog(@"app about to termiante\n");
    /*
     Called when the application is about to terminate.
     Save data if appropriate.
     See also applicationDidEnterBackground:.
     */
}
- (void)saveContext
{
    NSError *error = nil;
    //NSManagedObjectContext *managedObjectContext = self.managedObjectContext;
    if (managedObjectContext != nil) {
        if ([managedObjectContext hasChanges] && ![managedObjectContext save:&error]) {
            // Replace this implementation with code to handle the error appropriately.
            // abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development. 
            NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
            abort();
        } 
    }
}

#pragma mark - Core Data stack

// Returns the managed object context for the application.
// If the context doesn't already exist, it is created and bound to the persistent store coordinator for the application.
- (NSManagedObjectContext *)managedObjectContext
{
    if (__managedObjectContext != nil) {
        return __managedObjectContext;
    }
    
    NSPersistentStoreCoordinator *coordinator = [self persistentStoreCoordinator];
    if (coordinator != nil) {
        __managedObjectContext = [[NSManagedObjectContext alloc] init];
        [__managedObjectContext setPersistentStoreCoordinator:coordinator];
    }
    if(__managedObjectContext.persistentStoreCoordinator.managedObjectModel==nil){
        NSLog(@"moc function nil\n");
    }
    return __managedObjectContext;
}

// Returns the managed object model for the application.
// If the model doesn't already exist, it is created from the application's model.
- (NSManagedObjectModel *)managedObjectModel
{
    if (__managedObjectModel != nil) {
        return __managedObjectModel;
    }
    //the following works only in simulator, but not on actual devices.  
//    NSURL *modelURL = [[NSBundle mainBundle] URLForResource:@"Parq" withExtension:@"momd"];
//    __managedObjectModel = [[NSManagedObjectModel alloc] initWithContentsOfURL:modelURL];
    
    __managedObjectModel = [NSManagedObjectModel mergedModelFromBundles:nil] ;    
    
    if(__managedObjectModel==nil){
        
        NSLog(@"mom function nil\n");
    }
    
    
    return __managedObjectModel;
}

// Returns the persistent store coordinator for the application.
// If the coordinator doesn't already exist, it is created and the application's store added to it.
- (NSPersistentStoreCoordinator *)persistentStoreCoordinator
{
    if (__persistentStoreCoordinator != nil) {
        return __persistentStoreCoordinator;
    }
    
    NSURL *storeURL = [[self applicationDocumentsDirectory] URLByAppendingPathComponent:@"Parq.sqlite"];
    
    NSError *error = nil;
    __persistentStoreCoordinator = [[NSPersistentStoreCoordinator alloc] initWithManagedObjectModel:[self managedObjectModel]];
    if (![__persistentStoreCoordinator addPersistentStoreWithType:NSSQLiteStoreType configuration:nil URL:storeURL options:nil error:&error]) {
        /*
         Replace this implementation with code to handle the error appropriately.
         
         abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development. 
         
         Typical reasons for an error here include:
         * The persistent store is not accessible;
         * The schema for the persistent store is incompatible with current managed object model.
         Check the error message to determine what the actual problem was.
         
         
         If the persistent store is not accessible, there is typically something wrong with the file path. Often, a file URL is pointing into the application's resources directory instead of a writeable directory.
         
         If you encounter schema incompatibility errors during development, you can reduce their frequency by:
         * Simply deleting the existing store:
         [[NSFileManager defaultManager] removeItemAtURL:storeURL error:nil]
         
         * Performing automatic lightweight migration by passing the following dictionary as the options parameter: 
         [NSDictionary dictionaryWithObjectsAndKeys:[NSNumber numberWithBool:YES], NSMigratePersistentStoresAutomaticallyOption, [NSNumber numberWithBool:YES], NSInferMappingModelAutomaticallyOption, nil];
         
         Lightweight migration will only work for a limited set of schema changes; consult "Core Data Model Versioning and Data Migration Programming Guide" for details.
         
         */
        NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
        abort();
    }    
    if(__persistentStoreCoordinator.managedObjectModel==nil){
        NSLog(@"persistence function nil\n");
    }
    return __persistentStoreCoordinator;
}

#pragma mark - Application's Documents directory

// Returns the URL to the application's Documents directory.
- (NSURL *)applicationDocumentsDirectory
{
    return [[[NSFileManager defaultManager] URLsForDirectory:NSDocumentDirectory inDomains:NSUserDomainMask] lastObject];
}
@end
