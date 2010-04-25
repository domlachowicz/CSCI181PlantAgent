// downloader.c
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
int parse_input (int argc, char **argv, int &port);
void print_usage ();
void print_plant_image (int sock);
void print_instructions ();

int
main (int argc, char **argv)
{
  int port, sock;
  string input;

  // Parse the command line
  if (parse_input (argc, argv, port) < 0)
    {
      print_usage ();
      return 1;
    }

  // Open a socket to the game server
  // Assumes that the game server is listening on the local host
  sock = open_client_sock ("localhost", port);
  if (sock < 0)
    {
      printf ("Error connecting to game server on port %d\n", port);
      return 1;
    }

  // Loop as long as the player is alive
  while (alive (sock))
    {
      int move = rand () % 4;
      switch (get_plant_type (sock))
	{
	case NO_PLANT:
	  break;

	case UNKNOWN_PLANT:
	  break;

	case NUTRITIOUS_PLANT:
	  printf ("nutritious\n");
	  break;

	case POISONOUS_PLANT:
	  printf ("poisonous\n");
	  break;
	}

      if (0 == move)
	{
	  move_up (sock);
	}
      else if (1 == move)
	{
	  move_down (sock);
	}
      else if (2 == move)
	{
	  move_left (sock);
	}
      else if (3 == move)
	{
	  move_right (sock);
	}

      switch (get_plant_type (sock))
	{
	case NO_PLANT:
	  break;
	default:
	  print_plant_image (sock);
	}

      switch (eat_plant (sock))
	{
	case NO_PLANT_TO_EAT:
	  break;

	case PLANT_ALREADY_EATEN:
	  break;

	case EAT_NUTRITIOUS_PLANT:
	  printf ("nutritious 2\n");
	  break;

	case EAT_POISONOUS_PLANT:
	  printf ("poisonous 2\n");
	  break;
	}
    }

  // Tell the server to end the game
  end_game (sock);

  // Print a message for the user
  printf ("Player is dead: GAME OVER!\n\n");
}

// parse_input
// -----------
// Parse the command line input
// Returns 0 on success; -1 on error
int
parse_input (int argc, char **argv, int &port)
{
  if (argc != 2)
    {
      return -1;
    }

  port = atoi (argv[1]);
  return 0;
}

// print_usage
// -----------
// Print proper command line usage
void
print_usage ()
{
  printf ("usage: player port\n");
}

// print_plant_image
// -----------------
// Print a plant image
void
print_plant_image (int sock)
{
  int row, col;
  int image[IMAGE_SIZE][IMAGE_SIZE];

  if (get_plant_image (sock, image) < 0)
    {
      printf ("Error getting image\n");

    }
  else
    {
      printf ("Plant image:\n");
      for (row = 0; row < IMAGE_SIZE; row++)
	{
	  for (col = 0; col < IMAGE_SIZE; col++)
	    {
	      printf ("%3d", image[row][col]);
	    }

	  printf ("\n");
	}
    }
}

// print_instructioAns
// ------------------
// Print instructions for the user
void
print_instructions ()
{
  printf ("Use i, j, k, l to move\n");
  printf ("Type e to eat a plant\n");
  printf ("Type o to observe a plant\n");
  printf ("Type exit to end the game\n");
  printf ("Type ? or help to view these intructions\n\n");
}
