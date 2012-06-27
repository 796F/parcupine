//
//  PaymentOptionCell.h
//  Parq
//
//  Created by Michael Xia on 6/23/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface PaymentOptionCell : UITableViewCell

@property (nonatomic, strong) IBOutlet UILabel* name;
@property (nonatomic, strong) IBOutlet UIImageView* pic;
@property (nonatomic, strong) IBOutlet UIImageView* del;
@property (nonatomic) int paymentType; //cc, paypal, eagle bucks?  

@end
