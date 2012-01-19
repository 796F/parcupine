//
//  ViewController.h
//  PARQScanTest
//
//  Created by Michael Xia on 1/19/12.
//  Copyright (c) 2012 Iudex Projects. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ZBarSDK.h"
@interface ViewController : UIViewController <ZBarReaderDelegate> {
    IBOutlet UILabel *label;
}

-(IBAction)button;
-(IBAction)secondButton;
@end
