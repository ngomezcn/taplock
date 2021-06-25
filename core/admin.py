from django.contrib import admin
from .models import UserProfile, iTapActivation, iTap, emailVerification
# Register your models here.


#admin.site.register(UserProfile, ArduinoKey )

myModels = [UserProfile, iTapActivation, iTap, emailVerification]  # iterable list
admin.site.register(myModels)
