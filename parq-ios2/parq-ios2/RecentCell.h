//
//  RecentCell.h
//  Parq
//
//  Created by Michael Xia on 6/20/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RecentCell : UITableViewCell

@property (nonatomic, strong) IBOutlet UILabel* recentSearchName;
@property (nonatomic, strong) IBOutlet UILabel* recentSearchDescription;
@end
