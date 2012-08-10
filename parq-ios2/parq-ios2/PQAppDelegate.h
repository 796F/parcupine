//
//  PQAppDelegate.h
//  parq-ios2
//
//  Created by Mark Yen on 3/16/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <RestKit/RestKit.h>
#import "DataLayer.h"
#import "NetworkLayer.h"
#import "SpotInfo.h"

@interface PQAppDelegate : UIResponder <UIApplicationDelegate>{
    NSManagedObjectModel *managedObjectModel;
    NSManagedObjectContext *managedObjectContext;
    NSPersistentStoreCoordinator *persistentStoreCoordinator;
    DataLayer * dataLayer;
    NetworkLayer* networkLayer;
    CLLocationManager *locationManager;
}

typedef enum{
    LookingAtMap,
    GettingDirectionOutside,
    GettingDirectionInside,
    DownwardPark,
    UpwardPark,
    TimerTickingClosedApp
} UserAction;

@property (strong, nonatomic) UIWindow *window;
@property (readonly, strong, nonatomic) NSManagedObjectContext *managedObjectContext;
@property (readonly, strong, nonatomic) NSManagedObjectModel *managedObjectModel;
@property (readonly, strong, nonatomic) NSPersistentStoreCoordinator *persistentStoreCoordinator;
@property (strong, nonatomic) DataLayer* dataLayer;
@property (strong, nonatomic) NetworkLayer* networkLayer;
@property (strong, nonatomic) CLLocationManager* locationManager;
@property (atomic) UserAction userAction;

- (void)saveContext;
- (NSURL *)applicationDocumentsDirectory;
@end
