#!/usr/bin/env python
"""
	*
	* Project Name: 	Eclassroom
	* Author List: 		Nikhil Vatwani,Shivani Valecha,
	* Filename: 		uploadnotes.py
	* Functions: 		checkFileDetails
	* Global Variables:	TOKEN,LOCALFILE,BACKUPPATH
	*
	
"""

import sys
import dropbox

from dropbox.files import WriteMode
from dropbox.exceptions import ApiError, AuthError

# Access token
TOKEN = 'dCfbe3nadZAAAAAAAAAADNj1mZT9tqB7LntG2R2_1DTbZAaSP8yXEgVqLatw_Xx6'

LOCALFILE = 'C:/Users/DELL/Downloads/client.py' #localfile path
BACKUPPATH = '/eclassroom/image1.jpg' # Keep the forward slash before destination filename

"""
    
	* Function Name:	backup
	* Output:		Uploads content of localfile to dropbox
	* Logic:		Uploads using dropboxobject.files_upload() function
	* Example Call:		audioRecorderCallbacks
	*
	
"""
# Uploads contents of LOCALFILE to Dropbox
def backup():
    with open(LOCALFILE, 'rb') as f:
        # We use WriteMode=overwrite to make sure that the settings in the file
        # are changed on upload
        print("Uploading " + LOCALFILE + " to Dropbox as " + BACKUPPATH + "...")
        try:
            dbx.files_upload(f.read(), BACKUPPATH, mode=WriteMode('overwrite'))
        except ApiError as err:
            # This checks for the specific error where a user doesn't have enough Dropbox space quota to upload this file
            if (err.error.is_path() and
                    err.error.get_path().error.is_insufficient_space()):
                sys.exit("ERROR: Cannot back up; insufficient space.")
            elif err.user_message_text:
                print(err.user_message_text)
                sys.exit()
            else:
                print(err)
                sys.exit()


# Run this script independently
if __name__ == '__main__':
    # Check for an access token
    if (len(TOKEN) == 0):
        sys.exit("ERROR: Looks like you didn't add your access token. Open up backup-and-restore-example.py in a text editor and paste in your token in line 14.")

    # Create an instance of a Dropbox class, which can make requests to the API.
    print("Creating a Dropbox object...")
    dbx = dropbox.Dropbox(TOKEN)

    # Check that the access token is valid
    try:
        dbx.users_get_current_account()
    except AuthError as err:
        sys.exit(
            "ERROR: Invalid access token; try re-generating an access token from the app console on the web.")

    

    print("Creating backup...")
    # Create a backup of the current settings file
    backup()

print("Done!")
