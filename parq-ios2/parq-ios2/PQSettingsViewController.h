//
//  PQSettingsViewController.h
//  Parq
//
//  Created by Michael Xia on 6/19/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UserObject.h"

@interface PQSettingsViewController : UITableViewController < UITableViewDataSource, UITableViewDelegate>{
    
}
@property (weak, nonatomic) IBOutlet UIView* nameCellView;
@property (weak, nonatomic) IBOutlet UIView* addrCellView;
@property (weak, nonatomic) IBOutlet UIView* plateCellView;
@property (weak, nonatomic) IBOutlet UIView* ssnCellView;
@property (weak, nonatomic) IBOutlet UIView* balanceCellView;

@property (weak, nonatomic) IBOutlet UIView* soundCellView;
@property (weak, nonatomic) IBOutlet UIView* vibrateCellView;

@property (weak, nonatomic) IBOutlet UILabel* nameLabel;
@property (weak, nonatomic) IBOutlet UILabel* addrLabel;
@property (weak, nonatomic) IBOutlet UILabel* plateLabel;
@property (weak, nonatomic) IBOutlet UILabel* ssnLabel;
@property (weak, nonatomic) IBOutlet UILabel* balanceLabel;

@property (weak, nonatomic) UserObject* user;

@end
