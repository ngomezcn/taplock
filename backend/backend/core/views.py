
# Django
from api_rest.settings import SERVER_IP
from django.core.exceptions import *
from django.core.mail import send_mail
from django.core import mail
from django.core.exceptions import ValidationError
from django.core.validators import validate_email
from django.db.utils import Error, IntegrityError
from django.utils.html import strip_tags
from django.utils.crypto import get_random_string
from django.contrib.auth import authenticate
from django.shortcuts import render
from django.template.loader import render_to_string

# Django Rest Framework
from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.authtoken.views import ObtainAuthToken
from rest_framework.authtoken.models import Token
from rest_framework.permissions import IsAuthenticated

# Python Modules
import datetime
import hashlib

# Custom Modules
from .models import EmailVerification, UserProfile, UserProfile, RecoverPasswordToken
from .serializers import ClaimiTapSerializer, UserSerializer, RequestRecoverPasswordSerializer, RecoverPasswordSerializer
from .error_manager import codes


def verificateEmailAccount(request):

    """
    Verify the user account using a URL that has been sent to their email.
    
    ...

    How it works
    -------
    The View get as parameter a key generated previosly (on another View), if the key is correct the verification will be active.

    Variables
    -------
    token : string
        String that we get form the parameter "?key=", is going to check if there is on the database.
    
    verification : EmailVerification obj    
        Save a object EmailVerification that we get using the var token.
        
    user : UserProfile obj
        Save a object UserProfile that we get using the field email that contains the object EmailVerification.  
    """ 
    
    # ASK: Is useful login user after activate account?    

    token = request.GET.get('key')

    try:
        verification = EmailVerification.objects.get(token=token)
        print(verification.email)
        if verification.valid:            
            user = UserProfile.objects.get(email=verification.email)
        
            user.email_verification = True
            user.save()
            
            verification.valid = False
            verification.save()
            
            return render(request, "email_success.html")
        else:
            return render(request, "error.html")
        
    except Exception as e: # If key dont exist
        return render(request, "404.html")
             


class requestRecoverPassword(APIView):
    
    """
    Request: http post http://172.10.10.10:8000/request-recover-password/ email=naimgomezcn@gmail.com
    
    Send a 6 digits recover code to the email of the user. 
    
    ...

    Variables
    -------
    """     
    
    def post(self, request, *args, **kwargs):
        serializer = RequestRecoverPasswordSerializer(data=request.data)
        
        if serializer.is_valid(raise_exception=True):
        
            try: 
                email = serializer.validated_data['email']
            except Exception as e:
                return Response(codes.recoverPassword.missing_parameter(e.args), status.HTTP_400_BAD_REQUEST)

            try:                 
                try:
                    validate_email(email)
                except ValidationError as e:
                    return Response(codes.recoverPassword.bad_email_format(e.args), status.HTTP_400_BAD_REQUEST)
                                                                
                # User creation handler                              
                try:              
                    
                    user = UserProfile.objects.get(email=email)                                         
                                                   
                except Exception as e:                    
                    return Response(codes.recoverPassword.user_does_not_exist(e.args), status.HTTP_400_BAD_REQUEST)        
                            
                # Generate recovery code
                try:
                
                    recover_code = get_random_string(length=6, allowed_chars='1234567890')
                                        
                    RecoverPasswordToken.objects.create(
                    email=email, 
                    recover_code=recover_code)
                    
                except Exception as e:
                    return Response(codes.recoverPassword.activation_email_sending_failed(e.args))

                # Send email
                try:        
                    
                    context = {
                        'code':  int(recover_code),
                        'name':  user.name,
                        'email': email
                    }

                    subject = 'TapLock Admin - Recuperar contraseña'
                    html_message = render_to_string('recover_password.html', context)
                    plain_message = strip_tags(html_message)
                    from_email = 'cliente@taplock.es'
                    to = email
                    mail.send_mail(subject, plain_message, from_email, [to], html_message=html_message)
                    
            
                    return Response({               
                        'message': "Se ha enviado el codigo de recuperación",
                        'email': email,
                        'name':  user.name,    
                    })  

                except Exception as e:
                    #TODO: Improve this
                    return Response("EMAIL FAILS" + str(e.args))
            except Exception as e: 
                return Response(codes.recoverPassword.undefined_error(e.args))  
        else:
            return Response(codes.server.serializer_error())  

