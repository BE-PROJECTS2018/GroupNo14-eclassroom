"""
	*
	* Project Name: 	Eclassroom
	* Author List: 		NIkhil Vatwani,Vijay Kataria
	* Filename: 		demo4.py
	* Functions: 		 audioRecorderCallback(fname), detectedCallback(),callOnStart(detector)
	* Global Variables:	 conn,interrupted,s
	*
	
"""

import snowboydecoder
import sys
import signal
import speech_recognition as sr
import os
import socket

"""
This demo file shows  how to use the new_message_callback to interact with
the recorded audio after a keyword is spoken. It uses the speech recognition
library in order to convert the recorded audio into text.
Information on installing the speech recognition library can be found at:
https://pypi.python.org/pypi/SpeechRecognition/
"""
HOST = '192.168.0.104' # Server IP or Hostname
PORT = 12482# Pick an open Port (1000+ recommended), must match the client sport
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print('Socket created')

#managing error exception
try:
    s.bind((HOST, PORT))
except socket.error:
    print('Bind failed')    

s.listen(15)
print('Socket awaiting messages')
(conn, addr) = s.accept()
print('Connected')

interrupted = False
"""
    
	* Function Name:	audioRecorderCallback(fname)
	* Input:		fname
	* Output:		int to inform the caller that the program exited correctly or incorrectly 
	*			(C code standard)
	* Logic:		Ask the user to input the number of elements required from the Fibonacci Series
	*			and call the function  print_fibonacci_series
	* Example Call:		audioRecorderCallbacks
	*
	
"""

def audioRecorderCallback(fname):
    print "converting audio to text"
    r = sr.Recognizer()
    with sr.AudioFile(fname) as source:
        audio = r.record(source)  # read the entire audio file
    # recognize speech using Google Speech Recognition
    try:
        # for testing purposes, we're just using the default API key
        # to use another API key, use `r.recognize_google(audio, key="GOOGLE_SPEECH_RECOGNITION_API_KEY")`
        # instead of `r.recognize_google(audio)`
        txt = r.recognize_google(audio)
        print(txt)
        conn.send(txt)
    except sr.UnknownValueError:
        print "Google Speech Recognition could not understand audio"
        callOnStart(detector)
    except sr.RequestError as e:
        print "Could not request results from Google Speech Recognition service; {0}".format(e)

    os.remove(fname)

""""
	* Function Name:	detectedCallback
	* Output:		writes 
	* Logic:		Checks for interruption like keyboard interruption using signal_handler
	* Example Call:		detectedCallback
	*
"""


def detectedCallback():
  sys.stdout.write("recording audio...")
  sys.stdout.flush()

def signal_handler(signal, frame):
    global interrupted
    interrupted = True
""""
	* Function Name:	interrupt_callback
	* Input:		None
	* Output:		interrupted =TRUE if there is any interruption else false
	* Logic:		Checks for interruption like keyboard interruption using signal_handler
	* Example Call:		interrupt_callback
	*
"""


def interrupt_callback():
    global interrupted
    return interrupted

""""
	* Function Name:	CallOnStart
	* Input:		Snowboydeocder object
	* Logic:		Checks ring buffer if hotword is detected and calls detected_callback
	* Example Call:		Called automatically by the Operating Syste
	*
"""
def callOnStart(detector):
    word = detector.start(conn,detected_callback=detectedCallback,
               audio_recorder_callback=audioRecorderCallback,
               interrupt_check=interrupt_callback,
               sleep_time=0.01)
#hotwords
words = ['nextslide.pmdl','previousslide.pmdl','captureimage.pmdl','move.pmdl','backward.pmdl','aglaslide.pmdl','pichlaslide.pmdl','Search.pmdl']

sensitivity = [0.5]*len(words)
detector = snowboydecoder.HotwordDetector(words, sensitivity=sensitivity)
callOnStart(detector)
