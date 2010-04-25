// player.c
// --------
// CS 181
// 
// Example player for the final project

#include "../shared/socklib.h"
#include "../shared/game_util.h"
#include <string>
#include <stdlib.h>
#include <iostream>

using namespace std;

// Function Prototypes
int parse_input(int argc, char **argv, int &port);
void print_usage();
void print_plant_image(int sock);
void print_instructions();

int main(int argc, char **argv) {
  int port, sock;
  string input;

  // Parse the command line
  if(parse_input(argc, argv, port) < 0) {
    print_usage();
	return 1;
  }
  
  // Print a welcome message and instructions
  printf("\n\n");
  printf("--------------------------\n");
  printf("Welcome to the CS 181 game\n");
  printf("--------------------------\n\n");

  // Open a socket to the game server
  // Assumes that the game server is listening on the local host
  sock = open_client_sock("localhost", port);
  if(sock < 0) {
    printf("Error connecting to game server on port %d\n", port);
	return 1;
  }

  // Print status messages
  printf("Connected to game server at port %d\n\n", port);

  // Print life settings
  printf("Starting Life: %d\nPlant Bonus: %d\nPlant Penalty: %d\n\n",
  get_starting_life(sock), get_plant_bonus(sock), get_plant_penalty(sock));

  // Print instructions
  print_instructions();

  // Loop as long as the player is alive
  while(alive(sock)) {
	// Print the current round
	printf("Round: %d\n\n", get_round(sock));

	// Print game info
	printf("Player is at (%d, %d) with life %d\n",
	getx(sock), gety(sock), get_life(sock));
    
	switch(get_plant_type(sock)) {
	  case NO_PLANT:
	    printf("No plant at in this location\n");
		break;

	  case UNKNOWN_PLANT:
	    printf("Plant at this location has not been eaten\n");
		printf("\n");
		break;

	  case NUTRITIOUS_PLANT:
	    printf("Plant at this location has been eaten\n");
		printf("Plant was nutritious\n");
		printf("\n");
		break;

	  case POISONOUS_PLANT:
	    printf("Plant at this location has been eaten\n");
		printf("Plant was poisonous\n");
		printf("\n");
		break;
	}

	printf("\n");

	// Get the next move
	printf("Enter a move: ");
	cin >> input;
	printf("\n");
	
	// Handle user input
	if(input == "" || input == "exit") {
	  end_game(sock);
	  break;

	} else if(input == "i") {
	  move_up(sock);
	  printf("Moved UP\n");

	} else if(input == "j") {
	  move_left(sock);
	  printf("Moved LEFT\n");

	} else if(input == "k") {
	  move_down(sock);
	  printf("Moved DOWN\n");
	  
	} else if(input == "l") {
	  move_right(sock);
	  printf("Moved RIGHT\n");
	  
	} else if(input == "e") {
	  switch(eat_plant(sock)) {
	    case NO_PLANT_TO_EAT:
		  printf("No plant to eat\n");
		  break;

		case PLANT_ALREADY_EATEN:
		  printf("Plant already eaten\n");
		  break;

		case EAT_NUTRITIOUS_PLANT:
		  printf("Ate a nutritious plant!\n");
		  break;

		case EAT_POISONOUS_PLANT:
		  printf("Ate a poisonous plant!\n");
		  break;
	  }	  
	} else if(input == "o"){
	  switch(get_plant_type(sock)) {
	  case NO_PLANT:
	    printf("No plant to observe\n");
	    break;
	  default:
	    print_plant_image(sock);	    
	  }
	} else if(input == "?" || input == "help") {
	  print_instructions();
	} else {
	  printf("Invalid input: %s\n", input.c_str());
	}

    printf("\n");
  }

  // Tell the server to end the game
  end_game(sock);

  // Print a message for the user
  printf("Player is dead: GAME OVER!\n\n");
}

// parse_input
// -----------
// Parse the command line input
// Returns 0 on success; -1 on error
int parse_input(int argc, char **argv, int &port) {
  if(argc != 2) {
    return -1;
  } 
  
  port = atoi(argv[1]);
    return 0;
  }

// print_usage
// -----------
// Print proper command line usage
void print_usage() {
  printf("usage: player port\n");
}

// print_plant_image
// -----------------
// Print a plant image
void print_plant_image(int sock) {
  int row, col;
  int image[IMAGE_SIZE][IMAGE_SIZE];
  
  if(get_plant_image(sock, image) < 0) {
    printf("Error getting image\n");
  
  } else {
    printf("Plant image:\n");
    for(row = 0; row < IMAGE_SIZE; row++) {
      for(col = 0; col < IMAGE_SIZE; col++) {
	    printf("%3d", image[row][col]);
	  }
   
      printf("\n");
	}
  }
}

// print_instructioAns
// ------------------
// Print instructions for the user
void print_instructions() {  
  printf("Use i, j, k, l to move\n");
  printf("Type e to eat a plant\n");
  printf("Type o to observe a plant\n");
  printf("Type exit to end the game\n");
  printf("Type ? or help to view these intructions\n\n");
}