class recoverPassword(APIView):
    def post(self, request, *args, **kwargs):
            serializer = RecoverPasswordSerializer(data=request.data)
            
            if serializer.is_valid(raise_exception=True):
            
                try: 
                    email = serializer.validated_data['email']
                    code = serializer.validated_data['code']                    
                    new_password = serializer.validated_data['newPassword']
                except Exception as e:
                    #TODO: Improve this
                    return Response(codes.signUp.missing_parameter(e.args),status.HTTP_400_BAD_REQUEST)

                try: 
                    
                    # Email validation handler                                                     
                    try:
                        validate_email(email)
                    except ValidationError as e:
                        return Response(codes.signUp.bad_email_format(e.args), status.HTTP_400_BAD_REQUEST)                                                     
                    
                    try:                        
                        verification = RecoverPasswordToken.objects.get(recover_code=code)  
                    except Exception as e:
                        return Response("No se encuentra el codigito", status.HTTP_400_BAD_REQUEST)                                                                  
                    
                    try:
                        print("verification.email", verification.email)
                        user = UserProfile.objects.get(email=email)  
                    except Exception as e:
                        return Response("No se encuentra el usuario", status.HTTP_400_BAD_REQUEST)     
                        
                    if verification.valid:
                        
                        user.set_password(new_password)
                        user.save() 
                            
                        verification.valid = False
                        verification.save()

                        return Response({
                            'message': "La contraseña ha sido cambiada con exito.",                  
                            'email': user.email,
                            'name': user.name                        
                        })
                    else:
                        return Response({
                            'message': "El codigo ha caducado."                  
                        })                       

                except Exception as e: 
                    return Response(codes.signUp.undefined_error(e.args))  
            else:
                return Response(codes.server.serializer_error())  

from .serializers import SignInSerializer
from .models import *
class ClaimiTap(APIView):
    def post(self, request, *args, **kwargs):        
        serializer = ClaimiTapSerializer(data=request.data)    

        if serializer.is_valid(raise_exception=True):
            
            try: 
                email    = serializer.validated_data['email']       
                password    = serializer.validated_data['password']       
                activation_key = serializer.validated_data['activation_key']       
                address = serializer.validated_data['address']       
                name = serializer.validated_data['name']       

            except Exception as e:
                return Response(codes.signUp.missing_parameter(e.args), status.HTTP_400_BAD_REQUEST)
            
            try: 
                # Email validation handler                                                     
                try:
                    validate_email(str(email))
                except ValidationError as e:
                    return Response(codes.signUp.bad_email_format(e.args), status.HTTP_400_BAD_REQUEST)

                # Get user
                try:                
                    user = UserProfile.objects.get(email=email)
                    if user.email_verification != True:
                        return Response(
                            codes.changePassword.pending_email_verification())
                except Exception as e:
                    return Response(codes.changePassword.user_does_not_exist(e.args), status.HTTP_400_BAD_REQUEST)
                
                # Get iTap
                try:                
                    itap = iTap.objects.get(activation_key=activation_key)        
                except Exception as e:
                    return Response({e.args}, status.HTTP_400_BAD_REQUEST)

                scheme = {}
                base = {
                        "0":
                            {
                                "name":"",
                                "address": "",
                                "status": "",
                                "slots": "",
                                "is_active": "false"
                            }
                        }

                
                try:                
                    itap.owner = user.email
                    itap.name = name
                    itap.address = address
                    itap.is_active = True                    
                    itap.save()

                    print(user.email)
                    a = 0
                    for i in iTap.objects.all().filter(owner=user.email):
                        scheme = {"1":"2"}
                        print(scheme)
                        print("h")

                    return Response({"todo good"})

                except Exception as e:
                    return Response({e.args}, status.HTTP_400_BAD_REQUEST)

            except Exception as e: 

                return Response(codes.signUp.undefined_error(e.args))  
        else:
            print(serializer.error_messages)
            print(serializer.errors)

            return Response(codes.server.serializer_error(serializer.errors), status.HTTP_400_BAD_REQUEST)


