#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>
#import "Spot.h"

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
@property (nonatomic) MKCircle* circle;

- (id)initWithLatitude:(CLLocationDegrees)latitude
		  andLongitude:(CLLocationDegrees)longitude
              andTitle:(NSString *)title
             andCorner:(CalloutCorner) corner
               andCircle:(MKCircle*) circle;

@end
