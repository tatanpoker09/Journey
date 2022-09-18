import os
import pyaudio
import asyncio
import pvporcupine
from dotenv import load_dotenv

load_dotenv()

access_key = os.getenv("PICOVOICE_ACCESS_KEY")

handle = pvporcupine.create(access_key=access_key, keyword_paths=["hey-journey.ppn", "yo-journey.ppn"])

FRAMES_PER_BUFFER = 256
SAMPLE_RATE = 16000
p = pyaudio.PyAudio()
stream = p.open(
    frames_per_buffer=FRAMES_PER_BUFFER,
    rate=SAMPLE_RATE,
    format=pyaudio.paInt16,
    channels=1,
    input=True,
)


def get_next_audio_frame():

    data= stream.read(FRAMES_PER_BUFFER)
    print(data)
    return data


while True:
    keyword_index = handle.process(get_next_audio_frame())
    if keyword_index >= 0:
        print("hey")
        # Insert detection event callback here
        pass
