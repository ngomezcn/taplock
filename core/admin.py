from django.contrib import admin
from .models import UserProfile, iTapActivation, iTap
# Register your models here.


#admin.site.register(UserProfile, ArduinoKey )

myModels = [UserProfile, iTapActivation, iTap]  # iterable list
admin.site.register(myModels)
