//
//  BookmarkCell.h
//  Parq
//
//  Created by Michael Xia on 6/20/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Mapkit/Mapkit.h>

@interface BookmarkCell : UITableViewCell{

}

@property (nonatomic, strong) IBOutlet UILabel* bookmarkName;
@property (nonatomic) CLLocationCoordinate2D region;
@property (nonatomic) IBOutlet UIImageView* minus;

@end
