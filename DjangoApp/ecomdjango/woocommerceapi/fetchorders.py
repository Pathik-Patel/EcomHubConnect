from woocommerce import API
import json
from django.http import JsonResponse
from django.middleware.csrf import get_token
from rest_framework.decorators import api_view

def get_csrf_token(request):
    csrf_token = get_token(request)
    return JsonResponse({'csrfToken': csrf_token})
from rest_framework.parsers import JSONParser 
@api_view(['GET', 'POST', 'DELETE'])
def callll(request):
    client_data = JSONParser().parse(request)

    # data = [
    #               {
    #                   "id": 14,
    #                   "parent_id": 0,
    #                   "status": "completed",
    #                   "currency": "USD",
    #                   "version": "6.0.0",
    #                   "prices_include_tax": "false",
    #                   "date_created": "2024-02-29T20:51:03",
    #                   "date_modified": "2024-02-29T20:51:32",
    #                   "discount_total": "0.00",
    #                   "discount_tax": "0.00",
    #                   "shipping_total": "0.00",
    #                   "shipping_tax": "0.00",
    #                   "cart_tax": "0.00",
    #                   "total": "10.00",
    #                   "total_tax": "0.00",
    #                   "customer_id": 0,
    #                   "order_key": "wc_order_FThqDDupPs6uB",
    #                   "billing": {
    #                       "first_name": "",
    #                       "last_name": "",
    #                       "company": "",
    #                       "address_1": "",
    #                       "address_2": "",
    #                       "city": "",
    #                       "state": "",
    #                       "postcode": "",
    #                       "country": "",
    #                       "email": "",
    #                       "phone": ""
    #                   },
    #                   "shipping": {
    #                       "first_name": "",
    #                       "last_name": "",
    #                       "company": "",
    #                       "address_1": "",
    #                       "address_2": "",
    #                       "city": "",
    #                       "state": "",
    #                       "postcode": "",
    #                       "country": "",
    #                       "phone": ""
    #                   },
    #                   "payment_method": "",
    #                   "payment_method_title": "",
    #                   "transaction_id": "",
    #                   "customer_ip_address": "",
    #                   "customer_user_agent": "",
    #                   "created_via": "admin",
    #                   "customer_note": "",
    #                   "date_completed": "2024-02-29T20:51:32",
    #                   "date_paid": "2024-02-29T20:51:32",
    #                   "cart_hash": "",
    #                   "number": "14",
    #                   "meta_data": [
    #                       {
    #                           "id": 47,
    #                           "key": "_new_order_email_sent",
    #                           "value": "true"
    #                       }
    #                   ],
    #                   "line_items": [
    #                       {
    #                           "id": 1,
    #                           "name": "Pen",
    #                           "product_id": 13,
    #                           "variation_id": 0,
    #                           "quantity": 1,
    #                           "tax_class": "",
    #                           "subtotal": "10.00",
    #                           "subtotal_tax": "0.00",
    #                           "total": "10.00",
    #                           "total_tax": "0.00",
    #                           "taxes": [],
    #                           "meta_data": [],
    #                           "sku": "",
    #                           "price": 10,
    #                           "parent_name": "null"
    #                       }
    #                   ],
    #                   "tax_lines": [],
    #                   "shipping_lines": [],
    #                   "fee_lines": [],
    #                   "coupon_lines": [],
    #                   "refunds": [],
    #                   "date_created_gmt": "2024-02-29T20:51:03",
    #                   "date_modified_gmt": "2024-02-29T20:51:32",
    #                   "date_completed_gmt": "2024-02-29T20:51:32",
    #                   "date_paid_gmt": "2024-02-29T20:51:32",
    #                   "currency_symbol": "$",
    #                   "_links": {
    #                       "self": [
    #                           {
    #                               "href": "http://localhost/wordpress/index.php/wp-json/wc/v3/orders/14"
    #                           }
    #                       ],
    #                       "collection": [
    #                           {
    #                               "href": "http://localhost/wordpress/index.php/wp-json/wc/v3/orders"
    #                           }
    #                       ]
    #                   }
    #               },
    #               {
    #                   "id": 15,
    #                   "parent_id": 0,
    #                   "status": "completed",
    #                   "currency": "USD",
    #                   "version": "6.0.0",
    #                   "prices_include_tax": "false",
    #                   "date_created": "2024-02-29T20:51:03",
    #                   "date_modified": "2024-02-29T20:51:32",
    #                   "discount_total": "0.00",
    #                   "discount_tax": "0.00",
    #                   "shipping_total": "0.00",
    #                   "shipping_tax": "0.00",
    #                   "cart_tax": "0.00",
    #                   "total": "10.00",
    #                   "total_tax": "0.00",
    #                   "customer_id": 0,
    #                   "order_key": "wc_order_FThqDDupPs6uB",
    #                   "billing": {
    #                       "first_name": "",
    #                       "last_name": "",
    #                       "company": "",
    #                       "address_1": "",
    #                       "address_2": "",
    #                       "city": "",
    #                       "state": "",
    #                       "postcode": "",
    #                       "country": "",
    #                       "email": "",
    #                       "phone": ""
    #                   },
    #                   "shipping": {
    #                       "first_name": "",
    #                       "last_name": "",
    #                       "company": "",
    #                       "address_1": "",
    #                       "address_2": "",
    #                       "city": "",
    #                       "state": "",
    #                       "postcode": "",
    #                       "country": "",
    #                       "phone": ""
    #                   },
    #                   "payment_method": "",
    #                   "payment_method_title": "",
    #                   "transaction_id": "",
    #                   "customer_ip_address": "",
    #                   "customer_user_agent": "",
    #                   "created_via": "admin",
    #                   "customer_note": "",
    #                   "date_completed": "2024-02-29T20:51:32",
    #                   "date_paid": "2024-02-29T20:51:32",
    #                   "cart_hash": "",
    #                   "number": "14",
    #                   "meta_data": [
    #                       {
    #                           "id": 47,
    #                           "key": "_new_order_email_sent",
    #                           "value": "true"
    #                       }
    #                   ],
    #                   "line_items": [
    #                       {
    #                           "id": 1,
    #                           "name": "Pen",
    #                           "product_id": 13,
    #                           "variation_id": 0,
    #                           "quantity": 1,
    #                           "tax_class": "",
    #                           "subtotal": "10.00",
    #                           "subtotal_tax": "0.00",
    #                           "total": "10.00",
    #                           "total_tax": "0.00",
    #                           "taxes": [],
    #                           "meta_data": [],
    #                           "sku": "",
    #                           "price": 10,
    #                           "parent_name": "null"
    #                       }
    #                   ],
    #                   "tax_lines": [],
    #                   "shipping_lines": [],
    #                   "fee_lines": [],
    #                   "coupon_lines": [],
    #                   "refunds": [],
    #                   "date_created_gmt": "2024-02-29T20:51:03",
    #                   "date_modified_gmt": "2024-02-29T20:51:32",
    #                   "date_completed_gmt": "2024-02-29T20:51:32",
    #                   "date_paid_gmt": "2024-02-29T20:51:32",
    #                   "currency_symbol": "$",
    #                   "_links": {
    #                       "self": [
    #                           {
    #                               "href": "http://localhost/wordpress/index.php/wp-json/wc/v3/orders/14"
    #                           }
    #                       ],
    #                       "collection": [
    #                           {
    #                               "href": "http://localhost/wordpress/index.php/wp-json/wc/v3/orders"
    #                           }
    #                       ]
    #                   }
    #               }
    #           ]
    # return JsonResponse({"reply":data})
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
        obj = wcapi.get("orders", params={"per_page": 100, "after":max_date})

    obj = json.loads(obj.text)
    print(obj)
    return obj


