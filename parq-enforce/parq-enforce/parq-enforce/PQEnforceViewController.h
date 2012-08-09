//
//  PQEnforceViewController.h
//  parq-enforce
//
//  Created by Michael Xia on 8/3/12.
//  Copyright (c) 2012 Michael Xia. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <AVFoundation/AVFoundation.h>

@interface PQEnforceViewController : UIViewController <AVCaptureVideoDataOutputSampleBufferDelegate>{
    
}

@property (weak, nonatomic) IBOutlet UILabel* text;
@property (weak, nonatomic) IBOutlet UIView* cameraView;

@end
