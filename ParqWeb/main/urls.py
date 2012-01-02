from django.conf.urls.defaults import patterns, include, url

urlpatterns = patterns('parqweb.main.views',
                       url(r'^about/$','about'),
                       url(r'^faq/$','faq'),
                       url(r'^features/$','features'),
                       url(r'^$','home'),
                       )

