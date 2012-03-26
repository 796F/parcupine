from django.conf.urls.defaults import patterns, include, url

from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    url(r'^user/',include('parqweb.parquser.urls')),
    url(r'^client/',include('parqweb.parqclient.urls')),
    url(r'^actions/',include('parqweb.actions.urls')),
    url(r'^admin/', include(admin.site.urls)),
    url(r'', include('parqweb.main.urls')),

)

