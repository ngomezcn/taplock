from django.contrib import admin
from django.urls import path

from core.views import signIn, signUp, signOff # Sesion manager

from core.views import HelloView, echoMail, activateAccount # Test purposes

urlpatterns = [
    path('admin/', admin.site.urls),    
    path('hello/', HelloView.as_view(), name='hello'),
    
    path('email/',  echoMail.as_view(),  name='echoMail'),
    path('test/', activateAccount),
    
    # User Sesion Manager """
    path('sign-in/',  signIn.as_view(),  name='signIn'),
    path('sign-up/',  signUp.as_view(),  name='signUp'),    
    path('sign-off/', signOff.as_view(), name='signOff'),
]
