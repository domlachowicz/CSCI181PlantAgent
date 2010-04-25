## game_util.py
# # -----------
# # CS 181
# #
# # Utility code for the final project game
# #/

#IMAGE_SIZE

IMAGE_SIZE = 6

# command_t
# ---------
# Commands the player can send to the game
#

MOVE_UP, \
MOVE_LEFT, \
MOVE_DOWN, \
MOVE_RIGHT, \
EAT_PLANT, \
GET_PLANT_TYPE, \
GET_PLANT_IMAGE, \
GET_LIFE, \
GET_X, \
GET_Y, \
GET_ROUND, \
GET_STARTING_LIFE, \
GET_PLANT_BONUS, \
GET_PLANT_PENALTY, \
ALIVE, \
END_GAME = range(16)

#eat_plant_t
# -----------
# Result of eating a plant
#
NO_PLANT_TO_EAT, \
PLANT_ALREADY_EATEN, \
EAT_NUTRITIOUS_PLANT, \
EAT_POISONOUS_PLANT = range(4)

# plant_t
# -------
# Type of plant in a square
#
NO_PLANT, \
UNKNOWN_SQUARE, \
UNKNOWN_PLANT, \
NUTRITIOUS_PLANT, \
POISONOUS_PLANT = range(5)

from socklib import *
import struct

## move_up
# # -------
# # Move UP one tile
# # Returns 0 on success, -1 on error
# #/

def move_up(sock):
    return write_int(sock, MOVE_UP)

### move_left
## # ---------
## # Move LEFT one tile
## # Returns 0 on success, -1 on error
## #/

def move_left(sock):
    return write_int(sock, MOVE_LEFT)


# move_down
 # ---------
 # Move DONW one tile
 # Returns 0 on success, -1 on error
 #/
def move_down(sock):
    return write_int(sock, MOVE_DOWN)


# move_right
 # ----------
 # Move RIGHT one tile
 # Returns 0 on success, -1 on error
 #/
def move_right(sock):
    return write_int(sock, MOVE_RIGHT)



# eat_plant
 # ---------
 # Eat the plant at the current location
 # Returns the result on success; -1 on failure
 #/
def eat_plant(sock):
    return write_and_read_int(sock, EAT_PLANT);


# get_plant_type
 # --------------
 # Get the type of the plant at the current location
 # Returns the result on success; -1 on failure
 #/
def get_plant_type(sock):
    return write_and_read_int(sock, GET_PLANT_TYPE)


# get_plant_image
 # ---------------
 # Get the image of the plant at the current location
 # Returns 0 on success; -1 on failure
 #/
def get_plant_image(sock):
    image = []
   
    write_int(sock, GET_PLANT_IMAGE)
    for row in range(IMAGE_SIZE):
        imageRow = []
        for col in range(IMAGE_SIZE):
            imageRow.append(read_short(sock))
        image.append(imageRow)
    return image


# get_life
 # --------
 # Get the player's current life count
 # Returns the result on success; -1 on failure
 #/
def get_life( sock):
    return write_and_read_int(sock, GET_LIFE)


# getx
 # ----
 # Get the x-coordinate of the player's current location
 # Returns the result on success; -1 on failure
 #/
def getx(sock):
    return write_and_read_int(sock, GET_X)


# gety
 # ----
 # Get the y-coordinate of the player's current location
 # Returns the result on success; -1 on failure
 #/
def gety(sock):
    return write_and_read_int(sock, GET_Y)

# get_round
 # ---------
 # Get the current round of the game
 #/
def get_round(sock):
    return write_and_read_int(sock, GET_ROUND)


# get_starting_life
 # -----------------
 # Get the player's  starting life
 #/
def get_starting_life(sock):
    return write_and_read_int(sock, GET_STARTING_LIFE)


# get_plant_bonus
 # ---------------
 # Get the life bonus for eating a nutritious plant
 #/
def get_plant_bonus(sock):
    return write_and_read_int(sock, GET_PLANT_BONUS)


# get_plant_penalty
 # -----------------
 # Get the life penalty for eating a poisonous plant
 #/
def get_plant_penalty(sock):
    return write_and_read_int(sock, GET_PLANT_PENALTY)


# alive
 # -----
 # Check whether the player is alive
 # Returns the result on success; -1 on failure
 #/
def alive(sock):
    return write_and_read_int(sock, ALIVE)


# end_game 
 # --------
 # End the current game 
 #/
def end_game(sock):
    write_int(sock, END_GAME)


# write_and_read_int
 # ------------------
 # Writes and reads and integer from the given socket
 # Returns the result on success; -1 on failure
 #/
def write_and_read_int(sock, data):
    write_int(sock, data)
    return read_int(sock)
    


# write_int
 # ---------
 # Write an integer from the given socket
 # Returns 0 on success; -1 on error
 #
def write_int(sock, data):
    packet = struct.pack("i", data)
    if packet == None:
        return -1
    else:
        write_data(sock, packet)


# read_int
 # --------
 # Read an integer from the given socket
 # Returns the result on success
 # Returns -1 on failure
 #

def read_int(sock):
    buff = read_data(sock, struct.calcsize('i'))
    if buff == None:
        return -1
    else:
        return struct.unpack('i', buff)[0]
        
    

def read_short(sock):
    buff = sock.recv(4)
    while len(buff) < 4:
        buff = buff + sock.recv(4- len(buff))
    if buff == None:
        return -1
    
    else:
        return struct.unpack('i', buff)[0]

