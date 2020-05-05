from boto3 import resource
from boto3.dynamodb.conditions import Key

dynamodb_resource = resource('dynamodb')
table = dynamodb_resource.Table('customers')


def lambda_handler(event, context):
    table = dynamodb_resource.Table('customers')
    response = table.scan()

    return response