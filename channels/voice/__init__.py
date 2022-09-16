class JourneyChannel:
    def __init__(self, name__):
        self.name = name__
        self._sessions = dict()

    def start(self):
        pass

    def send_message(self, originator_id, message):
        print("Received message: " + message)
        pass

    def process_message(self, func):
        def wrapper():

            func(session, message)
            print("after")
        return wrapper

    def get_session(self, dialogue_id):
        return self._sessions[dialogue_id]