//
//  BookmarkCell.h
//  Parq
//
//  Created by Michael Xia on 6/20/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Mapkit/Mapkit.h>
#import "PQBookmarksViewController.h"

@interface BookmarkCell : UITableViewCell<UITextFieldDelegate>{

}

@property (nonatomic, strong) IBOutlet UILabel* bookmarkName;
@property (nonatomic) CLLocationCoordinate2D region;
@property (nonatomic) IBOutlet UIButton* minus;
@property (nonatomic) IBOutlet UIButton* goMap;
@property (weak, nonatomic) IBOutlet UITextField* editBookmarkNameField;
@property (nonatomic) IBOutlet UIButton* editMe;
@property (weak, nonatomic) PQBookmarksViewController* parent;

-(void) beginEditing;
-(void) endEditing;

@end
