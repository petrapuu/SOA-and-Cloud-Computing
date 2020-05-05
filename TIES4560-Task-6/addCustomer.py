import json
import boto3

dynamodb = boto3.resource('dynamodb')
table = dynamodb.Table('customers')

def lambda_handler(event, context):
    table.put_item(Item=event)
    message = str(event)
    return {
        "statusCode": 200,
        "body": json.dumps(message)
    }