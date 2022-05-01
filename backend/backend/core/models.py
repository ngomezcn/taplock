from datetime import datetime
from django.contrib.auth.base_user import BaseUserManager
from django.db import models 
from django.contrib.auth.models import AbstractBaseUser, PermissionsMixin
from django.utils import timezone

class UserProfileManager(BaseUserManager):
    
    def create_user(self, email, name, phone, password):
        
        if not email: 
            raise ValueError('Usuario debe tener un Email')
        
        email = self.normalize_email(email)
        user = self.model(email=email, name=name, phone=phone)
        
        user.set_password(password)
        user.save(using=self._db)
        
        return user
    
    def create_superuser(self, email, name, phone, password):
        user = self.create_user(email=email, name=name, phone=phone, password=password)
        
        user.is_superuser = True
        user.is_staff = True
        user.save(using=self._db)
        
        return user

class EmailVerification(models.Model):
    email = models.EmailField(max_length=32, default="-" )
    token = models.CharField(max_length=255, default="-")
    creation = models.DateTimeField(max_length=255, verbose_name="Datetime creation", default=datetime.now)
    valid = models.BooleanField(default=True,   verbose_name="Token status")
    
    def __str__(self):
        return self.email
    
    
class RecoverPasswordToken(models.Model):
    email = models.EmailField(max_length=32, default="-" )
    recover_code = models.CharField(max_length=255, default="-")
    creation = models.DateTimeField(max_length=255, verbose_name="Datetime creation", default=datetime.now)
    valid = models.BooleanField(default=True,   verbose_name="Code status")
    
    def __str__(self):
        return self.email
 
class UserProfile(AbstractBaseUser, PermissionsMixin):
    """
    A class used to represent a client

    ...

    Attributes
    ----------
    says_str : str
        a formatted string to print out what the animal says
    email : email
        the email of the client
    name : str
        the name of the client
    phone : str
        the phone number of the client
    is_active : bool
        the current status of the account
    is_staff : bool
        the privilegies of the client
    email_verification: bool
        the status of the email verification of the client

    
    Methods
    -------
    says(sound=None)
        Prints the animals name and what sound it makes
    """
    
    # TODO: Aceptar la protección de datos
    terms_of_service = models.BooleanField(default=True)
    
    # TODO: Notificar al cliente si hay info nueva que le interese.
    
    email = models.EmailField(max_length=32, unique=True)
    name = models.CharField(max_length=32)
    phone = models.CharField(max_length=9, default="null")
    creation = models.DateTimeField(default=datetime.now)

    is_active = models.BooleanField(default=True)
    is_staff = models.BooleanField(default=False) 
    
    # TODO: SMS verification
    email_verification = models.BooleanField(default=False)
        
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
import uuid
from random import randint, randrange
from django.utils.crypto import get_random_string

from .utils import random_string_ascii

def random1():
    return get_random_string(length=6, allowed_chars='1234567890QWERTYUIOPASDFGHJKLZXCVBNM')

def random2():
    return get_random_string(length=7, allowed_chars='1234567890')

class iTap(models.Model):
    owner = models.EmailField(max_length=32, default="", blank=True)
    name = models.CharField(max_length=32, default="", blank=True)    
    address = models.CharField(max_length=32, default="", blank=True)
        
    users = models.JSONField(default=dict, blank=True)
    requested_slots = models.IntegerField(default=0)
    slots = models.IntegerField(default=5)
    
    is_active = models.BooleanField(default=False)
    
    seed = models.CharField(default=random1, null=True, blank=True, unique=True, max_length=36)
    activation_key = models.CharField(default=random2,null=True, blank=True, unique=True, max_length=6)
    

    # 0 - Is not active, 
    # 1 - There is a thecnician working on in
    # 2 - Full active
    timestamp_creation = models.DateTimeField(default=timezone.now)
    status = models.IntegerField(default=0)    
    
