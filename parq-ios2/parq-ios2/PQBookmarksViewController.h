//
//  PQBookmarksViewController.h
//  Parq
//
//  Created by Michael Xia on 6/19/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface PQBookmarksViewController : UIViewController <UITableViewDelegate, UITableViewDataSource,UIActionSheetDelegate>{

    
}

@property (weak, nonatomic) IBOutlet UITableView *table;
@property (weak, nonatomic) IBOutlet UISegmentedControl *bookmarkSelectionBar;

@property (weak, nonatomic) IBOutlet UIBarButtonItem* leftButton;
@property (weak, nonatomic) IBOutlet UIBarButtonItem* midButton;
@property (weak, nonatomic) IBOutlet UIBarButtonItem* rightButton;

@property (strong, nonatomic) NSArray* bookmarks;
@property (strong, nonatomic) NSArray* recentSearches;
@property (strong, nonatomic) NSArray* contacts;
//@property (atomic) int tableDisplayType;
@property (atomic) bool userIsEditing;

@end
