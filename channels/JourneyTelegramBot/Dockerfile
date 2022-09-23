FROM python:3.10
WORKDIR /app
COPY ./channels/JourneyTelegramBot/requirements.txt ./
RUN pip install -r requirements.txt
COPY ./apis/PythonAPI/requirements.txt ./
RUN pip install -r requirements.txt
COPY ./channels/JourneyTelegramBot /app
COPY ./apis/PythonAPI /app
CMD ["python","-u","main.py"]