from .serializers import ResendEmailVerificationSerializer
class ResendEmailVerification(APIView):
    def post(self, request, *args, **kwargs):        
        serializer = ResendEmailVerificationSerializer(data=request.data)       

        if serializer.is_valid(raise_exception=True):
        
            try: 
                email    = serializer.validated_data['email']                
                
            except Exception as e:
                #TODO: Improve this
                return Response(codes.signUp.missing_parameter(e.args), status.HTTP_400_BAD_REQUEST)
            
            try: 
                
                # Email validation handler                                                     
                try:
                    validate_email(str(email))
                except ValidationError as e:
                    return Response(codes.signUp.bad_email_format(e.args), status.HTTP_400_BAD_REQUEST)

                try:                
                    user = UserProfile.objects.get(email=email)
                    if(user.email_verification == True):
                        return Response(codes.ResendEmailVerification.account_already_verificate(), status.HTTP_400_BAD_REQUEST)
        
                except Exception as e:
                    return Response(codes.signUp.activation_email_sending_failed(e.args), status.HTTP_400_BAD_REQUEST)

                # Generate email token for verification
                try:
                
                    email_token = get_random_string(length=50, allowed_chars='1234567890-QWERTYUIOPASDFGHJKLZXCVBNM')
                    
                    EmailVerification.objects.create(
                    email=email, 
                    token=email_token)

                except Exception as e:
                    return Response(codes.signUp.activation_email_sending_failed(e.args))
                            
                # Send email verification
                try:                

                    link = "http://" + SERVER_IP + "/verification/?key=" + email_token                        
                    context = {
                        'link':  link,
                        'name':  user.name,
                        'email': email
                    }
                    
                    subject = 'TapLock Admin - Activación'
                    html_message = render_to_string('mail_template.html', context)
                    plain_message = strip_tags(html_message)
                    from_email = 'cliente@taplock.es'
                    to = email
                    mail.send_mail(subject, plain_message, from_email, [to], html_message=html_message)


                    return Response({               
                        'message': "Se ha vuelto a enviar el codigo de activación.",
                        'email': email,
                    }, status.HTTP_200_OK) 

                except Exception as e:
                    #TODO: Improve this
                    return Response("EMAIL FAILS" + str(e.args), status.HTTP_400_BAD_REQUEST)
            except Exception as e: 

                return Response(codes.signUp.undefined_error(e.args))  
        else:
            print(serializer.error_messages)
            print(serializer.errors)

            return Response(codes.server.serializer_error(serializer.errors), status.HTTP_400_BAD_REQUEST)


