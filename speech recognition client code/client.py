#!/usr/bin/env python
"""
	*
	* Project Name: 	Eclassroom
	* Author List: 		Nikhil Vatwani,Vijay Kataria
	* Filename: 		client.py
	* Functions: 		 fintWord
	* Global Variables:	 s,Host,Port
	*
	
"""

import socket
import pyautogui
import os
import subprocess

HOST = '192.168.0.104' # Enter IP or Hostname of your server
PORT = 12482# Pick an open Port (1000+ recommended), must match the server port
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((HOST,PORT))
"""
    
	* Function Name:	findWord
	* Input:		word to be searched
	* Output:		slide where word is present
	* Logic:		Checks for word using ctrl + F
	* Example Call:		findWord(presentation)
	*
	
"""

def findWord(word):
    pyautogui.hotkey('ctrl','f')

    pyautogui.hotkey('ctrl','a')
    
    pyautogui.press('backspace')
    pyautogui.typewrite(word[2:-1])

    pyautogui.press("enter")



#Lets loop awaiting for your input
while True:
    """
    command = input('Enter your command: ')
    s.send(str.encode(command))
    """
    reply = s.recv(1024)
    txt = None
    if str(reply) == "b'1'":
        pyautogui.press("down")
    elif str(reply) == "b'2'":
        pyautogui.press("up")
    elif str(reply) == "b'3'":
        os.system('C:/Users/Niknom/Downloads/CommandCam /devname "MJPEG Camera" /filename C:/Users/Niknom/Downloads/abc.jpg')
        #print("uploading...")
        filepath = "C:/Users/Niknom/AppData/Local/Programs/Python/Python36-32/texttospeech.py"
        subprocess.call("python "+filepath)
        #print("uploaded")
    elif str(reply) == "b'4'":
        txt = s.recv(1024)
        if str(txt) == "b'free'":
            txt = 3
        elif str(txt) == "b'Tu'":
            txt = 2
        elif str(txt) == "b'food'":
            txt = 4
        try:
            for i in range(0,int(txt)):
                pyautogui.press("down")
        except ValueError:
            print("please repeat")
        
        
    elif str(reply) == "b'5'":
        txt = s.recv(1024)
        if str(txt) == "b'free'":
            txt = 3
        elif str(txt) == "b'Tu'":
            print(txt)
            txt = 2
            print(txt)
        elif str(txt) == "b'food'":
            txt = 4
        try:
            for i in range(0,int(txt)):
                pyautogui.press("up")
        except ValueError:
            print("please repeat")
        
    elif str(reply) == "b'6'":
        pyautogui.press("down")
    elif str(reply) == "b'7'":
        pyautogui.press("up")
    elif str(reply) == "b'8'":
        txt = s.recv(1024)
        findWord(str(txt))
        reply = None
    elif str(reply) == "b'9'":
        pyautogui.press("down")
    elif str(reply) == "b'10'":
        pyautogui.press("up")
    print("txt"+str(txt))
    print(reply)
