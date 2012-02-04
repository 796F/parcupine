//
//  ParqAccountsViewController.h
//  Parq
//
//  Created by Mark Yen on 1/3/12.
//  Copyright (c) 2012 Massachusetts Institute of Technology. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "EditEmailViewController.h"

@interface ParqAccountViewController : UITableViewController{
    IBOutlet EditEmailViewController* editEmailView;
    
    NSString* currentEmail;
    id reference;
}


-(IBAction)editEmailClicked:(id)sender;

@end
