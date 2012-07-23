//
//  BookmarkCell.m
//  Parq
//
//  Created by Michael Xia on 6/20/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "BookmarkCell.h"

@implementation BookmarkCell
@synthesize bookmarkName;
@synthesize region;
@synthesize minus;
@synthesize goMap;
@synthesize editMe;
@synthesize editBookmarkNameField;
@synthesize parent;

-(IBAction)mapPressed:(id)sender{
    NSLog(@"pressed map button\n");
    [parent performSegueWithIdentifier:@"showBookmarkMap" sender:parent];
}

-(IBAction)redMinusPressed:(id)sender{
    NSLog(@"red minus pressed\n");
}

-(void) textFieldDidBeginEditing:(UITextField *)textField{
    NSLog(@"editing...\n");
}

-(void) beginEditing{
    self.selectionStyle = UITableViewCellSelectionStyleNone;
    [UIView animateWithDuration:0.5 animations:^{
        self.goMap.center = CGPointMake(self.goMap.center.x-50, self.goMap.center.y);
        
    }];
}

-(void) endEditing{
    self.selectionStyle = UITableViewCellSelectionStyleGray;
    [UIView animateWithDuration:0.5 animations:^{        
        self.goMap.center = CGPointMake(self.goMap.center.x+50, self.goMap.center.y);
        
    }];

}

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