''' Sign Up API View'''
from .serializers import SignUpSerializer
class signUp(APIView):
        
            
    def post(self, request, *args, **kwargs):        
        serializer = SignUpSerializer(data=request.data)       

        if serializer.is_valid(raise_exception=True):
        
            try: 
                email    = serializer.validated_data['email']                
                name     = serializer.validated_data['name']
                phone    = serializer.validated_data['phone']
                password = serializer.validated_data['password']
                terms_of_service = serializer.validated_data['terms_of_service']
                
                if(terms_of_service == False):
                    return Response(codes.signUp.accept_terms_of_service(), status.HTTP_400_BAD_REQUEST)
                    
                
            except Exception as e:
                #TODO: Improve this
                print("asdasd",e.args)
                return Response(codes.signUp.missing_parameter(e.args), status.HTTP_400_BAD_REQUEST)
            
            '''
            try:
                fieldValidation()
            except Exception as e:
                return Response({"Ha ocurrido un error mientras se validaban los datos"}, status.HTTP_400_BAD_REQUEST )
            '''
            try: 
                
                # Email validation handler                                                     
                try:
                    validate_email(str(email))
                except ValidationError as e:
                    print(e.args)
                    print("asdasd",e.args)

                    return Response(codes.signUp.bad_email_format(e.args), status.HTTP_400_BAD_REQUEST)
                            
                '''
                TODO-ASK: Is necesary a complex field validation? 
                            I mean if the user says that his phone number is "pikaxu666" is not our problem.
                '''
                # User creation handler                              
                try:
                    
                    UserProfile.objects.create_user(  
                    email    = email, 
                    name     = name, 
                    phone    = phone,
                    password = password                    
                    )                                  
                except Exception as e:                    
                    return Response(codes.signUp.user_already_exist(e.args), status.HTTP_400_BAD_REQUEST)                    
                    
                # Generate email token for verification
                try:
                    # IMPROVE-IDEA: If a the random string has not enough entropy we can use a hash.
                    # hash_input = str(datetime.datetime.utcnow()) + name + phone + email + password
                    # hash_input.encode("utf-8")                        
                    # hash_token = hashlib.sha1(hash_input).hexdigest()
                    # email_token = hash_token
                
                    email_token = get_random_string(length=50, allowed_chars='1234567890-QWERTYUIOPASDFGHJKLZXCVBNM')
                    
                    EmailVerification.objects.create(
                    email=email, 
                    token=email_token)

                except Exception as e:
                    return Response(codes.signUp.activation_email_sending_failed(e.args))

                # Send email verification

                try:                

                    link = "http://" + SERVER_IP + "/verification/?key=" + email_token                        
                    context = {
                        'link':  link,
                        'name':  name,
                        'email': email
                    }
                    
                    subject = 'TapLock Admin - Activación'
                    html_message = render_to_string('mail_template.html', context)
                    plain_message = strip_tags(html_message)
                    from_email = 'cliente@taplock.es'
                    to = email
                    mail.send_mail(subject, plain_message, from_email, [to], html_message=html_message)


                    return Response({               
                        'message': "Se ha enviado el código de activación.",
                        'email': email,
                        'name':  name,    
                    }, status.HTTP_200_OK) 

                except Exception as e:
                    #TODO: Improve this
                    return Response("EMAIL FAILS" + str(e.args), status.HTTP_400_BAD_REQUEST)
            except Exception as e: 

                return Response(codes.signUp.undefined_error(e.args))  
        else:
            print(serializer.error_messages)
            print(serializer.errors)

            return Response(codes.server.serializer_error(serializer.errors), status.HTTP_400_BAD_REQUEST)  
         
from .serializers import SignInSerializer                                                                    
class signIn(ObtainAuthToken):
    def post(self, request, *args, **kwargs):
        
        serializer = SignInSerializer(data=request.data)        
        if serializer.is_valid(raise_exception=True):
            
            email = serializer.validated_data['email']
            password = serializer.validated_data['password'] 
            
            try:
                validate_email(email)
            except ValidationError as e: # Input email has bad format.
                return Response(codes.signIn.bad_email_format(e.args), status.HTTP_400_BAD_REQUEST)             
                    
            try:  
                user = UserProfile.objects.get(email=email)                
            except Exception as e: # User is has no account   
                return Response(codes.signIn.user_does_not_exist(e.args), status.HTTP_400_BAD_REQUEST)             
            
            user = authenticate(username=email, password=password)
            try:
                if user is not None:           
                    if user.email_verification:                        
                        
                        token, created = Token.objects.get_or_create(user=user)                            
                        return Response({
                            'token': token.key,
                            'email': user.email,
                            'is_active': True,
                            'name': user.name,
                            'email_verification': user.email_verification,
                        })
            
                    else: # User has to veritifcate the email account
                        return Response(
                            codes.signIn.pending_email_verification(status=status.HTTP_400_BAD_REQUEST), status=status.HTTP_400_BAD_REQUEST)                                      
                        
                else: # Client bad login credentials 
                    return Response(codes.signIn.wrong_credentials(), status=status.HTTP_400_BAD_REQUEST)      
                
            except Exception as e: # Catch unknown exception 
                return Response(codes.signIn.undefined_error(e.args))      

        else: # Serializer is not valid
            return Response(codes.server.serializer_error())  
            
