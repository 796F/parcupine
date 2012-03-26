from django.conf.urls.defaults import patterns, include, url

urlpatterns = patterns('parqweb.parquser.views',
                       url(r'account/$','account'),
                       url(r'renew/$','renew'),
                       url(r'view/location/$','view_location'),
                       )
