import json
import bson
import pymongo
import datetime
import time
import threading

from bson.objectid import ObjectId
from datetime import datetime
from time import gmtime, strftime, strptime
from pymongo import MongoClient
from datetime import datetime, timedelta


IP_ADDRESS_OF_MONGODB = "130.238.15.114"
PORT_OF_MONGODB = 27017


client = MongoClient("130.238.15.114",27017)

db = client.monad
coll = db.feedback

checker = False

#Step 3: Receive feedbacks comming from client-app
#try:
print "Enter a number between 0 to 5 as your feedback: ",
number= int(raw_input())
#break
#except ValueError:
#print "Oops! That was no valid number! Try again..."
star = number
if star == 0:
  translate = "No Feedback!"
elif star == 1:
  translate = "Very bad" 
elif star == 2:
  translate = "Bad"
elif star == 3:
  translate = "Average"
elif star == 4:
  translate = "Good"
elif star == 5:
  translate = "Very good"

print "Enter the SuggestionID: ",
suggNo= raw_input()
newsuggest = {"SuggestionID": suggNo}
newfeedback = {"feedback": translate}


#step4:Store the feedbacks coming from client-app

def storData():
 coll.insert_one(newsuggest)
 coll.insert_one(newfeedback)

storData()

#this checker variable is just for testing step1 since we dont have real database
checker = True

#step1: Pending Request(Get query continuesly)
if checker:

 def RetrieveDB():
  threading.Timer(60.0, RetrieveDB).start()
  cursor = db.RequestTravel.find({"feedback":-1})
  for document in cursor:
   print "Send Feed Back Request"

 RetrieveDB()


#step2: Ask for feedback (call function from Notification Module)
 scurrent = strftime("%Y.%m.%d %H:%M:%S", gmtime())
 currentTime = datetime.strptime(scurrent,"%Y.%m.%d %H:%M:%S")
 print currentTime

 #Retrieve destination time from db
 cursor = db.TravelRequest.find_one({"_id" :ObjectId("561cffc0763cf41e8b12bdb5")},{"EndTime" : 1 , "_id" : 0})
 stcursor= str(cursor)
 time = stcursor[33:-2]
 date = stcursor[19:-14]
 dt = date+" "+time
 destinationTime = datetime.strptime(dt,"%Y.%m.%d %H:%M:%S")
 print destinationTime

 #Compare Current time with destination time
 diff = currentTime - destinationTime
 print diff
 
#  """ The feedback request should be send after destination time"""
 if (diff) > timedelta(seconds = 100):
   print " Now send feedback request!"

#later on the checker should be false after storing data in DB
   checker = False



#retrieve data from database
#cursor = coll.find({"feedback":"Bad"})
#for document in cursor:
 #print (document)


client.close()