from .serializers import CustomSerializer, testSerializer

class changePassword(ObtainAuthToken):
    def post(self, request, *args, **kwargs):
        
        serializer = CustomSerializer(data=request.data)        
        if serializer.is_valid(raise_exception=True):
            
            email = serializer.validated_data['email']
            password = serializer.validated_data['password'] 
            new_password = serializer.validated_data['newPassword'] 
            try:  
                user = UserProfile.objects.get(email=email)                
            except Exception as e: # User is has no account   
                return Response(codes.changePassword.user_does_not_exist(e.args), status.HTTP_400_BAD_REQUEST)             
            
            user = authenticate(username=email, password=password)
            
            try:
                if user is not None:           
                    if user.email_verification:       
                        if password != new_password:
                            user.set_password(new_password)
                            user.save()

                            return Response({
                                'message': "La contraseña ha sido cambiada con exito.",                  
                                'email': user.email,
                                'name': user.name,
                                'user_id': user.pk,                        
                                'lastPassword': password,
                                'newPassword': new_password                           
                            })
                        else: # If the new password is the same.
                            return Response(
                                codes.changePassword.passwords_are_the_same())
                        
                    else: # User has to veritifcate the email account
                        return Response(
                            codes.changePassword.pending_email_verification())                                      
                        
                else: # Client bad login credentials 
                    return Response(codes.changePassword.wrong_credentials())      
                
            except Exception as e: # Catch unknown exception 
                return Response(codes.changePassword.undefined_error(e.args))      

        else: # Serializer is not valid
            return Response(codes.server.serializer_error())  

'''        
class signOff(APIView):
    def get(self, request, format=None):

        try:
            content = {'logout_user': str(request.user)}
            request.user.auth_token.delete()    
            return Response(content, status=status.HTTP_200_OK)    
            
        except AttributeError as e: 
                print(e.args)
                if "'AnonymousUser' object has no attribute 'auth_token'" in e.args:
                    return Response({
                        'exception': "El usuario no ha iniciado sesión."                 
                    }, status=status.HTTP_400_BAD_REQUEST)
                else:
                    return Response({
                        'exception': "Ha ocurrido un error indefinido.",                        
                    }, status=status.HTTP_400_BAD_REQUEST)
'''    

'''
verification 
'''
class echoMail(APIView):

    def post(self, request, *args, **kwargs):
        # send and email

        subject = 'Subject'
        html_message = render_to_string('mail_template.html', {'context': 'values'})
        plain_message = strip_tags(html_message)
        from_email = 'From <from@example.com>'
        to = 'naimgomezcn@gmail.com'
        mail.send_mail(subject, plain_message, from_email, [to], html_message=html_message)
        
        #send_mail(subject, html_message,from_email,[to])
        
        return Response("HOLA")
            
class HelloView(APIView):
    permission_classes = [IsAuthenticated]

    def get(self, request):
        content = {'message': 'Hello, World!'}
        return Response(content)


class test(APIView):

    def post(self, request, *args, **kwargs):
        
        serializer = UserSerializer(data=request.data)        

        print(serializer.initial_data['password'])
        print(serializer.initial_data['email'])

        if serializer.is_valid(raise_exception=False):
            print("LOOOOOL")
            content = {
                "caca": "BIEN",
                "xd": "BIEN",
                "name": "BIEN",
                "email": "BIEN",
                "password": "BIEN"

            }
            return Response(content, status.HTTP_200_OK)
        else:
            print(serializer.error_messages)
            return Response(codes.server.serializer_error(), status.HTTP_400_BAD_REQUEST)  





