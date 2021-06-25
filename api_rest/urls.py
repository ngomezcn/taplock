from django.contrib import admin
from django.urls import path

from core.views import signIn, signUp, signOff # Sesion manager

from core.views import HelloView # Test purposes

urlpatterns = [
    path('admin/', admin.site.urls),    
    path('hello/', HelloView.as_view(), name='hello'),
    
    # User Sesion Manager """
    path('sign-in/',  signIn.as_view(),  name='signIn'),
    path('sign-up/',  signUp.as_view(),  name='signUp'),    
    path('sign-off/', signOff.as_view(), name='signOff'),
]
