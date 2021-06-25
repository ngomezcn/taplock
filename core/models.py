from django.contrib.auth.base_user import BaseUserManager
from django.db import models
from django.contrib.auth.models import AbstractBaseUser, PermissionsMixin
from django.utils import timezone

class UserProfileManager(BaseUserManager):
    
    def create_user(self, email, name, phone, password=None):
        
        if not email: 
            raise ValueError('Usuario debe tener un Email')
        
        email = self.normalize_email(email)
        user = self.model(email=email, name=name, phone=phone)
        
        
        user.set_password(password)
        user.save(using=self._db)
        
        return user
    
    def create_superuser(self, email, name, password):
        user = self.create_user(email, name, password)
        
        user.is_superuser = True
        user.is_staff = True
        user.save(using=self._db)
        
        return user

 
class UserProfile(AbstractBaseUser, PermissionsMixin):
    email = models.EmailField(max_length=255, unique=True)
    name = models.CharField(max_length=245)
    phone = models.CharField(max_length=9, default="null")
    
    is_active = models.BooleanField(default=True)
    is_staff = models.BooleanField(default=False)
        
    objects = UserProfileManager()
        
    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = ['name', 'phone']
    
    def __str__(self):
        return self.email
    
''' LUISMA MODEL
{
    "609513287":
    {
       "name":"Naïm Gómez Cano",
       "phone": "609513287",
       "status": 0,
       "token": "asd123asd",
       "visible": false
    }
}
'''

class iTap(models.Model):
    name = models.CharField(max_length=32)
    activation_date = models.DateField(timezone.now())
    
    slots = models.IntegerField(default=5)
    tokens = models.JSONField(default=dict, blank=True)
    
    address = models.CharField(max_length=32, default="NULL")
    city = models.CharField(max_length=32, default="NULL")
    region = models.CharField(max_length=32, default="NULL")
    zipcode = models.CharField(max_length=32, default="NULL")
    
    key = models.CharField(max_length=8, default="null")
        
    
class iTapActivation(models.Model):
    key = models.CharField(max_length=8, default="null")
    activated = models.BooleanField(default=False)
    owner = models.CharField(max_length=32, default="null")

    