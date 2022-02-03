import importlib
import sys
import threading
from pathlib import Path

from flask import Flask, request

app = Flask(__name__)
SCRIPTS_PATH = 'scripts'

mod = None


def run_script_thread(script_name, script_arguments):
    global mod
    print("Running script {} with arguments {}".format(script_name, script_arguments))
    sys.argv[1:] = script_arguments
    script_name = script_name.replace(".py", "")
    try:
        mod = importlib.import_module(SCRIPTS_PATH + "." + script_name)
    except Exception as e:
        print("Error importing script {}".format(e))
        return "Script {} returned error".format(e)
    return "OK"


def run_method_thread(method_name, method_arguments):
    global mod
    print("Running method {} with arguments {}".format(method_name, method_arguments))
    try:
        if method_arguments:
            function = getattr(mod, method_name)
            result = function(*method_arguments)
        else:
            result = getattr(mod, method_name)()
    except Exception as e:
        print("Error calling method {}".format(e))
        return "Script {} returned error".format(e)
    return "OK"


@app.route('/method', methods=['POST'])
def call_specific_method():
    global mod
    params = request.json
    method_name = params['method_name'] if 'method_name' in params else None
    method_arguments = params['method_arguments'] if 'method_arguments' in params else None
    if method_name:
        try:
            x = threading.Thread(target=run_method_thread, args=(method_name, method_arguments))
            x.start()
            x.join()
        except Exception as e:
            return "Script {} returned error".format(e), 405
        return "Called method", 200
    return "Method can't be null", 400


@app.route('/script', methods=['POST'])
def run_script():
    # Get the script from the request
    script = request.json
    script_name = script['script_name']
    script_arguments = script['script_arguments']
    try:
        x = threading.Thread(target=run_script_thread, args=(script_name, script_arguments))
        x.start()
        x.join()
    except Exception as e:
        return "Script {} returned error".format(e)

    return "200"


if __name__ == '__main__':
    Path("./scripts").mkdir(parents=True, exist_ok=True)
    app.run(host='0.0.0.0', port=9090)
