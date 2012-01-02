from parqweb.main.models import User,Admin
from django.contrib.auth.backends import ModelBackend

class ParqBackend(ModelBackend):
    def authenticate(self,username=None,password=None):
        try:
            user = User.objects.get(email=username,password=password)
            return user.django_user
        except User.DoesNotExist:
            return None
        
class AdminParqBackend(ModelBackend):
    def authenticate(self,username=None,password=None):
        try:
            user = Admin.objects.get(email=username,password=password)
            return user.django_user
        except Admin.DoesNotExist:
            return None
        
