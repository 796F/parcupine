#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>
#import "Spot.h"
#import "PQSpotAnnotation.h"

@interface CalloutMapAnnotation : NSObject <MKAnnotation> {
	CLLocationDegrees _latitude;
	CLLocationDegrees _longitude;
}

typedef enum {
    kTopRightCorner,
    kTopLeftCorner,
    kBottomLeftCorner,
    kBottomRightCorner
} CalloutCorner;

@property (nonatomic) CLLocationDegrees latitude;
@property (nonatomic) CLLocationDegrees longitude;
@property (nonatomic, copy) NSString *title;
@property (nonatomic) CalloutCorner corner;
@property (nonatomic) PQSpotAnnotation* circle;
//@property (nonatomic, retain) Spot* spot;

- (id)initWithLatitude:(CLLocationDegrees)latitude
		  andLongitude:(CLLocationDegrees)longitude
              andTitle:(NSString *)title
             andCorner:(CalloutCorner) corner
             andCircle:(PQSpotAnnotation*) circle;
//               andSpot:(Spot*) spot;

@end
