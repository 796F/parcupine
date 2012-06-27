//
//  PaymentOptionCell.m
//  Parq
//
//  Created by Michael Xia on 6/23/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import "PaymentOptionCell.h"

@implementation PaymentOptionCell

@synthesize name;
@synthesize paymentType;
@synthesize pic;
@synthesize del;

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
