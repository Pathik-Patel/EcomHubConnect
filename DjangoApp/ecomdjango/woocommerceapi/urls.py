
from django.contrib import admin
from django.urls import path, include

from woocommerceapi.fetchorders import callll,getforecasting


urlpatterns = [
   path('hello', callll),
   path('getforecasting', getforecasting)
]
