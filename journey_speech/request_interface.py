import requests
import json

nlp_url = "http://localhost:7150/parse"
backend_url = "http://localhost:8080/dialogue/"


def nlp_process(message_text):
    body = {"text": message_text}
    response = requests.post(nlp_url, json=body)
    return response.json()


def send_response_to_backend(response):
    prompt = response['request']['text']
    intent = response['request']['intent']
    domain = response['request']['domain']
    entities_json = response['request']['entities']
    response = response["directives"]
    entities_map = {}
    for entity in entities_json:
        entities_map[entity['type']] = entity['text']
    for directive in response:
        if directive['name'] == 'reply':
            response = directive['payload']['text']
            break
    data = {'prompt': prompt, 'response': response, 'intent': intent, 'domain': domain,
            'entities': json.dumps(entities_map)}
    print(entities_map)
    response = requests.post(backend_url, json=data)
    return response
