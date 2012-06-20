//
//  PQBookmarksViewController.h
//  Parq
//
//  Created by Michael Xia on 6/19/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface PQBookmarksViewController : UIViewController <UITableViewDelegate, UITableViewDataSource>{

    
}

@property (weak, nonatomic) IBOutlet UITableView *table;
@property (weak, nonatomic) IBOutlet UISegmentedControl *bookmarkSelectionBar;
@property (strong, nonatomic) NSArray* bookmarks;
@property (strong, nonatomic) NSArray* recentSearches;
@property (strong, nonatomic) NSArray* contacts;
//@property (atomic) int tableDisplayType;

@end
