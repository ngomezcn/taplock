import random
import string
from django.utils import timezone


def random_string_ascii(chars = string.ascii_uppercase + string.digits + str(timezone.now) , N=10):
    return ''.join(random.choice(chars) for _ in range(N))

