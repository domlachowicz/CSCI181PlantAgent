# player.py
# --------
# CS 181
# 
# Example player for the final project


  

import sys
from game_util import *
from socklib import *


# print_usage
# -----------
# Print proper command line usage
def print_usage():
    print "usage: player port"
    

# print_plant_image
# -----------------
# Print a plant image
def print_plant_image(sock):
    image = get_plant_image(sock)
    print "Plant image:"
    for row in range(IMAGE_SIZE):
        for col in range(IMAGE_SIZE):
            print "%(bit)3d"%{'bit':image[row][col]},
            
        print



# print_instructions
# ------------------
# Print instructions for the user
def print_instructions(): 
    print "Use i, j, k, l to move"
    print "Type e to eat a plant"
    print "Type exit to end the game"
    print "Type ? or help to view these intructions\n"


def main(argv):
    #Parse the command line
    if len(argv) != 2:
        print_usage()
        return 1
    port = int(argv[1])

    #Print a welcome message and instructions
    print "\n\n"
    print "--------------------------\n"
    print "Welcome to the CS 181 game\n"
    print "--------------------------\n\n"

    #Open a socket to the game server
    #Assumes that the game server is listening on the local host
    sock = open_client_sock("localhost", port)
  
    #Print status messages
    print   "Connected to game server at port", port,"\n"

    # Print life settings
    print "Starting Life:", get_starting_life(sock)
    print "Plant Bonus:", get_plant_bonus(sock)
    print "Plant Penalty:", get_plant_penalty(sock)

    # Print instructions
    print_instructions()

    # Loop as long as the player is alive
    while alive(sock):
        # Print the current round
        print "Round:", get_round(sock)

        # Print game info
        print "Player is at (", getx(sock),", ", gety(sock), ") with life", get_life(sock)  

        #If there is a plant, print its information
        plant = get_plant_type(sock)

        if plant == NO_PLANT:
            print "No plant at this location"
        elif plant == UNKNOWN_PLANT:
            print "Plant at this location has not been eaten"
            #print_plant_image(sock)
            print
        elif plant == NUTRITIOUS_PLANT:
            print "Plant at this location has been eaten"
            print "Plant was nutritious"
            #print_plant_image(sock)
            print
        elif plant == POISONOUS_PLANT:
            print "Plant at this location has been eaten"
            print "Plant was poisonous"
            #print_plant_image(sock)
            print

        #Get the next move

        print "Enter a move: "
        move = raw_input()
        
        # Handle user input
        if move == "" or move == "exit": 
            end_game(sock);
            break;
        elif move == "i": 
            move_up(sock);
            print "Moved UP"

        elif move == "j":
            move_left(sock)
            print "Moved LEFT"
            
        elif move == "k":
            move_down(sock)
            print "Moved DOWN"
            
        elif move == "l":
            move_right(sock)
            print "Moved RIGHT"
            
        elif move == "e":
            plantAction = eat_plant(sock)
            if plantAction == NO_PLANT_TO_EAT:
                print "No plant to eat"
                
            elif plantAction == PLANT_ALREADY_EATEN:
                print "Plant already eaten"
                
            elif plantAction == EAT_NUTRITIOUS_PLANT:
                print "Ate a nutritious plant!"
                
            elif plantAction == EAT_POISONOUS_PLANT:

               print "Ate a poisonous plant!"


        elif move == "o":
            plant = get_plant_type(sock)
            if plant == NO_PLANT:
                print "No plant at this location to observe"
            elif plant == UNKNOWN_PLANT:
                print_plant_image(sock)
                print
            elif plant == NUTRITIOUS_PLANT:
                print "You already ate the plant."
                print "Plant was nutritious"
                #print_plant_image(sock)
                print
            elif plant == POISONOUS_PLANT:
                print "You already ate the plant."
                print "Plant was poisonous"
                #print_plant_image(sock)
                print


                
        elif move == "?" or move == "help":
            print_instructions()

        else:
            print "Invalid input:", move
        print

    #end of while loop
    # Tell the server to end the game
    end_game(sock)
    # Print a message for the user
    print "Player is dead: GAME OVER!\n"
    

main(sys.argv)
    
    


