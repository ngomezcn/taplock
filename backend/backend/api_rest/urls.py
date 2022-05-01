from django.contrib import admin
from django.urls import path

from core.views import signIn, signUp # signOff # Sesion manager

from core.views import *

from website.views import home

urlpatterns = [
    path('', home, name='home'),    

    path('admin/', admin.site.urls),    
    path('hello/', HelloView.as_view(), name='hello'),
    
    path('app/email/',  echoMail.as_view(),  name='echoMail'),
    path('verification/', verificateEmailAccount),
    path('app/ttest/',  test.as_view(),  name='test'),


    # User Sesion Manager 
    path('app/sign-in/',  signIn.as_view(),  name='signIn'),
    path('app/sign-up/',  signUp.as_view(),  name='signUp'),  
    path('app/change-password/',  changePassword.as_view(),  name='changePassword'),    
    path('app/recover-password/', recoverPassword.as_view(), name='recoverPassword'),
    path('app/request-recover-password/', requestRecoverPassword.as_view(), name='requestRecoverPassword'),
    path('app/resend-email/',  ResendEmailVerification.as_view(),  name='resend-email'),
    path('app/claim-itap/',  ClaimiTap.as_view(),  name='claim-itap'),
    

  
   # path('sign-off/', signOff.as_view(), name='signOff'),
]
