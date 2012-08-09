//
//  PQAppDelegate.h
//  parq-enforce
//
//  Created by Michael Xia on 8/3/12.
//  Copyright (c) 2012 Michael Xia. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface PQAppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (readonly, strong, nonatomic) NSManagedObjectContext *managedObjectContext;
@property (readonly, strong, nonatomic) NSManagedObjectModel *managedObjectModel;
@property (readonly, strong, nonatomic) NSPersistentStoreCoordinator *persistentStoreCoordinator;

- (void)saveContext;
- (NSURL *)applicationDocumentsDirectory;

@end
