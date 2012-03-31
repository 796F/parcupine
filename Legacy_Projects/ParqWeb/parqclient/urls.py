from django.conf.urls.defaults import patterns, include, url

urlpatterns = patterns('parqweb.parqclient.views',
                       url(r'activity_space/$','space_activity'),
                       url(r'account/$','account'),

                       )
