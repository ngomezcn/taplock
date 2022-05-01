import datetime
from django.utils import timezone

# Rest Framework 
from rest_framework import status

'''
To keep KIS we only can use those HTTP codes 

200 - OK
400 - Solicitud incorrecta (error del cliente): un json con error \ más detalles deben regresar al cliente.
401 - No autorizado
500 - Error interno del servidor: un json con un error debe regresar al cliente solo cuando no hay riesgo de seguridad al hacerlo.
'''
# If ALL fails it will save us ;)
INTERNAL_SERVER_ERROR = "internal_error(exception=None) -> Failed  "

class codes:
    """
    A class used to manage all error responses from the Api Rest, 
    this class is maked to be called from views.py also there is no problem is we use this on another context.
    
    TODO: Make a log system to save all data on the disk
    
    """

    
  
    class server:   
        """
        A class used to manage internal server errors.
        
        ...

        Methods
        -------
        internalError(exception=e.args) : dict
            If server have and undefined internal error we call this,             
            Returns a response in json format with useful information.  
            
        internalError(exception=e.args) : dict
            If something wrongs happends when searlizer data.  
        """                     
        
        def internal_error(exception=None, status=status.HTTP_500_INTERNAL_SERVER_ERROR):    
            try:        
                context = {
                        "status" : str(status),
                        "code" : "internal-2001",
                        "timestamp": str(datetime.datetime.utcnow()),
                        "message" : "Error interno desconocido.",
                        "exception:": str(exception)}
                
                return context
            except Exception as e:
                return {INTERNAL_SERVER_ERROR}
            
        def serializer_error(exception=None, status=status.HTTP_500_INTERNAL_SERVER_ERROR):
            try:
                return {
                        "status" : str(status),
                        "code" : "internal-2000",
                        "timestamp":str(datetime.datetime.utcnow()),
                        "message" : "Error de serializador",
                        "exception:": str(exception)}     
                
            except Exception as e:
                return codes.server.internal_error(e.args)
              
            
    class signIn:      
        """
        A class used to manage Sign In user errors.
        
        ...

        Methods
        -------
        undefinedError(exception=e.args) : dict
            If we have and undefined error while login we call this,
            Returns a response in json format with useful information.            
        wrongCredentials(exception=e.args) : dict
            Returns a response in json format with useful information.    
        """  
        
        def undefined_error(exception=None, status=status.HTTP_400_BAD_REQUEST):
            try:
                return {
                        "status" : str(status),
                        "code" : "signIn-2001",
                        "timestamp": str(datetime.datetime.utcnow()),
                        "message" : "Error desconocido.",
                        "exception:": str(exception)}     
                
            except Exception as e:
                return codes.server.internal_error(e.args)
            
        def wrong_credentials(exception=None, status=status.HTTP_400_BAD_REQUEST):
            try:
                return {    
                        "status" : str(status),
                        "code" : "signIn-2002",
                        "timestamp": str(datetime.datetime.utcnow()),
                        "message" : "El email y la contraseña no coinciden.",
                        "exception:": str(exception)}     
                
            except Exception as e:
                codes.server.internal_error(e.args)
                return -1
            
        def pending_email_verification(exception=None, status=status.HTTP_400_BAD_REQUEST):
            try:
                return {                     
                        "status" : str(status),
                        "code" : "signIn-2003",
                        "timestamp": str(datetime.datetime.utcnow()),
                        "message" : "Vaya... Al parecer todavia no ha activado su cuenta",
                        "is_active": False,
                        "exception:": str(exception)}     
                
            except Exception as e:
                return codes.server.internal_error(e.args)
            
        def bad_email_format(exception=None, status=status.HTTP_400_BAD_REQUEST):
            try:
                return {                     
                        "status" : str(status),
                        "code" : "signIn-2004",
                        "timestamp": str(datetime.datetime.utcnow()),
                        "message" : "El email introducido no tiene un formato correcto.",
                        "exception:": str(exception)}     
            except Exception as e:
                return codes.server.internal_error(e.args)
            
        def user_does_not_exist(exception=None, status=status.HTTP_400_BAD_REQUEST):
            try:
                return {                     
                        "status" : str(status),
                        "code" : "signIn-2005",
                        "timestamp": str(datetime.datetime.utcnow()),
                        "message" : "La cuenta indicada no existe.",
                        "exception:": str(exception)}     
            except Exception as e:
                return codes.server.internal_error(e.args)

    class signUp:
        
        def undefined_error(exception=None, status=status.HTTP_400_BAD_REQUEST):
            try:
                return {
                        "status" : str(status),
                        "code" : "signUp-7000",
                        "timestamp": str(datetime.datetime.utcnow()),
                        "message" : "Error desconocido.",
                        "exception:": str(exception)}     
            except Exception as e:
                return codes.server.internal_error(e.args)
            
        def bad_email_format(exception=None, status=status.HTTP_400_BAD_REQUEST):
            try:
                return {                     
                        "status" : str(status),
                        "code" : "signUp-7001",
                        "timestamp": str(datetime.datetime.utcnow()),
                        "message" : "El email introducido no tiene un formato correcto.",
                        "exception:": str(exception)}     
            except Exception as e:
                return codes.server.internal_error(e.args)
            
        def user_already_exist(exception=None, status=status.HTTP_400_BAD_REQUEST):
            try:
                return {                     
                        "status" : str(status),
                        "code" : "signUp-7002",
                        "timestamp": str(datetime.datetime.utcnow()),
                        "message" : "Este usuario ya existe.",
                        "exception:": str(exception)}    
            except Exception as e:
                return codes.server.internal_error(e.args)
            
        def missing_parameter(exception=None, status=status.HTTP_400_BAD_REQUEST):
                    try:
                        return {                     
                                "status" : str(status),
                                "code" : "signUp-7004",
                                "timestamp": str(datetime.datetime.utcnow()),
                                "message" : "Faltan parametros para poder crear el usuario.",
                                "exception:": str(exception)}     
                        
                    except Exception as e:
                        return codes.server.internal_error(e.args)
                    
        def activation_email_sending_failed(exception=None, status=status.HTTP_400_BAD_REQUEST):
                    try:
                        return {                     
                                "status" : str(status),
                                "code" : "signUp-7005",
                                "timestamp": str(datetime.datetime.utcnow()),
                                "message" : "Ha ocurrido un error al enviar el email de activación.",
                                "exception:": str(exception)}     
                        
                    except Exception as e:
                        return codes.server.internal_error(e.args)
                    
        def accept_terms_of_service(exception=None, status=status.HTTP_400_BAD_REQUEST):
                    try:
                        return {                     
                                "status" : str(status),
                                "code" : "signUp-7006",
                                "timestamp": str(datetime.datetime.utcnow()),
                                "message" : "Debe aceptar los términos del servicio para poder crear la cuenta.",
                                "exception:": str(exception)}     
                        
                    except Exception as e:
                        return codes.server.internal_error(e.args)
                                        
                      
                      
    class ResendEmailVerification:
        def account_already_verificate(exception=None, status=status.HTTP_400_BAD_REQUEST):
                    try:
                        return {                     
                                "status" : str(status),
                                "code" : "ResendEmailVerification-6001",
                                "timestamp": str(datetime.datetime.utcnow()),
                                "message" : "Esta cuenta ya esta verificada.",
                                "exception:": str(exception)}     
                        
                    except Exception as e:
                        return codes.server.internal_error(e.args)
                      
    class changePassword:
        def undefined_error(exception=None, status=status.HTTP_400_BAD_REQUEST):
            try:
                return {
                        "status" : str(status),
                        "code" : "changePassword-2001",
                        "timestamp": str(datetime.datetime.utcnow()),
                        "message" : "Error desconocido.",
                        "exception:": str(exception)}     
                
            except Exception as e:
                return codes.server.internal_error(e.args)
            
        def wrong_credentials(exception=None, status=status.HTTP_400_BAD_REQUEST):
            try:
                return {    
                        "status" : str(status),
                        "code" : "changePassword-2002",
                        "timestamp": str(datetime.datetime.utcnow()),
                        "message" : "El email y la contraseña no coinciden.",
                        "exception:": str(exception)}     
                
            except Exception as e:
                codes.server.internal_error(e.args)
                return -1
            
        def pending_email_verification(exception=None, status=status.HTTP_400_BAD_REQUEST):
            try:
                return {                     
                        "status" : str(status),
                        "code" : "changePassword-2003",
                        "timestamp": str(datetime.datetime.utcnow()),
                        "message" : "Debe activar su cuenta mediante el enlace de email",
                        "exception:": str(exception)}     
                
            except Exception as e:
                return codes.server.internal_error(e.args)
            
        def bad_email_format(exception=None, status=status.HTTP_400_BAD_REQUEST):
            try:
                return {                     
                        "status" : str(status),
                        "code" : "changePassword-2004",
                        "timestamp": str(datetime.datetime.utcnow()),
                        "message" : "El email introducido no tiene un formato correcto.",
                        "exception:": str(exception)}     
            except Exception as e:
                return codes.server.internal_error(e.args)
            
        def user_does_not_exist(exception=None, status=status.HTTP_400_BAD_REQUEST):
            try:
                return {                     
                        "status" : str(status),
                        "code" : "changePassword-2005",
                        "timestamp": str(datetime.datetime.utcnow()),
                        "message" : "La cuenta indicada no existe.",
                        "exception:": str(exception)}     
            except Exception as e:
                return codes.server.internal_error(e.args)
            
        def passwords_are_the_same(exception=None, status=status.HTTP_400_BAD_REQUEST):
            try:
                return {                     
                        "status" : str(status),
                        "code" : "changePassword-2005",
                        "timestamp": str(datetime.datetime.utcnow()),
                        "message" : "Las dos contraseñas son iguales.",
                        "exception:": str(exception)}     
            except Exception as e:
                return codes.server.internal_error(e.args)


    class recoverPassword:        
        def undefined_error(exception=None, status=status.HTTP_400_BAD_REQUEST):
            try:
                return {
                        "status" : str(status),
                        "code" : "recoverPassword-7000",
                        "timestamp": str(datetime.datetime.utcnow()),
                        "message" : "Error desconocido.",
                        "exception:": str(exception)}     
            except Exception as e:
                return codes.server.internal_error(e.args)
            
        def bad_email_format(exception=None, status=status.HTTP_400_BAD_REQUEST):
            try:
                return {                     
                        "status" : str(status),
                        "code" : "recoverPassword-7001",
                        "timestamp": str(datetime.datetime.utcnow()),
                        "message" : "El email introducido no tiene un formato correcto.",
                        "exception:": str(exception)}     
            except Exception as e:
                return codes.server.internal_error(e.args)
            
        def user_does_not_exist(exception=None, status=status.HTTP_400_BAD_REQUEST):
            try:
                return {                     
                        "status" : str(status),
                        "code" : "recoverPassword-7002",
                        "timestamp": str(datetime.datetime.utcnow()),
                        "message" : "La cuenta indicada no existe.",
                        "exception:": str(exception)}     
            except Exception as e:
                return codes.server.internal_error(e.args)
            
        def missing_parameter(exception, status=status.HTTP_400_BAD_REQUEST):
                    try:
                        return {                     
                                "status" : str(status),
                                "code" : "recoverPassword-7003",
                                "timestamp": str(datetime.datetime.utcnow()),
                                "message" : "Faltan parametros para poder crear el usuario.",
                                "exception:": str(exception)}     
                        
                    except Exception as e:
                        return codes.server.internal_error(e.args)
                    
        def activation_email_sending_failed(exception=None, status=status.HTTP_400_BAD_REQUEST):
                    try:
                        return {                     
                                "status" : str(status),
                                "code" : "recoverPassword-7004",
                                "timestamp": str(datetime.datetime.utcnow()),
                                "message" : "Ha ocurrido un error al enviar el email de activación.",
                                "exception:": str(exception)}     
                        
                    except Exception as e:
                        return codes.server.internal_error(e.args)