//
//  ViewController.m
//  PARQScanTest
//
//  Created by Michael Xia on 1/19/12.
//  Copyright (c) 2012 Iudex Projects. All rights reserved.
//

#import "ViewController.h"  

@implementation ViewController

- (IBAction)button {
    //scan qr code, then display text inside the code.  
    
    //create new view for scanning
    ZBarReaderViewController * reader = [ZBarReaderViewController new];
    
    //set the delegate to receive results
    reader.readerDelegate = self;
    
    //configure reader for type of barcode
    [reader.scanner setSymbology:ZBAR_QRCODE config:ZBAR_CFG_ENABLE to:0];
    
    //present the scanner
    [self presentModalViewController:reader animated:YES];
    
}

-(IBAction)secondButton{
    ZBarReaderController * reader = [ZBarReaderController new];
    reader.readerDelegate = self;
    reader.sourceType =  UIImagePickerControllerSourceTypePhotoLibrary;
    [reader.scanner setSymbology:ZBAR_QRCODE config:ZBAR_CFG_ENABLE to:0];
    [self presentModalViewController:reader animated:YES];
    
}
//this method is essentially onActivityResult()
-(void) imagePickerController:(UIImagePickerController *)reader didFinishPickingMediaWithInfo:(NSDictionary *)info{
    
    id<NSFastEnumeration> results = [info objectForKey:ZBarReaderControllerResults];
    ZBarSymbol* dunno;
    for(dunno in results)
        break;
    
    label.text = dunno.data;

    //once we display the result string, dismiss the scanner.  
    [reader dismissModalViewControllerAnimated:YES];
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated
{
	[super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated
{
	[super viewDidDisappear:animated];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
}

@end
