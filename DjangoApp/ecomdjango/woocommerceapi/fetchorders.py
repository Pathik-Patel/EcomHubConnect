from woocommerce import API
import json
from django.http import JsonResponse
from rest_framework.decorators import api_view
from rest_framework.parsers import JSONParser 

@api_view(['GET', 'POST', 'DELETE'])
def callll(request):
    client_data = JSONParser().parse(request)
    return JsonResponse({"reply":woocommerce_orders(client_data["domain"],client_data["consumerKey"], client_data["secretConsumerKey"],client_data["lastModifiedDate"],"v3")},safe=False)
     

def woocommerce_orders(domain, consumerkey, consumersecretkey,max_date, woocommerce_version):
    wcapi = API(
        url=domain,
        consumer_key=consumerkey,
        consumer_secret=consumersecretkey,
        wp_api=True,
        version="wc/"+ woocommerce_version,
        query_string_auth=True, # Force Basic Authentication as query string true and using under HTTPS
        timeout=1000
    )

    if(max_date == "Not Found"):
            obj = wcapi.get("orders", params={"per_page": 100})
    else:
        print(max_date)
        obj = wcapi.get("orders", params={"per_page": 100, "modified_after":max_date})

    obj = json.loads(obj.text)
    print(obj)
    return obj


