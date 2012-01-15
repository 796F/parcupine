//
//  ServerCalls.m
//  hello
//
//  Created by Michael Xia on 1/13/12.
//  Copyright (c) 2012 Iudex Projects. All rights reserved.
//

#import "ServerCalls.h"
#import "ASIHTTPRequest.h"
#import "JSONKit.h"
#import "UserObject.h"
#import "Parser.h"

@implementation ServerCalls

//[NSNumber numberWithInteger:919]; cannot put normal int into NSArray.

+ (UserObject*) authEmail:(NSString*)emailIn Password:(NSString*)passwordIn{
    
    NSString* endpoint = @"http://75.101.132.219:8080/parkservice.auth/";
    NSArray* keys = [NSArray arrayWithObjects:@"email", @"password", nil];
    NSArray* value = [NSArray arrayWithObjects:emailIn, passwordIn, nil];
    NSDictionary* info = [NSDictionary dictionaryWithObjects:value forKeys:keys];
    NSError *error;
    NSData* jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:&error];
    
    ASIHTTPRequest* trequest = [ASIHTTPRequest requestWithURL:[NSURL URLWithString:endpoint]];
    [trequest appendPostData:jsonData];
    [trequest addRequestHeader:@"Content-Type" value:@"application/json"];
    [trequest setRequestMethod:@"POST"];
    [trequest startSynchronous];
    
    if(trequest.responseStatusCode==200){
        NSString *response = [trequest responseString];
        return [Parser parseUserObjectString:response];
    }else{
        return nil;
    }
}

@end
