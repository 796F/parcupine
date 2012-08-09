//
//  PQEnforceViewController.m
//  parq-enforce
//
//  Created by Michael Xia on 8/3/12.
//  Copyright (c) 2012 Michael Xia. All rights reserved.
//

#import "PQEnforceViewController.h"


@interface PQEnforceViewController ()

@end

@implementation PQEnforceViewController
@synthesize cameraView;
@synthesize text;

-(void) captureOutput:(AVCaptureOutput *)captureOutput didOutputSampleBuffer:(CMSampleBufferRef)sampleBuffer fromConnection:(AVCaptureConnection *)connection{
    NSLog(@"CAPTURE OUTPUT\n");
    //sample buffer ref
    //CVPixelBufferRef pixelBuffer = CMSampleBufferGetImageBuffer(sampleBuffer);
    
}

-(void) startCam{
    //start session
    AVCaptureSession* session = [[AVCaptureSession alloc] init];
    session.sessionPreset = AVCaptureSessionPresetHigh;
    //grab a device
    AVCaptureDevice* device = [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];
    //create input
    NSError* error;
    AVCaptureDeviceInput* input = [[AVCaptureDeviceInput alloc] initWithDevice:device error:&error];
    [session addInput:input];
    AVCaptureVideoDataOutput* output = [[AVCaptureVideoDataOutput alloc] init];
    [session addOutput:output];
    
    dispatch_queue_t que = dispatch_queue_create("myQue", NULL);
    [output setSampleBufferDelegate:self queue:que];
    [session startRunning];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self startCam];
	// Do any additional setup after loading the view.
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

@end
