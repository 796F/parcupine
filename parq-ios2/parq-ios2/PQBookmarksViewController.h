//
//  PQBookmarksViewController.h
//  Parq
//
//  Created by Michael Xia on 6/19/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PQMapViewController.h"

@interface PQBookmarksViewController : UIViewController <UITableViewDelegate, UITableViewDataSource,UIActionSheetDelegate>{

    
}

@property (weak, nonatomic) IBOutlet UITableView *table;
@property (weak, nonatomic) IBOutlet UISegmentedControl *bookmarkSelectionBar;

@property (weak, nonatomic) IBOutlet UIBarButtonItem* leftButton;
@property (weak, nonatomic) IBOutlet UIBarButtonItem* midButton;
@property (weak, nonatomic) IBOutlet UIBarButtonItem* rightButton;

@property (strong, nonatomic) NSMutableArray* bookmarks;
@property (strong, nonatomic) NSMutableArray* recentSearches;
@property (strong, nonatomic) NSMutableArray* contacts;
//@property (atomic) int tableDisplayType;
@property (atomic) bool userIsEditing;
@property (nonatomic) PQMapViewController* parent;

@end
